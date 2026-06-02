package jeu.logic;

import jeu.model.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    @Test
    void testAttaqueAnimalSurObstacle() {
        Combat combat = new Combat();
        // L'ours a 4 points d'attaque selon votre constructeur : Animal(nom, pV, attaque, sang, os, volant)
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);

        int degatsEffectifs = combat.appliquerDegats(ours, rocher);

        // Correction : on attend 4 dégâts (la valeur de l'attaque de l'ours)
        assertEquals(4, degatsEffectifs, "L'ours doit infliger ses 4 points d'attaque.");

        // Correction : 6 PV initiaux - 4 dégâts = 2 PV restants
        assertEquals(2, rocher.getVie(), "Le rocher (6 PV) devrait subir 4 dégâts et avoir 2 PV restants.");
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
        for(int i=0; i<4; i++) {
            adv.getCartesLigneHaut().set(i, null);
        }
        Animal lion = new Animal("Lion", 5, 4, 1, 0, false);
        j.placerCarteJoueur(lion, 0);
        combat.gererAttaqueFinTour(j, adv, score);
        assertEquals(4, score.getValeurEcart(), "Le score devrait augmenter de 4 (attaque du lion).");
    }

    @Test
    void testPlacementDesCartesSurJoueur() {
        Joueur j = new Joueur();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);
        boolean res1 = j.placerCarteJoueur(ours, 0);
        boolean res2 = j.placerCarteJoueur(rocher, 2);
        assertTrue(res1, "Le placement de l'ours devrait réussir.");
        assertTrue(res2, "Le placement du rocher devrait réussir.");
        assertEquals(ours, j.getCarteJoueur(0), "L'ours doit être en position 0.");
        assertEquals(rocher, j.getCarteJoueur(2), "Le rocher doit être en position 2.");
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
        GestionPartie jeu = new GestionPartie();
        // On simule une instance de jeu
        // Note : il faut accéder à l'objet score interne ou en recréer un pour tester

        // Test de victoire : on simule un écart de score de 5 en faveur du joueur
        // Vous devrez peut-être ajouter une méthode publique pour modifier le score dans Score.java
        // ou simuler des attaques dans le moteur de jeu.

        // Alternative : tester directement la logique de fin de partie via l'objet score
        Score score = new Score();
        score.ajouterPointsJoueur(5);

        assertTrue(score.estVictoireJoueur(), "Le score devrait indiquer une victoire (>= 5).");

        // Test de défaite : écart de -5
        Score scoreDefaite = new Score();
        scoreDefaite.ajouterPointsEnnemi(5);

        assertFalse(scoreDefaite.estVictoireJoueur(), "Le score devrait indiquer une défaite (<= -5).");
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
        // Le loup doit être créé avec 2 PV pour que 2 - 1 = 1
        Animal attaquant = new Animal("Loup", 2, 3, 2, 0, false);
        // Le porc-épic possède le pouvoir PP
        Animal porcEpic = new Animal("Porc-épic", 2, 1, 1, 0, false, new Pouvoir("Piques pointues", "PP", 0));

        // Application du pouvoir
        combat.appliquerPouvoir(porcEpic, attaquant);

        // Vérification de la vie : 2 PV initiaux - 1 dégât (PP) = 1 PV restant
        assertEquals(1, attaquant.getVie(), "Le loup devrait subir 1 dégât en retour.");
    }

    @Test
    public void testPouvoirPuant() {
        Combat combat = new Combat();
        // Le loup a 4 points d'attaque
        Animal attaquant = new Animal("Loup", 5, 4, 3, 0, false);
        // La punaise a le pouvoir "Puant" (type "P")
        Animal punaise = new Animal("Punaise", 5, 1, 2, 0, false, new Pouvoir("Puant", "P", 0));

        // Application du pouvoir
        combat.appliquerPouvoir(punaise, attaquant);

        // Vérification : l'attaque du loup doit être réduite de 1 (4 - 1 = 3)
        assertEquals(3, attaquant.getAttack(), "Le loup devrait voir son attaque réduite à 3.");
    }

    @Test
    public void testContactMortel() {
        Combat combat = new Combat();
        // 1. Initialisation avec le pouvoir CM (type "CM")
        Animal vipere = new Animal("Vipère", 1, 1, 2, 0, false, new Pouvoir("Contact mortel", "CM", 0));
        Animal attaquant = new Animal("Loup", 5, 4, 3, 0, false);

        // 2. Application du pouvoir
        String resultat = combat.appliquerPouvoir(vipere, attaquant);

        // 3. Vérifications
        assertTrue(resultat.contains("meurt instantanément"), "Le message doit indiquer le décès.");
        assertEquals(0, attaquant.getVie(), "La créature blessée par le contact mortel devrait avoir 0 PV.");
    }

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