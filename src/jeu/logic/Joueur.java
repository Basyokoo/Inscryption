package jeu.logic;

import jeu.model.*;
import java.util.ArrayList;
import java.util.Random;

public class Joueur {
    private ArrayList<Animal> m_cartesEnMain;
    private ArrayList<Carte> m_ligneJ;
    private ArrayList<Animal> m_pioche;
    private int m_nbOsDisponibles;
    private int m_nbSangDisponibles;
    private boolean isNull = true;
    private int m_nbobsJ = 0;
    private int m_sang = 0;
    private int m_nbCartePioche = 0;
    private ArrayList<Pouvoir> m_pouvoir;
    private ArrayList<Pouvoir> m_pouvoirADonner;

    public Joueur() {
        this.m_nbOsDisponibles = 0;
        this.m_nbSangDisponibles = 0;
        this.m_cartesEnMain = new ArrayList<>();
        this.m_ligneJ = new ArrayList<Carte>();
        this.m_pouvoir = new ArrayList<Pouvoir>();
        this.m_pouvoirADonner = new ArrayList<Pouvoir>();
        for (int i = 0; i < 4; i++){
            this.m_pouvoir.add(null);
            this.m_cartesEnMain.add(null);
            this.m_ligneJ.add(null);
        }

        this.placerObst();
    }

    // --- Gestion des ressources (Os et Sang) ---
    public int getNbOsDisponibles() { return m_nbOsDisponibles; }

    public void ajouterOs(int quantite) {
        if (quantite > 0) this.m_nbOsDisponibles += quantite;
    }

    public void consommerOs(int quantite) {
        if (quantite > 0) this.m_nbOsDisponibles -= quantite;
    }

    public int getNbSangDisponibles() { return m_nbSangDisponibles; }

    public void ajouterSang(int quantite) {
        if (quantite > 0) this.m_nbSangDisponibles += quantite;
    }

    public void consommerSang(int quantite) {
        if (quantite > 0) this.m_nbSangDisponibles -= quantite;
    }
    public void initialiserPiocheDeBase() {
        this.m_pioche = new ArrayList<>();
    }

    public Animal piocher() {
        if (this.m_pioche.isEmpty()) {
            return null;
        }
        Animal tempCarte = new Animal(this.m_pioche.get(0));
        this.m_pioche.remove(0);
        return tempCarte;
    }

    public int getSizePioche(){ return this.m_nbCartePioche;}

    public void ajouterCartes(Animal carte) {
        this.m_pioche.add(carte);
    }

    public int getNombreCartes() {
        return this.m_pioche.size();
    }

    public boolean addPvr(Pouvoir p){
        if (this.m_pouvoirADonner.size() != 0){
            for (int i = 0; i < this.m_pouvoirADonner.size(); i ++){
                if (this.m_pouvoirADonner.get(i) == null) this.m_pouvoirADonner.set(i,p); return true;
            }
        }else{
            this.m_pouvoirADonner.add(p);
        }
        return true;
    }

    public String getPvrType(int index){
        return this.getCartesLigneBas(index).getPouvoir().getType();
    }

    public void ajouterMain(Animal carte, int index){
        if (carte != null && index >= 0 && index < m_cartesEnMain.size()) {
            this.m_cartesEnMain.set(index, carte);
            this.isNull = false;
        }
    }

    public String changerSlot(Carte c, int pos){
        int placeAChanger = getPosition(c);
        this.m_ligneJ.set(pos,c);
        this.m_ligneJ.set(placeAChanger,null);
        return "changement fait";
    }


    public boolean placerCarteJoueur(Carte c, int pos){
        if (pos >= 0 && pos < 4) {
            this.m_ligneJ.set(pos, c);
            return true;
        }
        return false;
    }

    public boolean enleverCarteJoueur(int index){
        this.m_cartesEnMain.set(index, null);
        return true;
    }

