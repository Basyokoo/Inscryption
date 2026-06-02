import jeu.logic.GestionPartie;

public class Main {
    public static void main(String[] args) {
        GestionPartie jeu = new GestionPartie();
        for (int i = 1; i <= 3; i++) {
            if (!jeu.lancerJeu()) {
                System.err.println("Erreur lors de l'initialisation du jeu. Arrêt.");
                return;
            }
            while (!jeu.verifFinPartie()) {
                jeu.debutTour();

                boolean tourEnCours = true;
                while (tourEnCours) {
                    tourEnCours = jeu.boucleTour();
                }
                if (jeu.getNumPartie() == 2) {
                    jeu.gererPierreSacrifice();
                }
            }

            jeu.finPartie();
        }
        System.out.println("=== Fin de la session de jeu ===");
    }
}
