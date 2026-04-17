package com.madmeca.app;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RentabiliteActivity extends AppCompatActivity {
    //param
    private EditText edtPrixUnitaire, edtCoutUnitaire, edtChargesFixes, edtQtePrevue;

    //indicateur
    private TextView txtCaPrevu, txtCoutVariableTotal, txtMargeBrute, txtChargesFixes, txtResultat, txtResultatNet;

    //seuil
    private TextView badgeRentabilite;
    private TextView txtMargeUnitaire, txtSeuilUnite, txtValQtePrevue, txtDecisionSeuil, txtDecisionMarge, txtDecisionCorrelation;
    private ProgressBar progressRentabilite;

    //variable de calcul;
    private double qtePrevueDeLaPrevision =0;
    private double rValeur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentabilite);
        qtePrevueDeLaPrevision = getIntent().getDoubleExtra("qtePrevue", 0);
        rValeur = getIntent().getDoubleExtra("valR", 0);
    initViews();
    setupListeners();
    setupBottomNavigation();
    calculerTout();
    }
    private void initViews(){
        edtPrixUnitaire = findViewById(R.id.edtPrixUnitaire);
        edtCoutUnitaire = findViewById(R.id.edtCoutUnitaire);
        edtChargesFixes = findViewById(R.id.edtChargesFixes);
        edtQtePrevue = findViewById(R.id.edtQtePrevue);

        // initialisation de qtt prevus
        edtQtePrevue.setText(String.format("%.0f", qtePrevueDeLaPrevision));

        txtCaPrevu = findViewById(R.id.txtCaPrevu);
        txtCoutVariableTotal = findViewById(R.id.txtCoutVariableTotal);
        txtMargeBrute = findViewById(R.id.txtMargeBrute);
        txtChargesFixes = findViewById(R.id.txtChargesFixes);
        txtResultatNet = findViewById(R.id.txtResultatNet);

        txtMargeUnitaire = findViewById(R.id.txtMargeUnitaire);
        txtSeuilUnite = findViewById(R.id.txtSeuilUnite);
        txtValQtePrevue = findViewById(R.id.txtValQtePrevue);
        progressRentabilite = findViewById(R.id.progressRentabilite);
        badgeRentabilite = findViewById(R.id.badgeRentabilite);
        txtDecisionSeuil = findViewById(R.id.txtDecisionSeuil);
        txtDecisionMarge = findViewById(R.id.txtDecisionMarge);
        txtDecisionCorrelation = findViewById(R.id.txtDecisionCorrelation);
    }

    private void setupListeners(){
        TextWatcher watch = new TextWatcher(){
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                calculerTout();
        }};
        edtPrixUnitaire.addTextChangedListener(watch);
        edtCoutUnitaire.addTextChangedListener(watch);
        edtChargesFixes.addTextChangedListener(watch);
    }
    private void calculerTout(){
      
            double prixU = parse(edtPrixUnitaire);
            double coutU = parse(edtCoutUnitaire);
            double chargeF = parse(edtChargesFixes);
            double qtt= qtePrevueDeLaPrevision;

            //calcul de base
            double margeU=prixU - coutU;
            double caPrevu = prixU*qtt;
            double cvTotal=coutU*qtt;
            double mcvTotal=margeU*qtt;
            double resultatNet= mcvTotal - chargeF;

            //seui; de rentabilite
            double seuil=(margeU>0)?chargeF/margeU:0;

            txtCaPrevu.setText(String.format("%.0f Ar",caPrevu));
            txtCoutVariableTotal.setText(String.format("%.0f Ar", cvTotal));
            txtMargeBrute.setText(String.format("%.0f Ar", mcvTotal));
            txtChargesFixes.setText(String.format("%.0f Ar", chargeF));

            txtResultatNet.setText(String.format("%.0f Ar", resultatNet));
            txtResultatNet.setTextColor(resultatNet >= 0 ? Color.parseColor("#1B5E20") : Color.RED);

            txtMargeUnitaire.setText(String.format("%.0f Ar/unité", margeU));
            txtSeuilUnite.setText(String.format("%.0f unités", seuil));
            txtValQtePrevue.setText(String.format("%.0f unités", qtt));

            //barre de progress
            if(seuil>0){
                int ratio = (int)((qtt / seuil) * 100);
            progressRentabilite.setProgress(Math.min(ratio, 100));
            }

            updateDecision(qtt, seuil, resultatNet);
        }
    

    private void updateDecision(double qtt,double seuil, double net){
        if(qtt >=seuil && qtt>0){
            badgeRentabilite.setText("RENTABLE");
           badgeRentabilite.setBackgroundResource(R.drawable.bg_resultat_vert); // Vert
            txtDecisionSeuil.setText(String.format("V (%.0f) > seuil (%.0f) : bénéficiaire", qtt, seuil));
        }else{
            badgeRentabilite.setText("DEFICITAIRE");
            badgeRentabilite.setTextColor(Color.RED);
            txtDecisionSeuil.setText(String.format("V (%.0f) < seuil (%.0f) : perte", qtt, seuil));
        }
        txtDecisionCorrelation.setText(String.format("Corrélation r = %.2f : %s", rValeur, rValeur > 0.8 ? "fiable" : "peu fiable"));
        txtDecisionMarge.setText(String.format("Marge nette : %+.0f Ar", net));
    }
    private double parse(EditText edt)
    {
        String val = edt.getText().toString();
        return val.isEmpty()? 0:Double.parseDouble(val);
    }
    private void setupBottomNavigation() {

        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setSelectedItemId(R.id.nav_rentabilite);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_calcul || item.getItemId() == R.id.nav_prevision) {
                finish();
                return true;
            }
            return true;
        });
    }

}