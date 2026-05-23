package jeu.model;

import java.util.ArrayList;

public class MainJoueur {
    private ArrayList<Animal> m_cartesEnMain;
    private int m_nbOsDisponibles;
    public MainJoueur() {
        m_nbOsDisponibles = 0;
        m_cartesEnMain = new ArrayList<>();
    }
    public void ajouterMain(Animal carte){
        if (carte != null) {
            this.m_cartesEnMain.add(carte);
        }
    }
    public void retirerMain(Animal carte){
        if (carte != null) {
            this.m_cartesEnMain.remove(carte);
        }
    }
    public void ajouterOs(int quantite){
        if (quantite > 0) {
            this.m_nbOsDisponibles += quantite;
        }
    }
    public void consommerOs(int quantite){
        if (quantite > 0) {
            this.m_nbOsDisponibles -= quantite;
        }
    }
    public ArrayList<Animal> getCartesEnMain() {
        return m_cartesEnMain;
    }
    public int getNbOsDisponibles() {
        return m_nbOsDisponibles;
    }
}
