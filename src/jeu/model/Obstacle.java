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

    @Override
    public boolean getVolant() {
        return false;
    }

    @Override
    public Pouvoir getPouvoir() {
        return null;
    }

    @Override
    public void ajouterAttack(int i) {
        return;
    }

    @Override
    public int getAttack() {
        return 0;
    }
}
