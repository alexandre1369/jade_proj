package Contexte;

public class Point_interet {

    String type;
    Coordonnee coordonnee;

    public Point_interet(){
        this.type = "";
        this.coordonnee = new Coordonnee();
    }

    public Point_interet(String type, Coordonnee coordonnees){
        this.type = type;
        this.coordonnee = coordonnees;
    }

    public String getType(){
        return type;
    }

    public Coordonnee getCoordonnee(){
        return coordonnee;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setCoordonnee(Coordonnee coordonnee){
        this.coordonnee = coordonnee;
    }

}
