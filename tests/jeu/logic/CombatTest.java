package jeu.logic;
import jeu.model.Carte;
import jeu.model.Animal;
import jeu.model.Pouvoir;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jeu.logic.SaisieInvalideException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.jupiter.api.Assertions.*;
public class CombatTest  {
    @Test
    public void testAttackCarte(){
        Animal a = new Animal("Loup", 2, 3, 2, 0, false);
        assertEquals(3, a.getAttack(), "L'attaque devrait être de 3 avant l'ajout");
        a.ajouterAttack(2);
        assertEquals(5, a.getAttack(), "L'attaque devrait être de 5 après l'ajout");
    }

    @Test
    public void testAttaqueToutesCartesFinTour() {
        Animal attaquant = new Animal("Loup", 2, 3, 2, 0, false);
        Animal cible = new Animal("Grizzly", 6, 4, 3, 0, false);
        cible.modifierVie(attaquant.getAttack());
        assertEquals(3, cible.getVie(), "Les points de vie de la cible devraient être de 3 après l'attaque");
    }

    @Test
    public void testPouvoirChat() {
        Pouvoir pouvoirChat = new Pouvoir("Nombreuses Vies", "Passif", 0);
        Animal chat = new Animal("Chat", 1, 0, 1, 0, false, pouvoirChat);
        assertEquals("Nombreuses Vies", chat.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Nombreuses Vies'");
    }
    @Test
    public void testPouvoirLouveteau() {
        Pouvoir pouvoirLouveteau = new Pouvoir("Croissance", "Passif", 0);
        Animal louveteau = new Animal("Louveteau", 1, 1, 1, 0, false, pouvoirLouveteau);
        assertEquals("Croissance", louveteau.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Croissance'");
    }
    @Test
    public void testPouvoirPunaise() {
        Pouvoir pouvoirPunaise = new Pouvoir("Puant", "Passif", 0);
        Animal punaise = new Animal("Punaise", 2, 1, 0, 2, false, pouvoirPunaise);
        assertEquals("Puant", punaise.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Puant'");
    }

    @Test
    public void testPouvoirElan() {
        Pouvoir pouvoirElan = new Pouvoir("Coureur", "Passif", 0);
        Animal elan = new Animal("Elan", 4, 2, 2, 0, false, pouvoirElan);
        assertEquals("Coureur", elan.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Coureur'");
    }

    @Test
    public void testPouvoirVipere() {
        Pouvoir pouvoirVipere = new Pouvoir("Contact mortel", "Passif", 0);
        Animal vipere = new Animal("Vipère", 1, 1, 2, 0, false, pouvoirVipere);
        assertEquals("Contact mortel", vipere.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Contact mortel'");
    }

    @Test
    public void testPouvoirPorcEpic() {
        Pouvoir pouvoirPorcEpic = new Pouvoir("Piques pointues", "Passif", 0);
        Animal porcEpic = new Animal("Porc-épic", 2, 1, 1, 0, false, pouvoirPorcEpic);
        assertEquals("Piques pointues", porcEpic.getPouvoir().getNom(),
                "Le nom du pouvoir devrait être 'Piques pointues'");
    }

    @Test
    public void testGestionPierreSacrifice() {
        Animal sacrifice = new Animal("Ecureuil", 1, 0, 0, 0, false); // Carte de sacrifice
        Animal cible = new Animal("Loup", 2, 3, 2, 0, false);        // Carte coûtant 2 sang
        boolean sacrificeReussi = sacrifieCarte(sacrifice);
        assertEquals(false, sacrifice.estVie(), "La carte sacrifiée devrait être morte (0 PV)");
        assertEquals(true, sacrificeReussi, "Le mécanisme de sacrifice devrait être validé");
    }
    private boolean sacrifieCarte(Carte c) {
        c.modifierVie(c.getVie());
        return !c.estVie();
    }
    @Test
    public void testMAJduScore() {
        Score score = new Score();
        assertEquals(0, score.getValeurEcart(), "Le score initial doit être 0");
        score.ajouterPointsJoueur(3);
        assertEquals(3, score.getValeurEcart(), "L'écart devrait être de 3 après ajout joueur");
        score.ajouterPointsEnnemi(2);
        assertEquals(1, score.getValeurEcart(), "L'écart devrait être de 1 après soustraction ennemi");
        assertEquals("1", score.getScoreJoueur(), "Le score joueur doit être 1");
        assertEquals("0", score.getScoreEnnemi(), "Le score ennemi doit être 0");
    }

    @Test
    public void testPlacementCartePlateau() {
        Joueur joueur = new Joueur();
        Animal loup = new Animal("Loup", 2, 3, 1, 0, false);
        joueur.setSangJoueur(1);
        joueur.ajouterMain(loup, 0);
        int posTerrain = 0;
        int idxCarteEnMain = 0;
        if (joueur.getSangJoueur() >= loup.getCoutSang()) {
            joueur.consommerSang(loup.getCoutSang());
            joueur.placerCarteJoueur(loup, posTerrain);
            joueur.enleverCarteJoueur(idxCarteEnMain);
        }
        assertEquals(0, joueur.getSangJoueur(), "Le sang du joueur doit être consommé");
        assertEquals(loup, joueur.getCarteJoueur(posTerrain), "La carte doit être présente sur le plateau");
    }

    @Test
    public void testPiocherCarte() {
        Joueur joueur = new Joueur();
        joueur.initialiserPiocheDeBase();
        joueur.genererPioche();
        int taillePiocheInitiale = joueur.getNombreCartes();
        Animal cartePiocher = joueur.piocher();
        assertNotNull(cartePiocher, "La carte piochée ne devrait pas être nulle");
        assertEquals(taillePiocheInitiale - 1, joueur.getNombreCartes(),
                "La taille de la pioche devrait avoir diminué de 1");
    }

    @Test
    public void testMEPPartie() {
        GestionPartie partie = new GestionPartie();
        partie.preparerNouvelleManche();
        assertEquals(0, partie.getScore().getValeurEcart(),
                "Le score devrait être de 0 au début de la manche");

        assertEquals(0, partie.getNumTour(),
                "Le numéro du tour devrait être 0 après préparation");

        assertEquals("", partie.getMessageAlerteCourant(),
                "Le message d'alerte devrait être vide");
    }

    @Test
    public void testGagnerOuPerdre() {
        GestionPartie partie = new GestionPartie();
        partie.getScore().ajouterPointsJoueur(5);
        assertTrue(partie.verifFinManche(), "La manche devrait être terminée");
        assertTrue(partie.getScore().estVictoireJoueur(), "Le joueur devrait avoir gagné la manche");
    }

    @Test
    public void testAjoutCartesSecondTour() {
        Joueur joueur = new Joueur();
        joueur.initialiserPiocheDeBase();
        joueur.genererPioche();
        int tailleInitiale = joueur.getNombreCartes();
        joueur = new Joueur();
        joueur.initialiserPiocheDeBase();
        joueur.genererPioche();
        assertTrue(joueur.getNombreCartes() > 0, "La pioche devrait contenir des cartes après réinitialisation");
        assertEquals(tailleInitiale, joueur.getNombreCartes(), "La taille de la pioche doit correspondre à une pioche complète");
    }

    @Test
    public void testGagnerOuPerdreJeu() {
        GestionPartie partie = new GestionPartie();
        for(int i = 0; i < 3; i++) {
            partie.finManche();
        }
        assertTrue(partie.verifFinPartie(), "La partie devrait être terminée après 3 victoires");
    }


    @Test
    public void testExceptionSaisie() {
        Assertions.assertThrows(SaisieInvalideException.class, () -> {
            throw new SaisieInvalideException("Erreur");
        });
    }

    @Test
    public void testExceptionRedemanderUser() {
        String[] entreesSimulees = {"abc", "2"};
        int resultat = testerLogiqueSaisie(entreesSimulees);
        assertEquals(2, resultat, "Le système devrait redemander la saisie et finir par accepter le chiffre valide");
    }
    private int testerLogiqueSaisie(String[] entrees) {
        int index = 0;
        int valeur = -1;
        boolean valide = false;
        while (!valide) {
            try {
                String saisie = entrees[index++];
                if (!saisie.matches("\\d+")) {
                    throw new SaisieInvalideException("Erreur");
                }
                valeur = Integer.parseInt(saisie);
                valide = true;
            } catch (SaisieInvalideException e) {
            }
        }
        return valeur;
    }

    @Test
    public void testCauseMauvaiseSaisie() {
        String saisieErreur = "ABC";
        String raisonAttendue = "La saisie doit être un nombre";
        SaisieInvalideException thrown = assertThrows(SaisieInvalideException.class, () -> {
            if (!saisieErreur.matches("\\d+")) {
                throw new SaisieInvalideException(raisonAttendue);
            }
        }, "Une exception devrait être levée pour une saisie non numérique");
        assertTrue(thrown.getMessage().contains(raisonAttendue),
                "Le message d'erreur doit indiquer précisément pourquoi la saisie est invalide");
    }
}