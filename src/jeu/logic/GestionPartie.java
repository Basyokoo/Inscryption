package jeu.logic;

import jeu.model.*;
import jeu.view.AffichageConsole;

public class GestionPartie {
    private int m_numPartie;
    private int m_victoire;
    private int m_numTour;
    private Plateau m_plat;
    private Joueur m_j;
    private Pioche m_pioche;
    private MainJoueur m_main;
    private Adversaire m_adv;
    private AffichageConsole m_affichage;

    public GestionPartie(){
        this.m_numPartie = 0;
        this.m_victoire = 0;
        this.m_numTour = 0;
    }

    public boolean lancerJeu(){
        this.m_plat = new Plateau();
        this.m_j = new Joueur();
        this.m_adv = new Adversaire();

        this.m_affichage = new AffichageConsole();
        if (!this.m_affichage.initEcran()) {
            System.out.println("Erreur : Impossible d'initialiser l'écran.");
            return false;
        }

        return true;
    }

    public boolean debutTour() {
        this.m_numTour++;

        this.m_adv.planifierProchainTour(this.m_numTour);
        this.m_adv.envoyerIntentionsAuPlateau(this.m_plat);

        this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_victoire);

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
