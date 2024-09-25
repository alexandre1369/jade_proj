package Contexte;



public class RareStone {
    String type;
    int valeur;

    public RareStone() {
        //de moins en moins de chance d'avoir une pierre rare
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

    public String getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }


}
