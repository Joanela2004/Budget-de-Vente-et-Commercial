package com.madmeca.app;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.LinearLayout;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VenteAdapter adapter;
    private List<Vente> listeVentes;
    private Button btnAjouter;
    private EditText edtAnnee, edtMontant;
    private LinearLayout layoutSaisie;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liaison des composants
        edtAnnee = findViewById(R.id.edtAnnee);
        edtMontant = findViewById(R.id.edtMontant);
        btnAjouter = findViewById(R.id.btnAjouterLigne);
        layoutSaisie = findViewById(R.id.layoutSaisie);
        recyclerView = findViewById(R.id.recyclerViewVente);
    
    if (recyclerView == null) {
        Toast.makeText(this, "RecyclerView non trouvé !", Toast.LENGTH_LONG).show();
        return;
    }
        // Initialisation des données
        listeVentes = new ArrayList<>();
        listeVentes.add(new Vente(1, 1990, 4200.0));

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VenteAdapter(listeVentes);
        recyclerView.setAdapter(adapter);

        // Gestion du clic sur le bouton "Ajouter"
        btnAjouter.setOnClickListener(v -> {
            
            //si cachÉ, on l'affiche
            if(layoutSaisie.getVisibility() == View.GONE) {
                layoutSaisie.setVisibility(View.VISIBLE);
                btnAjouter.setText("Enregistrer la vente");
            }else{
               
            
            String strAnnee = edtAnnee.getText().toString().trim();
            String strMontant = edtMontant.getText().toString().trim();

          
            if (strAnnee.isEmpty() || strMontant.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int annee = Integer.parseInt(strAnnee);
                double montant = Double.parseDouble(strMontant);
                int nouveauT = listeVentes.size() + 1;

                listeVentes.add(new Vente(nouveauT, annee, montant));
                adapter.notifyItemInserted(listeVentes.size() - 1);
                
                recyclerView.scrollToPosition(listeVentes.size() - 1);

                // Vider les champs
                edtAnnee.setText("");
                edtMontant.setText("");
                layoutSaisie.setVisibility(View.GONE);
            btnAjouter.setText("Ajouter une vente");

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Format numérique invalide", Toast.LENGTH_SHORT).show();
            }
        }});
    }
}