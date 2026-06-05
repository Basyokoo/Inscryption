package jeu.logic;

public class Score {
    private int m_balanceScore;

    public Score() {
        this.m_balanceScore = 0;
    }

    public void ajouterPointsJoueur(int points) {
        if (points > 0) {
            this.m_balanceScore += points;
        }
    }

    public void ajouterPointsEnnemi(int points) {
        if (points > 0) {
            this.m_balanceScore -= points;
        }
    }

    public int getValeurEcart() {
        return this.m_balanceScore;
    }

    public boolean estVictoireJoueur() {
        return this.m_balanceScore >= 5;
    }

    public boolean estVictoireEnnemi() {
        return this.m_balanceScore <= -5;
    }

    public String getScoreJoueur() {
        return String.valueOf(this.m_balanceScore > 0 ? this.m_balanceScore : 0);
    }

    public String getScoreEnnemi() {
        return String.valueOf(this.m_balanceScore < 0 ? Math.abs(this.m_balanceScore) : 0);
    }

    public void setValeurJoueur(int nouvelleValeur) {
        this.m_balanceScore = nouvelleValeur;
    }

    public void setValeurEnnemi(int nouvelleValeur) {
        this.m_balanceScore = -nouvelleValeur;
    }
}
