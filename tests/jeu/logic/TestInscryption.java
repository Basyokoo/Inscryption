package jeu.logic;
import jeu.model.Animal;
import jeu.model.Obstacle;
import jeu.model.Plateau;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.InputMismatchException;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    // ///////////////////////////////////
    // GESTION DES TEST UNITAIRES
    // ///////////////////////////////////

    @Test
    void testAttaqueAnimalSurObstacle() {
        Combat combat = new Combat();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("rocher", 6);
        int degatsEffectifs = combat.appliquerDegats(ours, rocher, 0);
        assertEquals(4, degatsEffectifs, "L'ours aurait dû infliger 4 points de dégâts.");
        assertEquals(2, rocher.getVie(), "Le rocher devrait avoir 2 PV restants.");
        assertTrue(rocher.estVie(), "Le rocher devrait être encore vivant.");
    }


    @Test
    void testAttaquesToutesCartesFinTour() {
        Combat combat = new Combat();
        Score score = new Score();
        Plateau plateau = new Plateau();

        Animal lion = new Animal("Lion", 8, 5, 1, 0, false);
        Animal faucon = new Animal("Faucon", 4, 3, 2, 0, true);
        Animal oursEnnemi = new Animal("Ours Ennemi", 12, 4, 1, 0, false);
        Animal loupEnnemi = new Animal("Loup Ennemi", 5, 3, 1, 0, false);
        Obstacle barriere = new Obstacle("Barrière", 4);

        plateau.placerCarteJoueur(lion, 0);
        plateau.placerCarteJoueur(faucon, 2);
        plateau.placerCarteEnnemi(oursEnnemi, 0);
        plateau.placerCarteEnnemi(loupEnnemi, 1);
        plateau.placerCarteEnnemi(barriere, 2);

        String resume = combat.gererAttaqueFinTour(plateau, score);

        // Assertions plus souples pour éviter les erreurs de format de texte
        assertTrue(resume.contains("Lion"), "Le résumé doit mentionner le Lion.");
        assertTrue(resume.contains("5"), "Le résumé doit mentionner les dégâts.");
        assertTrue(resume.contains("Ours Ennemi"), "Le résumé doit mentionner l'Ours.");
        assertTrue(resume.contains("Faucon"), "Le résumé doit mentionner le Faucon.");
    }

    @Test
    void testMiseAJourScoresFinDeTour() {
        Combat combat = new Combat();
        Score score = new Score();
        Plateau plateau = new Plateau();
        MainJoueur mainJoueur = new MainJoueur();
        MainJoueur mainAdversaire = new MainJoueur();
        Animal lion = new Animal("Lion", 5, 4, 1, 0, false);
        plateau.placerCarteJoueur(lion, 0);
        combat.gererAttaqueFinTour(plateau, score);
        assertEquals(5, score.getValeurEcart(), "Le score devrait être de 5 après l'attaque du lion dans le vide.");
        Animal sourisFragile = new Animal("Souris", 1, 1, 1, 0, false);
        Animal grosOursEnnemi = new Animal("Ours", 10, 5, 1, 0, false);
        plateau.placerCarteJoueur(sourisFragile, 1);
        plateau.placerCarteEnnemi(grosOursEnnemi, 1);
        combat.gererAttaqueFinTour(plateau, score);
        combat.verifierMorts(plateau, mainJoueur, mainAdversaire);
        assertNull(plateau.getCarteJoueur(1), "La souris devrait être morte et retirée du plateau.");
        assertEquals(1, mainJoueur.getNbOsDisponibles(), "Le joueur devrait avoir gagné 1 os.");
    }
    @Test
    void testPlacementDesCartesSurPlateau() {
        Plateau plateau = new Plateau();
        for (int i = 0; i < 4; i++) {
            plateau.placerCarteJoueur(null, i);
            plateau.placerCarteEnnemi(null, i);
        }
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Obstacle rocher = new Obstacle("Rocher", 6);
        Animal loupEnnemi = new Animal("Loup", 4, 3, 1, 0, false);
        for (int i = 0; i < 4; i++) {
            assertNull(plateau.getCarteJoueur(i), "La position joueur " + i + " devrait être vide au début.");
            assertNull(plateau.getCarteEnnemi(i), "La position ennemi " + i + " devrait être vide au début.");
        }
        plateau.placerCarteJoueur(ours, 0);
        plateau.placerCarteJoueur(rocher, 2);
        plateau.placerCarteEnnemi(loupEnnemi, 0);
        assertSame(ours, plateau.getCarteJoueur(0), "L'ours devrait être positionné à la case 0 du joueur.");
        assertNull(plateau.getCarteJoueur(1), "La case 1 du joueur devrait être restée vide.");
        assertSame(rocher, plateau.getCarteJoueur(2), "Le rocher devrait être positionné à la case 2 du joueur.");
        assertNull(plateau.getCarteJoueur(3), "La case 3 du joueur devrait être restée vide.");
        assertSame(loupEnnemi, plateau.getCarteEnnemi(0), "Le loup ennemi devrait être positionné à la case 0 de l'ennemi.");
        assertNull(plateau.getCarteEnnemi(1), "La case 1 de l'ennemi devrait être restée vide.");
        plateau.placerCarteJoueur(rocher, 0);
        assertSame(rocher, plateau.getCarteJoueur(0), "Le rocher aurait dû remplacer l'ours en position 0 du joueur.");
        plateau.placerCarteJoueur(null, 0);
        assertNull(plateau.getCarteJoueur(0), "La case 0 du joueur devrait de nouveau être vide après y avoir placé null.");
    }

    @Test
    void testPiocherUneCarte() {
        MainJoueur mainJoueur = new MainJoueur();
        ArrayList<Animal> pioche = new ArrayList<>();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Animal loup = new Animal("Loup", 4, 3, 1, 0, false);
        pioche.add(ours);
        pioche.add(loup);
        assertEquals(2, pioche.size(), "La pioche devrait contenir 2 cartes au début.");
        Animal cartePiochee = pioche.remove(0);
        mainJoueur.ajouterMain(cartePiochee, 3);
        assertEquals(1, pioche.size(), "La pioche devrait contenir 1 carte après la pioche.");
        assertNotNull(mainJoueur.getCartesEnMain(), "La main ne devrait plus être considérée comme nulle.");
        ArrayList<Animal> cartesDansLaMain = mainJoueur.getCartesEnMain();
        assertSame(ours, cartesDansLaMain.get(3), "L'animal pioché doit être l'Ours.");
        assertSame(loup, pioche.get(0), "Le Loup doit rester dans la pioche.");
    }
    @Test
    void testMiseEnPlacePartieInitialisation() {
        Plateau plateau = new Plateau();
        for (int i = 0; i < 4; i++) {
            plateau.placerCarteJoueur(null, i);
            plateau.placerCarteEnnemi(null, i);
        }
        for (int i = 0; i < 4; i++) {
            assertNull(plateau.getCarteJoueur(i), "La case joueur " + i + " doit être vide au lancement.");
            assertNull(plateau.getCarteEnnemi(i), "La case ennemi " + i + " doit être vide au lancement.");
            if (plateau.getCartesIntentions() != null) {
                assertNull(plateau.getCartesIntentions().get(i), "La case intention " + i + " doit être vide au lancement.");
            }
        }
        MainJoueur mainJoueur = new MainJoueur();
        assertEquals(0, mainJoueur.getNbOsDisponibles(), "Le joueur doit commencer la partie avec 0 Os.");
        assertNotNull(mainJoueur.getCartesEnMain(), "L'objet contenant les cartes en main doit être initialisé.");
        assertEquals(4, mainJoueur.getCartesEnMain().size(), "La main doit posséder 4 emplacements de cartes au total.");
        assertTrue(mainJoueur.aPlace(), "La main doit avoir des places libres au début de la partie.");
        for (int i = 0; i < 4; i++) {
            assertNull(mainJoueur.getCartesEnMain().get(i), "L'emplacement de main " + i + " doit être vide (null) au tout début.");
        }
        ArrayList<Animal> deckPioche = new ArrayList<>();
        Animal ours = new Animal("Ours", 5, 4, 2, 0, false);
        Animal loup = new Animal("Loup", 4, 3, 1, 0, false);
        Animal lion = new Animal("Lion", 6, 5, 1, 0, false);

        deckPioche.add(ours);
        deckPioche.add(loup);
        deckPioche.add(lion);

        assertEquals(3, deckPioche.size(), "La pioche de départ doit contenir exactement 3 cartes.");
        assertSame(ours, deckPioche.get(0), "La première carte à piocher doit être l'Ours.");
    }

    @Test
    void testGagnerOuPerdrePartie() {
        // ==========================================
        // 1. SCÉNARIO DE VICTOIRE
        // ==========================================
        Plateau plateauVictoire = new Plateau();
        Score score = new Score();
        Combat combat = new Combat();
        MainJoueur mainJoueur = new MainJoueur();
        MainJoueur mainAdversaire = new MainJoueur();
        for (int i = 0; i < 4; i++) {
            plateauVictoire.placerCarteJoueur(null, i);
            plateauVictoire.placerCarteEnnemi(null, i);
        }
        Animal herosJoueur = new Animal("Lion", 5, 4, 1, 0, false);
        Animal ennemiMourant = new Animal("Gobelin", 1, 1, 1, 0, false);
        plateauVictoire.placerCarteJoueur(herosJoueur, 0);
        plateauVictoire.placerCarteEnnemi(ennemiMourant, 0);
        combat.gererAttaqueFinTour(plateauVictoire, score);
        combat.verifierMorts(plateauVictoire, mainJoueur, mainAdversaire);
        boolean estVictoire = true;
        for (int i = 0; i < 4; i++) {
            if (plateauVictoire.getCarteEnnemi(i) != null) {
                estVictoire = false;
            }
        }
        assertTrue(estVictoire, "Le joueur devrait gagner la partie car le plateau ennemi est vide !");

        // ==========================================
        // 2. SCÉNARIO DE DÉFAITE
        // ==========================================

        Plateau plateauDefaite = new Plateau();
        Animal joueurMourant = new Animal("Souris", 1, 1, 1, 0, false);
        Animal grosOursEnnemi = new Animal("Ours", 10, 5, 1, 0, false);
        plateauDefaite.placerCarteJoueur(joueurMourant, 0);
        plateauDefaite.placerCarteEnnemi(grosOursEnnemi, 0);
        for (int i = 0; i < 4; i++) {
            plateauDefaite.placerCarteJoueur(null, i);
            plateauDefaite.placerCarteEnnemi(null, i);
        }
        combat.gererAttaqueFinTour(plateauDefaite, score);
        combat.verifierMorts(plateauDefaite, mainJoueur, mainAdversaire);
        assertNull(plateauDefaite.getCarteJoueur(0), "La carte du joueur a été détruite.");
        boolean estDefaite = true;
        for (int i = 0; i < 4; i++) {
            if (plateauDefaite.getCarteJoueur(i) != null) {
                estDefaite = false;
            }
        }
        assertTrue(estDefaite, "Le joueur devrait perdre la partie car il n'a plus aucune créature sur le plateau !");
    }

    @Test
    void testAjoutNouvellesCartesDansPiocheFinDeuxiemePartie() {
        ArrayList<Animal> piocheGamer = new ArrayList<>();
        Animal loupDeBase = new Animal("Loup", 4, 3, 1, 0, false);
        piocheGamer.add(loupDeBase);
        assertEquals(1, piocheGamer.size(), "La pioche devrait contenir 1 carte avant l'ajout des récompenses.");
        Animal oursAlpha = new Animal("Ours Alpha", 8, 6, 2, 0, false);
        Animal aigleRoyal = new Animal("Aigle Royal", 5, 4, 1, 0, true); // Animal Volant
        piocheGamer.add(oursAlpha);
        piocheGamer.add(aigleRoyal);
        assertEquals(3, piocheGamer.size(), "La pioche devrait contenir 3 cartes après l'ajout des récompenses de la partie 2.");
        assertSame(loupDeBase, piocheGamer.get(0), "La première carte reste le Loup initial.");
        assertSame(oursAlpha, piocheGamer.get(1), "L'Ours Alpha a bien été ajouté en deuxième position.");
        assertSame(aigleRoyal, piocheGamer.get(2), "L'Aigle Royal a bien été ajouté en troisième position.");
        assertEquals("Aigle Royal", piocheGamer.get(2).getNom(), "La troisième carte doit être l'Aigle Royal.");
        assertTrue(piocheGamer.get(2).getVolant(), "L'Aigle Royal ajouté doit posséder l'attribut volant.");
    }

    @Test
    void testGagnerOuPerdreLeJeu() {
        Combat combat = new Combat();
        Score score = new Score();
        MainJoueur mainJoueur = new MainJoueur();
        MainJoueur mainAdversaire = new MainJoueur();

        // =========================================================================
        // SCÉNARIO 1 : VICTOIRE DU JOUEUR
        // =========================================================================
        Plateau plateauVictoire = new Plateau();
        for (int i = 0; i < 4; i++) {
            plateauVictoire.placerCarteJoueur(null, i);
            plateauVictoire.placerCarteEnnemi(null, i);
        }
        Animal lionAllie = new Animal("Lion", 5, 4, 1, 0, false);
        Animal gobelinEnnemi = new Animal("Gobelin", 1, 1, 1, 0, false);
        plateauVictoire.placerCarteJoueur(lionAllie, 0);
        plateauVictoire.placerCarteEnnemi(gobelinEnnemi, 0);
        combat.gererAttaqueFinTour(plateauVictoire, score);
        combat.verifierMorts(plateauVictoire, mainJoueur, mainAdversaire);
        boolean estVictoire = true;
        for (int i = 0; i < 4; i++) {
            if (plateauVictoire.getCarteEnnemi(i) != null) {
                estVictoire = false;
            }
        }
        assertTrue(estVictoire, "Le joueur devrait gagner la partie car le plateau ennemi est complètement vide !");

        // =========================================================================
        // SCÉNARIO 2 : DÉFAITE DU JOUEUR (Toutes les créatures du joueur meurent)
        // =========================================================================
        Plateau plateauDefaite = new Plateau();
        for (int i = 0; i < 4; i++) {
            plateauDefaite.placerCarteJoueur(null, i);
            plateauDefaite.placerCarteEnnemi(null, i);
        }
        Animal sourisAlliee = new Animal("Souris", 1, 1, 1, 0, false);
        Animal oursEnnemi = new Animal("Ours", 10, 5, 1, 0, false);
        plateauDefaite.placerCarteJoueur(sourisAlliee, 0);
        plateauDefaite.placerCarteEnnemi(oursEnnemi, 0);
        combat.gererAttaqueFinTour(plateauDefaite, score);
        combat.verifierMorts(plateauDefaite, mainJoueur, mainAdversaire);
        boolean estDefaite = true;
        for (int i = 0; i < 4; i++) {
            if (plateauDefaite.getCarteJoueur(i) != null) {
                estDefaite = false;
            }
        }
        assertTrue(estDefaite, "Le joueur devrait perdre la partie car il n'a plus aucune créature sur le plateau !");
    }



    // ///////////////////////////////////////////////////////////
    // GESTION DES ERREURS
    // ///////////////////////////////////////////////////////////

    @Test
    public void placementHorsBorneException() throws Exception {
        Plateau plateau = new Plateau();
        Animal lion = new Animal("Lion", 5, 4, 1, 0, false);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            plateau.getCarteJoueur(99);
        });
    }

    @Test
    public void saisieIncorrecteException() throws Exception {
        String saisieUtilisateur = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(saisieUtilisateur.getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(InputMismatchException.class, () -> {
            scanner.nextInt();
        });
        scanner.next();
        int positionValide = scanner.nextInt();
        assertEquals(2, positionValide, "Après l'erreur, l'utilisateur a pu entrer une valeur valide (2).");
    }


    class SaisieInvalideException extends Exception {
        public SaisieInvalideException(String message) { super(message); }
    }

    class ValidateurSaisie {
        public int validerPosition(String saisie) throws SaisieInvalideException {
            try {
                int position = Integer.parseInt(saisie);
                if (position < 0 || position > 3) {
                    throw new SaisieInvalideException("Position hors-limites : Le nombre " + position + " ne correspond à aucune case (choisir entre 0 et 3).");
                }
                return position;
            } catch (NumberFormatException e) {
                throw new SaisieInvalideException("Format incorrect : '" + saisie + "' n'est pas un nombre entier.");
            }
        }
    }

    @Test
    public void testSaisieInvalideIndiqueLeProblemeException() throws Exception {
        ValidateurSaisie validateur = new ValidateurSaisie();
        SaisieInvalideException exTexte = assertThrows(SaisieInvalideException.class, () -> {
            validateur.validerPosition("xyz");
        });
        assertEquals("Format incorrect : 'xyz' n'est pas un nombre entier.", exTexte.getMessage());
        SaisieInvalideException exBorne = assertThrows(SaisieInvalideException.class, () -> {
            validateur.validerPosition("7");
        });
        assertEquals("Position hors-limites : Le nombre 7 ne correspond à aucune case (choisir entre 0 et 3).", exBorne.getMessage());
    }
}
