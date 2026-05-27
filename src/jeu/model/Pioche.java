package jeu.model;

import java.util.ArrayList;

public class Pioche {
    private ArrayList<Animal> m_cartes;

    public Pioche() {
        m_cartes = new ArrayList<>();
    }
    public void initialiserPiocheDeBase() {
        m_cartes = new ArrayList<>();
    }
    public Animal piocher() {
        if (m_cartes.isEmpty()) {
            return null;
        }
        Animal tempCarte = new Animal(m_cartes.get(0));
        m_cartes.remove(0);
        return tempCarte;
    }
    public void ajouterCartes(Animal carte) {
        m_cartes.add(carte);
    }
    public int getNombreCartes() {
        return m_cartes.size();
    }
}
