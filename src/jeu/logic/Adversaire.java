package jeu.logic;

import jeu.model.*;
import java.util.ArrayList;
import java.util.Random;

public class Adversaire {
    private Carte[] m_actionsPlanifiees;
    private ArrayList<Carte> m_ligneE;
    private ArrayList<Carte> m_ligneEPT;
    private ArrayList<Pouvoir> m_pouvoir;
    private int m_nbObsE = 0;

    public Adversaire(){
        this.m_actionsPlanifiees = new Carte[4];
        this.m_ligneE = new ArrayList<Carte>();
        this.m_ligneEPT = new ArrayList<Carte>();
        this.m_pouvoir = new ArrayList<Pouvoir>();

        for (int i = 0; i < 4; i++) {
            this.m_pouvoir.add(null);
            this.m_ligneE.add(null);
            this.m_ligneEPT.add(null);
        }
        this.placerObst();
    }

    public boolean placerCarteEnnemi(Carte c, int pos){
        if (pos >= 0 && pos < 4) {
            this.m_ligneE.set(pos, c);
            return true;
        }
        return false;
    }

    public boolean setCarteIntention(Carte c, int pos) {
        if (pos >= 0 && pos < 4) {
            this.m_ligneEPT.set(pos, c);
            return true;
        }
        return false;
    }

    public boolean avancerLigne(){
        for (int i = 0; i < 4; i++){
            if(this.m_ligneEPT.get(i) != null && estCaseLibre(i)){
                placerCarteEnnemi(this.m_ligneEPT.get(i), i);
                this.m_ligneEPT.set(i, null);
            }
        }
        return true;
    }

    public Carte getCarteEnnemi(int place){
        return m_ligneE.get(place);
    }

    public ArrayList<Carte> getCartesIntentions() {
        return m_ligneEPT;
    }

    public ArrayList<Carte> getCartesLigneHaut() {
        return m_ligneE;
    }

    public Carte[] getActionsPlani() {
        return this.m_actionsPlanifiees;
    }

    public void planifierProchainTour() {
        for (int i = 0; i < 4; i++) {
            this.m_actionsPlanifiees[i] = null;
        }

        Random rNum = new Random();
        Animal ani = null;
        int nbEcu = 0;

        for (int i = 0; i < 4; i++){
            int rnd = rNum.nextInt(2);
            if(rnd == 0){
                setCarteIntention(null,i);
            }else{
                int rndAni = rNum.nextInt(12);
                switch (rndAni) {
                    case 0: ani = new Animal("Chat", 1, 0, 1, 0, false, new Pouvoir("Nombreuses Vies", "NV", 1)); break;
                    case 1: ani = new Animal("Grizzly", 6, 4, 3, 0, false); break;
                    case 2: ani = new Animal("Coyote", 1, 2, 0, 4, false); break;
                    case 3: ani = new Animal("Moineau", 2, 1, 1, 0, true); break;
                    case 4: ani = new Animal("Corbeau", 3, 2, 2, 0, true); break;
                    case 5: ani = new Animal("Hermine", 3, 1, 1, 0, false); break;
                    case 6: ani = new Animal("Louveteau", 1, 1, 1, 0, false, new Pouvoir("Croissance", "CR", 1)); break;
                    case 7: ani = new Animal("Loup", 2, 3, 2, 0, false); break;
                    case 8: ani = new Animal("Punaise", 2, 1, 0, 2, false, new Pouvoir("Puant", "P", 0)); break;
                    case 9: ani = new Animal("Elan", 4, 2, 2, 0, false, new Pouvoir("Coureur", "C", 0)); break;
                    case 10: ani = new Animal("Vipère", 1, 1, 2, 0, false, new Pouvoir("Contact mortel", "CM", 0)); break;
                    case 11: ani = new Animal("Porc-épic", 2, 1, 1, 0, false, new Pouvoir("Piques pointues", "PP", 0)); break;
                }
            }
            setCarteIntention(ani,i);
        }
    }

    public void envoyerIntentionsAuPlateau() {
        for (int i = 0; i < 4; i++) {
            if (estCaseLibre(i)) {
                this.setCarteIntention(this.m_actionsPlanifiees[i], i);
            } else {
                // Optionnel : Gérer le cas où l'adversaire "perd" son action s'il est bloqué
                this.setCarteIntention(null, i);
            }
        }
    }

    public boolean placerObst(){
        Random rNum = new Random();
        int rnd1 = rNum.nextInt(2) + 1;

        for (int i = 0; i < rnd1; i++){
            Obstacle obs;
            int sapOuKayou = rNum.nextInt(2);
            if(sapOuKayou == 0){
                obs = new Obstacle("Rocher", 5);
            } else {
                obs = new Obstacle("Sapin", 3);
            }

            int rndPos = rNum.nextInt(4);
            if (this.m_nbObsE < 4 && this.m_ligneE.get(rndPos) == null){
                this.m_ligneE.set(rndPos, obs);
                this.m_nbObsE++;
            }
        }
        return true;
    }

    public void verifierMorts() {
        for (int i = 0; i < m_ligneE.size(); i++) {
            Carte c = m_ligneE.get(i);
            if (c != null && !c.estVie()) {
                m_ligneE.set(i, null);
            }
        }
    }
    public boolean estCaseLibre(int pos) {
        // Vérifie si la case dans la ligne active est vide
        return this.m_ligneE.get(pos) == null;
    }

}
