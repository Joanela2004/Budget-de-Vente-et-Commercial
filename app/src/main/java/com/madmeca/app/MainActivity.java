package com.madmeca.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VenteAdapter adapter;
    private List<Vente> listeVentes;
    
    private Button btnAjouter, btnEnregistrer, btnAnnuler;
    private EditText edtAnnee, edtVi;
    private LinearLayout layoutSaisie;
    private TextView txtAnneeDebut, txtAnneeFin;
    
    private int positionModification = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation UI
        edtAnnee = findViewById(R.id.edtAnnee);
        edtVi = findViewById(R.id.edtVi);
        btnAjouter = findViewById(R.id.btnAjouterLigne);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        btnAnnuler = findViewById(R.id.btnAnnuler);
        layoutSaisie = findViewById(R.id.layoutSaisie);
        recyclerView = findViewById(R.id.recyclerViewVente);
        txtAnneeDebut = findViewById(R.id.txtAnneeDebut);
        txtAnneeFin = findViewById(R.id.txtAnneeFin);

        // L'année est calculée, pas saisie
        edtAnnee.setFocusable(false);

        listeVentes = new ArrayList<>();
        listeVentes.add(new Vente(1, 1990, 4200.0));

        adapter = new VenteAdapter(listeVentes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Bouton Ajouter
        btnAjouter.setOnClickListener(v -> {
            positionModification = -1;
            layoutSaisie.setVisibility(View.VISIBLE);
            btnAjouter.setVisibility(View.GONE);
            
            if (!listeVentes.isEmpty()) {
                int derniereAnnee = listeVentes.get(listeVentes.size() - 1).getAnnee();
                edtAnnee.setText(String.valueOf(derniereAnnee + 1));
              edtAnnee.setFocusable(false);
                edtAnnee.setEnabled(false);
                edtVi.requestFocus();
            } else {

                edtAnnee.setText("");
                edtAnnee.setEnabled(true);
                edtAnnee.setFocusable(true);
        edtAnnee.setFocusableInTouchMode(true);
                edtAnnee.requestFocus();
               
            }
        });

        btnEnregistrer.setOnClickListener(v -> sauvegarderVente());
        btnAnnuler.setOnClickListener(v -> masquerSaisie());

        rafraichirInfos();
    }

    public void preparerModification(int position) {
        positionModification = position;
        Vente v = listeVentes.get(position);
        edtAnnee.setText(String.valueOf(v.getAnnee()));
        edtVi.setText(String.valueOf(v.getVi()));
        edtAnnee.setEnabled(false);
        layoutSaisie.setVisibility(View.VISIBLE);
        btnAjouter.setVisibility(View.GONE);
        edtVi.requestFocus();
    }

    private void sauvegarderVente() {
        String viStr = edtVi.getText().toString().trim();
        if (viStr.isEmpty()) {
            Toast.makeText(this, "Montant requis", Toast.LENGTH_SHORT).show();
            return;
        }

        double vi = Double.parseDouble(viStr);
        int annee = Integer.parseInt(edtAnnee.getText().toString());

        if (positionModification == -1) {
            listeVentes.add(new Vente(listeVentes.size() + 1, annee, vi));
            adapter.notifyItemInserted(listeVentes.size() - 1);
        } else {
            listeVentes.get(positionModification).setVi(vi);
            adapter.notifyItemChanged(positionModification);
        }
        rafraichirInfos();
        masquerSaisie();
    }

    public void recalculerChronologie() {
        if (listeVentes.isEmpty()) {
            txtAnneeDebut.setText("----");
            txtAnneeFin.setText("----");
        } else {
            int anneeDepart = listeVentes.get(0).getAnnee();
            for (int i = 0; i < listeVentes.size(); i++) {
                Vente v = listeVentes.get(i);
                v.setT(i + 1);
                v.setAnnee(anneeDepart + i); 
            }
        }
        adapter.notifyDataSetChanged();
        rafraichirInfos();
    }

    private void masquerSaisie() {
        layoutSaisie.setVisibility(View.GONE);
        btnAjouter.setVisibility(View.VISIBLE);
        edtVi.setText("");
        positionModification = -1;
    }

    private void rafraichirInfos() {
        if (!listeVentes.isEmpty()) {
            txtAnneeDebut.setText(String.valueOf(listeVentes.get(0).getAnnee()));
            txtAnneeFin.setText(String.valueOf(listeVentes.get(listeVentes.size() - 1).getAnnee()));
        }
    }
}