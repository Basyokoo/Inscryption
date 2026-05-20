package jeu.model;

public abstract class Carte {
    private String m_nom;
    private int m_pointsVies = 0;
    private String m_type;

    public Carte(String m, int num, String type){
        this.m_nom = m;
        this.m_pointsVies = num;
        this.m_type = type;
    }

    public String getNom(){
        return this.m_nom;
    }

    public int getVie(){
        return this.m_pointsVies;
    }

    public String setPv(int n){
        if(n < 0){
            this.m_pointsVies += n;
            return "Dégats subit : " + n + ".";
        }else if(n > this.m_pointsVies){
            this.m_pointsVies = n;
            return "Point de mis à : " + n + ".";
        }
        return "Problème lors du changement de vie";
    }

    public boolean estVie(){
        return this.m_pointsVies != 0;
    }
}
