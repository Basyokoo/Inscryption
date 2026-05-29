package jeu.logic;

import jeu.model.*;

public class Combat {

    public String gererAttaqueFinTour(Joueur joueur, Adversaire adversaire, Score score) {
        StringBuilder resumeCombat = new StringBuilder("--- Phase de Combat ---\n");

        for (int i = 0; i < 4; i++) {
            // Attaque du joueur vers la ligne de l'adversaire
            executerAttaque(joueur.getCartesLigneBas().get(i),
                    adversaire.getCartesLigneHaut().get(i), i, false, resumeCombat, score);

            // Attaque de l'ennemi vers la ligne du joueur
            executerAttaque(adversaire.getCartesLigneHaut().get(i),
                    joueur.getCartesLigneBas().get(i), i, true, resumeCombat, score);
        }
        return resumeCombat.toString();
    }

    private int executerAttaque(Carte attaquant, Carte cible, int position, boolean estEnnemi, StringBuilder resume, Score score) {
        if (!(attaquant instanceof Animal) || !attaquant.estVie()) {
            return 0;
        }
        Animal animalAttaquant = (Animal) attaquant;

        if (cible == null || !cible.estVie()) {
            int degats = animalAttaquant.getAttack();
            if (!estEnnemi) {
                score.ajouterPointsJoueur(degats);
                resume.append(animalAttaquant.getNom()).append(" attaque le score adverse pour ").append(degats).append(" pts.\n");
            } else {
                score.ajouterPointsEnnemi(degats);
                resume.append("L'ennemi ").append(animalAttaquant.getNom()).append(" attaque votre score pour ").append(degats).append(" pts.\n");
            }
            return degats;
        } else {
            return appliquerDegats(animalAttaquant, cible, position);
        }
    }

    public Integer appliquerDegats(Animal attaquant, Carte cible, Integer position) {
        int puissance = attaquant.getAttack();

        if (attaquant.getVolant() && (cible instanceof Animal && !((Animal) cible).getVolant())) {
            cible.modifierVie(puissance);
            return puissance;
        }

        cible.modifierVie(puissance);
        return puissance;
    }
}