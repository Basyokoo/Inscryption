
import jeu.logic.GestionPartie;

public class Main {
    public static void main(String[] args) {
        GestionPartie jeu = new GestionPartie();

        // Boucle sur 3 parties maximum selon tes consignes
        for (int i = 1; i <= 3; i++) {
            if (!jeu.lancerJeu()) {
                System.err.println("Erreur lors de l'initialisation de l'écran du jeu. Arrêt.");
                return;
            }

            // Boucle des tours de jeu
            while (!jeu.verifFinPartie()) {
                jeu.debutTour();
                boolean tourEnCours = true;
                while (tourEnCours) {
                    tourEnCours = jeu.boucleTour();
                }
            }

            jeu.finPartie();

            // Gestion de la consigne spécifique pour la pierre de sacrifice à la partie 2
            if (i == 2) {break;}
        }
        System.out.println("=== Fin de la session de jeu ===");
    }
}