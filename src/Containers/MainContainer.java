package Containers;
import Agents.RobotAgent;
import Agents.RobotAgentFactory;
import Agents.SpaceShipAgent;
import Agents.ShipAgentFactory;
import Contexte.Coordonnee;
import Contexte.TerrainManager;
import Graphique.TerrainPanel;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

import javax.swing.*;
import java.awt.*;

public class MainContainer {
    private static MainContainer instance; // Instance unique de MainContainer
    private AgentContainer container; // Référence au conteneur JADE
    private TerrainPanel terrainPanel; // Panneau de visualisation
    private final RobotAgent[] robots; // Liste des robots
    private TerrainManager terrainManager; // Gestionnaire de terrain
    private final int nb_robot = 1;
    private Coordonnee position_vaisseau;
    private int taille_terrain = 20; ;

    public MainContainer() {
        robots = new RobotAgent[this.nb_robot]; // Crée une liste de 5 robots
        position_vaisseau = new Coordonnee(taille_terrain/2, taille_terrain/2);
        terrainManager = new TerrainManager(taille_terrain, taille_terrain, position_vaisseau.getX(), position_vaisseau.getY());
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
        terrainPanel = new TerrainPanel(robots, terrainManager.getTaille_x(), terrainManager.getTaille_y(), terrainManager,50);
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
            terrainManager = new TerrainManager(taille_terrain, taille_terrain, position_vaisseau.getX(), position_vaisseau.getY());

            SpaceShipAgent vaisseau = new SpaceShipAgent(position_vaisseau.getX(), position_vaisseau.getY(), nb_robot);
            ShipAgentFactory.createShipAgent(container, "Vaisseau", vaisseau);


            // Créer les robots et les ajouter à la liste pour la visualisation
            for (int i = 0; i < this.nb_robot; i++) { // Crée 5 robots
                RobotAgent robot = new RobotAgent(position_vaisseau.getX(), position_vaisseau.getY(), position_vaisseau.getX(), position_vaisseau.getY(),terrainManager, i);
                robots[i] = robot;
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

    public void updateVisualization(Coordonnee coordonnee, int id) {
        robots[id].setX(coordonnee.getX());
        robots[id].setY(coordonnee.getY());
        terrainPanel.updateRobots(robots);
    }

    public void updateRockVisu(TerrainManager terrainManager) {
        terrainPanel.updateTerrain(terrainManager);
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
