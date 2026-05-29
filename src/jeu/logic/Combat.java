package jeu.logic;

import jeu.model.*;

public class Combat {

    // Note : Ajout du Score en paramètre pour pouvoir le modifier
    public String gererAttaqueFinTour(Plateau plateau, Score score) {
        StringBuilder resumeCombat = new StringBuilder("--- Phase de Combat ---\n");
        int totalDegatsJoueur = 0;
        int totalDegatsEnnemi = 0;

        for (int i = 0; i < 4; i++) {
            // Attaque du joueur
            int degatsJ = executerAttaque(plateau.getCartesLigneBas().get(i),
                    plateau.getCartesLigneHaut().get(i), i, false, resumeCombat, score);
            totalDegatsJoueur += degatsJ;

            // Attaque de l'ennemi
            int degatsE = executerAttaque(plateau.getCartesLigneHaut().get(i),
                    plateau.getCartesLigneBas().get(i), i, true, resumeCombat, score);
            totalDegatsEnnemi += degatsE;
        }
        return resumeCombat.toString();
    }

    private int executerAttaque(Carte attaquant, Carte cible, int position, boolean estEnnemi, StringBuilder resume, Score score) {
        if (!(attaquant instanceof Animal) || !attaquant.estVie()) {
            return 0;
        }
        Animal animalAttaquant = (Animal) attaquant;

        // Logique de dégâts : Si pas de cible, on inflige des dégâts au score
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
            // Combat classique contre une carte
            return appliquerDegats(animalAttaquant, cible, position);
        }
    }

    public Integer appliquerDegats(Animal attaquant, Carte cible, Integer position) {
        int puissance = attaquant.getAttack();

        // Règle du vol : attaque directement si l'adversaire n'est pas volant
        if (attaquant.getVolant() && (cible instanceof Animal && !((Animal) cible).getVolant())) {
            cible.modifierVie(puissance);
            return puissance;
        }

        // Combat standard
        cible.modifierVie(puissance);
        return puissance;
    }

    public void verifierMorts(Plateau plateau, MainJoueur mainJoueur, MainJoueur mainAdversaire) {
        // Logique existante : vérification des points de vie <= 0
        for (int i = 0; i < 4; i++) {
            if (plateau.getCartesLigneBas().get(i) != null && !plateau.getCartesLigneBas().get(i).estVie()) {
                mainJoueur.ajouterOs(1);
                plateau.getCartesLigneBas().set(i, null);
            }
            if (plateau.getCartesLigneHaut().get(i) != null && !plateau.getCartesLigneHaut().get(i).estVie()) {
                if (mainAdversaire != null) mainAdversaire.ajouterOs(1);
                plateau.getCartesLigneHaut().set(i, null);
            }
        }
    }
}