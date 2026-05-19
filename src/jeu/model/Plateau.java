package jeu.model;

import java.util.ArrayList;

public class Plateau {
    private ArrayList<Carte> m_ligneJ;
    private ArrayList<Carte> m_ligneE;
    private ArrayList<Carte> m_ligneEPT;

    public Plateau(){
        m_ligneJ = new ArrayList<Carte>(4);
        m_ligneE = new ArrayList<Carte>(4);
        m_ligneEPT = new ArrayList<Carte>(4);
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

    public Carte getCarteJoueur(int place){
        return m_ligneJ.get(place);
    }

    public Carte getCarteEnnemi(int place){
        return m_ligneE.get(place);
    }

}