package jeu.model;

public abstract class Carte {
    private String m_nom;
    private int m_pointsVies = 0;
    private String m_type;
    private int m_Attack = 0;
    private Pouvoir m_pouvoir;

    public Carte(String m, int num, String type){
        this.m_nom = m;
        this.m_pointsVies = num;
        this.m_type = type;
    }

    public Carte(String m, int num, String type, int degat){
        this.m_nom = m;
        this.m_pointsVies = num;
        this.m_type = type;
        this.m_Attack = degat;
    }

    public String getNom(){
        return this.m_nom;
    }

    public int getVie(){
        return this.m_pointsVies;
    }

    public String modifierVie(int changement) {
        this.m_pointsVies += changement;

        if (this.m_pointsVies < 0) {
            this.m_pointsVies = 0;
        }

        if (changement < 0) {
            return "Dégâts subis : " + changement + ".";
        } else {
            return "Points de vie modifiés de : +" + changement + ".";
        }
    }


    public boolean estVie(){
        return this.m_pointsVies > 0;
    }

    @Override
    public String toString(){
        return this.m_nom;
    }
}
