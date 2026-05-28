import jeu.logic.GestionPartie;
import jeu.model.Animal;

public class Main {
  public static void main(String[] args) {
    System.out.println("=== Lancement du jeu Inscryption ===");
    GestionPartie gestionnaire = new GestionPartie();
    boolean jeuLance = gestionnaire.lancerJeu();

    if (!jeuLance) {
      System.out.println("Erreur lors de l'initialisation du jeu. Arrêt.");
      return;
    }

    while (!gestionnaire.verifFinPartie()) {
      gestionnaire.debutTour();
      boolean tourEnCours = gestionnaire.boucleTour();
      if (!tourEnCours) {
        break;
      }
    }
    gestionnaire.finPartie();
    System.out.println("=== Fin de la session de jeu ===");
  }
}
