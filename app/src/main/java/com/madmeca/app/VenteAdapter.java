package com.madmeca.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VenteAdapter extends RecyclerView.Adapter<VenteAdapter.VenteViewHolder> {

    private final List<Vente> listeVentes;

    public VenteAdapter(List<Vente> listeVentes) {
        this.listeVentes = listeVentes;
    }

    @NonNull
    @Override
    public VenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vente, parent, false);
        return new VenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenteViewHolder holder, int position) {
        Vente vente = listeVentes.get(position);

        holder.txtT.setText(String.valueOf(vente.getT()));
        holder.txtAnnee.setText(String.valueOf(vente.getAnnee()));
        holder.txtMontant.setText(String.valueOf(vente.getMontant()));
    }

    @Override
    public int getItemCount() {
        return listeVentes.size();
    }

    // ViewHolder
    public static class VenteViewHolder extends RecyclerView.ViewHolder {
        TextView txtT, txtAnnee, txtMontant;

        public VenteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtT = itemView.findViewById(R.id.txtT);
            txtAnnee = itemView.findViewById(R.id.txtAnnee);
            txtMontant = itemView.findViewById(R.id.txtMontant);
            
            if (txtT == null || txtAnnee == null || txtMontant == null) {
                throw new RuntimeException("Un des TextViews n'a pas été trouvé dans item_vente.xml. Vérifiez les IDs !");
            }
        }
    }
}