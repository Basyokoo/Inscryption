import jeu.logic.GestionPartie;

public class Main {
    public static void main(String[] args) {
        GestionPartie jeu = new GestionPartie();
        for (int i = 1; i <= 3; i++) {
            jeu.lancerJeu();
            while (!jeu.verifFinPartie()) {
                jeu.debutTour();
                boolean tourEnCours = true;
                while (tourEnCours) {
                    tourEnCours = jeu.boucleTour(); // Continue tant que le joueur ne passe pas au combat
                }
            }
            jeu.finPartie();
            if (i == 2) jeu.gererPierreSacrifice(); // Consigne spécifique
        }
    }
}