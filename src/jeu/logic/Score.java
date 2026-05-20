package jeu.logic;

public class Score {
    private int  m_score;
    public Score(int score) {
        this.m_score = score;
    }
    public void ajouterPointsJoueur(int pointsJoueur) {
        this.m_score += pointsJoueur;
    }
    public void ajouterPointsEnnemie(int pointsEnnemie) {
        this.m_score += pointsEnnemie;
    }
    public int getValeurEcart(){
        return this.m_score;
    }
    public boolean estVictoireJoueur(){
        return this.m_score > 0;
    }
    public boolean estVictoireEnnemie(){
        return this.m_score < 0;
    }
}
