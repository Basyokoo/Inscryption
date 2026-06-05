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
import jeu.logic.Adversaire;
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

    // Liste pour stocker et faire défiler l'historique des messages de logs
    private final ArrayList<String> m_historiqueLogs = new ArrayList<>();
    private static final int MAX_LIGNES_LOGS = 8; // Hauteur maximale du cadre de logs

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

    /**
     * NOUVEAUTÉ : Permet d'ajouter un message dans la boîte de logs.
     * Si la taille dépasse MAX_LIGNES_LOGS, la ligne la plus ancienne (index 0) est retirée.
     */
    public void ajouterLog(String message) {
        if (message == null || message.trim().isEmpty()) return;

        // Découpe le texte si jamais il contient des retours à la ligne \n
        String[] lignes = message.split("\n");
        for (String ligne : lignes) {
            if (!ligne.trim().isEmpty()) {
                this.m_historiqueLogs.add(ligne);
            }
        }

        // Système FIFO : supprime l'élément le plus ancien tant qu'on dépasse le max
        while (this.m_historiqueLogs.size() > MAX_LIGNES_LOGS) {
            this.m_historiqueLogs.remove(0);
        }
    }

    /**
     * AJOUT : Dessine le cadre de logs en bas à droite de la console
     */
    private void dessinerCadreLogs(int ligneDebut) {
        int largeurLogs = 55;
        int colLogs = Math.max(m_margeGauche + 68, m_largeurEcran - largeurLogs - 4);

        // Dessin du haut du cadre avec le titre "Logs"
        m_graphics.putString(colLogs, ligneDebut, "╭─ Logs " + "─".repeat(largeurLogs - 9) + "╮");

        // Dessin du contenu des logs (ligne par ligne)
        for (int i = 0; i < MAX_LIGNES_LOGS; i++) {
            String texteLigne = "";
            if (i < m_historiqueLogs.size()) {
                texteLigne = m_historiqueLogs.get(i);
            }

            // Tronquer le texte s'il dépasse l'espace utile du cadre
            int espaceUtile = largeurLogs - 2;
            if (texteLigne.length() > espaceUtile) {
                texteLigne = texteLigne.substring(0, espaceUtile - 3) + "...";
            }

            String ligneFormatee = String.format("│%-" + espaceUtile + "s│", texteLigne);
            m_graphics.putString(colLogs, ligneDebut + 1 + i, ligneFormatee);
        }

        // Dessin du bas du cadre
        m_graphics.putString(colLogs, ligneDebut + 1 + MAX_LIGNES_LOGS, "╰" + "─".repeat(largeurLogs - 2) + "╯");
    }

    public void dessinerJeuSansPlateau(ArrayList<Carte> cartesTerrainJoueur, ArrayList<Carte> intentionsAdversaire,
                                       ArrayList<Carte> cartesTerrainAdversaire, jeu.logic.Joueur joueur,
                                       int score, int numPartie, int victoireJ, int victoireE) {

        m_screen.doResizeIfNecessary();
        mettreAJourDimensions();
        this.effacer();

        this.m_graphics.putString(2, 0, String.format("MANCHE EN COURS : %d   |   Manches Gagnées Joueur : %d / 2   |   Manches Gagnées Robot : %d / 2", (numPartie + 1), victoireJ, victoireE));
        this.m_graphics.putString(2, 1, "═".repeat(m_largeurEcran - 4));

        int ligneIntentions = 3;
        int ligneAdverse = ligneIntentions + m_HAUTEUR_CARTE + 1;
        int ligneJoueur = ligneAdverse + m_HAUTEUR_CARTE + 1;

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
            try {
                Carte c = (cartesTerrainJoueur != null && i < cartesTerrainJoueur.size()) ? cartesTerrainJoueur.get(i) : null;
                if (c != null) {
                    this.dessineCarte(c, ligneJoueur, i);
                } else {
                    this.dessinerCaseVide("B" + (i + 1), ligneJoueur, i);
                }
            } catch (Exception e) {
                this.dessinerCaseVide("ERREUR", ligneJoueur, i);
            }
        }

        // Section Infos
        int ligneScore = ligneJoueur + m_HAUTEUR_CARTE + 1;
        this.m_graphics.putString(m_margeGauche, ligneScore, "Score balance : " + score + "   |   Os disponibles : " + joueur.getNbOsDisponibles() + "   |   Gouttes de sang disponibles : " + joueur.getSangJoueur());
        this.m_graphics.putString(m_margeGauche, ligneScore + 1, "-".repeat(79));

        this.m_graphics.putString(m_margeGauche, ligneScore + 2, "Votre main :");
        int ligneTexteMain = ligneScore + 3;
        int numeroCarte = 1;

        this.updatePioche(getColDep(2), ligneTexteMain, joueur);

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

        // --- DESSIN DE LA BOÎTE DE LOGS EN BAS À DROITE ---
        dessinerCadreLogs(ligneMenu);

        this.m_ligneBoiteSaisie = ligneMenu + 6;
        this.rafraichir();
    }

    public void updatePioche(int coor, int ligne, Joueur joueur){
        this.m_graphics.putString(coor, ligne - 1, "Cartes Restantes: " + joueur.getNombreCartes());
        this.dessinerCaseVide("Pioche", ligne, 2);
    }

    public void afficherMessageAlerte(String message) {
        if (m_graphics == null) return;
        this.m_graphics.putString(m_margeGauche, this.m_ligneBoiteSaisie - 1, "⚠️  " + message + " ".repeat(30));
        this.ajouterLog("[ALERTE] " + message);
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
                    if (inputBuffer.length() < 15) {
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

        m_graphics.putString(col, ligneDep + 6, "│");
        if (c.estAnimal() && c.getPouvoir() != null && c.getPouvoir().getType() != null) {
            String pouvoirFormate = String.format("Pvr: %-" + (espaceUtile - 5) + "s", c.getPouvoir().getType().toString());
            m_graphics.putString(col + 1, ligneDep + 6, pouvoirFormate);
        } else {
            m_graphics.putString(col + 1, ligneDep + 6, " ".repeat(espaceUtile));
        }
        m_graphics.putString(coinDroit, ligneDep + 6, "│");

        for (int l = 7; l < m_HAUTEUR_CARTE - 1; l++) {
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

    public void afficherFin(Object entiteGagnante, boolean estFinPartieTotale) {
        if (m_graphics == null) return;

        this.effacer();
        m_screen.doResizeIfNecessary();
        mettreAJourDimensions();

        int largeurBoite = 65;
        int hauteurBoite = 9;
        int colDep = Math.max(2, (m_largeurEcran - largeurBoite) / 2);
        int ligneDep = Math.max(2, (m_hauteurEcran - hauteurBoite) / 2);

        m_graphics.putString(colDep, ligneDep, "╔" + "═".repeat(largeurBoite - 2) + "╗");
        for (int i = 1; i < hauteurBoite - 1; i++) {
            m_graphics.putString(colDep, ligneDep + i, "║" + " ".repeat(largeurBoite - 2) + "║");
        }
        m_graphics.putString(colDep, ligneDep + hauteurBoite - 1, "╚" + "═".repeat(largeurBoite - 2) + "╝");

        String titre;
        String messageLigne1;
        String messageLigne2 = estFinPartieTotale ? "Pressez [ENTRÉE] pour quitter le jeu..." : "Pressez [ENTRÉE] pour lancer la manche suivante...";

        boolean joueurAGagne = !(entiteGagnante instanceof Adversaire);

        if (estFinPartieTotale) {
            titre = joueurAGagne ? "  VICTOIRE TOTALE !  " : "  JEU TERMINÉ  ";
            messageLigne1 = joueurAGagne ? "Incroyable !! Vous avez gagné le match contre le robot !" : "Dommage... Le robot a remporté le match.";
        } else {
            titre = joueurAGagne ? "  MANCHE GAGNÉE  " : "  MANCHE PERDUE  ";
            messageLigne1 = joueurAGagne ? "Bien joué, vous remportez cette manche !" : "Le robot s'empare de cette manche.";
        }

        int posTitre = colDep + (largeurBoite - titre.length()) / 2;
        m_graphics.putString(posTitre, ligneDep + 2, titre);

        int posL1 = colDep + (largeurBoite - messageLigne1.length()) / 2;
        m_graphics.putString(posL1, ligneDep + 4, messageLigne1);

        int posL2 = colDep + (largeurBoite - messageLigne2.length()) / 2;
        m_graphics.putString(posL2, ligneDep + 6, messageLigne2);

        m_screen.setCursorPosition(null);
        this.rafraichir();

        try {
            while (true) {
                KeyStroke key = m_screen.readInput();
                if (key.getKeyType() == KeyType.Enter) {
                    if (estFinPartieTotale) {
                        m_screen.stopScreen();
                    }
                    break;
                }
            }

            while (m_screen.pollInput() != null) {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}