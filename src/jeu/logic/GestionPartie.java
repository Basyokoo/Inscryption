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
    private Pouvoir m_pouv;
    private String m_action = "";
    private Score m_score;

    public GestionPartie(){
        this.m_numPartie = 0;
        this.m_victoire = 0;
        this.m_numTour = 0;
        this.m_main = new MainJoueur();
        this.m_plat = new Plateau();
        this.m_adv = new Adversaire();
        this.m_affichage = new AffichageConsole();
        this.m_pioche = new Pioche();
        this.m_j = new Joueur();
        this.m_score = new Score();
    }

    public boolean lancerJeu(){
        this.m_plat = new Plateau();
        this.m_j = new Joueur();
        this.m_adv = new Adversaire();
        this.m_main = new MainJoueur();
        this.m_pioche = new Pioche();
        this.m_score = new Score();
        this.m_pioche.initialiserPiocheDeBase();
        this.m_main.creeMain();

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
        this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());
        this.m_affichage.rafraichir();
        return true;
    }

    public boolean boucleTour() {
        this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());
        this.m_affichage.rafraichir();
        this.m_action = this.m_j.choisirAction();

        switch (this.m_action) {
            case "1":
                if (this.m_main.aPlace() && this.m_pioche.getNombreCartes() > 0) {
                    Animal cartePiochee = this.m_pioche.piocher();
                    int indexLibre = this.m_main.getCartesEnMain().indexOf(null);
                    this.m_main.ajouterMain(cartePiochee, indexLibre);
                }
                return true;

            case "2":
                this.propCarte();
                return true;

            case "3":
                System.out.println("Fin de votre phase d'action. Lancement des combats !");
                Combat combat = new Combat();
                combat.gererAttaquesFinTour(this.m_plat);
                combat.verifierMorts(this.m_plat, this.m_main, null);
                return false;

            default:
                return true;
        }
    }

    public boolean verifFinPartie(){
        if (this.m_score.estVictoireJoueur() || this.m_score.estVictoireEnnemi()){
            return true;
        }
        return false;
    }

    public boolean finPartie(){
        this.m_affichage.effacer();
        if (this.m_score.estVictoireJoueur()) {
            System.out.println("Félicitations, vous avez gagné la partie !");
            this.m_victoire++;
        } else {
            System.out.println("Défaite... L'adversaire a remporté la partie.");
        }
        this.m_affichage.rafraichir();
        return true;
    }

    public boolean propCarte(){
        return true;
    }
}