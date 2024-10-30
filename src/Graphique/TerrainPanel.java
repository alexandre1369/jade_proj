package Graphique;

import Agents.RobotAgent;
import Contexte.TerrainManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TerrainPanel extends JPanel {
    private RobotAgent[] robots; // Liste des robots à dessiner
    private int terrainWidth; // Largeur du terrain
    private int terrainHeight; // Hauteur du terrain
    private TerrainManager terrainManager; // Gestionnaire de terrain
    private int taille_case = 10;

    public TerrainPanel(RobotAgent[] robots, int width, int height, TerrainManager te) {
        this(robots, width, height, te, 10); // Appel du constructeur avec taille_case par défaut
    }

    public TerrainPanel(RobotAgent[] robots, int width, int height, TerrainManager te,int taille_case) {
        this.robots = robots;
        this.terrainWidth = width;
        this.terrainHeight = height;
        this.terrainManager = te;
        setPreferredSize(new Dimension(terrainWidth * taille_case, terrainHeight * taille_case)); // taille_case pixels par case
        this.taille_case = taille_case;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTerrain(g);
        // drawVisited(g);
        drawShip(g);
        drawInfranchissable(g);
        draw_rock(g);
        drawRobots(g);
    }

    private void drawTerrain(Graphics g) {
        // Dessiner le terrain (vous pouvez personnaliser cette partie)
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < terrainWidth; i++) {
            for (int j = 0; j < terrainHeight; j++) {
                g.drawRect(i * taille_case, j * taille_case, taille_case, taille_case); // Dessiner la case
            }
        }
    }

    private void drawInfranchissable(Graphics g) {
        // Dessiner les cases infranchissables
        g.setColor(Color.RED);
        for (int i = 0; i < terrainWidth; i++) {
            for (int j = 0; j < terrainHeight; j++) {
                if (terrainManager.getCase(i, j).getDifficulte().equals("inaccessible")) {
                    g.fillRect(i * taille_case, j * taille_case, taille_case, taille_case); // Dessiner la case infranchissable
                }
            }
        }
    }

    private void draw_rock(Graphics g) {
        // Dessiner les cailloux
        g.setColor(Color.GRAY);
        for (int i = 0; i < terrainWidth; i++) {
            for (int j = 0; j < terrainHeight; j++) {
                if (terrainManager.getCase(i, j).getNb_pierre() > 0) {
                    g.fillRect(i * taille_case, j * taille_case, taille_case, taille_case); // Dessiner la case avec caillou
                }
            }
        }
    }

    private void drawRobots(Graphics g) {
        g.setColor(Color.RED);
        for (RobotAgent robot : robots) {
            g.fillOval(robot.getX() * taille_case, robot.getY() * taille_case, taille_case, taille_case); // Dessiner le robot
        }
    }

    private void drawVisited(Graphics g) {
        // Dessiner les cases visitées
        g.setColor(Color.GREEN);
        for (int i = 0; i < terrainWidth; i++) {
            for (int j = 0; j < terrainHeight; j++) {
                if (terrainManager.getCase(i, j).isReveler()) {
                    g.fillRect(i * taille_case, j * taille_case, taille_case, taille_case); // Dessiner la case visitée
                }
            }
        }
    }

    private void drawShip(Graphics g) {
        // Dessiner le vaisseau
        g.setColor(Color.BLUE);
        for (int i = 0; i < terrainWidth; i++) {
            for (int j = 0; j < terrainHeight; j++) {
                if (i == terrainManager.getX_ship() && j == terrainManager.getY_ship()) {
                    g.fillRect(i * taille_case, j * taille_case, taille_case, taille_case); // Dessiner le vaisseau
                }
            }
        }
    }

    public void updateRobots(RobotAgent[] robots) {
        for (int i = 0; i < this.robots.length; i++) {
            if (this.robots[i].getId() == robots[i].getId()) {
                this.robots[i] = robots[i];
            }
        }
        repaint(); // Redessiner le panneau
    }

    public void updateTerrain(TerrainManager terrainManager) {
        this.terrainManager = terrainManager;
        repaint(); // Redessiner le panneau
    }
}

