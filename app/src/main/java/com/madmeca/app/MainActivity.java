package com.madmeca.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VenteAdapter adapter;
    private List<Vente> listeVentes;
    
    private Button btnAjouter;
    private LinearLayout layoutTotal;
    private TextView txtAnneeDebut, txtAnneeFin, txtMoyenneT, txtMoyenneVi;
    private TextView txtSommeT, txtSommeVi, txtSommeEcartT, txtSommeEcartV, txtSommeT2, txtSommeV2, txtSommeTV;
    private TextView txtValA, txtValB, txtEquation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAjouter = findViewById(R.id.btnAjouterLigne);
        layoutTotal = findViewById(R.id.layoutTotal);
        recyclerView = findViewById(R.id.recyclerViewVente);
        
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

        listeVentes = new ArrayList<>();
         listeVentes.add(new Vente(1, 1990, 4200.0));

        adapter = new VenteAdapter(listeVentes, this); // "this" pour permettre les clics
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAjouter.setOnClickListener(v -> {
            showSaisieDialog(-1);
        });

        rafraichirInfos();
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
        String anneeStr = edtAnneeDialog.getText().toString().trim();
        String viStr = edtViDialog.getText().toString().trim();

        if (anneeStr.isEmpty() || viStr.isEmpty()) {
            if (anneeStr.isEmpty()) {
                edtAnneeDialog.setError("L'année est requise");
            }
            if (viStr.isEmpty()) {
                edtViDialog.setError("Le chiffre d'affaires est requis");
            }
            return;
        }

        int annee = Integer.parseInt(anneeStr);
        double vi = Double.parseDouble(viStr);

        if (position != -1) {
            // Modification
            listeVentes.get(position).setVi(vi);
        } else {
            // Ajout
            int t = listeVentes.size() + 1;
            listeVentes.add(new Vente(t, annee, vi));
        }
        adapter.notifyDataSetChanged();
        rafraichirInfos();
        dialog.dismiss();
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
            txtSommeEcartT.setText(String.format("%.2f", sommeEcartT));
            txtSommeEcartV.setText(String.format("%.2f", sommeEcartV));
            txtSommeT2.setText(String.format("%.2f", sommeEcartT2));
            txtSommeV2.setText(String.format("%.2f", sommeEcartV2));
            txtSommeTV.setText(String.format("%.2f", sommeEcartTV));

            if (sommeEcartT2 != 0) {
                double a = sommeEcartTV / sommeEcartT2;
                double b = moyVi - (a * moyT);
                txtValA.setText(String.format("%.4f", a));
                txtValB.setText(String.format("%.2f", b));
                txtEquation.setText(String.format("V = %.4ft + %.2f", a, b));
            }

            txtMoyenneT.setText(String.format("%.2f", moyT));
            txtMoyenneVi.setText(String.format("%.2f", moyVi));
            txtAnneeDebut.setText(String.valueOf(listeVentes.get(0).getAnnee()));
            txtAnneeFin.setText(String.valueOf(listeVentes.get(listeVentes.size() - 1).getAnnee()));

            adapter.updateMoyennes(moyT, moyVi);
        } else {
            layoutTotal.setVisibility(View.GONE);
        }
    }

       public void supprimerVente(int position) {
        listeVentes.remove(position);
              for (int i = 0; i < listeVentes.size(); i++) {
            listeVentes.get(i).setT(i + 1);
        }
        adapter.notifyDataSetChanged();
        rafraichirInfos();
    }
}