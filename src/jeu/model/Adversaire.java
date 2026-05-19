package jeu.model;
import java.util.ArrayList;

import java.util.ArrayList;

public class Adversaire {
    private ArrayList<Carte> m_actionsPlanifiees;

    public Adversaire() {
        this.m_actionsPlanifiees = new ArrayList<>();
    }
    public void planifierProchainTour(int numTour) {
        this.m_actionsPlanifiees.clear();
        if (numTour == 1) {
            this.m_actionsPlanifiees.add(new Animal("Loup", 4, 2, 2, 0, false));
        } else if (numTour == 2) {
            this.m_actionsPlanifiees.add(new Obstacle("Souche", 3));
            this.m_actionsPlanifiees.add(new Animal("Corbeau", 3, 2, 1, 0, true));
        } else {
            this.m_actionsPlanifiees.add(new Animal("Ours", 6, 4, 3, 0, false));
        }
    }

    public void executerProchainTour(Plateau plateau) {
        plateau.avancerLigne();
        if (this.m_actionsPlanifiees != null && !this.m_actionsPlanifiees.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                if (plateau.getCarteEnnemi(i) == null && !this.m_actionsPlanifiees.isEmpty()) {
                    Carte carteAPlacer = this.m_actionsPlanifiees.remove(0);
                    plateau.placerCarteEnnemi(carteAPlacer, i);
                }
            }
        }
    }
}
