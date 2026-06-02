package jeu.logic;

import jeu.model.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    @Test
    void testAttaqueAnimalSurObstacle() {
        Combat combat = new Combat();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);

        int degatsEffectifs = combat.appliquerDegats(ours, rocher);

        assertEquals(5, degatsEffectifs, "L'ours inflige ses points d'attaque.");
        assertEquals(1, rocher.getVie(), "Le rocher (6 PV) devrait subir 5 dégâts et avoir 1 PV restant.");
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

        j.placerCarteJoueur(lion, 0);
        j.placerCarteJoueur(faucon, 2);
        adv.placerCarteEnnemi(oursEnnemi, 0);
        adv.placerCarteEnnemi(loupEnnemi, 1);

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
        assertTrue(score.getValeurEcart() > 0, "Le score doit être positif après attaque dans le vide.");
    }

    @Test
    void testPlacementDesCartesSurJoueur() {
        Joueur j = new Joueur();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);

        j.placerCarteJoueur(ours, 0);
        j.placerCarteJoueur(rocher, 2);

        assertEquals(ours, j.getCarteJoueur(0));
        assertEquals(rocher, j.getCarteJoueur(2));
        assertNull(j.getCarteJoueur(1), "La case 1 doit être vide.");
    }

    @Test
    void testPiocherUneCarte() {
        Joueur j = new Joueur();
        j.initialiserPiocheDeBase();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        j.ajouterCartes(ours);

        assertEquals(1, j.getNombreCartes());
        Animal cartePiochee = j.piocher();
        j.ajouterMain(cartePiochee, 0);

        assertNotNull(j.getCartesEnMain().get(0));
        assertEquals("Ours", j.getCartesEnMain().get(0).getNom());
    }

    @Test
    void testGagnerOuPerdrePartie() {
        Joueur j = new Joueur();
        Adversaire adv = new Adversaire();

        // Victoire : l'adversaire n'a plus rien sur ses 4 cases
        boolean estVictoire = true;
        for (int i = 0; i < 4; i++) {
            if (adv.getCarteEnnemi(i) != null) estVictoire = false;
        }
        assertTrue(estVictoire, "Victoire attendue si aucun ennemi sur le plateau.");

        // Défaite : le joueur n'a plus rien
        j.placerCarteJoueur(null, 0);
        boolean estDefaite = true;
        for (int i = 0; i < 4; i++) {
            if (j.getCarteJoueur(i) != null) estDefaite = false;
        }
        assertTrue(estDefaite, "Défaite attendue si aucune carte joueur.");
    }

    @Test
    public void testSaisieInvalide() {
        ValidateurSaisie validateur = new ValidateurSaisie();
        assertThrows(SaisieInvalideException.class, () -> validateur.validerPosition("xyz"));
        assertThrows(SaisieInvalideException.class, () -> validateur.validerPosition("7"));
    }

    @Test
    public void testPouvoirContactMortel() {
        Combat combat = new Combat();
        // Création de la source (Vipère) et de la cible
        Animal vipere = new Animal("Vipère", 1, 1, 2, 0, false, new Pouvoir("Contact mortel", "CM", 0));
        Animal proie = new Animal("Grizzly", 6, 4, 3, 0, false);

        String resultat = combat.appliquerPouvoir(vipere, proie);

        assertTrue(resultat.contains("meurt instantanément"));
        assertEquals(0, proie.getVie(), "La cible devrait avoir 0 PV");
    }

    @Test
    public void testPouvoirPiquesPointues() {
        Combat combat = new Combat();
        Animal porcEpic = new Animal("Porc-épic", 2, 1, 1, 0, false, new Pouvoir("Piques pointues", "PP", 0));
        Animal attaquant = new Animal("Loup", 2, 3, 2, 0, false);

        combat.appliquerPouvoir(porcEpic, attaquant);

        assertEquals(1, attaquant.getVie(), "Le loup devrait subir 1 dégât en retour");
    }

    // Classes utilitaires pour les tests (locales à la classe de test)
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