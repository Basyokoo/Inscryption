package jeu.model;

public class Animal extends Carte{
    protected int m_pointsAttaque;
    protected int m_coutSang;
    protected int m_coutOs;
    protected boolean m_volatile;
    // Création du constructeur
    public Animal(String nom, int pV, int pointsAttaque, int sang, int os, boolean volant) {
        super(nom);
        super(pV);
        this.m_pointsAttaque = pointsAttaque;
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_volatile = volant;
    }

    public int getPointsAttaque(){
        return this.m_pointsAttaque;
    }
    public int getCoutSang(){
        return this.m_coutSang;
    }
    public int getCoutOs(){
        return this.m_coutOs;
    }
    public boolean getVolant(){
        return this.m_volatile;
    }
}
