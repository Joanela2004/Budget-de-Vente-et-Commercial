package com.madmeca.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import retrofit2.Call;
import android.util.Log;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
public class MainActivity extends AppCompatActivity {

    private VenteApi api;

    private RecyclerView recyclerView;
    private VenteAdapter adapter;
    private List<Vente> listeVentes;

    private Button btnAjouter;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout layoutTotal;
    private TextView txtAnneeDebut, txtAnneeFin, txtMoyenneT, txtMoyenneVi;
    private TextView txtSommeT, txtSommeVi, txtSommeEcartT, txtSommeEcartV, txtSommeT2, txtSommeV2, txtSommeTV;
    private TextView txtValA, txtValB, txtEquation;

    private double aVal = 0;
    private double bVal = 0;
    private double rVal = 0;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = RetrofitClient.getClient().create(VenteApi.class);
        
        initViews();
        setupBottomNavigation();
        listeVentes = new ArrayList<>();
        
        adapter = new VenteAdapter(listeVentes, this); 
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
 layoutManager.setStackFromEnd(true);
 recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        chargerVentes();

        btnAjouter.setOnClickListener(v -> showSaisieDialog(-1));
    }

    private void chargerVentes() {
        api.getVentes().enqueue(new Callback<List<Vente>>() {
            @Override
            public void onResponse(Call<List<Vente>> call, Response<List<Vente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listeVentes.clear();
                    listeVentes.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    rafraichirInfos();
                }
            }

            @Override
            public void onFailure(Call<List<Vente>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur chargement : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
        protected void onResume() {
            super.onResume();
            
            if (bottomNavigationView != null) {
                bottomNavigationView.post(() -> {
                    bottomNavigationView.setSelectedItemId(R.id.nav_calcul);
                });
            }
        }
    public void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAjouter = findViewById(R.id.btnAjouter);
        layoutTotal = findViewById(R.id.layoutTotal);
        txtAnneeDebut = findViewById(R.id.txtAnneeDebut);
        txtAnneeFin = findViewById(R.id.txtAnneeFin);
        txtMoyenneT = findViewById(R.id.txtMoyenneT);
        txtMoyenneVi = findViewById(R.id.txtMoyenneVi);
        txtSommeT = findViewById(R.id.txtSommeT);
        txtSommeVi = findViewById(R.id.txtSommeVi);
        txtSommeEcartT = findViewById(R.id.txtSommeEcartT);
        txtSommeEcartV = findViewById(R.id.txtSommeEcartV);
        txtSommeT2 = findViewById(R.id.txtSommeT2);
        txtSommeV2 = findViewById(R.id.txtSommeV2);
        txtSommeTV = findViewById(R.id.txtSommeTV);
        txtValA = findViewById(R.id.txtValA);
        txtValB = findViewById(R.id.txtValB);
        txtEquation = findViewById(R.id.txtEquation);
    }

   public void showSaisieDialog(int position){
    View view = getLayoutInflater().inflate(R.layout.layout_dialog_saisie, null);
    final TextView txtDialogTitle = view.findViewById(R.id.dialog_title);
    final EditText edtAnneeDialog = view.findViewById(R.id.dialog_edtAnnee);
    final EditText edtViDialog = view.findViewById(R.id.dialog_edtVi);
    final Button btnSave = view.findViewById(R.id.btn_dialog_save);
    final Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);

    if (position != -1) {
        // Modification
        txtDialogTitle.setText("Modifier une donnée");
        edtAnneeDialog.setText(String.valueOf(listeVentes.get(position).getAnnee()));
        edtViDialog.setText(String.valueOf(listeVentes.get(position).getVi()));
        edtAnneeDialog.setEnabled(false);
        edtViDialog.requestFocus();
    } else {
        // Ajout
        txtDialogTitle.setText("Ajouter une donnée");
        if(!listeVentes.isEmpty()){
            int derniereAnnee=listeVentes.get(listeVentes.size()-1).getAnnee();
            edtAnneeDialog.setText(String.valueOf(derniereAnnee + 1));
            edtAnneeDialog.setEnabled(false);
            edtViDialog.requestFocus();
        }else{
            edtAnneeDialog.setEnabled(true);
            edtAnneeDialog.requestFocus();
        }
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setView(view);
    AlertDialog dialog = builder.create();
    if(dialog.getWindow() != null){
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }   
  btnSave.setOnClickListener(v -> {
    int annee = Integer.parseInt(edtAnneeDialog.getText().toString());
    double vi = Double.parseDouble(edtViDialog.getText().toString());
    Vente vData = new Vente(listeVentes.size() + 1, annee, vi);

    if (position != -1) {

        api.updateVente(vData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    chargerVentes();
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Mis à jour !", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("DEBUG_API", "Erreur update: " + response.code());
                    Toast.makeText(MainActivity.this, "Erreur serveur update", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DEBUG_API", "Erreur connexion : " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    } else {
        api.addVente(vData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    chargerVentes();
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Ajouté avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("DEBUG_API", "Erreur ajout code: " + response.code());
                    Toast.makeText(MainActivity.this, "Erreur serveur ajout", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DEBUG_API", "Erreur connexion : " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
});
    btnCancel.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
      }


    public void rafraichirInfos() {
        if (!listeVentes.isEmpty()) {
            layoutTotal.setVisibility(View.VISIBLE);
            int n = listeVentes.size();
            double sommeT = 0, sommeVi = 0;

            for (Vente v : listeVentes) {
                sommeT += v.getT();
                sommeVi += v.getVi();
            }

            double moyT = sommeT / n;
            double moyVi = sommeVi / n;

            double sommeEcartT2 = 0, sommeEcartV2 = 0, sommeEcartTV = 0, sommeEcartT = 0, sommeEcartV = 0;
            for (Vente v : listeVentes) {
                double ecartT = v.getT() - moyT;
                double ecartVi = v.getVi() - moyVi;
                sommeEcartT += ecartT;
                sommeEcartV += ecartVi;
                sommeEcartV2 += ecartVi * ecartVi;
                sommeEcartT2 += ecartT * ecartT;
                sommeEcartTV += ecartT * ecartVi;
            }

            txtSommeT.setText(String.format("%.0f", sommeT));
            txtSommeVi.setText(String.format("%.0f", sommeVi));
            txtSommeEcartT.setText(String.format("%.0f", sommeEcartT));
            txtSommeEcartV.setText(String.format("%.0f", sommeEcartV));
            txtSommeT2.setText(String.format("%.0f", sommeEcartT2));
            txtSommeV2.setText(String.format("%.0f", sommeEcartV2));
            txtSommeTV.setText(String.format("%.0f", sommeEcartTV));

            if (sommeEcartT2 != 0) {
                this.aVal = sommeEcartTV / sommeEcartT2;
                this.bVal = moyVi - (aVal * moyT);
                txtValA.setText(String.format("%.0f", aVal));
                txtValB.setText(String.format("%.0f", bVal));
                txtEquation.setText(String.format("V = %.0ft + %.0f", aVal, bVal));
            }

            if (sommeEcartT2!=0 && sommeEcartV2!=0){
                double r = sommeEcartTV/Math.sqrt(sommeEcartT2*sommeEcartV2);
                this.rVal=r;
            }
            txtMoyenneT.setText(String.format("%.0f", moyT));
            txtMoyenneVi.setText(String.format("%.0f", moyVi));
            txtAnneeDebut.setText(String.valueOf(listeVentes.get(0).getAnnee()));
            txtAnneeFin.setText(String.valueOf(listeVentes.get(listeVentes.size() - 1).getAnnee()));

            adapter.updateMoyennes(moyT, moyVi);
        } else {
            layoutTotal.setVisibility(View.GONE);
        }
    }

     public void supprimerVente(int position) {
        int annee = listeVentes.get(position).getAnnee();
        api.deleteVente(annee).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                chargerVentes(); 
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void allerAPrevision() {
    if (listeVentes.isEmpty()) {
        Toast.makeText(this, "Saisissez des données d'abord", Toast.LENGTH_SHORT).show();
        return;
    }
    
    Intent intent = new Intent(this, PrevisionActivity.class);
    intent.putExtra("valA", aVal); 
    intent.putExtra("valB", bVal);
    intent.putExtra("valR", rVal);
    intent.putExtra("anneeDepart", listeVentes.get(0).getAnnee());
    intent.putExtra("derniereValeurReelle", listeVentes.get(listeVentes.size()-1).getVi());
    intent.putExtra("listeData", (ArrayList<Vente>) listeVentes);
    startActivity(intent);
}
private void allerARentabilite() {
    if (listeVentes.isEmpty()) {
        Toast.makeText(this, "Saisissez des données d'abord", Toast.LENGTH_SHORT).show();
        return;
    }
    
    int tSuivant = listeVentes.size() + 1;
    double qtePrevue = (aVal * tSuivant) + bVal;

    Intent intent = new Intent(this, RentabiliteActivity.class);
    intent.putExtra("qtePrevue", qtePrevue);
    intent.putExtra("valR", rVal);
    startActivity(intent);
}
private void setupBottomNavigation() {
    bottomNavigationView = findViewById(R.id.bottomNavigation);
   bottomNavigationView.setSelectedItemId(R.id.nav_calcul);

    bottomNavigationView.setOnItemSelectedListener(item -> {
        int id = item.getItemId();
        if (id == R.id.nav_prevision) {
            allerAPrevision();
            return true;
        } else if (id == R.id.nav_rentabilite) {
             allerARentabilite();
            return true;
        } else if (id == R.id.nav_calcul) {
           return true;
        }
        return false;
    });
}
}