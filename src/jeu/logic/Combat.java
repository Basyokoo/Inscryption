package jeu.logic;

import jeu.model.*;

public class Combat {

    public String gererAttaqueFinTour(Joueur joueur, Adversaire adversaire, Score score) {
        StringBuilder resumeCombat = new StringBuilder("--- Phase de Combat ---\n");

        // 1. Attaque du joueur vers l'adversaire
        for (int i = 0; i < 4; i++) {
            executerAttaque(joueur.getCartesLigneBas().get(i),
                    adversaire.getCartesLigneHaut().get(i), i, false, resumeCombat, score);
        }

        adversaire.verifierMorts();

        // 2. Attaque de l'adversaire vers le joueur
        for (int i = 0; i < 4; i++) {
            executerAttaque(adversaire.getCartesLigneHaut().get(i),
                    joueur.getCartesLigneBas().get(i), i, true, resumeCombat, score);
        }

        joueur.verifierMorts();

        return resumeCombat.toString();
    }

    private void executerAttaque(Carte attaquant, Carte cible, int position, boolean estEnnemi, StringBuilder resume, Score score) {
        if (attaquant == null || !(attaquant.estAnimal()) || !attaquant.estVie()) return;

        Animal animalAttaquant = (Animal) attaquant;
        int degats = animalAttaquant.getAttack();

        if (animalAttaquant.getVolant() || cible == null || !cible.estVie()) {
            if (!estEnnemi) {
                score.ajouterPointsJoueur(degats);
                if (animalAttaquant.getVolant()) {
                    resume.append(animalAttaquant.getNom()).append(" survole la ligne et attaque le score adverse pour ").append(degats).append(" pts.\n");
                } else {
                    resume.append(animalAttaquant.getNom()).append(" attaque le score pour ").append(degats).append(" pts.\n");
                }
            } else {
                score.ajouterPointsEnnemi(degats);
                if (animalAttaquant.getVolant()) {
                    resume.append("L'ennemi ").append(animalAttaquant.getNom()).append(" survole votre ligne et vous attaque pour ").append(degats).append(" pts.\n");
                } else {
                    resume.append("L'ennemi ").append(animalAttaquant.getNom()).append(" attaque votre score pour ").append(degats).append(" pts.\n");
                }
            }
            return;
        }

        int degatsInfliges = appliquerDegats(animalAttaquant, cible);
        resume.append(animalAttaquant.getNom()).append(" attaque ").append(cible.getNom())
                .append(" pour ").append(degatsInfliges).append(" pts.\n");
    }

    public int appliquerDegats(Animal attaquant, Carte cible) {
        int puissance = attaquant.getAttack();

        cible.modifierVie(puissance);

        return puissance;
    }
}