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
    private String m_action = "";
    private Score m_score;

    public GestionPartie() {
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

    public boolean lancerJeu() {
        this.m_plat = new Plateau();
        this.m_main = new MainJoueur();
        this.m_pioche = new Pioche();
        this.m_score = new Score();
        this.m_numTour = 0;

        this.m_pioche.initialiserPiocheDeBase();
        this.m_main.creeMain();

        this.m_affichage = new AffichageConsole();
        return this.m_affichage.initEcran();
    }

    public boolean debutTour() {
        this.m_numTour++;
        this.m_adv.planifierProchainTour(this.m_numTour);
        this.m_adv.envoyerIntentionsAuPlateau(this.m_plat);
        this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());

        this.m_action = this.m_affichage.afficherChoix();
        return true;
    }

    public boolean boucleTour() {
        switch (this.m_action) {
            case "1": // Action Piocher
                if (this.m_main.aPlace() && this.m_pioche.getNombreCartes() > 0) {
                    Animal cartePiochee = (Animal) this.m_pioche.piocher();
                    int indexLibre = this.m_main.getCartesEnMain().indexOf(null);
                    if (indexLibre != -1) {
                        this.m_main.ajouterMain(cartePiochee, indexLibre);
                    }
                }

                this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());
                this.m_action = this.m_affichage.afficherChoix();
                return true;

            case "2": // Action Poser une carte (Exemple de squelette pour ton choix 2)
                this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());
                this.m_action = this.m_affichage.afficherChoix();
                return true;

            case "3": // Terminer le tour et lancer les combats
                Combat combat = new Combat();
                combat.gererAttaqueFinTour(this.m_plat, this.m_score);
                combat.verifierMorts(this.m_plat, this.m_main, null);
                return false;

            default:
                // Si l'utilisateur tape n'importe quoi d'autre, on réaffiche simplement la boîte
                this.m_affichage.dessinerJeuComplet(this.m_plat, this.m_main, this.m_score.getValeurEcart());
                this.m_action = this.m_affichage.afficherChoix();
                return true;
        }
    }

    public boolean verifFinPartie() {
        return Math.abs(this.m_score.getValeurEcart()) >= 5;
    }

    public boolean finPartie() {
        this.m_affichage.effacer();
        if (this.m_score.estVictoireJoueur()) {
            System.out.println("Partie gagnée !");
            this.m_victoire++;
        } else {
            System.out.println("Partie perdue.");
        }
        this.m_numPartie++;
        return true;
    }

    public void gererPierreSacrifice() {
        System.out.println("Phase Pierre de Sacrifice : Choisissez une carte à sacrifier pour récupérer son pouvoir.");
    }

    public int getNumPartie() { return m_numPartie; }
}