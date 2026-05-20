package jeu.logic;

import jeu.model.*;

public class GestionPartie {
    private int m_numPartie;
    private int m_victoire;
    private Plateau m_plat;
    private Joueur m_j;
    private Pioche m_pioche;
    private MainJoueur m_main;
    private Adversaire m_adv;

    public GestionPartie(){
        this.m_numPartie = 0;
        this.m_victoire = 0;
    }

    public boolean lancerJeu(){
        this.m_plat = new Plateau();
        this.m_j = new Joueur();
        this.m_adv = new Adversaire();
        return true;
    }

    public boolean debutPartie(){
        this.m_pioche = new Pioche();
        this.m_main = new MainJoueur();
        // affichage plateau avec main et carte
        this.m_j.choisirAction();
        return true;
    }

    public boolean boucleTour(){
        return true;
    }

    public boolean verifFinPartie(){
        if (this.m_victoire == 5){
            finPartie();
            return true;
        }
        return false;
    }

    public boolean finPartie(){
        return true;
    }

    public boolean propCarte(){
        return true;
    }
}
