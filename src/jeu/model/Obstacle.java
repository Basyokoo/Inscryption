package jeu.model;

public class Obstacle extends Carte {

    // Constructeur
    public Obstacle(String nom, int pV) {
        super(nom, pV,"OBS");
    }

    public String setPouvoir(Pouvoir p) {
        return "Impossible de mettre un pouvoir sur un obstacle !";
    }

    @Override
    public boolean estAnimal() {
        return false;
    }
}
