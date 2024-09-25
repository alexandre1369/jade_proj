package Agents;

import Contexte.Case_Terrain;
import Contexte.Coordonnee;
import Contexte.TerrainManager;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;


public class RobotAgent extends Agent {

    int poid_max;
    int poid_actuel;
    int x;
    int y;
    int x_vaisseau;
    int y_vaisseau;
    int valeur_transportee;

    TerrainManager terrainManager;

    @Override
    protected void setup() {
        this.poid_max = 5;
        poid_actuel = 0;
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            x = (int) args[0];
            y = (int) args[1];
            x_vaisseau = (int) args[2];
            y_vaisseau = (int) args[3];
            terrainManager = new TerrainManager((TerrainManager) args[4]);
        }else{
            System.out.println("Erreur lors de la création du robot");
            doDelete();
        }
        System.out.println("Robot " + getLocalName() + " prêt !");
        addBehaviour(new SearchBehaviour());
    }

    public void collecterPierre(int poid, int valeur) {
        System.out.println("Pierre collectée");
        poid_actuel += poid;
        valeur_transportee += valeur;
    }

    public void deposerPierre(){
        // Déposer les pierres au vaisseau
        // TODO : Envoyer un message au vaisseau pour déposer les pierres
        poid_actuel = 0;
        valeur_transportee = 0;

    }

    public void explorer_case(Case_Terrain case_actuelle){
        int temps_de_exploration = case_actuelle.getTemps_de_exploration();
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime < temps_de_exploration * 1000){
            // Attente du temps de parcourt
        }
        case_actuelle.reveler();
    }

    public Coordonnee[] déplacement_vers_case(int x, int y){
        //TODO : Implémenter l'algorithme de déplacement
        int x_actuelle = this.x;
        int y_actuelle = this.y;
        Coordonnee[] deplacement = new Coordonnee[terrainManager.getNbCase()];
        int i = 0;
        while (x_actuelle != x || y_actuelle != y){
            if (x_actuelle < x){
                x_actuelle++;
            }else if (x_actuelle > x){
                x_actuelle--;
            }
            if (y_actuelle < y){
                y_actuelle++;
            }else if (y_actuelle > y){
                y_actuelle--;
            }
            if (case_disponible(x_actuelle, y_actuelle)) {
                System.out.println("Déplacement vers la case " + x_actuelle + " " + y_actuelle);
                deplacement[i] = new Coordonnee(x_actuelle, y_actuelle);
            } else {
                while (!case_disponible(x_actuelle, y_actuelle)){
                    x_actuelle += (int) (Math.random() * 3)-1;
                    y_actuelle += (int) (Math.random() * 3)-1;
                }
            }
            i++;
        }
        return deplacement;
    }

    public void deplacement(Coordonnee[] deplacement){
        for (Coordonnee coordonnee : deplacement){
            deplacement(coordonnee.getX(), coordonnee.getY());
        }
    }


    public void rentrer_au_vaisseau(){
        Coordonnee[] deplacement = déplacement_vers_case(x_vaisseau, y_vaisseau);
        System.out.println("calcul du chemin pour rentrer au vaisseau");
        for (Coordonnee coordonnee : deplacement){
            System.out.println(coordonnee.getX() + " " + coordonnee.getY());
        }
        deplacement(deplacement);
    }

    public void deplacement(int x, int y){
        Case_Terrain case_actuelle = terrainManager.getCase(x,y);
        if (case_actuelle.getDifficulte().equals("inaccessible")){
            System.out.println("Case inaccessible, déplacement impossible");

        }

        int temps_de_parcourt = terrainManager.getCase(x, y).getTemps_de_parcourt();
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime < temps_de_parcourt * 1000){
            // Attente du temps de parcourt
        }
        if (this.x + x == x_vaisseau && this.y + y == y_vaisseau){
            System.out.println("Retour au vaisseau");
            deposerPierre();
        } else {
            System.out.println("Déplacement vers la case " + x + " " + y);
        }
        this.x += x;
        this.y += y;
    }

    public boolean case_disponible(int x, int y){
        if (this.x + x < 0 || this.x + x >= terrainManager.getTaille_x() || this.y + y < 0 || this.y + y >= terrainManager.getTaille_y()){
            return false;
        }
        return !(terrainManager.getCase(this.x + x, this.y + y).getDifficulte().equals("inaccessible")) && !terrainManager.getCase(this.x + x, this.y + y).isReveler();
    }

    public void deplacement(){
        do {
            int x = (int) (Math.random() * 3) - 1;
            int y = (int) (Math.random() * 3) - 1;
            System.out.println("Déplacement vers la case " + x + " " + y);
        }while(case_disponible(x, y));
        deplacement(x, y);
    }

    public void fouiller() {
        Case_Terrain case_actuelle = terrainManager.getCase(x, y);
        explorer_case(case_actuelle);
        if (case_actuelle.isReveler()) {
            if (case_actuelle.getPierre() != null) {
                while(poid_actuel < poid_max && case_actuelle.getNb_pierre() > 0){
                    collecterPierre(1, case_actuelle.getPierre().getValeur());
                    case_actuelle.retirerPierre();
                }
            } else {
                System.out.println("Pas de pierre à cet endroit");
                deplacement();
            }
        }
    }


    private class SearchBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            if (poid_actuel == poid_max) {
                System.out.println("Retour au vaisseau");
                rentrer_au_vaisseau();
            } else {
                System.out.println("Recherche de pierres...");
                fouiller();
            }

            // Logique de recherche des pierres
            // Utiliser le signal du vaisseau pour s'orienter
            // Retourner au vaisseau après avoir trouvé des pierres
        }
    }
}
