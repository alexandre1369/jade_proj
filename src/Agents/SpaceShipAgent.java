package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import Contexte.RareStone; // Assurez-vous d'importer la classe RareStone

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;

public class SpaceShipAgent extends Agent {
    int x;
    int y;
    int nb_robot;
    int nb_cailloux;
    ArrayList<RareStone> Stockage = new ArrayList<RareStone>();
    int robot_abrite;

    public SpaceShipAgent(int x, int y, int nb_robot) {
        this.x = x;
        this.y = y;
        this.nb_robot = nb_robot;
        this.nb_cailloux = 0;
        this.robot_abrite = 0;
    }

    public SpaceShipAgent() {
        this.x = 0;
        this.y = 0;
        this.nb_robot = 0;
        this.nb_cailloux = 0;
        this.robot_abrite = 0;
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            this.x = (int) args[0];
            this.y = (int) args[1];
            this.nb_robot = (int) args[2];
        }
        this.nb_cailloux = 0;
        this.Stockage = new ArrayList<RareStone>();
        // System.out.println("Vaisseau prêt !");

        // Enregistrement du service dans le DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        // Définir le service offert par cet agent (par ex. "VaisseauService")
        ServiceDescription sd = new ServiceDescription();
        sd.setType("VaisseauService");
        sd.setName("ServiceDeCommunicationVaisseau");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            // System.out.println("Vaisseau enregistré avec le service VaisseauService");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Ajout du comportement personnalisé pour recevoir des messages
        addBehaviour(new RecevoirMessageBehaviour(this));
    }

    public void déposerCailloux(RareStone pierre) {
        this.nb_cailloux += 1;
        this.Stockage.add(pierre);
    }

    public void retour_au_bercailloux() {
        this.robot_abrite++;
    }

    public void go_to_work() {
        this.robot_abrite = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Comportement pour recevoir un message
    private class RecevoirMessageBehaviour extends Behaviour {
        public RecevoirMessageBehaviour(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            ACLMessage messageRecu = myAgent.receive();  // Utiliser myAgent pour accéder à l'agent
            if (messageRecu != null) {
                // Décodage depuis Base64
                try {
                    // System.out.println("Message reçu de " + messageRecu.getSender().getName());
                    byte[] data = Base64.getDecoder().decode(messageRecu.getContent().trim()); // Utiliser trim pour supprimer les espaces
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    ObjectInputStream in = new ObjectInputStream(bis);
                    RareStone[] pierresRaresRecues = (RareStone[]) in.readObject();
                    for (RareStone pierre : pierresRaresRecues) {
                        // System.out.println("Pierre reçue : " + pierre.getType() + " - Valeur : " + pierre.getValeur());
                        déposerCailloux(pierre);
                    }
                    rechercherRobots(); //TODO: a retirer juste pour tester
                } catch (IllegalArgumentException e) {
                    System.err.println("Erreur de décodage Base64 ship : " + e.getMessage());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                // Pas de message disponible, on bloque le comportement jusqu'à la réception
                block();
            }
        }

        private AID[] rechercherRobots() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("RobotService");
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                AID[] robots = new AID[result.length];
                for (int i = 0; i < result.length; ++i) {
                    robots[i] = result[i].getName();
                    // System.out.println("Robot trouvé : " + robots[i].getName());
                }
                return robots;
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            return null;
        }


        @Override
        public boolean done() {
            return false;  // Retourne false pour continuer à recevoir des messages
        }
    }
}
