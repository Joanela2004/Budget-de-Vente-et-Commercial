package com.madmeca.app;
public class Vente {
    private int t;
    private int annee;
    private double montant;

    public Vente(int t, int annee, double montant){
        this.t=t;
        this.annee=annee;
        this.montant=montant;
    }
    public int getAnnee() {
        return annee;
    }

    public double getMontant() {
        return montant;
    }

    public int getT() {
        return t;
    }
}