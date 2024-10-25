package Contexte;

import java.io.Serializable;

public class Coordonnee implements Serializable {
    private static final long serialVersionUID = 1L; // Ajoutez un identifiant de version


    int x;
    int y;

    public Coordonnee(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordonnee() {
        this.x = 0;
        this.y = 0;
    }

    public void setX(int x) {this.x = x;}

    public void setY(int y) {this.y = y;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public static int calculeDistance(Coordonnee c1, Coordonnee c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }
}


