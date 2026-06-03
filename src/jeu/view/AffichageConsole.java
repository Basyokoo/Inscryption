package jeu.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import jeu.logic.Joueur;
import jeu.model.Animal;
import jeu.model.Carte;

import java.io.IOException;
import java.util.ArrayList;

public class AffichageConsole {
    static final int m_LARGEUR_CARTE = 16;
    static final int m_HAUTEUR_CARTE = 11;

    private Screen m_screen;
    private TextGraphics m_graphics;

    private int m_largeurEcran;
    private int m_hauteurEcran;
    private int m_decalageDynamique;
    private int m_margeGauche;
    private int m_ligneBoiteSaisie;

    public boolean initEcran() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setTerminalEmulatorTitle("Mon Jeu de Cartes");
            factory.setInitialTerminalSize(new TerminalSize(140, 45));

            com.googlecode.lanterna.terminal.Terminal terminal = factory.createTerminal();

            if (terminal instanceof SwingTerminalFrame frame) {
                frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            }

            this.m_screen = new TerminalScreen(terminal);
            this.m_screen.startScreen();
            this.m_screen.setCursorPosition(null);
            this.m_graphics = m_screen.newTextGraphics();

            try { Thread.sleep(150); } catch (InterruptedException ignored) {}

            mettreAJourDimensions();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mettreAJourDimensions() {
        TerminalSize size = m_screen.getTerminalSize();
        this.m_largeurEcran = size.getColumns();
        this.m_hauteurEcran = size.getRows();

        int largeurTotaleCartes = m_LARGEUR_CARTE * 4;
        int espaceRestant = m_largeurEcran - largeurTotaleCartes;

        if (espaceRestant > 0) {
            int portion = espaceRestant / 5;
            this.m_decalageDynamique = Math.max(4, portion);
            this.m_margeGauche = portion;
        } else {
            this.m_decalageDynamique = 4;
            this.m_margeGauche = 2;
        }
    }

    public int getColDep(int coor) {
        return m_margeGauche + (m_LARGEUR_CARTE + m_decalageDynamique) * coor;
    }

    public void dessinerJeuSansPlateau(ArrayList<Carte> cartesTerrainJoueur, ArrayList<Carte> intentionsAdversaire, ArrayList<Carte> cartesTerrainAdversaire, jeu.logic.Joueur joueur, int score) {
        m_screen.doResizeIfNecessary();
        mettreAJourDimensions();
        this.effacer();

        int ligneIntentions = 1;
        int ligneAdverse = ligneIntentions + m_HAUTEUR_CARTE + 2;
        int ligneJoueur = ligneAdverse + m_HAUTEUR_CARTE + 2;

        // 1. Ligne Intentions Adversaire
        for (int i = 0; i < 4; i++) {
            Carte c = (intentionsAdversaire != null && i < intentionsAdversaire.size()) ? intentionsAdversaire.get(i) : null;
            if (c != null) { this.dessineCarte(c, ligneIntentions, i); }
            else { this.dessinerCaseVide("A" + (i + 1), ligneIntentions, i); }
        }

        // 2. Ligne Terrain Adversaire
        for (int i = 0; i < 4; i++) {
            Carte c = (cartesTerrainAdversaire != null && i < cartesTerrainAdversaire.size()) ? cartesTerrainAdversaire.get(i) : null;
            if (c != null) { this.dessineCarte(c, ligneAdverse, i); }
            else { this.dessinerCaseVide("A" + (i + 1), ligneAdverse, i); }
        }

        // 3. Ligne Terrain Joueur
        for (int i = 0; i < 4; i++) {
            Carte c = (cartesTerrainJoueur != null && i < cartesTerrainJoueur.size()) ? cartesTerrainJoueur.get(i) : null;
            if (c != null) { this.dessineCarte(c, ligneJoueur, i); }
            else { this.dessinerCaseVide("B" + (i + 1), ligneJoueur, i); }
        }

        // Section Infos
        int ligneScore = ligneJoueur + m_HAUTEUR_CARTE + 2;
        this.m_graphics.putString(m_margeGauche, ligneScore, "Score balance : " + score + "   |   Os disponibles : " + joueur.getNbOsDisponibles() + "   |   Goutes de sang disponibles : " + joueur.getSangJoueur());
        this.m_graphics.putString(m_margeGauche, ligneScore + 1, "-".repeat(79));

        this.m_graphics.putString(m_margeGauche, ligneScore + 2, "Votre main :");
        int ligneTexteMain = ligneScore + 3;
        int numeroCarte = 1;

        this.updatePioche(getColDep(2),ligneTexteMain, joueur);

        if (joueur.getCartesEnMain() != null) {
            for (Animal c : joueur.getCartesEnMain()) {
                if (c != null) {
                    String infoMain = String.format("  %d. %-12s PV: %d  Att: %d | Cout Sang: %d  Cout Os: %d",
                            numeroCarte, c.getNom(), c.getVie(), c.getAttack(), c.getCoutSang(), c.getCoutOs());
                    this.m_graphics.putString(m_margeGauche, ligneTexteMain, infoMain);
                    ligneTexteMain++;
                }
                numeroCarte++;
            }
        }

        int ligneMenu = ligneTexteMain + 1;
        this.m_graphics.putString(m_margeGauche, ligneMenu, "--- C'EST VOTRE TOUR ---");
        this.m_graphics.putString(m_margeGauche, ligneMenu + 1, "[1] Piocher une carte (" + joueur.getNombreCartes() + " restantes)");
        this.m_graphics.putString(m_margeGauche, ligneMenu + 2, "[2] Jouer une carte de votre main");
        this.m_graphics.putString(m_margeGauche, ligneMenu + 3, "[3] Sacrifier un animal");
        this.m_graphics.putString(m_margeGauche, ligneMenu + 4, "[4] Terminer le tour");

        this.m_ligneBoiteSaisie = ligneMenu + 6;
        this.rafraichir();
    }

