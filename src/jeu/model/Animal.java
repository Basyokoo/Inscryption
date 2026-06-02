package jeu.model;

public class Animal extends Carte{
    protected int m_pointsAttaque;
    protected int m_coutSang;
    protected int m_coutOs;
    protected boolean m_volatile;
    private Pouvoir m_pouvoir = null;


    public Animal(Animal ani){
        super(ani.getNom(), ani.getVie(), "ANI");
        this.m_pointsAttaque = ani.getAttack();
        this.m_coutOs = ani.getCoutOs();
        this.m_coutSang = ani.getCoutSang();
        this.m_volatile = ani.getVolant();
        this.m_pouvoir = ani.getPouvoir();
    }
    public Animal(String nom, int pV, int pointsAttaque, int sang, int os, boolean volant) {
        super(nom,pV,"ANI");
        this.m_pointsAttaque = pointsAttaque;
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_volatile = volant;
    }

    public Animal(String nom, int pV, int pointsAttaque, int sang, int os, boolean volant, Pouvoir p) {
        super(nom,pV,"ANI");
        this.m_pointsAttaque = pointsAttaque;
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_volatile = volant;
        this.m_pouvoir = p;
    }

    public int getAttack(){
        return this.m_pointsAttaque;
    }
    public int getCoutSang(){
        return this.m_coutSang;
    }
    public int getCoutOs(){
        return this.m_coutOs;
    }
    public boolean getVolant(){
        return this.m_volatile;
    }

    public String setPouvoir(Pouvoir p) {
        this.m_pouvoir = p;
        return null;
    }

    @Override
    public boolean estAnimal() {
        return true;
    }

    public Pouvoir getPouvoir(){
        if (this.m_pouvoir == null){
            return null;
        }else{
            return this.m_pouvoir;
        }
    }

    public void setAttack(int nouvelleAttaque) {
        this.m_pointsAttaque = nouvelleAttaque;
    }
}
