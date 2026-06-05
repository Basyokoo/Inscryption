package jeu.logic;

import jeu.model.Animal;
import jeu.model.Carte;
import jeu.model.Pouvoir;
import jeu.view.AffichageConsole;
import java.util.ArrayList;

public class GestionPartie {
    private int m_numPartie;
    private int m_victoireJ;
    private int m_victoireE;
    private int m_numTour;
    private Joueur m_j;
    private Adversaire m_adv;
    private AffichageConsole m_affichage;
    private Combat m_combat;
    private String m_action = "";
    private Score m_score;
    private boolean piocherTour = true;
    private String m_messageAlerteCourant = "";

    public GestionPartie() {
        this.m_numPartie = 0;
        this.m_victoireJ = 0;
        this.m_victoireE = 0;
        this.m_numTour = 0;
        this.m_adv = new Adversaire();
        this.m_affichage = new AffichageConsole();
        this.m_j = new Joueur();
        this.m_score = new Score();
        this.m_combat = new Combat();
    }

    public boolean lancerJeu() {
        this.preparerNouvelleManche();
        return this.m_affichage.initEcran();
    }

    public void preparerNouvelleManche() {
        this.m_j.clear();
        this.m_adv.clear();
        for(int i = 0; i < 4; i++) {
            this.m_j.ajouterCartesMain(null);
            this.m_adv.addCarte(null);
        }

        this.m_adv.placerObst();
        this.m_j.setSangJoueur(0);
        this.m_j.setNbOsDisponibles(0);

        this.m_j.initialiserPiocheDeBase();
        this.m_j.genererPioche();
        this.m_j.creeMain();
        this.m_messageAlerteCourant = "";
    }

    public String verifMort(Adversaire adv, Joueur jou){
        adv.verifierMorts(); jou.verifierMorts();
        return "Vérification succès";
    }

    public boolean debutTour() {
        this.m_numTour++;
        this.m_adv.planifierProchainTour();
        this.m_adv.envoyerIntentionsAuPlateau();
        return true;
    }

    public void demanderActionJoueur() {
        rafraichirEcran();

        if (!this.m_messageAlerteCourant.isEmpty()) {
            this.m_affichage.afficherMessageAlerte(this.m_messageAlerteCourant);
        }

        this.m_action = this.m_affichage.afficherChoix();
    }

    public boolean boucleTour() {
        String actionAExecuter = this.m_action;
        this.m_messageAlerteCourant = "";

        switch (actionAExecuter) {
            case "1":
                if (this.piocherTour) {
                    if (!this.m_j.aPlace()) {
                        this.m_messageAlerteCourant = "Main pleine ! Impossible de piocher.";
                        this.m_action = "";
                        return true;
                    }

                    if (this.m_j.getNombreCartes() <= 0) {
                        this.m_messageAlerteCourant = "Pioche vide ! Impossible de piocher.";
                        this.m_action = "";
                        return true;
                    }

                    Animal cartePiochee = this.m_j.piocher();
                    int indexLibre = this.m_j.getCartesEnMain().indexOf(null);
                    if (indexLibre != -1) {
                        this.m_j.ajouterMain(cartePiochee, indexLibre);
                    }

                    if (this.m_numTour == 2) {
                        gererPierreSacrifice();
                    }

                    this.m_action = "";
                    this.piocherTour = false;
                    return true;
                } else {
                    this.m_messageAlerteCourant = "Attention vous ne pouvez piocher qu'une seule fois !";
                    this.m_action = "";
                    return true;
                }

            case "2":
                this.placerCarte();
                this.m_action = "";
                return true;

            case "3":
                this.sacrifice();
                this.m_action = "";
                return true;

            case "4":
                this.finTour();
                return false;

            default:
                this.m_action = "";
                return true;
        }
    }

