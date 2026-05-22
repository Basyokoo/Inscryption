package jeu.model;

import java.util.ArrayList;

public class Adversaire {
    private Carte[] m_actionsPlanifiees;

    public Adversaire() {
        this.m_actionsPlanifiees = new Carte[4];
    }

    public Carte[] getActionsPlani() {
        return this.m_actionsPlanifiees;
    }


    public void planifierProchainTour(int numTour) {
        // On nettoie le tableau pour le nouveau tour
        for (int i = 0; i < 4; i++) {
            this.m_actionsPlanifiees[i] = null;
        }

        if (numTour == 1) {
            this.m_actionsPlanifiees[0] = new Animal("Loup", 4, 2, 2, 0, false);
        } else if (numTour == 2) {
            this.m_actionsPlanifiees[1] = new Obstacle("Souche", 3);
            this.m_actionsPlanifiees[3] = new Animal("Corbeau", 3, 2, 1, 0, true);
        } else {
            this.m_actionsPlanifiees[2] = new Animal("Ours", 6, 4, 3, 0, false);
        }
    }

    public void envoyerIntentionsAuPlateau(Plateau plateau) {
        for (int i = 0; i < 4; i++) {
            plateau.setCarteIntention(this.m_actionsPlanifiees[i], i);
        }
    }


    public void executerProchainTour(Plateau plateau) {
        plateau.avancerLigne();
    }
}