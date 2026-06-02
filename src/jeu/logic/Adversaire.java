package jeu.logic;

import jeu.model.*;
import java.util.ArrayList;
import java.util.Random;

public class Adversaire {
    private Carte[] m_actionsPlanifiees;
    private ArrayList<Carte> m_ligneE;
    private ArrayList<Carte> m_ligneEPT;
    private ArrayList<Pouvoir> m_pouvoir;
    private int m_nbObsE = 0;

    public Adversaire(){
        this.m_actionsPlanifiees = new Carte[4];
        this.m_ligneE = new ArrayList<Carte>();
        this.m_ligneEPT = new ArrayList<Carte>();
        this.m_pouvoir = new ArrayList<Pouvoir>();

        for (int i = 0; i < 4; i++) {
            this.m_pouvoir.add(null);
            this.m_ligneE.add(null);
            this.m_ligneEPT.add(null);
        }
        this.placerObst();
    }

    public boolean placerCarteEnnemi(Carte c, int pos){
        if (pos >= 0 && pos < 4) {
            this.m_ligneE.set(pos, c);
            return true;
        }
        return false;
    }

    public boolean setCarteIntention(Carte c, int pos) {
        if (pos >= 0 && pos < 4) {
            this.m_ligneEPT.set(pos, c);
            return true;
        }
        return false;
    }

    public boolean avancerLigne(){
        for (int i = 0; i < 4; i++){
            if(this.m_ligneEPT.get(i) != null && estCaseLibre(i)){
                placerCarteEnnemi(this.m_ligneEPT.get(i), i);
                this.m_ligneEPT.set(i, null);
            }
        }
        return true;
    }

    public Carte getCarteEnnemi(int place){
        return m_ligneE.get(place);
    }

    public ArrayList<Carte> getCartesIntentions() {
        return m_ligneEPT;
    }

    public ArrayList<Carte> getCartesLigneHaut() {
        return m_ligneE;
    }

    public Carte[] getActionsPlani() {
        return this.m_actionsPlanifiees;
    }

    public void planifierProchainTour(int numTour) {
        for (int i = 0; i < 4; i++) {
            this.m_actionsPlanifiees[i] = null;
        }

        if (numTour == 1) {
            this.m_actionsPlanifiees[0] = new Animal("Loup", 4, 2, 2, 0, false);
        } else if (numTour == 2) {
            this.m_actionsPlanifiees[1] = new Obstacle("Souche", 3);
            this.m_actionsPlanifiees[3] = new Animal("Corbeau", 3, 2, 1, 0, true);
        } else {
            this.m_actionsPlanifiees[2] = new Animal("Ours", 6, 4, 3, 0, false);
        }
    }

    public void envoyerIntentionsAuPlateau() {
        for (int i = 0; i < 4; i++) {
            if (estCaseLibre(i)) {
                this.setCarteIntention(this.m_actionsPlanifiees[i], i);
            } else {
                // Optionnel : Gérer le cas où l'adversaire "perd" son action s'il est bloqué
                this.setCarteIntention(null, i);
            }
        }
    }

    public boolean placerObst(){
        Random rNum = new Random();
        int rnd1 = rNum.nextInt(2) + 1;

        for (int i = 0; i < rnd1; i++){
            Obstacle obs;
            int sapOuKayou = rNum.nextInt(2);
            if(sapOuKayou == 0){
                obs = new Obstacle("Rocher", 5);
            } else {
                obs = new Obstacle("Sapin", 3);
            }

            int rndPos = rNum.nextInt(4);
            if (this.m_nbObsE < 4 && this.m_ligneE.get(rndPos) == null){
                this.m_ligneE.set(rndPos, obs);
                this.m_nbObsE++;
            }
        }
        return true;
    }

    public void verifierMorts() {
        for (int i = 0; i < m_ligneE.size(); i++) {
            Carte c = m_ligneE.get(i);
            if (c != null && !c.estVie()) {
                m_ligneE.set(i, null);
            }
        }
    }
    public boolean estCaseLibre(int pos) {
        // Vérifie si la case dans la ligne active est vide
        return this.m_ligneE.get(pos) == null;
    }

}