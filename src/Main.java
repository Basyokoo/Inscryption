import jeu.logic.GestionPartie;

public class Main {
  public static void main(String[] args) {
    System.out.println("=== Lancement du jeu Inscryption ===");
    GestionPartie gestionnaire = new GestionPartie();
    gestionnaire.lancerJeu();
    gestionnaire.debutTour();
    gestionnaire.boucleTour();
    if (gestionnaire.verifFinPartie()) {
      gestionnaire.finPartie();
    }
  }
}