    public void updatePioche(int coor, int ligne, Joueur joueur){
        this.m_graphics.putString(coor, ligne -1, "Cartes Restantes: " + joueur.getNombreCartes());
        this.dessinerCaseVide("Pioche",ligne, 2);
    }

    // NOUVELLE MÉTHODE : Permet d'afficher une alerte visuelle rouge bien visible au dessus de la saisie
    public void afficherMessageAlerte(String message) {
        if (m_graphics == null) return;
        this.m_graphics.putString(m_margeGauche, this.m_ligneBoiteSaisie - 1, "⚠️  " + message + " ".repeat(30));
        this.rafraichir();
    }

    public String afficherChoix() {
        StringBuilder inputBuffer = new StringBuilder();
        int ligneBoite = this.m_ligneBoiteSaisie;
        int largeurBoite = 65;
        int colBoite = m_margeGauche;

        while (true) {
            m_graphics.putString(colBoite, ligneBoite, "╭" + "─".repeat(largeurBoite - 2) + "╮");
            String texteAffiche = " Votre choix : " + inputBuffer.toString();
            int paddingEspaces = (largeurBoite - 2) - texteAffiche.length();
            if (paddingEspaces > 0) {
                texteAffiche += " ".repeat(paddingEspaces);
            } else {
                texteAffiche = texteAffiche.substring(0, largeurBoite - 2);
            }

            m_graphics.putString(colBoite, ligneBoite + 1, "│" + texteAffiche + "│");
            m_graphics.putString(colBoite, ligneBoite + 2, "╰" + "─".repeat(largeurBoite - 2) + "╯");

            m_screen.setCursorPosition(new TerminalPosition(colBoite + 15 + inputBuffer.length(), ligneBoite + 1));
            this.rafraichir();

            try {
                KeyStroke keyStroke = m_screen.readInput();

                if (keyStroke.getKeyType() == KeyType.Enter) {
                    m_screen.setCursorPosition(null);
                    return inputBuffer.toString().trim();
                } else if (keyStroke.getKeyType() == KeyType.Backspace) {
                    if (inputBuffer.length() > 0) {
                        inputBuffer.deleteCharAt(inputBuffer.length() - 1);
                    }
                } else if (keyStroke.getKeyType() == KeyType.Character) {
                    if (inputBuffer.length() < 5) {
                        inputBuffer.append(keyStroke.getCharacter());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    public boolean dessineCarte(Carte c, int ligneDep, int coor) {
        if (m_graphics == null) return false;

        int col = getColDep(coor);
        int coinDroit = col + m_LARGEUR_CARTE - 1;
        int espaceUtile = m_LARGEUR_CARTE - 2;

        m_graphics.putString(col, ligneDep, "╭");
        for (int i = col + 1; i < coinDroit; i++) {
            m_graphics.putString(i, ligneDep, "─");
        }
        m_graphics.putString(coinDroit, ligneDep, "╮");

        m_graphics.putString(col, ligneDep + 1, "│");
        String nomFormate = String.format("%-" + espaceUtile + "s",
                c.getNom().length() > espaceUtile ? c.getNom().substring(0, espaceUtile) : c.getNom());
        m_graphics.putString(col + 1, ligneDep + 1, nomFormate);
        m_graphics.putString(coinDroit, ligneDep + 1, "│");

        m_graphics.putString(col, ligneDep + 2, "├");
        for (int i = col + 1; i < coinDroit; i++) {
            m_graphics.putString(i, ligneDep + 2, "─");
        }
        m_graphics.putString(coinDroit, ligneDep + 2, "┤");

        m_graphics.putString(col, ligneDep + 3, "│");
        String pvFormate = String.format("PV: %-" + (espaceUtile - 4) + "d", c.getVie());
        m_graphics.putString(col + 1, ligneDep + 3, pvFormate);
        m_graphics.putString(coinDroit, ligneDep + 3, "│");

        m_graphics.putString(col, ligneDep + 4, "│");
        if (c.estAnimal()) {
            String attFormate = String.format("Att: %-" + (espaceUtile - 5) + "d", c.getAttack());
            m_graphics.putString(col + 1, ligneDep + 4, attFormate);
        } else {
            m_graphics.putString(col + 1, ligneDep + 4, " ".repeat(espaceUtile));
        }
        m_graphics.putString(coinDroit, ligneDep + 4, "│");

        m_graphics.putString(col, ligneDep + 5, "│");
        if (c.estAnimal() && c.getVolant()) {
            String volantFormate = String.format("Volant%-" + (espaceUtile - 6) + "s", "");
            m_graphics.putString(col + 1, ligneDep + 5, volantFormate);
        } else {
            m_graphics.putString(col + 1, ligneDep + 5, " ".repeat(espaceUtile));
        }
        m_graphics.putString(coinDroit, ligneDep + 5, "│");

        for (int l = 6; l < m_HAUTEUR_CARTE - 1; l++) {
            m_graphics.putString(col, ligneDep + l, "│");
            m_graphics.putString(col + 1, ligneDep + l, " ".repeat(espaceUtile));
            m_graphics.putString(coinDroit, ligneDep + l, "│");
        }

        m_graphics.putString(col, ligneDep + m_HAUTEUR_CARTE - 1, "╰");
        for (int i = col + 1; i < coinDroit; i++) {
            m_graphics.putString(i, ligneDep + m_HAUTEUR_CARTE - 1, "─");
        }
        m_graphics.putString(coinDroit, ligneDep + m_HAUTEUR_CARTE - 1, "╯");

        return true;
    }

    private void dessinerCaseVide(String label, int ligneDebut, int coordonnee) {
        if (m_graphics == null) return;

        int col = getColDep(coordonnee);
        int coinDroit = col + m_LARGEUR_CARTE - 1;

        m_graphics.putString(col, ligneDebut, "╭");
        for (int i = col + 1; i < coinDroit; i++) {
            m_graphics.putString(i, ligneDebut, "─");
        }
        m_graphics.putString(coinDroit, ligneDebut, "╮");

        for (int ligne = 1; ligne < m_HAUTEUR_CARTE - 1; ligne++) {
            m_graphics.putString(col, ligneDebut + ligne, "│");
            m_graphics.putString(coinDroit, ligneDebut + ligne, "│");

            if (ligne == m_HAUTEUR_CARTE / 2) {
                int espaceInterieur = m_LARGEUR_CARTE - 2;
                int padding = Math.max(0, (espaceInterieur - label.length()) / 2);
                String labelCentre = " ".repeat(padding) + label + " ".repeat(padding);
                while (labelCentre.length() < espaceInterieur) {
                    labelCentre += " ";
                }
                m_graphics.putString(col + 1, ligneDebut + ligne, labelCentre);
            } else {
                m_graphics.putString(col + 1, ligneDebut + ligne, " ".repeat(m_LARGEUR_CARTE - 2));
            }
        }

        m_graphics.putString(col, ligneDebut + m_HAUTEUR_CARTE - 1, "╰");
        for (int i = col + 1; i < coinDroit; i++) {
            m_graphics.putString(i, ligneDebut + m_HAUTEUR_CARTE - 1, "─");
        }
        m_graphics.putString(coinDroit, ligneDebut + m_HAUTEUR_CARTE - 1, "╯");
    }

    public void rafraichir() {
        try {
            if (m_screen != null) { m_screen.refresh(); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void effacer() {
        if (m_screen != null) { m_screen.clear(); }
    }
}
