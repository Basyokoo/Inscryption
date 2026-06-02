package jeu.model;

public abstract class Carte {
    private String m_nom;
    private int m_pointsVies = 0;
    private int m_vieInit = 0;
    private int m_Attack = 0;

    public Carte(String m, int num, String type){
        this.m_nom = m;
        this.m_pointsVies = num;
    }

    public Carte(String m, int num, String type, int degat){
        this.m_nom = m;
        this.m_pointsVies = num;
        this.m_vieInit = num;
        this.m_Attack = degat;
    }

    public int getInitVie(){return m_vieInit;}

    public String getNom(){
        return this.m_nom;
    }

    public int getVie(){
        return this.m_pointsVies;
    }

    public String modifierNom(String n){
        this.m_nom = n;
        return "Changement de nom fait !";
    }

    public String modifierVie(int i){
        this.m_pointsVies = i;
        return "Modification de la vie faite";
    }

    public String ajouterVie(int changement) {
        this.m_pointsVies += changement;

        if (this.m_pointsVies < 0) {
            this.m_pointsVies = 0;
        }

        if (changement < 0) {
            return "Dégâts subis : " + changement + ".";
        } else {
            return "Points de vie modifiés de : +" + changement + ".";
        }
    }


    public abstract String setPouvoir(Pouvoir p);

    public abstract boolean estAnimal();

    public abstract boolean getVolant();

    public abstract Pouvoir getPouvoir();

    public abstract void ajouterAttack(int i);

    public abstract int getAttack();

    public boolean estVie(){
        return this.m_pointsVies > 0;
    }

    @Override
    public String toString(){
        return "Carte : this.m_nom";
    }
}
