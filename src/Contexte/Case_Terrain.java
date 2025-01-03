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
        //diificulter facile a 40%, moyen a 30%, difficile a 20%, inaccessible a 10%
        int index = (int) (Math.random() * 100);
        if (index < 40) {
            this.difficulte = difficultes[0];
        } else if (index < 70) {
            this.difficulte = difficultes[1];
        } else if (index < 90) {
            this.difficulte = difficultes[2];
        } else {
            this.difficulte = difficultes[3];
        }

        // 1 chance sur 10 d'avoir une pierre rare
        if (Math.random() < 0.5 && !this.difficulte.equals("inaccessible")) { //TODO: change to 0.1
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
            this.reveler = true;
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

    public Case_Terrain(boolean ship){
        this.difficulte = "facie";
        this.temps_de_parcourt = 1;
        this.temps_de_exploration = 0;
        this.reveler = true;
        this.nb_pierre = 0;
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
        if (this.nb_pierre == 0){
            this.pierre = null;
        }
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
