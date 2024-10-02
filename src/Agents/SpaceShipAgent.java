package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SpaceShipAgent extends Agent {
    int x;
    int y;
    int nb_robot;
    int nb_cailloux;
    int valeur_cailloux;
    int robot_abrite;

    public SpaceShipAgent(int x, int y, int nb_robot) {
        this.x = x;
        this.y = y;
        this.nb_robot = nb_robot;
        this.nb_cailloux = 0;
        this.valeur_cailloux = 0;
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
        this.valeur_cailloux = 0;
        System.out.println("Vaisseau prêt !");
        addBehaviour(new SignalBehaviour(this, 1000)); // Émet un signal toutes les secondes
    }

    public void déposerCailloux(int nb_cailloux, int valeur_cailloux) {
        this.nb_cailloux += nb_cailloux;
        this.valeur_cailloux += valeur_cailloux;
    }

    public void retour_au_bercailloux() {
        this.robot_abrite++;
    }

    public void go_to_work(){
        this.robot_abrite = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private class SignalBehaviour extends TickerBehaviour {
        public SignalBehaviour(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            System.out.println("Signal émis !");
            // Émettre le signal pour guider les robots
        }
    }

    private class ReceiveBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if(msg != null) {
                System.out.println("Received message from " + msg.getSender().getName());
                System.out.println("Content: " + msg.getContent());
            } else {
                block();
            }
        }
    };
}
