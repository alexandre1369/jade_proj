package Agents;

import Contexte.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import Containers.MainContainer;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.*;
import java.util.*;

public class RobotAgent extends Agent {
    int id;
    int poid_max;
    int poid_actuel;
    RareStone[] pierresRares = new RareStone[5];
    int x;
    int y;
    int x_vaisseau;
    int y_vaisseau;
    int valeur_transportee;
    Queue<Point_interet> list_objectif = new LinkedList<>();
    AID vaisseauAID;

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
        this.id = a.id;
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
            this.terrainManager = new TerrainManager((TerrainManager) args[4]);
            this.list_objectif = new LinkedList<>();

            this.id = (int) args[5];
        } else {
            System.out.println("Erreur lors de la création du robot");
            doDelete();
        }
        System.out.println("Robot " + getLocalName() + " prêt !");

        // Enregistrement du service dans le DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        // Définir le service offert par cet agent (par ex. "VaisseauService")
        ServiceDescription sd = new ServiceDescription();
        sd.setType("RobotService");
        sd.setName("ServiceDeCommunicationRobot");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println("Robot enregistré avec le service RobotService");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }



        // Ajouter un comportement pour rechercher le vaisseau et lui envoyer un message
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                vaisseauAID = rechercherVaisseau();
                if (vaisseauAID != null) {
                    System.out.println("envoi du message");
                    envoyerMessage(RobotAgent.this, vaisseauAID, "Bonjour Vaisseau !");
                } else {
                    System.out.println("Aucun vaisseau trouvé !");
                }
            }
        });
        addBehaviour(new SearchBehaviour());
        addBehaviour(new RecevoirMessageBehaviour(this));
    }

    public void collecterPierre(int poid, int valeur) {
        System.out.println("Pierre collectée");
        pierresRares[poid_actuel] = new RareStone(valeur);
        poid_actuel += poid;
        valeur_transportee += valeur;

    }

    public void deposerPierre() {
        // Déposer les pierres au vaisseau
        // TODO : Envoyer un message au vaisseau pour déposer les pierres
        envoyerTableau(pierresRares);
        System.out.println("Dépôt des pierres au vaisseau");
        poid_actuel = 0;
        valeur_transportee = 0;

    }

    // Fonction pour rechercher l'agent Vaisseau dans le DF
    private AID rechercherVaisseau() {
        AID vaisseauAID = null;
        try {
            // Définir une description pour rechercher un service
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("VaisseauService");  // Type du service recherché
            dfd.addServices(sd);

            // Recherche dans le DF
            DFAgentDescription[] result = DFService.search(this, dfd);
            if (result.length > 0) {
                vaisseauAID = result[0].getName();  // Récupère l'AID du premier résultat
                System.out.println("Vaisseau trouvé : " + vaisseauAID.getLocalName());
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return vaisseauAID;
    }

    private AID[] rechecherRobots(){
        AID[] robotsAID = null;
        try {
            // Définir une description pour rechercher un service
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("RobotService");  // Type du service recherché
            dfd.addServices(sd);

            // Recherche dans le DF
            DFAgentDescription[] result = DFService.search(this, dfd);
            if (result.length > 0) {
                robotsAID = new AID[result.length];
                for (int i = 0; i < result.length; i++) {
                    robotsAID[i] = result[i].getName();
                    System.out.println("Robot trouvé : " + robotsAID[i].getLocalName());
                }
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return robotsAID;
    }


    // Fonction pour envoyer un message à un agent avec un AID spécifique
    private void envoyerMessage(Agent monAgent, AID destinataire, String contenu) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(destinataire);
        message.setContent(contenu);
        monAgent.send(message);
        System.out.println("Message envoyé à " + destinataire.getLocalName() + " : " + contenu);
    }

    public void explorer_case(Case_Terrain case_actuelle) {
        int temps_de_exploration = case_actuelle.getTemps_de_exploration();
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime < temps_de_exploration * 1000) {
            // Attente du temps de parcourt
        }
        case_actuelle.reveler();
    }


    public Coordonnee choiceTheGoodWay(Coordonnee vectorDirector, List<Coordonnee> history) {
        double lastBestValue = Double.POSITIVE_INFINITY;
        Coordonnee theGoodWay = null;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (case_disponible(this.x + i, this.y + j) && (i != 0 || j != 0)) {
                    /* Heuristic
                    * 1. minimise the manhattan distance between case and vectorDirector
                    * 2. minimise the time travel to the case
                    * 3. Bonus discover
                    * 4. Malus for backward on previous visited case
                    * */
                    int heuristicDirection = Math.abs(i - vectorDirector.getX()) + Math.abs(j - vectorDirector.getY());
                    int heuristicTime = terrainManager.getCase(this.x + i, this.y + j).getTemps_de_parcourt();
                    int bonusDiscover = terrainManager.getCase(this.x + i, this.y + j).isReveler()? 0 : 1;
                    int malusBackward = history.contains(new Coordonnee(this.x + i, this.y + j))? 10 : 0;
                    double value = heuristicDirection + heuristicTime + bonusDiscover + malusBackward;
                    if (value < lastBestValue) {
                        lastBestValue = value;
                        theGoodWay = new Coordonnee(i, j);
                    }
                }
            }
        }
        System.out.println("        lastBestValue : "+lastBestValue);
        return theGoodWay;
    }

    public void goToCase(int x, int y) {
        List<Coordonnee> history = new ArrayList<>();
        System.out.println(this.id+" going to "+x+" "+y);
        while (this.x != x || this.y != y) {
            // compute vector director rounded
            Coordonnee vectorDirector = new Coordonnee(x-this.x, y-this.y);
            double norm = Math.sqrt(Math.pow(vectorDirector.getX(), 2) + Math.pow(vectorDirector.getY(), 2));
            vectorDirector.setX((int) Math.round(vectorDirector.getX()/norm));
            vectorDirector.setY((int) Math.round(vectorDirector.getY()/norm));
            Coordonnee theGoodWay = choiceTheGoodWay(vectorDirector, history);
            history.add(new Coordonnee(this.x + theGoodWay.getX(), this.y + theGoodWay.getY()));
            deplacement(theGoodWay.getX(), theGoodWay.getY());
        }
    }

