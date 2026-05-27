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
    static final int m_HAUTEUR_ECRAN = m_HAUTEUR_CARTE * 4 + 10;

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

        int empIntention = 0;
        for (Carte c : plateau.getCartesIntentions()) {
            if (c != null) {
                this.dessineCarte(c, 1, empIntention); // Ligne de texte 1
            }
            empIntention++;
        }

        int empAdv = 0;
        for (Carte c : plateau.getCartesLigneHaut()) {
            if (c != null) {
                this.dessineCarte(c, 5, empAdv); // Ligne 5 par exemple
            }
            empAdv++;
        }

        int empJoueur = 0;
        for (Carte c : plateau.getCartesLigneBas()) {
            if (c != null) {
                this.dessineCarte(c, 12, empJoueur); // Ligne 12 pour laisser de l'espace
            }
            empJoueur++;
        }

        this.m_graphics.putString(2, 20, "Score balance : " + score);
        this.m_graphics.putString(2, 22, "Votre main :");

        int ligneTexteMain = 23;
        int numeroCarte = 1;
        for (Carte c : main.getCartesEnMain()) {
            String infoMain = String.format("  %d. %-10s PV: %d", numeroCarte, c.getNom(), c.getVie());
            this.m_graphics.putString(2, ligneTexteMain, infoMain);
            ligneTexteMain++;
            numeroCarte++;
        }

        this.rafraichir();
    }

    public boolean dessineCarte(Carte c, int ligneDep, int coor) {
        if (m_graphics == null) return false;

        int col = getColDep(coor);

        // 1. Haut de la carte
        m_graphics.setCharacter(col, ligneDep, '*');
        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep, '*');
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.setCharacter(i, ligneDep, '-');
        }

        // 2. Nom de la carte
        m_graphics.setCharacter(col, ligneDep + 1, '|');
        // On tronque ou formate le nom pour qu'il ne dépasse pas de la carte
        String nomFormate = String.format(" %-11s", c.getNom());
        m_graphics.putString(col + 1, ligneDep + 1, nomFormate);
        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep + 1, '|');

        // 3. Milieu de la carte (Ligne de séparation interne)
        m_graphics.setCharacter(col, ligneDep + 2, '|');
        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep + 2, '|');
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.setCharacter(i, ligneDep + 2, '-');
        }

        // 4. Points de Vie (PV)
        m_graphics.setCharacter(col, ligneDep + 3, '|');
        String pvFormate = String.format(" PV: %-7d", c.getVie());
        m_graphics.putString(col + 1, ligneDep + 3, pvFormate);
        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep + 3, '|');

        m_graphics.setCharacter(col, ligneDep + 4, '|');
        if(c instanceof Animal ani){
            String attFormate = String.format(" Att: %-6d", ani.getAttack());
            m_graphics.putString(col + 1, ligneDep + 4, attFormate);
        }else{
            m_graphics.putString(col + 1, ligneDep + 4, "           ");
        }

        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep + 4, '|');

        m_graphics.setCharacter(col, ligneDep + 5, '*');
        m_graphics.setCharacter(col + m_SIZE_CARTE, ligneDep + 5, '*');
        for (int i = col + 1; i < col + m_SIZE_CARTE; i++) {
            m_graphics.setCharacter(i, ligneDep + 5, '-');
        }

        return true;
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
}
