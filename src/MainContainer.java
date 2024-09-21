import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

/**
 * Cette classe permet de lancer le conteneur principal de JADE
 * pour lancer les agents.
 */
public class MainContainer {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Properties p = new ExtendedProperties();
            // Activer l'interface graphique
            p.setProperty("gui", "true");
            ProfileImpl pc = new ProfileImpl(p);
            AgentContainer container = rt.createMainContainer(pc);
            container.start();


            AgentController shipAgent = container.createNewAgent("Vaisseau", "Agents.ShipAgent", null);
            shipAgent.start();

            for (int i = 0; i < 5; i++) { // Crée 5 robots
                AgentController robotAgent = container.createNewAgent("Robot" + i, "Agents.RobotAgent", null);
                robotAgent.start();
            }


        } catch (ControllerException e) {
            e.printStackTrace();
        }

    }
}
