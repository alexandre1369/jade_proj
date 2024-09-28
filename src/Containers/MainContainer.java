package Containers;
import Agents.RobotAgent;
import Agents.RobotAgentFactory;
import Contexte.TerrainManager;
import Graphique.TerrainPanel;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainContainer {
    private static MainContainer instance; // Instance unique de MainContainer
    private AgentContainer container; // Référence au conteneur JADE
    private TerrainPanel terrainPanel; // Panneau de visualisation
    private List<RobotAgent> robots; // Liste des robots
    private TerrainManager terrainManager; // Gestionnaire de terrain

    public MainContainer() {
        robots = new ArrayList<>();
        terrainManager = new TerrainManager(5, 5);
    }

    // Méthode pour obtenir l'instance unique de MainContainer
    public static MainContainer getInstance() {
        if (instance == null) {
            instance = new MainContainer();
        }
        return instance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainContainer mainContainer = new MainContainer();
            mainContainer.createAndShowGUI();
            mainContainer.startJADEContainer();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Simulation des Robots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Initialiser le panneau de terrain
        terrainPanel = new TerrainPanel(robots, terrainManager.getTaille_x(), terrainManager.getTaille_y(), terrainManager);
        frame.add(terrainPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

    }

    private void startJADEContainer() {
        try {
            Runtime rt = Runtime.instance();
            ExtendedProperties p = new ExtendedProperties();
            p.setProperty("gui", "true");
            ProfileImpl pc = new ProfileImpl(p);
            container = rt.createMainContainer(pc);
            container.start();

            // Créer le terrain
            terrainManager = new TerrainManager(5, 5);

            // Créer les robots et les ajouter à la liste pour la visualisation
            for (int i = 0; i < 1; i++) { // Crée 5 robots
                RobotAgent robot = new RobotAgent(2, 2, 2, 2, terrainManager,i);
                robots.add(robot);
                RobotAgentFactory.createRobotAgent(container, "Robot" + i, robot);
            }

            // Mettre à jour le panneau de visualisation
            terrainPanel.updateRobots(robots);
            terrainPanel.updateTerrain(terrainManager);
            instance = this;
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    public void updateVisualization(RobotAgent robot) {
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getId() == robot.getId()) {
                robots.set(i, robot);
                break;
            }
        }
        terrainPanel.updateRobots(robots);
    }

    // Méthode pour arrêter le conteneur
    public void kill() {
        try {
            if (container != null) {
                container.kill(); // Arrêter le conteneur JADE
            }
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