    public ArrayList<Animal> getCartesEnMain() {
        return m_cartesEnMain;
    }
    public String genererPioche(){
        Random rNum = new Random();
        Animal ani = null;
        int nbEcu = 0;

        for (int i = 0; i < 12; i++){
            int rnd = rNum.nextInt(3);
            if((rnd == 0 || rnd == 1) && nbEcu < 4){
                ani = new Animal("Ecureuil", 1, 0, 0, 0, false);
                nbEcu++;
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
            ajouterCartes(ani);
        }
        this.m_nbCartePioche = m_pioche.size();
        return "Pioche générée avec succès !";
    }

    public int getPosition(Carte c){return m_ligneJ.indexOf(c);}

    public String creeMain(){
        Random rNum = new Random();
        Animal ani = new Animal("Ecureuil", 1, 0, 0, 0, false);
        ajouterMain(ani, 0);
        int nbEcu = 0;

        for (int i = 1; i < 4; i++){
            int rnd = rNum.nextInt(3);
            if((rnd == 0 || rnd == 1) && nbEcu < 1){
                ani = new Animal("Ecureuil", 1, 0, 0, 0, false);
                nbEcu++;
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
            ajouterMain(ani, i);
        }
        return "Main générée avec succès !";
    }

    public boolean aPlace() {
        return this.m_cartesEnMain.contains(null);
    }

    public boolean aPlace(int index) {
        return this.m_cartesEnMain.get(index) == null;
    }

    public boolean aPlaceCarte(int index) {
        return this.m_ligneJ.get(index) == null;
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
            if(this.m_nbobsJ < 4 && this.m_ligneJ.get(rndPos) == null){
                this.m_ligneJ.set(rndPos, obs);
                this.m_nbobsJ++;
            }
        }
        return true;
    }

    public Carte getCarteJoueur(int place){
        return m_ligneJ.get(place);
    }

    public int getSangJoueur(){
        return this.m_sang;
    }

    public ArrayList<Carte> getCartesLigneBas() {
        return m_ligneJ;
    }

    public Carte getCartesLigneBas(int index) {
        return m_ligneJ.get(index);
    }

    public void verifierMorts() {
        for (int i = 0; i < m_ligneJ.size(); i++) {
            Carte c = m_ligneJ.get(i);
            if (c != null && !c.estVie()) {
                if(c.estAnimal()){
                    if (c.getPouvoir() != null) {
                        this.m_pouvoirADonner.add(c.getPouvoir());
                    }
                }
                ajouterOs(1);
                m_ligneJ.set(i, null);
            }
        }
    }


    public String appliquerPouvoir(Animal source, Carte cible) {
        if (source == null || source.getPouvoir() == null) {
            return "Aucun pouvoir.";
        }

        String type = source.getPouvoir().getType();
        switch (type) {

            case "CR":
                if (source.estAnimal() && source.getNom().equals("Louveteau")){
                    source.modifierNom("Loup"); source.ajouterCoutSang(1); source.ajouterAttack(2); source.modifierVie(3); source.setPouvoir(null);
                    return "Le louveteau a grandit";
                }
                return "Problème de croissance";

            case "CM":
                if (cible.estAnimal()) {
                    cible.modifierVie(0); // Réduit la vie à 0
                    return "Contact mortel : " + cible.getNom() + " meurt instantanément.";
                }
                return "Contact mortel sans effet sur un obstacle.";

            case "P":
                if (cible.estAnimal()){
                    cible.ajouterAttack(-1);
                    return "Pouvoir Puant : Attaque adverse réduite.";
                }
                return "Pouvoir puant sans effet sur un obstacle.";


            case "PP":
                if (cible.estAnimal()) {
                    cible.ajouterVie(-1);
                    return "Piques pointues : " + cible.getNom() + " subit 1 dégât en retour.";
                }
                return "Piques pointues : aucun effet sur cible non-animale.";
            case "C":
                if (this.aPlace(getPosition(source)+1)){
                    this.changerSlot(source,getPosition(source)+1);
                    return "Pouvoir Coureur : Déplacement à droite fait.";
                } else if (!this.aPlace(getPosition(source)+1) && this.aPlace(getPosition(source)-1)) {
                    this.changerSlot(source,getPosition(source)-1);
                    return "Pouvoir Coureur : Déplacement à gauche fait.";
                }
                return "Pouvoir Coureur : Déplacement non fait.";

            case "NV":
                if (source.getVie() <= 0 && source.getPouvoir().getActive() > 0){
                    source.modifierVie(source.getInitVie());
                    source.getPouvoir().modifActive(-1);
                    return "Nombreuses vies : sacrifice fait.";
                }
                return "Nombreuses vies : sacrifice non fait.";

            default:
                return "Erreur pouvoir non gérer !";
        }
    }


}

