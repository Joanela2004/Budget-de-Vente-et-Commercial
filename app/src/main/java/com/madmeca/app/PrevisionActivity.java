package com.madmeca.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PrevisionActivity extends AppCompatActivity {

    private List<Vente> listeVentes = new ArrayList<>();
    private EditText edtPeriodeT, edtAnnee;
    private TextView txtResultatVi, txtResultatPourcentage, txtEquation, txtCorrelation;
    private TextView txtAnneePrevue, txtAnneeDerniere, txtEquationDetaillee;
    private CombinedChart combinedChart;
    private Button btnCalculer;
    private LinearLayout layoutVariation;

    private double a, b, r;
    private int anneeDepart;
    private double derniereValeurReelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevision);

        Intent intent = getIntent();
        a = intent.getDoubleExtra("valA", 0);
        b = intent.getDoubleExtra("valB", 0);
        r = intent.getDoubleExtra("valR", 0);
        anneeDepart = intent.getIntExtra("anneeDepart", 0);
        derniereValeurReelle = intent.getDoubleExtra("derniereValeurReelle", 0);
        
        ArrayList<Vente> tempListe = (ArrayList<Vente>) intent.getSerializableExtra("listeData");
        if (tempListe != null) {
            listeVentes = tempListe;
        }

        initViews();
        setupBottomNavigation();
    }

    private void initViews() {
        edtPeriodeT = findViewById(R.id.edtPeriodeT);
        edtAnnee = findViewById(R.id.edtAnnee);
        txtResultatVi = findViewById(R.id.txtResultatVi);
        txtResultatPourcentage = findViewById(R.id.txtResultatPourcentage);
        txtAnneeDerniere = findViewById(R.id.txtAnneeDerniere);
        txtAnneePrevue = findViewById(R.id.txtAnneePrevue);
        txtEquation = findViewById(R.id.txtEquation);
        txtCorrelation = findViewById(R.id.txtCorrelation);
        txtEquationDetaillee = findViewById(R.id.txtEquationDetaillee);
        btnCalculer = findViewById(R.id.btnCalculerPrevision);
        combinedChart = findViewById(R.id.combinedChart);
        layoutVariation = findViewById(R.id.layoutVariationBackground);

        if (!listeVentes.isEmpty()) {
            int derniereAnneeExistante = anneeDepart + (listeVentes.size() - 1);
            int prochaineAnnee = derniereAnneeExistante + 1;
            int periodeTSuivante = prochaineAnnee - anneeDepart + 1;

            if (txtAnneeDerniere != null) txtAnneeDerniere.setText("Vs " + derniereAnneeExistante);
            if (txtAnneePrevue != null) txtAnneePrevue.setText("Qté prévue " + prochaineAnnee);
            if (edtAnnee != null) edtAnnee.setText(String.valueOf(prochaineAnnee));
            if (edtPeriodeT != null) edtPeriodeT.setText(String.valueOf(periodeTSuivante));
        }

        if (txtEquation != null) txtEquation.setText(String.format("Vi = %.2f * T + %.2f", a, b));
        if (txtCorrelation != null) txtCorrelation.setText(String.format("%.4f", r));

        btnCalculer.setOnClickListener(v -> effectuerCalculPrevision());

        edtAnnee.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String anneeStr = s.toString();
                if (anneeStr.length() >= 4) {
                    try {
                        int anneeSaisie = Integer.parseInt(anneeStr);
                        int periodeT = anneeSaisie - anneeDepart + 1;
                        edtPeriodeT.setText(String.valueOf(periodeT));
                        if (txtAnneePrevue != null) txtAnneePrevue.setText("Qté prévue " + anneeSaisie);
                    } catch (NumberFormatException e) { }
                }
            }
        });
    }

    public void effectuerCalculPrevision() {
        String strT = edtPeriodeT.getText().toString().trim();
        if (strT.isEmpty()) return;

        try {
            int periodeT = Integer.parseInt(strT);
            double previsionV = (a * periodeT) + b;
            int anneeCible = anneeDepart + periodeT - 1;

            txtResultatVi.setText(String.format("%.0f", previsionV));
            if (txtEquationDetaillee != null) {
                txtEquationDetaillee.setText(String.format("V = %.1f * %d + %.1f", a, periodeT, b));
            }

            if (derniereValeurReelle > 0) {
                double variation = ((previsionV - derniereValeurReelle) / derniereValeurReelle) * 100;
                txtResultatPourcentage.setText(String.format("%+.1f%%", variation));
                GradientDrawable drawable = (GradientDrawable) layoutVariation.getBackground();
                drawable.setColor(variation < 0 ? Color.parseColor("#D32F2F") : Color.parseColor("#388E3C"));
            }

            construireGraphique(previsionV, anneeCible);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erreur de format", Toast.LENGTH_SHORT).show();
        }
    }

    private void construireGraphique(double previsionV, int anneeCible) {
        CombinedData data = new CombinedData();

        ArrayList<Entry> scatterEntries = new ArrayList<>();
        for (int i = 0; i < listeVentes.size(); i++) {
            scatterEntries.add(new Entry((float) (anneeDepart + i), (float) listeVentes.get(i).getVi()));
        }
        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterEntries, "Historique");
        scatterDataSet.setColor(Color.parseColor("#1A73E8"));
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(12f);
        scatterDataSet.setDrawValues(false);

        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int annee = anneeDepart; annee <= anneeCible; annee++) {
            int t = annee - anneeDepart + 1;
            lineEntries.add(new Entry((float) annee, (float) (a * t + b)));
        }
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Tendance");
        lineDataSet.setColor(Color.parseColor("#D32F2F"));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);

        ArrayList<Entry> prevEntry = new ArrayList<>();
        prevEntry.add(new Entry((float) anneeCible, (float) previsionV));
        ScatterDataSet prevDataSet = new ScatterDataSet(prevEntry, "Prévision");
        prevDataSet.setColor(Color.parseColor("#FFAB00"));
        prevDataSet.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        prevDataSet.setScatterShapeSize(18f);

        ScatterData scatterData = new ScatterData();
        scatterData.addDataSet(scatterDataSet);
        scatterData.addDataSet(prevDataSet);
        
        data.setData(scatterData);
        data.setData(new LineData(lineDataSet));

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(anneeDepart - 0.5f);
        xAxis.setAxisMaximum(anneeCible + 0.5f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        combinedChart.setData(data);
        combinedChart.getAxisRight().setEnabled(false);
        combinedChart.getDescription().setEnabled(false);
        combinedChart.setExtraOffsets(10, 10, 10, 10);
        combinedChart.animateX(500);
        combinedChart.invalidate();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_prevision);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_calcul || id == R.id.nav_rentabilite) {
                finish();
                return true;
            }
            return id == R.id.nav_prevision;
        });
    }
}