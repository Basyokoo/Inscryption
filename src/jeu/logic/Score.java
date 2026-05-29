package jeu.logic;

public class Score {
    private int m_score;

    public Score() {
        this.m_score = 0;
    }

    // Ajoute des points en faveur du joueur (augmente l'écart positivement)
    public void ajouterPointsJoueur(int points) {
        if (points > 0) {
            this.m_score += points;
        }
    }

    // Retire des points (ou ajoute des points à l'ennemi, ce qui diminue l'écart)
    public void ajouterPointsEnnemi(int points) {
        if (points > 0) {
            this.m_score -= points; // CORRECTION ICI : on soustrait pour l'ennemi
        }
    }

    public int getValeurEcart(){
        return this.m_score;
    }

    public boolean estVictoireJoueur(){
        return this.m_score >= 5; // Respecte la consigne de déséquilibre de 5
    }

    public boolean estVictoireEnnemi(){
        return this.m_score <= -5; // Respecte la consigne de déséquilibre de 5
    }

    public String getScore(){
        return String.valueOf(this.m_score);
    }
}