//    public List<Coordonnee> déplacement_vers_case(int x, int y) {
//        int x_actuelle = this.x;
//        int y_actuelle = this.y;
//        List<Coordonnee> deplacement = new ArrayList<>();
//        while (x_actuelle != x || y_actuelle != y) {
//            Coordonnee ancienne_position = new Coordonnee(x_actuelle, y_actuelle);
//            if (x_actuelle < x) {
//                x_actuelle++;
//            } else if (x_actuelle > x) {
//                x_actuelle--;
//            }
//            if (y_actuelle < y) {
//                y_actuelle++;
//            } else if (y_actuelle > y) {
//                y_actuelle--;
//            }
//            if (case_disponible(x_actuelle, y_actuelle)) {
//                deplacement.add(new Coordonnee(x_actuelle - ancienne_position.getX(), y_actuelle - ancienne_position.getY()));
//
//            } else {
//                while (!case_disponible(x_actuelle, y_actuelle)) {
//                    x_actuelle += (int) (Math.random() * 3) - 1;
//                    y_actuelle += (int) (Math.random() * 3) - 1;
//                }
//                deplacement.add(new Coordonnee(ancienne_position.getX() - x_actuelle, ancienne_position.getY() - y_actuelle));
//            }
//        }
//        return deplacement;
//    }
//
//    public void deplacement(Coordonnee[] deplacement) {
//        if (deplacement.length == 0 && (this.x == x_vaisseau && this.y == y_vaisseau)) {
//            deplacement(0, 0);
//        }
//        for (Coordonnee coordonnee : deplacement) {
//            deplacement(this.x + coordonnee.getX(), this.y + coordonnee.getY());
//        }
//
//    }


    public void rentrer_au_vaisseau() {
        goToCase(this.x_vaisseau, this.y_vaisseau);
        if (this.x == x_vaisseau && this.y == y_vaisseau) {
            MainContainer.getInstance().updateRockVisu(this.terrainManager);
            deposerPierre();
        }
    }

    public void deplacement(int x, int y) {
        Case_Terrain case_actuelle = terrainManager.getCase(this.x + x, this.y + y);
        int temps_de_parcourt = terrainManager.getCase(this.x + x, this.y + y).getTemps_de_parcourt();
        long currentTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTime < temps_de_parcourt * 100) {
            // Attente du temps de parcourt
        }
        this.x += x;
        this.y += y;
        System.out.println("    Déplacement du robot " + this.id + " en " + this.x + " " + this.y);
        MainContainer.getInstance().updateVisualization(new Coordonnee(this.x, this.y), this.id);
    }

    public boolean case_disponible(int x, int y) {
        if (x < 0 || x >= terrainManager.getTaille_x() || y < 0 || y >= terrainManager.getTaille_y()) {
            return false;
        }
        return !(terrainManager.getCase(x, y).getDifficulte().equals("inaccessible"));
    }

    public boolean case_visitee(int x, int y) {
        return terrainManager.getCase(x, y).isReveler();
    }

    public boolean case_dispo_et_pas_visitee(int x, int y) {
        return case_disponible(x, y) && !case_visitee(x, y);
    }

    public boolean case_visitee_et_pierre(int x, int y) {
        return case_visitee(x, y) && terrainManager.getCase(x, y).getNb_pierre() > 0;
    }

    public List<Coordonnee> find_good_case_near() {
        List<Coordonnee> cases_dispo = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (case_disponible(this.x + i, this.y + j) && (!case_visitee(this.x + i, this.y + j) || case_visitee_et_pierre(this.x + i, this.y + j))) {
                    if (case_visitee_et_pierre(this.x + i, this.y + j)) {
                        cases_dispo = new ArrayList<>();
                        cases_dispo.add(new Coordonnee(i, j));
                        return cases_dispo;
                    } else {
                        cases_dispo.add(new Coordonnee(i, j));
                    }
                }
            }
        }
        return cases_dispo;
    }

    public void deplacement() {
        List<Coordonnee> cases_dispo = find_good_case_near();
        if (cases_dispo.isEmpty()) {
            int x = 0;
            int y = 0;
            do {
                x = (int) (Math.random() * 3) - 1;
                y = (int) (Math.random() * 3) - 1;
            } while (!case_disponible(this.x + x, this.y + y));
            deplacement(x, y);
        } else {
            Coordonnee coordonnee = cases_dispo.get((int) (Math.random() * cases_dispo.size()));
            deplacement(coordonnee.getX(), coordonnee.getY());
        }
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

    public void recherche_objectif(){
        if (!list_objectif.isEmpty()){
            Point_interet objectif = list_objectif.poll();
            Coordonnee coordonnee = objectif.getCoordonnee();
            goToCase(coordonnee.getX(), coordonnee.getY());
        }else {
            fouiller();
        }
    }


    public void fouiller() {
        System.out.println("Fouille de la case " + x + " " + y);
        Case_Terrain case_actuelle = terrainManager.getCase(x, y);
        explorer_case(case_actuelle);
        if (case_actuelle.isReveler()) {
            if (case_actuelle.getPierre() != null) {
                while (poid_actuel < poid_max && case_actuelle.getNb_pierre() > 0) {
                    collecterPierre(1, case_actuelle.getPierre().getValeur());
                    case_actuelle.retirerPierre();
                }
                if (case_actuelle.getNb_pierre() != 0){
                    Point_interet objectif = new Point_interet("pierre", new Coordonnee(x, y));
                    System.out.println("ajout de l'objectif" + objectif.getCoordonnee().getX() + " " + objectif.getCoordonnee().getY());
                    list_objectif.add(objectif);
                    //trouver les robots et leurs envoyer les coordonnées
                    envoyerCooordonnee(new Coordonnee(x, y));
                }
            } else {
                System.out.println("Pas de pierre à cet endroit");
                deplacement();
            }
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    private class SearchBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            if (poid_actuel == poid_max) {
                System.out.println("Retour au vaisseau");
                rentrer_au_vaisseau();
            } else {
                System.out.println("Recherche de pierres...");
                recherche_objectif();
            }

            // Logique de recherche des pierres
            // Utiliser le signal du vaisseau pour s'orienter
            // Retourner au vaisseau après avoir trouvé des pierres
        }
    }

    private void envoyerTableau(RareStone[] pierresRares) {
        try {
            // Sérialisation du tableau d'objets
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(pierresRares);
            out.flush();
            byte[] data = bos.toByteArray();

            // Encodage en Base64
            String encodedData = Base64.getEncoder().encodeToString(data);

            // Création du message
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setContent(encodedData); // Utiliser la chaîne encodée
            if (vaisseauAID == null) {
                vaisseauAID = rechercherVaisseau();
                if (vaisseauAID == null) {
                    System.out.println("Aucun vaisseau trouvé !");
                    return;
                }
            }
            message.addReceiver(vaisseauAID);
            send(message);
            System.out.println("Tableau de pierres rares envoyé.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void envoyerCooordonnee(Coordonnee coordonnee) {
        try {
            // Sérialisation du tableau d'objets
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(coordonnee);
            out.flush();
            byte[] data = bos.toByteArray();

            // Encodage en Base64
            String encodedData = Base64.getEncoder().encodeToString(data);

            // Création du message
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setContent(encodedData); // Utiliser la chaîne encodée
            //envoyer les coordonnées aux autres robots
            AID[] robotsAID = rechecherRobots();
            if (robotsAID != null) {
                for (AID robotAID : robotsAID) {
                    if (robotAID.equals(getAID())) {
                        continue;
                    }
                    ACLMessage messageRobot = new ACLMessage(ACLMessage.INFORM);
                    messageRobot.setContent(encodedData); // Utiliser la chaîne encodée
                    messageRobot.addReceiver(robotAID);
                    send(messageRobot);
                    System.out.println("Coordonnée envoyée au robot." + robotAID.getLocalName());
                    System.out.println("Coordonnée envoyée : " + coordonnee.getX() + " " + coordonnee.getY());
                }
            } else {
                System.out.println("Aucun robot trouvé !");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RecevoirMessageBehaviour extends Behaviour {

        public RecevoirMessageBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            ACLMessage messageRecu = myAgent.receive();  // Utiliser receive pour bloquer le comportement
            if (messageRecu != null) {
                // Décodage depuis Base64
                try {
                    System.out.println("Message reçu de " + messageRecu.getSender().getName());
                    byte[] data = Base64.getDecoder().decode(messageRecu.getContent().trim()); // Utiliser trim pour supprimer les espaces
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    ObjectInputStream in = new ObjectInputStream(bis);
                    Coordonnee coordonnee = (Coordonnee) in.readObject();
                    System.out.println("Coordonnées reçues : " + coordonnee.getX() + " " + coordonnee.getY());
                    Coordonnee pos = new Coordonnee(x, y);
                    if (Coordonnee.calculeDistance(coordonnee, pos) < 5) {
                        Point_interet objectif = new Point_interet("pierre", coordonnee);
                        System.out.println("ajout de l'objectif" + objectif.getCoordonnee().getX() + " " + objectif.getCoordonnee().getY());
                        //list_objectif.add(objectif);
                    } else {
                        System.out.println("Coordonnées trop éloignées");
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Erreur de décodage Base64 robots : " + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                // Pas de message disponible, on bloque le comportement jusqu'à la réception
                block();
            }

        }

        @Override
        public boolean done() {
            return false;
        }
    }



}


