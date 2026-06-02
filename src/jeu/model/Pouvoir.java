package jeu.model;

public class Pouvoir {
    private String m_type;
    private String m_nom;
    private int m_activeDans = 0;

    public Pouvoir(String nom, String type, int active){
        this.m_activeDans = active;
        this.m_type = type;
        this.m_nom = nom;
    }

    public int getActive(){
        return this.m_activeDans;
    }
}
