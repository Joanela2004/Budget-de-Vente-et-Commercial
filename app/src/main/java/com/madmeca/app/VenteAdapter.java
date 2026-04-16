package com.madmeca.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VenteAdapter extends RecyclerView.Adapter<VenteAdapter.VenteViewHolder> {

    private List<Vente> listeVentes;

    public VenteAdapter(List<Vente> listeVentes) {
        this.listeVentes = listeVentes;
    }

    @NonNull
    @Override
    public VenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vente, parent, false);
        return new VenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenteViewHolder holder, int position) {
        Vente vente = listeVentes.get(position);

        holder.txtT.setText(String.valueOf(vente.getT()));
        holder.edtAnneeItem.setText(String.valueOf(vente.getAnnee()));
        holder.edtViItem.setText(String.valueOf(vente.getVi()));

        holder.btnModifier.setOnClickListener(v -> {
            if (v.getContext() instanceof MainActivity) {
                ((MainActivity) v.getContext()).preparerModification(holder.getAdapterPosition());
            }
        });

        holder.btnSupprimer.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listeVentes.remove(pos);
                if (v.getContext() instanceof MainActivity) {
                    ((MainActivity) v.getContext()).recalculerChronologie();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return listeVentes.size(); }

    public static class VenteViewHolder extends RecyclerView.ViewHolder {
        TextView txtT, edtAnneeItem, edtViItem;
        ImageButton btnSupprimer, btnModifier;

        public VenteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtT = itemView.findViewById(R.id.txtT);
            edtAnneeItem = itemView.findViewById(R.id.edtAnneeItem);
            edtViItem = itemView.findViewById(R.id.edtViItem);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
            btnModifier = itemView.findViewById(R.id.btnModifier);
        }
    }
}