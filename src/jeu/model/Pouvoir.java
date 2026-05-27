package jeu.model;

public class Pouvoir {
    private String m_type;
    private String m_nom;
    private int m_activeDans = 0;

    // dans le jeu la liste des pouvoirs actifs sera stocker dans un tableau parcouru au debut d'un tour pour savoir
    //quels effets sont a faire.

    public Pouvoir(String nom, String type, int active){
        this.m_activeDans = active;
        this.m_type = type;
        this.m_nom = nom;
    }
}
