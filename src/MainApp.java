import Agents.ShipAgent;
import Contexte.TerrainManager;
import Agents.RobotAgent;
import Containers.MainContainer;
import jade.wrapper.ControllerException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Classe MainApp pour afficher le terrain et les robots
public class MainApp extends JFrame {
    private TerrainManager terrainManager;
    private List<RobotAgent> robots;
    private int cellSize = 10;
    private MainContainer container; // Référence au conteneur JADE


    public MainApp(TerrainManager terrainManager, List<RobotAgent> robots) {
        this.terrainManager = terrainManager;
        this.robots = robots;

        setTitle("Simulation de Robots");
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ajout d'un JPanel personnalisé pour dessiner le terrain et les robots
        add(new TerrainPanel());
        // Lancer le conteneur JADE
        container = new MainContainer();
        //this.container = container.lancer_conteneur(terrainManager, new ShipAgent(25, 25, 1), robots);

        // Timer pour mettre à jour les robots et redessiner
        Timer timer = new Timer(1000, e -> updateRobots());
        timer.start();

        // Ajouter un écouteur pour gérer la fermeture de la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing(); // Appel de la fonction personnalisée
            }
        });
    }

    // Fonction personnalisée à exécuter lors de la fermeture de la fenêtre
    private void onWindowClosing() {
        // Arrêter le conteneur JADE proprement
        if (container != null) {
            container.kill(); // Méthode pour arrêter le conteneur
        }
        System.exit(0); // Fermer l'application
    }

    private class TerrainPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawTerrain(g);
            drawRobots(g);
        }

        private void drawTerrain(Graphics g) {
            // Dessiner le terrain (ex: une grille)

            for (int x = 0; x < terrainManager.getTaille_x(); x++) {
                for (int y = 0; y < terrainManager.getTaille_y(); y++) {
                    g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
            // dessiner les cailloux

            for (int x = 0; x < terrainManager.getTaille_x(); x++) {
                for (int y = 0; y < terrainManager.getTaille_y(); y++) {
                    if (terrainManager.getCase(x, y).getNb_pierre() > 0) {
                        g.setColor(Color.GRAY);
                        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }

            //dessiner les case impossible d'acces
            for (int x = 0; x < terrainManager.getTaille_x(); x++) {
                for (int y = 0; y < terrainManager.getTaille_y(); y++) {
                    if (Objects.equals(terrainManager.getCase(x, y).getDifficulte(), "inaccessible")) {
                        g.setColor(Color.RED);
                        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        }

        private void drawRobots(Graphics g) {
            // Dessiner les robots
            for (RobotAgent robot : robots) {
                g.setColor(Color.BLUE);
                int robotX = robot.getX() * cellSize; // Position du robot
                int robotY = robot.getY() * cellSize;
                g.fillOval(robotX + 1, robotY + 1, cellSize - 2, cellSize - 2);
            }
        }
    }

    private void updateRobots() {
        for (RobotAgent robot : robots) {
            System.out.println("Robot " + robot.getLocalName() + " en position " + robot.getX() + " " + robot.getY());
        }
        repaint();
    }

        // Méthode main pour lancer l'application
    public static void main(String[] args) {
        // Exemple de création du terrain et des robots
        TerrainManager terrainManager = new TerrainManager(100, 100);
        List<RobotAgent> robots = new ArrayList<>();
        ShipAgent vaisseau = new ShipAgent(25, 25, 1);
        robots.add(new RobotAgent(25, 25, 25, 25, terrainManager,0));


        // Démarrer l'application
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp(terrainManager, robots);
            app.setVisible(true);
        });
    }
}


