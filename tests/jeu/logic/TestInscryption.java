package jeu.logic;

import jeu.model.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    @Test
    void testAttaqueAnimalSurObstacle() {
        Combat combat = new Combat();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("rocher", 6);
        // Note: Assurez-vous que combat.appliquerDegats accepte (Attaquant, Cible, Pos)
        int degatsEffectifs = combat.appliquerDegats(ours, rocher, 0);
        assertEquals(4, degatsEffectifs, "L'ours devrait infliger 4 points de dégâts.");
        assertEquals(2, rocher.getVie(), "Le rocher devrait avoir 2 PV restants.");
    }

    @Test
    void testAttaquesToutesCartesFinTour() {
        Combat combat = new Combat();
        Score score = new Score();
        Joueur j = new Joueur();
        Adversaire adv = new Adversaire();

        Animal lion = new Animal("Lion", 8, 5, 1, 0, false);
        Animal faucon = new Animal("Faucon", 4, 3, 2, 0, true);
        Animal oursEnnemi = new Animal("Ours Ennemi", 12, 4, 1, 0, false);
        Animal loupEnnemi = new Animal("Loup Ennemi", 5, 3, 1, 0, false);
        Obstacle barriere = new Obstacle("Barrière", 4);

        j.placerCarteJoueur(lion, 0);
        j.placerCarteJoueur(faucon, 2);
        adv.placerCarteEnnemi(oursEnnemi, 0);
        adv.placerCarteEnnemi(loupEnnemi, 1);
        adv.placerCarteEnnemi(barriere, 2);

        String resume = combat.gererAttaqueFinTour(j, adv, score);

        assertTrue(resume.contains("Lion"));
        assertTrue(resume.contains("Ours Ennemi"));
        assertTrue(resume.contains("Faucon"));
    }

    @Test
    void testMiseAJourScoresFinDeTour() {
        Combat combat = new Combat();
        Score score = new Score();
        Joueur j = new Joueur();
        Adversaire adv = new Adversaire();

        Animal lion = new Animal("Lion", 5, 4, 1, 0, false);
        j.placerCarteJoueur(lion, 0);

        combat.gererAttaqueFinTour(j, adv, score);
        assertEquals(5, score.getValeurEcart(), "Le score devrait refléter l'attaque.");

        Animal sourisFragile = new Animal("Souris", 1, 1, 1, 0, false);
        Animal grosOursEnnemi = new Animal("Ours", 10, 5, 1, 0, false);
        j.placerCarteJoueur(sourisFragile, 1);
        adv.placerCarteEnnemi(grosOursEnnemi, 1);

        combat.gererAttaqueFinTour(j, adv, score);
        j.verifierMorts();

        assertNull(j.getCarteJoueur(1), "La souris devrait être morte et retirée.");
    }

    @Test
    void testPlacementDesCartesSurJoueur() {
        Joueur j = new Joueur();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);

        j.placerCarteJoueur(ours, 0);
        j.placerCarteJoueur(rocher, 2);

        assertSame(ours, j.getCarteJoueur(0));
        assertSame(rocher, j.getCarteJoueur(2));
        assertNull(j.getCarteJoueur(1));
    }

    @Test
    void testPiocherUneCarte() {
        Joueur j = new Joueur();
        j.initialiserPiocheDeBase();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        j.ajouterCartes(ours);

        assertEquals(1, j.getNombreCartes());
        Animal cartePiochee = j.piocher();
        j.ajouterMain(cartePiochee, 3);

        assertNotNull(j.getCartesEnMain().get(3));
        assertEquals("Ours", j.getCartesEnMain().get(3).getNom());
    }

    @Test
    void testGagnerOuPerdrePartie() {
        Combat combat = new Combat();
        Score score = new Score();
        Joueur j = new Joueur();
        Adversaire adv = new Adversaire();

        // Victoire : l'adversaire n'a plus rien
        Animal lion = new Animal("Lion", 5, 4, 1, 0, false);
        j.placerCarteJoueur(lion, 0);

        boolean estVictoire = true;
        for (int i = 0; i < 4; i++) {
            if (adv.getCarteEnnemi(i) != null) estVictoire = false;
        }
        assertTrue(estVictoire, "Victoire attendue si aucun ennemi.");

        // Défaite : le joueur n'a plus rien
        Animal oursEnnemi = new Animal("Ours", 10, 5, 1, 0, false);
        adv.placerCarteEnnemi(oursEnnemi, 0);
        j.placerCarteJoueur(null, 0);

        boolean estDefaite = true;
        for (int i = 0; i < 4; i++) {
            if (j.getCarteJoueur(i) != null) estDefaite = false;
        }
        assertTrue(estDefaite, "Défaite attendue si plus de cartes joueur.");
    }

    @Test
    public void testSaisieInvalide() {
        // Test du validateur interne
        ValidateurSaisie validateur = new ValidateurSaisie();
        assertThrows(SaisieInvalideException.class, () -> validateur.validerPosition("xyz"));
        assertThrows(SaisieInvalideException.class, () -> validateur.validerPosition("7"));
    }

    // Classes utilitaires pour les tests
    class SaisieInvalideException extends Exception {
        public SaisieInvalideException(String message) { super(message); }
    }

    class ValidateurSaisie {
        public int validerPosition(String saisie) throws SaisieInvalideException {
            try {
                int position = Integer.parseInt(saisie);
                if (position < 0 || position > 3) {
                    throw new SaisieInvalideException("Hors-limites");
                }
                return position;
            } catch (NumberFormatException e) {
                throw new SaisieInvalideException("Format incorrect");
            }
        }
    }
}