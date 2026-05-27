package jeu.view;

import jeu.model.*;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class AffichageConsole {
    static final int m_SIZE_CARTE = 13;
    static final int m_SIZE_DECALAGE = 3;
    static final int m_HAUTEUR_CARTE = 7;
    static final int m_LARGEUR_ECRAN = m_SIZE_CARTE * 4 + m_SIZE_DECALAGE * 3 + 10;
    static final int m_HAUTEUR_ECRAN = m_HAUTEUR_CARTE * 3 + 2 + 15;

    private Screen m_screen;
    private TextGraphics m_graphics;

    public boolean initEcran() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setInitialTerminalSize(new TerminalSize(m_LARGEUR_ECRAN, m_HAUTEUR_ECRAN));

            this.m_screen = new TerminalScreen(factory.createTerminal());
            this.m_screen.startScreen();
            this.m_screen.setCursorPosition(null);
            this.m_graphics = m_screen.newTextGraphics();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getColDep(int coor) {
        return 5 + (m_SIZE_CARTE + m_SIZE_DECALAGE) * coor;
    }

    public void dessinerJeuComplet(Plateau plateau, MainJoueur main, int score) {
        this.effacer();

        int ligneIntentions = 1;
        int ligneAdverse    = ligneIntentions + m_HAUTEUR_CARTE + 1;  // = 9
        int ligneJoueur     = ligneAdverse    + m_HAUTEUR_CARTE + 1;  // = 17

        // Ligne intentions adversaire (A1-A4)
        for (int i = 0; i < 4; i++) {
            Carte c = plateau.getCartesIntentions().get(i);
            if (c != null) {
                this.dessineCarte(c, ligneIntentions, i);
            } else {
                this.dessinerCaseVide("A" + (i + 1), ligneIntentions, i);
            }
        }

        // Ligne adversaire (A1-A4)
        for (int i = 0; i < 4; i++) {
            Carte c = plateau.getCartesLigneHaut().get(i);
            if (c != null) {
                this.dessineCarte(c, ligneAdverse, i);
            } else {
                this.dessinerCaseVide("A" + (i + 1), ligneAdverse, i);
            }
        }

        // Ligne joueur (B1-B4)
        for (int i = 0; i < 4; i++) {
            Carte c = plateau.getCartesLigneBas().get(i);
            if (c != null) {
                this.dessineCarte(c, ligneJoueur, i);
            } else {
                this.dessinerCaseVide("B" + (i + 1), ligneJoueur, i);
            }
        }

        int ligneScore = ligneJoueur + m_HAUTEUR_CARTE + 1;  // = 25
        this.m_graphics.putString(2, ligneScore,     "Score balance : " + score);
        this.m_graphics.putString(2, ligneScore + 2, "Votre main :");

        int ligneTexteMain = ligneScore + 3;
        int numeroCarte = 1;
        if (main.getCartesEnMain() != null) {
            for (Carte c : main.getCartesEnMain()) {
                String infoMain = String.format("  %d. %-10s PV: %d", numeroCarte, c.getNom(), c.getVie());
                this.m_graphics.putString(2, ligneTexteMain, infoMain);
                ligneTexteMain++;
                numeroCarte++;
            }
        } else {
            this.m_graphics.putString(2, ligneTexteMain, "Rien a afficher");
        }

        this.rafraichir();
    }

    public boolean dessineCarte(Carte c, int ligneDep, int coor) {
        if (m_graphics == null) return false;

        int col = getColDep(coor);

        m_graphics.putString(col, ligneDep, "╭");
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.putString(i, ligneDep, "─");
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep, "╮");

        String nomFormate = String.format(" %-" + (m_SIZE_CARTE - 2) + "s", c.getNom());
        m_graphics.putString(col,               ligneDep + 1, "│");
        m_graphics.putString(col + 1,           ligneDep + 1, nomFormate);
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 1, "│");

        m_graphics.putString(col, ligneDep + 2, "├");
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.putString(i, ligneDep + 2, "─");
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 2, "┤");

        String pvFormate = String.format(" PV: %-" + (m_SIZE_CARTE - 6) + "d", c.getVie());
        m_graphics.putString(col,                ligneDep + 3, "│");
        m_graphics.putString(col + 1,            ligneDep + 3, pvFormate);
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 3, "│");

        m_graphics.putString(col, ligneDep + 4, "│");
        if (c instanceof Animal ani) {
            String attFormate = String.format(" Att: %-" + (m_SIZE_CARTE - 7) + "d", ani.getAttack());
            m_graphics.putString(col + 1, ligneDep + 4, attFormate);
        } else {
            m_graphics.putString(col + 1, ligneDep + 4, " ".repeat(m_SIZE_CARTE - 1));
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 4, "│");

        m_graphics.putString(col, ligneDep + 5, "│");
        if (c instanceof Animal ani && ani.getVolant()) {
            String volantFormate = String.format(" %-" + (m_SIZE_CARTE - 2) + "s", "* Volant");
            m_graphics.putString(col + 1, ligneDep + 5, volantFormate);
        } else {
            m_graphics.putString(col + 1, ligneDep + 5, " ".repeat(m_SIZE_CARTE - 1));
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 5, "│");

        m_graphics.putString(col, ligneDep + 6, "╰");
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.putString(i, ligneDep + 6, "─");
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDep + 6, "╯");

        return true;
    }

    private void dessinerCaseVide(String label, int ligneDebut, int coordonnee) {
        if (m_graphics == null) return;

        int col = getColDep(coordonnee);

        m_graphics.putString(col, ligneDebut, "╭");
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.putString(i, ligneDebut, "─");
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDebut, "╮");

        // Lignes intérieures
        for (int ligne = 1; ligne < m_HAUTEUR_CARTE - 1; ligne++) {
            m_graphics.putString(col,ligneDebut + ligne, "│");
            m_graphics.putString(col + m_SIZE_CARTE, ligneDebut + ligne, "│");

            if (ligne == m_HAUTEUR_CARTE / 2) {
                // Label centré sur la ligne du milieu
                int espaceInterieur = m_SIZE_CARTE - 1;
                int padding = (espaceInterieur - label.length()) / 2;
                String labelCentre = " ".repeat(padding) + label + " ".repeat(padding);
                // Ajustement si longueur impaire
                if (labelCentre.length() < espaceInterieur) {
                    labelCentre += " ";
                }
                m_graphics.putString(col + 1, ligneDebut + ligne, labelCentre);
            } else {
                // Intérieur vide
                m_graphics.putString(col + 1, ligneDebut + ligne, " ".repeat(m_SIZE_CARTE - 1));
            }
        }

        m_graphics.putString(col, ligneDebut + m_HAUTEUR_CARTE - 1, "╰");
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.putString(i, ligneDebut + m_HAUTEUR_CARTE - 1, "─");
        }
        m_graphics.putString(col + m_SIZE_CARTE, ligneDebut + m_HAUTEUR_CARTE - 1, "╯");
    }

    public void rafraichir() {
        try {
            if (m_screen != null) {
                m_screen.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void effacer() {
        if (m_screen != null) {
            m_screen.clear();
        }
    }

    public void finScreen(){
        return;
    }
}