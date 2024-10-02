package Agents;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class ShipAgentFactory {

    public static AgentController createShipAgent(AgentContainer container, String name,  SpaceShipAgent shipAgent) {
        try {
            AgentController agent = container.createNewAgent(name, "Agents.SpaceShipAgent",
                    new Object[]{shipAgent.x, shipAgent.y, shipAgent.nb_robot});
            agent.start();
            return agent;
        } catch (ControllerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
