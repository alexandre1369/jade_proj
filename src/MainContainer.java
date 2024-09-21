import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.util.leap.Properties;
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
        } catch (ControllerException e) {
            e.printStackTrace();
        }

    }
}
