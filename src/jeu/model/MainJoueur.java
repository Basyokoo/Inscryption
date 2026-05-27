package jeu.model;

import java.util.ArrayList;

public class MainJoueur {
    private ArrayList<Animal> m_cartesEnMain = null;
    private int m_nbOsDisponibles;
    private boolean isNull = true;
    public MainJoueur() {
        m_nbOsDisponibles = 0;
        m_cartesEnMain = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            m_cartesEnMain.add(null);
        }
    }
    public void ajouterMain(Animal carte){
        if (carte != null) {
            this.m_cartesEnMain.add(carte);
            this.isNull = false;
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
        if (this.isNull) {
            return null;
        };
        return m_cartesEnMain;
    }
    public int getNbOsDisponibles() {
        return m_nbOsDisponibles;
    }
}
