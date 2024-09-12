package com.diasjoao.metrosultejo.ui.tariffs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.model.Tariff;

import java.util.List;

public class TariffRecyclerViewAdapter extends RecyclerView.Adapter<TariffRecyclerViewAdapter.TariffViewHolder> {

    private List<Tariff> tariffList;
    private Context context;

    public TariffRecyclerViewAdapter(List<Tariff> tariffList, Context context) {
        this.tariffList = tariffList;
        this.context = context;
    }

    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tariff_recycler_view_item, parent, false);
        return new TariffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
        Tariff tariff = tariffList.get(position);
        holder.ticketName.setText(tariff.getName());
        holder.ticketPrice.setText(tariff.getPrice());

        holder.itemView.setOnClickListener(v -> {
            
        });
    }

    @Override
    public int getItemCount() {
        return tariffList.size();
    }


    public class TariffViewHolder extends RecyclerView.ViewHolder {
        private TextView ticketName, ticketPrice;

        public TariffViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.ticket_name);
            ticketPrice = itemView.findViewById(R.id.ticket_price);
        }
    }
}
