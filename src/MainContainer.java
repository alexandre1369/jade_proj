import Contexte.TerrainManager;
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

            // Création du terrain
            TerrainManager terrainManager = new TerrainManager(10, 10);


            AgentController shipAgent = container.createNewAgent("Vaisseau", "Agents.ShipAgent", new Object[]{0, 0});
            shipAgent.start();

            for (int i = 0; i < 1; i++) { // Crée 5 robots
                AgentController robotAgent = container.createNewAgent("Robot" + i, "Agents.RobotAgent", new Object[]{0, 0, 25, 25, terrainManager});
                robotAgent.start();
            }


        } catch (ControllerException e) {
            e.printStackTrace();
        }

    }
}
