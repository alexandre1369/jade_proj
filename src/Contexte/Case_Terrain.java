package Contexte;

public class Case_Terrain {

    private String difficulte;
    private RareStone pierre;
    int nb_pierre;
    private int temps_de_parcourt;
    private int temps_de_exploration;
    private boolean reveler = false;

    public Case_Terrain() {
        String[] difficultes = {"facile", "moyen", "difficile","inaccessible"};
        this.difficulte = difficultes[(int) (Math.random() * 4)];
        // 1 chance sur 10 d'avoir une pierre rare
        if (Math.random() < 0.1) {
            this.pierre = new RareStone();
            //nombre de pierre entre 10-20
            this.nb_pierre = (int) (Math.random() * 10) + 10;
        }
        if (this.difficulte.equals("facile")) {
            this.temps_de_parcourt = 1;
        } else if (this.difficulte.equals("moyen")) {
            this.temps_de_parcourt = 2;
        } else if (this.difficulte.equals("difficile")) {
            this.temps_de_parcourt = 3;
        } else {
            this.temps_de_parcourt = 1000;
        }
        this.temps_de_exploration = 2*temps_de_parcourt;
    }

    public Case_Terrain(Case_Terrain a) {
        this.difficulte = a.difficulte;
        this.pierre = a.pierre;
        this.nb_pierre = a.nb_pierre;
        this.temps_de_parcourt = a.temps_de_parcourt;
        this.temps_de_exploration = a.temps_de_exploration;
    }

    public void reveler(){
        this.reveler = true;
    }

    public boolean isReveler() {
        return reveler;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public RareStone getPierre() {
        return pierre;
    }

    public void retirerPierre(){
        this.nb_pierre--;
    }

    public int getNb_pierre() {
        return nb_pierre;
    }

    public int getTemps_de_parcourt() {
        return temps_de_parcourt;
    }

    public int getTemps_de_exploration() {
        return temps_de_exploration;
    }
}
