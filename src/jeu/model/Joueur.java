package jeu.model;
import java.util.Scanner;
public class Joueur {
        public Joueur() {
        }

        /**
         * Demande au joueur de choisir une action via la console.
         * @return L'action choisie sous forme de chaîne de caractères.
         */
        public String choisirAction() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- C'EST VOTRE TOUR ---");
            System.out.println("Que voulez-vous faire ?");
            System.out.println("[1] Piocher une carte");
            System.out.println("[2] Jouer une carte de votre main");
            System.out.println("[3] Terminer le tour");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();
            return choix;
        }
}
