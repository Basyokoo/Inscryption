package jeu.view;

import jeu.model.Carte;

import java.util.ArrayList;

public class AffichageConsole {
    static final int m_SIZE_CARTE = 13;
    static final int m_SIZE_DECALAGE = 3;
    static final int m_HAUTEUR_CARTE = 7;
    static final int m_LARGEUR_ECRAN = m_SIZE_CARTE * 4 + m_SIZE_DECALAGE * 3 + 10;
    static final int m_HAUTEUR_ECRAN = m_HAUTEUR_CARTE * 4 + 10;

    private ArrayList<ArrayList<Character>> m_ecran;

    public boolean initEcran(){
        this.m_ecran = new ArrayList<>();
        for(int i = 0; i <= m_HAUTEUR_ECRAN; i++){
            ArrayList<Character> ligne = new ArrayList<Character>();
            for(int j = 0; j <= m_LARGEUR_ECRAN; j++){
                ligne.set(j,' ');
            }
            this.m_ecran.set(i, ligne);
        }
        return true;
    }

    public boolean ecrireChar(int ligne, int col, char c){
        this.m_ecran.get(ligne).set(col,c);
        return true;
    }

    public boolean ecrireTexte(int ligne, int col, String mot){
        for(int i = 0; i < mot.length();i++){
            ecrireChar(ligne, col + i, mot.charAt(i));
        }
        return true;
    }

    public int getColDep(int coor){
        return m_SIZE_CARTE + m_SIZE_DECALAGE * coor;
    }

    public boolean dessineCarte(Carte c, int ligneDep, int coor){
        int col = getColDep(coor);

        //Haut de carte
        ecrireChar(ligneDep, col, '*');
        ecrireChar(ligneDep, col+ m_SIZE_CARTE, '*');
        for(int i = col+1; i < col + m_SIZE_CARTE - 1; i++){
            ecrireChar(ligneDep, i, '-');
        }

        //Nom carte
        ecrireTexte(ligneDep+1, col, '|' + c.getNom());
        ecrireChar(ligneDep+1, col + m_SIZE_CARTE, '|');

        //Milieu de carte
        ecrireChar(ligneDep + 2, col, '|');
        ecrireChar(ligneDep + 2, col+ m_SIZE_CARTE, '|');
        for(int i = col+1; i < col + m_SIZE_CARTE - 1; i++){
            ecrireChar(ligneDep, i, '-');
        }

        //Pv
        ecrireTexte(ligneDep+3, col, String.format("| PV: %d     ",c.getVie()));
        ecrireChar(ligneDep+3, col + m_SIZE_CARTE, '|');

        //Attack
        ecrireTexte(ligneDep+3, col, String.format("| Att: %d    ",c.get()));
        ecrireChar(ligneDep+3, col + m_SIZE_CARTE, '|');


        return true;
    }
}
