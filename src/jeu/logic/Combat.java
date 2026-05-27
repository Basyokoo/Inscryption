package jeu.logic;

import jeu.model.Plateau;
import jeu.model.Carte;
import jeu.model.Animal;
import jeu.model.MainJoueur;
import java.util.ArrayList;
import java.util.List;

public class Combat {

    public String gererAttaquesFinTour(Plateau plateau) {
        StringBuilder resumeCombat = new StringBuilder("--- Phase de Combat ---\n");
        int totalDegatsJoueur = 0;
        int totalDegatsEnnemi = 0;

        for (int i = 0; i < 4; i++) {
            int degatsJ = executerAttaque(plateau.getCarteJoueur(i), plateau.getCarteEnnemi(i), i, false, resumeCombat);
            totalDegatsJoueur += degatsJ;
            int degatsE = executerAttaque(plateau.getCarteEnnemi(i), plateau.getCarteJoueur(i), i, true, resumeCombat);
            totalDegatsEnnemi += degatsE;
        }
        resumeCombat.append(String.format("Fin du combat. Dégâts totaux du joueur : %d | Ennemi : %d",
                totalDegatsJoueur, totalDegatsEnnemi));
        return resumeCombat.toString();
    }

    private int executerAttaque(Carte attaquant, Carte cible, int position, boolean estEnnemi, StringBuilder resume) {
        if (!(attaquant instanceof Animal) || !attaquant.estVie()) {
            return 0;
        }
        Animal animalAttaquant = (Animal) attaquant;
        int degatsInfliges = appliquerDegats(animalAttaquant, cible, position);
        if (degatsInfliges > 0) {
            String prefixe = estEnnemi ? "L'ennemi " : "";
            resume.append(prefixe)
                    .append(animalAttaquant.getNom())
                    .append(" inflige ")
                    .append(degatsInfliges)
                    .append(" dégâts en position ")
                    .append(position).append(".\n");
        }
        return degatsInfliges;
    }
    public Integer appliquerDegats(Animal attaquant, Carte cible, Integer position) {
        if (attaquant == null || attaquant.getAttack() <= 0) {
            return 0;
        }
        if (position < 0 || position > 3) {
            return 0;
        }
        int puissance = attaquant.getAttack();
        if (cible != null && cible.estVie()) {
            if (attaquant.getVolant() && (cible instanceof Animal && !((Animal) cible).getVolant())) {
                return puissance;
            }
            cible.modifierVie(puissance);
        }
        return puissance;
    }
    public void verifierMorts(Plateau plateau, MainJoueur mainJoueur, MainJoueur mainAdversaire) {
        for (int i = 0; i < 4; i++) {
            Carte carteJoueur = plateau.getCarteJoueur(i);
            if (carteJoueur != null && !carteJoueur.estVie()) {
                mainJoueur.ajouterOs(1);
                plateau.placerCarteJoueur(null, i);
            }
            Carte carteEnnemi = plateau.getCarteEnnemi(i);
            if (carteEnnemi != null && !carteEnnemi.estVie()) {
                if (mainAdversaire != null) {
                    mainAdversaire.ajouterOs(1);
                }

                plateau.placerCarteEnnemi(null, i);
            }
        }
    }
}