package jeu.model;

import java.util.Scanner;

public class Joueur {
    private Scanner m_scanner;

    public Joueur() {
        this.m_scanner = new Scanner(System.in);
    }
    public String choisirAction() {
        System.out.println("\n--- C'EST VOTRE TOUR ---");
        System.out.println("Que voulez-vous faire ?");
        System.out.println("[1] Piocher une carte");
        System.out.println("[2] Jouer une carte de votre main");
        System.out.println("[3] Terminer le tour");
        System.out.print("Votre choix : ");
        String choix = this.m_scanner.nextLine();
        return choix;
    }
    public int choisirPosition() {
        int position = -1;
        boolean saisieValide = false;
        while (!saisieValide) {
            System.out.print("Choisissez une position sur le plateau (0, 1, 2 ou 3) : ");
            String saisie = this.m_scanner.nextLine().trim();
            try {
                position = Integer.parseInt(saisie);
                if (position >= 0 && position <= 3) {
                    saisieValide = true;
                } else {
                    System.out.println("Position hors-limites ! Le nombre " + position + " ne correspond à aucune case. Choisissez entre 0 et 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Format incorrect : '" + saisie + "' n'est pas un nombre entier. Veuillez recommencer.");
            }
        }
        return position;
    }
}