    private void finTour(){
        this.m_adv.avancerLigne();

        String logsCombat = m_combat.gererAttaqueFinTour(this.m_j, this.m_adv, this.m_score);
        this.m_affichage.ajouterLog(logsCombat);

        this.m_adv.verifierMorts();
        this.m_j.verifierMorts();

        for (int i = 0; i < 4; i++) {
            Carte carteJ = m_j.getCarteJoueur(i);
            Carte carteE = m_adv.getCarteEnnemi(i);

            boolean joueurVivant = (carteJ != null && carteJ.estAnimal());
            boolean ennemiVivant  = (carteE != null && carteE.estAnimal());

            if (joueurVivant && ennemiVivant) {
                m_j.appliquerPouvoir((Animal) carteJ, carteE);
                m_adv.appliquerPouvoir((Animal) carteE, carteJ);
            }
            else if (joueurVivant) {
                m_j.appliquerPouvoir((Animal) carteJ, carteE);
            }
            else if (ennemiVivant) {
                m_adv.appliquerPouvoir((Animal) carteE, carteJ);
            }
        }

        this.m_adv.verifierMorts();
        this.m_j.verifierMorts();

        this.m_action = "";
        this.piocherTour = true;
    }

    private void rafraichirEcran() {
        this.m_affichage.dessinerJeuSansPlateau(
                this.m_j.getCartesLigneBas(),
                this.m_adv.getCartesIntentions(),
                this.m_adv.getCartesLigneHaut(),
                this.m_j,
                this.m_score.getValeurEcart(),
                this.m_numPartie,
                this.m_victoireJ,
                this.m_victoireE
        );
    }

    private boolean sacrifice(){
        boolean terrainAUnEspace = false;
        ArrayList<String> casesPrises = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (this.m_j.getCarteJoueur(i) != null && this.m_j.getCarteJoueur(i).estAnimal()) {
                terrainAUnEspace = true;
                casesPrises.add("B" + (i + 1));
            }
        }
        if (!terrainAUnEspace) {
            this.m_messageAlerteCourant = "Pas de sacrifice à faire !";
            return true;
        }

        rafraichirEcran();
        this.m_affichage.afficherMessageAlerte("Animal(aux) a sacrifier : " + casesPrises + ". Entrez le code :");

        boolean bon = false;
        int posTerrain = -1;

        while(!bon) {
            String emplacementChoisi = this.m_affichage.afficherChoix().toUpperCase();

            if (emplacementChoisi.equals("B1") && casesPrises.contains("B1")) { posTerrain = 0; bon = true;}
            else if (emplacementChoisi.equals("B2") && casesPrises.contains("B2")) {posTerrain = 1; bon = true;}
            else if (emplacementChoisi.equals("B3") && casesPrises.contains("B3")) {posTerrain = 2; bon = true;}
            else if (emplacementChoisi.equals("B4") && casesPrises.contains("B4")) {posTerrain = 3; bon = true;}
            else {
                this.m_affichage.afficherMessageAlerte("Emplacement incorrect veuillez choisir entre " + casesPrises);
                bon = false;
            }
        }

        if (m_j.getCartesLigneBas(posTerrain).getPouvoir() != null){
            if (!m_j.getPvrType(posTerrain).equals("NV")){
                m_j.tuer(posTerrain);
            } else {
                if (m_j.getCartesLigneBas(posTerrain).getPouvoir().getActive() > 0){
                    m_j.getCartesLigneBas(posTerrain).getPouvoir().modifActive(-1);
                    m_j.incrSang();
                    m_j.ajouterOs(1);
                } else {
                    m_j.tuer(posTerrain);
                }
            }
        } else {
            m_j.incrSang();
            m_j.tuer(posTerrain);
        }

