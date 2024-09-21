package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Cette classe permet de créer un agent Acheteur
 * qui va acheter un livre spécifié.
 */
public class Acheteur extends Agent {
    // Le livre à acheter
    private String livre;

    /**
     * Méthode d'initialisation de l'agent Acheteur
     */
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            livre = (String) args[0];
            System.out.println("Livre à acheter: " + livre);
            System.out.println("Buyer-agent " + getAID().getName() + " is ready.");

            // Ajout des comportements de l'agent Acheteur
            // TickerBehaviour pour essayer d'acheter le livre toutes les 30 secondes
            addBehaviour(new TickerBehaviour(this,30000) {
                private int compteur = 0;
                @Override
                protected void onTick() {
                    compteur++;
                    System.out.println("Trying to buy " + livre + ", attempt " + compteur);
                }
            });

            // CyclicBehaviour pour recevoir les messages
            // CyclicBehaviour est un comportement qui s'exécute en boucle
            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    ACLMessage msg = receive();
                    if(msg != null) {
                        System.out.println("Received message from " + msg.getSender().getName());
                        System.out.println("Content: " + msg.getContent());
                    } else {
                        block();
                    }
                }
            });
        } else {
            System.out.println("No book to buy specified");
            doDelete();
        }
        System.out.println("Hello! Buyer-agent " + getAID().getName() + " is ready.");
    }

    /**
     * Méthode de destruction de l'agent Acheteur
     */
    protected void takeDown() {
        System.out.println("Buyer-agent " + getAID().getName() + " is terminating.");
    }

    /**
     * Méthode de déplacement de l'agent Acheteur
     */
    protected void doMove() {
        System.out.println("Buyer-agent " + getAID().getName() + " is moving.");
    }
}
