package com.madmeca.app;
import java.io.Serializable;

public class Vente implements Serializable {
    private int t;
    private int annee;
    private double vi;

    public Vente(int t, int annee, double vi) {
        this.t = t;
        this.annee = annee;
        this.vi = vi;
    }

    public int getT() { return t; }
    public int getAnnee() { return annee; }
    public double getVi() { return vi; }

    public void setAnnee(int annee) { this.annee = annee; }
    public void setVi(double vi) { this.vi = vi; }
    public void setT(int t) { this.t = t; }
}