        this.m_messageAlerteCourant = "Animal sacrifie ! Choisissez votre prochaine action.";
        return true;
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
            this.m_messageAlerteCourant = "Terrain plein !";
            return true;
        }

        rafraichirEcran();
        this.m_affichage.afficherMessageAlerte("Choisissez le numéro de la carte à jouer (1 à 4) :");
        String choixIndexCarte = this.m_affichage.afficherChoix();
        int idxCarte;
        try {
            idxCarte = Integer.parseInt(choixIndexCarte) - 1;
        } catch (NumberFormatException e) {
            this.m_messageAlerteCourant = "Saisie invalide (chiffre requis).";
            return true;
        }

        if (idxCarte < 0 || idxCarte >= 4 || this.m_j.getCartesEnMain().get(idxCarte) == null) {
            this.m_messageAlerteCourant = "Carte invalide !";
            return true;
        }

        Animal carteAJouer = this.m_j.getCartesEnMain().get(idxCarte);
        if (carteAJouer.getCoutOs() > this.m_j.getNbOsDisponibles() || carteAJouer.getCoutSang() > this.m_j.getSangJoueur()) {
            this.m_messageAlerteCourant = "Ressources insuffisantes !";
            return true;
        }

        rafraichirEcran();
        this.m_affichage.afficherMessageAlerte("Places libres : " + casesLibres + ". Entrez le code :");

        boolean bonPlace = false;
        int posTerrain = -1;

        while (!bonPlace) {
            String emplacementChoisi = this.m_affichage.afficherChoix().toUpperCase();

            if (emplacementChoisi.equals("B1") && casesLibres.contains("B1")) { posTerrain = 0; bonPlace = true; }
            else if (emplacementChoisi.equals("B2") && casesLibres.contains("B2")) { posTerrain = 1; bonPlace = true; }
            else if (emplacementChoisi.equals("B3") && casesLibres.contains("B3")) { posTerrain = 2; bonPlace = true; }
            else if (emplacementChoisi.equals("B4") && casesLibres.contains("B4")) { posTerrain = 3; bonPlace = true; }
            else {
                this.m_affichage.afficherMessageAlerte("Emplacement incorrect veuillez choisir entre " + casesLibres);
            }
        }

        if(m_j.aPlaceCarte(posTerrain)) {
            this.m_j.consommerOs(carteAJouer.getCoutOs());
            this.m_j.consommerSang(carteAJouer.getCoutSang());

            this.m_j.placerCarteJoueur(carteAJouer, posTerrain);
            this.m_j.enleverCarteJoueur(idxCarte);

            this.m_messageAlerteCourant = "Carte placee ! Choisissez votre prochaine action.";
        }
        return true;
    }

    public void gererPierreSacrifice() {
        this.m_affichage.afficherMessageAlerte("Choisissez une carte à sacrifier (1-4) :");
        String choix = this.m_affichage.afficherChoix();

        try {
            int index = Integer.parseInt(choix) - 1;
            Carte cibleSacrifice = this.m_j.getCarteJoueur(index);

            if (cibleSacrifice.estAnimal()) {
                Animal animalSacrifie = (Animal) cibleSacrifice;
                Pouvoir pouvoirRecupere = animalSacrifie.getPouvoir();

                if (pouvoirRecupere != null) {
                    this.m_affichage.afficherMessageAlerte("Choisissez une carte à booster (1-4) :");
                    String choixDest = this.m_affichage.afficherChoix();
                    int indexDest = Integer.parseInt(choixDest) - 1;
                    Carte cibleDest = this.m_j.getCarteJoueur(indexDest);

                    if (cibleDest.estAnimal()) {
                        cibleDest.setPouvoir(pouvoirRecupere);
                        this.m_j.placerCarteJoueur(null, index);
                        this.m_messageAlerteCourant = "Pouvoir " + pouvoirRecupere.getType() + " transféré !";
                    }
                } else {
                    this.m_messageAlerteCourant = "Cette carte n'a pas de pouvoir.";
                }
            }
        } catch (Exception e) {
            this.m_messageAlerteCourant = "Sacrifice annulé ou invalide.";
        }
    }

    public boolean verifFinManche() {
        return Math.abs(this.m_score.getValeurEcart()) >= 5;
    }

    public boolean finManche() {
        Object gagnantManche = (this.m_score.estVictoireJoueur()) ? this.m_j : this.m_adv;

        if (this.m_score.estVictoireJoueur()) {
            this.m_victoireJ++;
            this.m_affichage.ajouterLog("--- VICTOIRE DE LA MANCHE (JOUEUR) ---");
        } else {
            this.m_victoireE++;
            this.m_affichage.ajouterLog("--- VICTOIRE DE LA MANCHE (ENNEMI) ---");
        }

        this.m_affichage.afficherFin(gagnantManche, false);

        this.m_score = new Score();
        this.m_numTour = 0;
        this.m_action = "";
        this.m_messageAlerteCourant = "";

        this.preparerNouvelleManche();

        this.m_numPartie++;
        return true;
    }

    public boolean finPartie() {
        Object gagnantFinal = (this.m_victoireJ >= 2) ? this.m_j : this.m_adv;

        this.m_affichage.afficherFin(gagnantFinal, true);

        if (this.m_victoireJ >= 2) {
            System.out.println("Match gagné par le Joueur !");
        } else {
            System.out.println("Match gagné par le Robot.");
        }
        return true;
    }

    public boolean verifFinPartie() {
        return m_victoireJ >= 2 || m_victoireE >= 2;
    }
}