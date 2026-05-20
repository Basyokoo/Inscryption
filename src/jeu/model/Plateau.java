package jeu.model;

import java.util.ArrayList;
import java.util.Random;

public class Plateau {
    private ArrayList<Carte> m_ligneJ;
    private ArrayList<Carte> m_ligneE;
    private ArrayList<Carte> m_ligneEPT;
    private int m_nbObsE = 0;
    private int m_nbobsJ = 0;

    public Plateau(){
        m_ligneJ = new ArrayList<Carte>(4);
        m_ligneE = new ArrayList<Carte>(4);
        m_ligneEPT = new ArrayList<Carte>(4);
        this.placerObst();
    }

    public boolean placerCarteJoueur(Carte c,int pos){
        this.m_ligneJ.set(pos, c);
        return true;
    }

    public boolean placerCarteEnnemi(Carte c,int pos){
        this.m_ligneE.set(pos, c);
        return true;
    }

    public boolean avancerLigne(){
        for (int i = 0; i < this.m_ligneEPT.size(); i++){
            if(this.m_ligneE.get(i) != null){
                placerCarteEnnemi(this.m_ligneEPT.get(i), i);
                this.m_ligneEPT.set(i, null);
            }
        }
        return true;
    }

    public boolean placerObst(){
        Random rNum = new Random();
        Obstacle obs;
        int rnd1 = rNum.nextInt(7);
        for (int i = 0; i <= rnd1; i++){
            int sapOuKayou = rNum.nextInt(2);
            if(sapOuKayou == 0){
                obs = new Obstacle("Rocher", 5);
            }else{
                obs = new Obstacle("Sapin", 3);
            }
            int rndPos = rNum.nextInt(4);
            int rndTab = rNum.nextInt(2);
            if(rndTab == 0 && this.m_nbobsJ < 4){
                this.m_ligneJ.set(rndPos, obs);
            }else if (rndTab == 1 && this.m_nbObsE < 4){
                this.m_ligneE.set(rndPos, obs);
            }else{
                return false;
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

}