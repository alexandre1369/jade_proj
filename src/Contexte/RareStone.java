package Contexte;
import java.io.Serializable;

public class RareStone implements Serializable {
    private static final long serialVersionUID = 1L; // Ajoutez un identifiant de version

    String type;
    int valeur;

    public RareStone() {
        // de moins en moins de chance d'avoir une pierre rare
        double random = Math.random();
        if (random < 0.1) {
            this.type = "Diamant";
            this.valeur = 100;
        } else if (random < 0.2) {
            this.type = "Rubis";
            this.valeur = 50;
        } else if (random < 0.3) {
            this.type = "Emeraude";
            this.valeur = 30;
        } else if (random < 0.4) {
            this.type = "Saphir";
            this.valeur = 20;
        } else {
            this.type = "Pierre précieuse";
            this.valeur = 10;
        }
    }


    public RareStone(String type, int valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    public RareStone(int valeur) {
        this.valeur = valeur;
        if (valeur == 100) {
            this.type = "Diamant";
        } else if (valeur == 50) {
            this.type = "Rubis";
        } else if (valeur == 30) {
            this.type = "Emeraude";
        } else if (valeur == 20) {
            this.type = "Saphir";
        } else {
            this.type = "Pierre précieuse";
        }
    }

    public String getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }
}
