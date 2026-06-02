package jeu.logic;

import jeu.model.*;

public class Combat {

    public String gererAttaqueFinTour(Joueur joueur, Adversaire adversaire, Score score) {
        StringBuilder resumeCombat = new StringBuilder("--- Phase de Combat ---\n");
        for (int i = 0; i < 4; i++) {
            executerAttaque(joueur.getCartesLigneBas().get(i),
                    adversaire.getCartesLigneHaut().get(i), i, false, resumeCombat, score);
            executerAttaque(adversaire.getCartesLigneHaut().get(i),
                    joueur.getCartesLigneBas().get(i), i, true, resumeCombat, score);
        }
        return resumeCombat.toString();
    }

    private void executerAttaque(Carte attaquant, Carte cible, int position , boolean estEnnemi, StringBuilder resume, Score score) {
        if (!(attaquant instanceof Animal) || !attaquant.estVie()) return;
        Animal animalAttaquant = (Animal) attaquant;
        if (animalAttaquant.getVolant() && (cible == null || !cible.estVie())) {
            int degats = animalAttaquant.getAttack();
            if (!estEnnemi) {
                score.ajouterPointsJoueur(degats);
                resume.append(animalAttaquant.getNom()).append(" survole et attaque le score adverse pour ").append(degats).append(" pts.\n");
            } else {
                score.ajouterPointsEnnemi(degats);
                resume.append("L'ennemi ").append(animalAttaquant.getNom()).append(" survole et vous attaque pour ").append(degats).append(" pts.\n");
            }
            return;
        }
        if (cible != null && cible.estVie()) {
            int degats = appliquerDegats(animalAttaquant, cible);
            resume.append(animalAttaquant.getNom()).append(" attaque ").append(cible.getNom())
                    .append(" pour ").append(degats).append(" pts.\n");
        }
        else if (cible == null) {
            int degats = animalAttaquant.getAttack();
            if (!estEnnemi) score.ajouterPointsJoueur(degats);
            else score.ajouterPointsEnnemi(degats);
            resume.append(animalAttaquant.getNom()).append(" attaque le score pour ").append(degats).append(" pts.\n");
        }
    }

    public int appliquerDegats(Animal attaquant, Carte cible) {
        int puissance = attaquant.getAttack();
        if (attaquant.getVolant() && (cible instanceof Animal && !((Animal) cible).getVolant())) {
            return 0;
        }
        cible.modifierVie(puissance);
        return puissance;
    }
    public String appliquerPouvoir(Animal source, Carte cible) {
        if (source == null || source.getPouvoir() == null) {
            return "Aucun pouvoir.";
        }

        Pouvoir p = source.getPouvoir();
        String type = p.getType();
        switch (type) {
            case "CM":
                if (cible instanceof Animal) {
                    cible.modifierVie(cible.getVie()); // Réduit la vie à 0
                    return "Contact mortel : " + cible.getNom() + " meurt instantanément.";
                }
                return "Contact mortel sans effet sur un obstacle.";

            case "P":
                return "Pouvoir Puant : Attaque adverse réduite.";

            case "PP":
                if (cible instanceof Animal) {
                    ((Animal) cible).modifierVie(-1);
                    return "Piques pointues : " + cible.getNom() + " subit 1 dégât en retour.";
                }
                return "Piques pointues : aucun effet sur cible non-animale.";
            case "C":
                return "Pouvoir Coureur : Déplacement en attente.";
            case "NV":
                return "Nombreuses vies : prêt pour le sacrifice.";

            default:
                return "Pouvoir " + p.getType() + " non géré.";
        }
    }
}
