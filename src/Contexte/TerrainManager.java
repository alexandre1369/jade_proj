package Contexte;

public class TerrainManager {

    int taille_x;
    int taille_y;
    Case_Terrain[][] terrain;

    public TerrainManager(int taille_x, int taille_y) {
        this.taille_x = taille_x;
        this.taille_y = taille_y;
        terrain = new Case_Terrain[taille_x][taille_y];
        for (int i = 0; i < taille_x; i++) {
            for (int j = 0; j < taille_y; j++) {
                terrain[i][j] = new Case_Terrain();
            }
        }
    }

    public TerrainManager(TerrainManager a) {
        this.taille_x = a.taille_x;
        this.taille_y = a.taille_y;
        terrain = new Case_Terrain[taille_x][taille_y];
        for (int i = 0; i < taille_x; i++) {
            for (int j = 0; j < taille_y; j++) {
                terrain[i][j] = new Case_Terrain(a.getCase(i, j));
            }
        }
    }

    public Case_Terrain getCase(int x, int y) {
        return terrain[x][y];
    }

    public int getNbCase() {
        return taille_x * taille_y;
    }

    public int getTaille_x() {
        return taille_x;
    }

    public int getTaille_y() {
        return taille_y;
    }

    public void updateTerrain() {
        // Met à jour l'état du terrain
    }
}