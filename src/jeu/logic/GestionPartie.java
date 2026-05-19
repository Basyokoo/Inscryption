package jeu.logic;

public class GestionPartie {
    private int m_numPartie;
    private int m_scoreEcart;

    public GestionPartie(){
        this.m_numPartie = 0;
        this.m_scoreEcart = 0;
    }

    public boolean debutJeu(){
        return true;
    }

    public boolean debutPartie(){
        return true;
    }

    public boolean boucleTour(){
        return true;
    }

    public boolean verifFinPartie(){
        if (m_scoreEcart == 5){
            finPartie();
            return true;
        }
        return false;
    }

    public boolean finPartie(){
        return true;
    }

    public boolean propCarte(){
        return true;
    }
}
