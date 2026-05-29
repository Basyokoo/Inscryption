package jeu.logic;

import jeu.model.Animal;
import jeu.model.Carte;
import jeu.view.AffichageConsole;
import jeu.logic.*;
import java.util.ArrayList;

public class GestionPartie {
    private int m_numPartie;
    private int m_victoire;
    private int m_numTour;
    private Joueur m_j;
    private Adversaire m_adv;
    private AffichageConsole m_affichage;
    private String m_action = "";
    private Score m_score;

    public GestionPartie() {
        this.m_numPartie = 0;
        this.m_victoire = 0;
        this.m_numTour = 0;
        this.m_adv = new Adversaire();
        this.m_affichage = new AffichageConsole();
        this.m_j = new Joueur();
        this.m_score = new Score();
    }

    public boolean lancerJeu() {
        this.m_score = new Score();
        this.m_numTour = 0;
        this.m_j = new Joueur();
        this.m_adv = new Adversaire();

        this.m_j.initialiserPiocheDeBase();
        this.m_j.genererPioche();
        this.m_j.creeMain();

        this.m_affichage = new AffichageConsole();
        return this.m_affichage.initEcran();
    }

    public boolean debutTour() {
        this.m_numTour++;
        this.m_adv.planifierProchainTour(this.m_numTour);
        this.m_adv.envoyerIntentionsAuPlateau();

        rafraichirEcran();

        this.m_action = this.m_affichage.afficherChoix();
        return true;
    }

    public boolean boucleTour() {
        switch (this.m_action) {
            case "1": // PIOCHER UNE CARTE
                // Vérifier s'il y a de la place dans la main (contient au moins un élément null)
                if (!this.m_j.aPlace()) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Main pleine ! Impossible de piocher.");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                if (this.m_j.getNombreCartes() <= 0) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Pioche vide ! Impossible de piocher.");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                // Si de la place est disponible, on procède à la pioche
                Animal cartePiochee = this.m_j.piocher();
                int indexLibre = this.m_j.getCartesEnMain().indexOf(null);
                if (indexLibre != -1) {
                    this.m_j.ajouterMain(cartePiochee, indexLibre);
                }

                rafraichirEcran();
                this.m_action = this.m_affichage.afficherChoix();
                return true;

            case "2": // PLACER UNE CARTE DE LA MAIN SUR LE TERRAIN
                // 1. Vérifier s'il y a au moins une place de libre sur la ligne du joueur (ligneJ)
                boolean terrainAUnEspace = false;
                ArrayList<String> casesLibres = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (this.m_j.getCarteJoueur(i) == null) {
                        terrainAUnEspace = true;
                        casesLibres.add("B" + (i + 1));
                    }
                }

                if (!terrainAUnEspace) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Terrain plein ! Nulle part ou poser de carte.");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                // 2. Demander quelle carte de la main jouer
                rafraichirEcran();
                this.m_affichage.afficherMessageAlerte("Choisissez le numéro de la carte a jouer (1 a 4) :");
                String choixIndexCarte = this.m_affichage.afficherChoix();

                int idxCarte;
                try {
                    idxCarte = Integer.parseInt(choixIndexCarte) - 1;
                } catch (NumberFormatException e) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Saisie invalide !");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                if (idxCarte < 0 || idxCarte >= 4 || this.m_j.getCartesEnMain().get(idxCarte) == null) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Emplacement de main vide ou invalide !");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                Animal carteAJouer = this.m_j.getCartesEnMain().get(idxCarte);

                // 3. VÉRIFICATION DES COÛTS (OS OU SANG)
                // (Note : N'ayant pas la valeur courante de sang stockée, nous vérifions principalement le coût en Os disponible)
                if (carteAJouer.getCoutOs() > this.m_j.getNbOsDisponibles()) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Pas assez d'os ! Requis: " + carteAJouer.getCoutOs() + " | Possedes: " + this.m_j.getNbOsDisponibles());
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                // 4. Proposer les emplacements libres (ex: B1, B2...)
                rafraichirEcran();
                this.m_affichage.afficherMessageAlerte("Places libres : " + casesLibres + ". Entrez le code :");
                String emplacementChoisi = this.m_affichage.afficherChoix().toUpperCase();

                int posTerrain = -1;
                if (emplacementChoisi.equals("B1") && casesLibres.contains("B1")) posTerrain = 0;
                else if (emplacementChoisi.equals("B2") && casesLibres.contains("B2")) posTerrain = 1;
                else if (emplacementChoisi.equals("B3") && casesLibres.contains("B3")) posTerrain = 2;
                else if (emplacementChoisi.equals("B4") && casesLibres.contains("B4")) posTerrain = 3;

                if (posTerrain == -1) {
                    rafraichirEcran();
                    this.m_affichage.afficherMessageAlerte("Case indisponible ou code errone !");
                    this.m_action = this.m_affichage.afficherChoix();
                    return true;
                }

                // 5. Consommer les ressources et placer la carte
                this.m_j.consommerOs(carteAJouer.getCoutOs());
                this.m_j.placerCarteJoueur(carteAJouer, posTerrain);
                this.m_j.getCartesEnMain().set(idxCarte, null); // Enlever la carte de la main

                // Réafficher l'écran mis à jour et reproposer le menu principal du tour
                rafraichirEcran();
                this.m_action = this.m_affichage.afficherChoix();
                return true;

            case "3": // FINIR LE TOUR
                this.m_adv.avancerLigne();

                Combat combat = new Combat();
                combat.gererAttaqueFinTour(this.m_j, this.m_adv, this.m_score);

                this.m_j.verifierMorts();
                this.m_adv.verifierMorts();
                return false; // Met fin au cycle while de boucleTour() pour passer au tour suivant

            default:
                rafraichirEcran();
                this.m_action = this.m_affichage.afficherChoix();
                return true;
        }
    }

    private void rafraichirEcran() {
        this.m_affichage.dessinerJeuSansPlateau(
                this.m_j.getCartesLigneBas(),
                this.m_adv.getCartesIntentions(),
                this.m_adv.getCartesLigneHaut(),
                this.m_j,
                this.m_score.getValeurEcart()
        );
    }

    public boolean verifFinPartie() {
        return Math.abs(this.m_score.getValeurEcart()) >= 5;
    }

    public boolean finPartie() {
        this.m_affichage.effacer();
        if (this.m_score.estVictoireJoueur()) {
            System.out.println("Partie gagnee !");
            this.m_victoire++;
        } else {
            System.out.println("Partie perdue.");
        }
        this.m_numPartie++;
        return true;
    }

    public void gererPierreSacrifice() {
        System.out.println("Phase Pierre de Sacrifice : Choisissez une carte a sacrifier pour recuperer son pouvoir.");
    }

    public int getNumPartie() { return m_numPartie; }
}