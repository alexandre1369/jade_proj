import jade.wrapper.AgentContainer;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;

/**
 * Cette classe permet de lancer un conteneur JADE
 * pour lancer les agents.
 */
public class JadeContainer {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            //indique que ce container n'est pas le container principal
            ProfileImpl pc = new ProfileImpl(false);
            // permette a ce containner de communiquer avec le container principal
            pc.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            AgentContainer container = rt.createAgentContainer(pc);
            // Création de l'agent Acheteur dans le container
            AgentController agentController =
                    container.createNewAgent("Acheteur", "Agents.Acheteur", new Object[]{"XML"});
            agentController.start();
            container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
