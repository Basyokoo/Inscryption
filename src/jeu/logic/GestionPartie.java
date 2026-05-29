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
        this.m_affichage.rafraichir();
        return true;
    }

    public boolean boucleTour() {
        this.m_action = this.m_j.choisirAction();

        switch (this.m_action) {
            case "1": // Piocher
                if (this.m_main.aPlace() && this.m_pioche.getNombreCartes() > 0) {
                    Animal cartePiochee = (Animal) this.m_pioche.piocher(); // Cast en Animal si nécessaire

                    // Trouver l'index du premier emplacement vide (null)
                    int indexLibre = this.m_main.getCartesEnMain().indexOf(null);

                    // Appeler la méthode avec les deux paramètres requis
                    if (indexLibre != -1) {
                        this.m_main.ajouterMain(cartePiochee, indexLibre);
                    }
                }
                return true;

            case "3":
                System.out.println("Fin de votre phase d'action. Lancement des combats !");
                Combat combat = new Combat();
                combat.gererAttaqueFinTour(this.m_plat, this.m_score);
                combat.verifierMorts(this.m_plat, this.m_main, null);
                return false;

            default:
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
        // Appelé après la 2ème partie
        System.out.println("Phase Pierre de Sacrifice : Choisissez une carte à sacrifier pour récupérer son pouvoir.");
        // Implémentation de la logique de transfert de pouvoir
    }

    public int getNumPartie() { return m_numPartie; }
}