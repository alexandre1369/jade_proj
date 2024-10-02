package Agents;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class RobotAgentFactory {

    public static AgentController createRobotAgent(AgentContainer container, String name,  RobotAgent robotAgent) {
        try {
            AgentController agent = container.createNewAgent(name, "Agents.RobotAgent",
                    new Object[]{robotAgent.x, robotAgent.y, robotAgent.x_vaisseau, robotAgent.y_vaisseau, robotAgent.terrainManager,robotAgent.id});
            agent.start();
            return agent;
        } catch (ControllerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
