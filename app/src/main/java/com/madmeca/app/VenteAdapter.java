package com.madmeca.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VenteAdapter extends RecyclerView.Adapter<VenteAdapter.VenteViewHolder> {

    private List<Vente> listeVentes;
    private MainActivity activity; // On ajoute l'accès à l'activité
    private double moyT = 0, moyVi = 0;

    // Constructeur mis à jour pour accepter l'activité
    public VenteAdapter(List<Vente> listeVentes, MainActivity activity) {
        this.listeVentes = listeVentes;
        this.activity = activity;
    }

    public void updateMoyennes(double moyT, double moyVi) {
        this.moyT = moyT;
        this.moyVi = moyVi;
        notifyDataSetChanged();
    }

    @Override
    public VenteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vente, parent, false);
        return new VenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VenteViewHolder holder, int position) {
        Vente v = listeVentes.get(position);
        holder.txtT.setText(String.valueOf(v.getT()));
        holder.txtVi.setText(String.valueOf(v.getVi()));
        
        holder.txtEcartT.setText(String.format("%.2f", v.getT() - moyT));
        holder.txtEcartV.setText(String.format("%.2f", v.getVi() - moyVi));
        
        double et = v.getT() - moyT;
        double ev = v.getVi() - moyVi;
        holder.txtT2.setText(String.format("%.2f", et * et));
        holder.txtV2.setText(String.format("%.2f", ev * ev));
        holder.txtTV.setText(String.format("%.2f", et * ev));

        // CLIC MODIFIER
        holder.btnModifier.setOnClickListener(v1 -> {
            activity.showSaisieDialog(position);
        });

        // CLIC SUPPRIMER
        holder.btnSupprimer.setOnClickListener(v1 -> {
            activity.supprimerVente(position);
        });
    }

    @Override
    public int getItemCount() {
        return listeVentes.size();
    }

    public static class VenteViewHolder extends RecyclerView.ViewHolder {
        TextView txtT, txtVi, txtEcartT, txtEcartV, txtT2, txtV2, txtTV;
        ImageButton btnModifier, btnSupprimer;

        public VenteViewHolder(View itemView) {
            super(itemView);
            txtT = itemView.findViewById(R.id.txtT);
            txtVi = itemView.findViewById(R.id.txtVi);
            txtEcartT = itemView.findViewById(R.id.txtEcartT);
            txtEcartV = itemView.findViewById(R.id.txtEcartV);
            txtT2 = itemView.findViewById(R.id.txtT2);
            txtV2 = itemView.findViewById(R.id.txtV2);
            txtTV = itemView.findViewById(R.id.txtTV);
            btnModifier = itemView.findViewById(R.id.btnModifier);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}