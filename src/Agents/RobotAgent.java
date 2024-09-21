package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class RobotAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("Robot " + getLocalName() + " prêt !");
        addBehaviour(new SearchBehaviour());
    }

    private class SearchBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("Recherche de pierres...");
            // Logique de recherche des pierres
            // Utiliser le signal du vaisseau pour s'orienter
            // Retourner au vaisseau après avoir trouvé des pierres
        }
    }
}
