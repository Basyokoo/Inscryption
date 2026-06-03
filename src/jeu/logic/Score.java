package jeu.logic;

import jeu.model.Carte;

public class Score {
    private int m_scoreJoueur;
    private int m_scoreEnnemi;

    public Score() {
        this.m_scoreEnnemi = 0;
        this.m_scoreJoueur = 0;
    }
    public void ajouterPointsJoueur(int points) {
        if (points > 0) {
            this.m_scoreJoueur += points;
            this.m_scoreEnnemi -= points;
        }
    }
    public void ajouterPointsEnnemi(int points) {
        if (points > 0) {
            this.m_scoreEnnemi += points;
            this.m_scoreJoueur -= points;
        }
    }

    public int getValeurEcart(){
        return this.m_scoreJoueur - this.m_scoreEnnemi;
    }

    public boolean estVictoireJoueur(){
        return this.m_scoreJoueur >= 5; // Respecte la consigne de déséquilibre de 5
    }

    public boolean estVictoireEnnemi(){
        return this.m_scoreEnnemi <= -5; // Respecte la consigne de déséquilibre de 5
    }

    public String getScoreJoueur(){
        return String.valueOf(this.m_scoreJoueur);
    }

    public String getScoreEnnemi(){
        return String.valueOf(this.m_scoreEnnemi);
    }

    public void setValeurEcart(int nouvelleValeur) {
        this.m_scoreJoueur = nouvelleValeur;
    }

    public void setValeurEnnemi(int nouvelleValeur) {
        this.m_scoreEnnemi = nouvelleValeur;
    }

    public void gererScoreJoueur(java.util.ArrayList<jeu.model.Carte> ligneJeu, int nbOs, int nbSang) {
        int nouveauScore = 0;
        for (jeu.model.Carte c : ligneJeu) {
            if (c != null && c.estAnimal()) {
                nouveauScore += c.getAttack() + c.getVie();
            }
        }
        nouveauScore += nbOs;
        nouveauScore += (nbSang * 2);

        // 3. Mise à jour de l'écart interne
        this.m_scoreJoueur = nouveauScore;
    }

    public void gererScoreEnnemi(int nbOs){
        this.ajouterPointsEnnemi(nbOs);
    }
}