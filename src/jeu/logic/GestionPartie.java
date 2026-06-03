package jeu.logic;

import jeu.model.Animal;
import jeu.model.Carte;
import jeu.model.Pouvoir;
import jeu.view.AffichageConsole;
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

    public String verifMort(Adversaire adv, Joueur jou){
        adv.verifierMorts(); jou.verifierMorts();
        return "Vérification succès";
    }

    public boolean debutTour() {
        this.m_numTour++;
        this.m_adv.planifierProchainTour();
        this.m_adv.envoyerIntentionsAuPlateau();

        rafraichirEcran();

        this.m_action = this.m_affichage.afficherChoix();
        return true;
    }

    public boolean boucleTour() {
        switch (this.m_action) {
            case "1":
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
                Animal cartePiochee = this.m_j.piocher();
                int indexLibre = this.m_j.getCartesEnMain().indexOf(null);
                if (indexLibre != -1) {
                    this.m_j.ajouterMain(cartePiochee, indexLibre);
                }

                rafraichirEcran();
                this.m_action = this.m_affichage.afficherChoix();
                if (this.m_numTour == 2) {
                    gererPierreSacrifice();
                }
                return true;

            case "2":
                this.placerCarte();

            case "3":


            case "4":
                this.m_adv.avancerLigne();

                Combat combat = new Combat();
                combat.gererAttaqueFinTour(this.m_j, this.m_adv, this.m_score);

                this.verifMort(m_adv,m_j);
                return false;

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

    private boolean sacrifice(){
        boolean terrainAUnEspace = false;
        ArrayList<String> casesPrises = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (this.m_j.getCarteJoueur(i) == null) {
                terrainAUnEspace = true;
                casesPrises.add("B" + (i + 1));
            }
        }
    }

    private boolean placerCarte(){
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
            this.m_affichage.afficherMessageAlerte("Terrain plein !");
            this.m_action = this.m_affichage.afficherChoix();
            return true;
        }

        rafraichirEcran();
        this.m_affichage.afficherMessageAlerte("Choisissez le numéro de la carte à jouer (1 à 4) :");
        String choixIndexCarte = this.m_affichage.afficherChoix();
        int idxCarte;
        try {
            idxCarte = Integer.parseInt(choixIndexCarte) - 1;
        } catch (NumberFormatException e) {
            return true;
        }

        if (idxCarte < 0 || idxCarte >= 4 || this.m_j.getCartesEnMain().get(idxCarte) == null) {
            return false;
        }

        Animal carteAJouer = this.m_j.getCartesEnMain().get(idxCarte);
        if (carteAJouer.getCoutOs() > this.m_j.getNbOsDisponibles() || carteAJouer.getCoutSang() > this.m_j.getNbSangDisponibles()) {
            rafraichirEcran();
            this.m_affichage.afficherMessageAlerte("Ressources insuffisantes ! " + "Requis: " + carteAJouer.getCoutOs() + " Os, " + carteAJouer.getCoutSang() + " Sang.");
            this.m_action = this.m_affichage.afficherChoix();
            return true;
        }


        rafraichirEcran();
        this.m_affichage.afficherMessageAlerte("Places libres : " + casesLibres + ". Entrez le code :");

        boolean bon = false;
        boolean bonPlace = false;
        int posTerrain = -1;

        while (!bonPlace){
            while(!bon) {
                String emplacementChoisi = this.m_affichage.afficherChoix().toUpperCase();


                if (emplacementChoisi.equals("B1") && casesLibres.contains("B1")) { posTerrain = 0; bon = true;}
                else if (emplacementChoisi.equals("B2") && casesLibres.contains("B2")) {posTerrain = 1; bon = true;}
                else if (emplacementChoisi.equals("B3") && casesLibres.contains("B3")) {posTerrain = 2; bon = true;}
                else if (emplacementChoisi.equals("B4") && casesLibres.contains("B4")) {posTerrain = 3; bon = true;}
                else {this.m_affichage.afficherMessageAlerte("Emplacement incorrect veuillez choisir entre " + casesLibres); bon = false;}

            }

            bon = false;
            if(m_j.aPlaceCarte(posTerrain)) {


                this.m_j.consommerOs(carteAJouer.getCoutOs());
                this.m_j.consommerSang(carteAJouer.getCoutSang());

                this.m_j.placerCarteJoueur(carteAJouer, posTerrain);
                this.m_j.enleverCarteJoueur(idxCarte);

                rafraichirEcran();
                this.m_action = this.m_affichage.afficherChoix();
                bonPlace = true;
            }
            else{
                this.m_affichage.afficherMessageAlerte("Erreur : places libres : " + casesLibres + ". Entrez le code :");
                this.m_action = this.m_affichage.afficherChoix();
            }
        }


        return true;
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
        this.m_affichage.afficherMessageAlerte("Choisissez une carte à sacrifier (1-4) :");
        String choix = this.m_affichage.afficherChoix();

        try {
            int index = Integer.parseInt(choix) - 1;
            Carte cibleSacrifice = this.m_j.getCarteJoueur(index);

            if (cibleSacrifice instanceof Animal) {
                Animal animalSacrifie = (Animal) cibleSacrifice;
                Pouvoir pouvoirRecupere = animalSacrifie.getPouvoir();

                if (pouvoirRecupere != null) {
                    this.m_affichage.afficherMessageAlerte("Choisissez une carte à booster (1-4) :");
                    String choixDest = this.m_affichage.afficherChoix();
                    int indexDest = Integer.parseInt(choixDest) - 1;
                    Carte cibleDest = this.m_j.getCarteJoueur(indexDest);

                    if (cibleDest instanceof Animal) {
                        ((Animal) cibleDest).setPouvoir(pouvoirRecupere);
                        this.m_j.placerCarteJoueur(null, index);
                        this.m_affichage.afficherMessageAlerte("Pouvoir " + pouvoirRecupere.getType() + " transféré !");
                    }
                } else {
                    this.m_affichage.afficherMessageAlerte("Cette carte n'a pas de pouvoir.");
                }
            }
        } catch (Exception e) {
            this.m_affichage.afficherMessageAlerte("Sacrifice annulé ou invalide.");
        }
    }

    public int getNumPartie() { return m_numPartie; }
}
