import jeu.logic.GestionPartie;

public class Main {
    public static void main(String[] args) {
        GestionPartie jeu = new GestionPartie();

        if (!jeu.lancerJeu()) {
            System.err.println("Erreur lors de l'initialisation du jeu. Arrêt.");
            return;
        }

        while (!jeu.verifFinPartie()) {

            while (!jeu.verifFinManche()) {

                jeu.debutTour();

                boolean tourEnCours = true;
                while (tourEnCours) {

                    jeu.demanderActionJoueur();

                    tourEnCours = jeu.boucleTour();
                }

                if (jeu.verifFinManche()) {
                    break;
                }
            }

            jeu.finManche();
        }

        jeu.finPartie();
        System.out.println("=== Fin de la session de jeu ===");
    }
}