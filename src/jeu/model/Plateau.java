package jeu.model;

import java.util.ArrayList;
import java.util.Random;

public class Plateau {
    private ArrayList<Carte> m_ligneJ;
    private ArrayList<Carte> m_ligneE;
    private ArrayList<Carte> m_ligneEPT; // Ligne des intentions (Ennemi Prochain Tour)
    private ArrayList<Pouvoir> m_pouvoir;
    private int m_nbObsE = 0;
    private int m_nbobsJ = 0;

    public Plateau(){
        m_ligneJ = new ArrayList<Carte>();
        m_ligneE = new ArrayList<Carte>();
        m_ligneEPT = new ArrayList<Carte>();
        m_pouvoir = new ArrayList<Pouvoir>();
        for (int i = 0; i < 4; i++) {
            m_ligneJ.add(null);
            m_ligneE.add(null);
            m_ligneEPT.add(null);
        }

        this.placerObst();
    }

    public boolean placerCarteJoueur(Carte c, int pos){
        if (pos >= 0 && pos < 4) {
            this.m_ligneJ.set(pos, c);
            return true;
        }
        return false;
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
            if(this.m_ligneE.get(i) == null && this.m_ligneEPT.get(i) != null){
                placerCarteEnnemi(this.m_ligneEPT.get(i), i);
                this.m_ligneEPT.set(i, null);
            }
        }
        return true;
    }

    public boolean placerObst(){
        Random rNum = new Random();
        int rnd1 = rNum.nextInt(3) + 1;

        for (int i = 0; i < rnd1; i++){
            Obstacle obs;
            int sapOuKayou = rNum.nextInt(2);
            if(sapOuKayou == 0){
                obs = new Obstacle("Rocher", 5);
            } else {
                obs = new Obstacle("Sapin", 3);
            }

            int rndPos = rNum.nextInt(4);
            int rndTab = rNum.nextInt(2);

            if(rndTab == 0 && this.m_nbobsJ < 4 && this.m_ligneJ.get(rndPos) == null){
                this.m_ligneJ.set(rndPos, obs);
                this.m_nbobsJ++;
            } else if (rndTab == 1 && this.m_nbObsE < 4 && this.m_ligneE.get(rndPos) == null){
                this.m_ligneE.set(rndPos, obs);
                this.m_nbObsE++;
            }
        }
        return true;
    }

    public Carte getCarteJoueur(int place){
        return m_ligneJ.get(place);
    }

    public Carte getCarteEnnemi(int place){
        return m_ligneE.get(place);
    }

    public ArrayList<Carte> getCartesIntentions() { return m_ligneEPT; }
    public ArrayList<Carte> getCartesLigneHaut() { return m_ligneE; }
    public ArrayList<Carte> getCartesLigneBas() { return m_ligneJ; }

    //public
}