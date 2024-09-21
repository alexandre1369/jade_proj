package Agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ShipAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Vaisseau prêt !");
        addBehaviour(new SignalBehaviour(this, 1000)); // Émet un signal toutes les secondes
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
}
