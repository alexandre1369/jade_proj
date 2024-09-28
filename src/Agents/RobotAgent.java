package Agents;

import Contexte.Case_Terrain;
import Contexte.Coordonnee;
import Contexte.TerrainManager;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import Containers.MainContainer;

import java.util.ArrayList;
import java.util.List;

public class RobotAgent extends Agent {
    int id;
    int poid_max;
    int poid_actuel;
    int x;
    int y;
    int x_vaisseau;
    int y_vaisseau;
    int valeur_transportee;

    TerrainManager terrainManager;

    public RobotAgent() {
    }

    public RobotAgent(int x, int y, int x_vaisseau, int y_vaisseau, TerrainManager terrainManager, int id) {
        this.x = x;
        this.y = y;
        this.x_vaisseau = x_vaisseau;
        this.y_vaisseau = y_vaisseau;
        this.terrainManager = terrainManager;
        this.id = id;
    }

    public RobotAgent(RobotAgent a) {
        this.x = a.x;
        this.y = a.y;
        this.x_vaisseau = a.x_vaisseau;
        this.y_vaisseau = a.y_vaisseau;
        this.terrainManager = new TerrainManager(a.terrainManager);
    }



    @Override
    protected void setup() {
        this.poid_max = 5;
        poid_actuel = 0;
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            this.x = (int) args[0];
            this.y = (int) args[1];
            this.x_vaisseau = (int) args[2];
            this.y_vaisseau = (int) args[3];
            this.terrainManager = (TerrainManager) args[4];
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
        System.out.println("Dépôt des pierres au vaisseau");
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

    public List<Coordonnee> déplacement_vers_case(int x, int y){
        //TODO : Implémenter l'algorithme de déplacement
        int x_actuelle = this.x;
        int y_actuelle = this.y;
        List<Coordonnee> deplacement = new ArrayList<>();
        int i = 0;
        System.out.println("obejctif " + x + " " + y);
        System.out.println("position actuelle " + x_actuelle + " " + y_actuelle);
        while (x_actuelle != x || y_actuelle != y){
            Coordonnee ancienne_position = new Coordonnee(x_actuelle, y_actuelle);
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
                System.out.println("Déplacement vers la case 1 " + x_actuelle + " " + y_actuelle);
                deplacement.add(new Coordonnee(x_actuelle - ancienne_position.getX(), y_actuelle - ancienne_position.getY()));

            } else {
                while (!case_disponible(x_actuelle, y_actuelle)){
                    x_actuelle += (int) (Math.random() * 3)-1;
                    y_actuelle += (int) (Math.random() * 3)-1;
                }
                System.out.println("Déplacement vers la case 1 " + x_actuelle + " " + y_actuelle);
                deplacement.add(new Coordonnee(ancienne_position.getX() - x_actuelle, ancienne_position.getY() - y_actuelle));
            }
            i++;
        }
        return deplacement;
    }

    public void deplacement(Coordonnee[] deplacement){
        System.out.println("depla 1 " + deplacement.length);
        if (deplacement.length == 0 && (this.x == x_vaisseau && this.y == y_vaisseau)){
            deplacement(0,0);
        }
        for (Coordonnee coordonnee : deplacement){
            deplacement(this.x + coordonnee.getX(), this.y + coordonnee.getY());
        }

    }


    public void rentrer_au_vaisseau(){
        List<Coordonnee> deplacement = déplacement_vers_case(x_vaisseau, y_vaisseau);
        System.out.println("calcul du chemin pour rentrer au vaisseau");
        for (Coordonnee coordonnee : deplacement){
            System.out.println(coordonnee.getX() + " " + coordonnee.getY());
            deplacement(coordonnee.getX(), coordonnee.getY());
        }

    }

    public void deplacement(int x, int y){
        System.out.println("Déplacement vers la case 0 " + (this.x + x) + " " + (this.y + y));
        Case_Terrain case_actuelle = terrainManager.getCase(this.x + x, this.y + y);
        if (case_actuelle.getDifficulte().equals("inaccessible")){
            System.out.println("Case inaccessible, déplacement impossible");

        }

        int temps_de_parcourt = terrainManager.getCase(this.x + x, this.y + y).getTemps_de_parcourt();
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime < temps_de_parcourt * 1000){
            // Attente du temps de parcourt
        }
        if (this.x + x == x_vaisseau && this.y + y == y_vaisseau){
            System.out.println("Retour au vaisseau");
            deposerPierre();
        } else {
            System.out.println("Déplacement vers la case 2 " + (this.x + x) + " " + (this.y + y));
        }
        this.x += x;
        this.y += y;
        MainContainer.getInstance().updateVisualization(this);
    }

    public boolean case_disponible(int x, int y){
        if (x < 0 || x >= terrainManager.getTaille_x() ||  y < 0 || y >= terrainManager.getTaille_y()){
            return false;
        }
        return !(terrainManager.getCase(x, y).getDifficulte().equals("inaccessible"));
    }

    public boolean case_visitee(int x, int y){
        return terrainManager.getCase(x, y).isReveler();
    }

    public boolean case_dispo_et_pas_visitee(int x, int y){
        return case_disponible(x, y) && !case_visitee(x, y);
    }

    public void deplacement(){
        int x;
        int y;
        do {
            x = (int) (Math.random() * 3) - 1;
            y = (int) (Math.random() * 3) - 1;
            System.out.println("Déplacement vers la case 3 " + (this.x + x) + " " + (this.y + y));
        }while((x == 0 && y == 0) || !case_dispo_et_pas_visitee(this.x + x, this.y + y));
        System.out.println("dep " + x + " " + y);
        deplacement(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }


    public void fouiller() {
        System.out.println("Fouille de la case " + x + " " + y);
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
