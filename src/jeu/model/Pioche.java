package jeu.model;

import java.util.ArrayList;
import java.util.Random;

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

    public String genererPioche(){
        Random rNum = new Random();
        Animal ani = null;
        int nbEcu = 0;

        for (int i = 0; i < 12; i++){
            int rnd = rNum.nextInt(3);
            if((rnd == 0 || rnd == 1) && nbEcu < 4){
                ani = new Animal("Ecureuil", 1, 0, 0, 0, false);
            }else{
                int rndAni = rNum.nextInt(12);
                switch (rndAni) {
                    case 0:
                        ani = new Animal("Chat", 1, 0, 1, 0, false, new Pouvoir("Nombreuses Vies", "NV", 0));
                        break;
                    case 1:
                        ani = new Animal("Grizzly", 6, 4, 3, 0, false);
                        break;
                    case 2:
                        ani = new Animal("Coyote", 1, 2, 0, 4, false);
                        break;
                    case 3:
                        ani = new Animal("Moineau", 2, 1, 1, 0, true);
                        break;
                    case 4:
                        ani = new Animal("Corbeau", 3, 2, 2, 0, true);
                        break;
                    case 5:
                        ani = new Animal("Hermine", 3, 1, 1, 0, false);
                        break;
                    case 6:
                        ani = new Animal("Louveteau", 1, 1, 1, 0, false, new Pouvoir("Croissance", "C", 1));
                        break;
                    case 7:
                        ani = new Animal("Loup", 2, 3, 2, 0, false);
                        break;
                    case 8:
                        ani = new Animal("Punaise", 2, 1, 0, 2, false, new Pouvoir("Puant", "P", 0));
                        break;
                    case 9:
                        ani = new Animal("Elan", 4, 2, 2, 0, false, new Pouvoir("Coureur", "C", 0));
                        break;
                    case 10:
                        ani = new Animal("Vipère", 1, 1, 2, 0, false, new Pouvoir("Contact mortel", "CM", 0));
                        break;
                    case 11:
                        ani = new Animal("Porc-épic", 2, 1, 1, 0, false, new Pouvoir("Piques pointues", "PP", 0));
                        break;
                }
            }
            ajouterCartes(ani);
        }

        return "Pioche générer avec succés !";

    }
}
