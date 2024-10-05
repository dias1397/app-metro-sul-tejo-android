package com.diasjoao.metrosultejo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.model.Tariff;
import com.diasjoao.metrosultejo.ui.activity.TariffActivity;

import java.util.List;

public class TariffAdapter extends RecyclerView.Adapter<TariffAdapter.TariffViewHolder> {

    private final List<Tariff> tariffList;
    private final Context context;

    public TariffAdapter(List<Tariff> tariffList, Context context) {
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

        holder.ticketNameTextView.setText(tariff.getName());
        holder.ticketPriceTextView.setText(String.format("Preço: %s", tariff.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TariffActivity.class);
            intent.putExtra("TARIFF_NAME", tariff.getName());
            intent.putExtra("TARIFF_ID", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tariffList.size();
    }


    public static class TariffViewHolder extends RecyclerView.ViewHolder {
        TextView ticketNameTextView;
        TextView ticketPriceTextView;

        public TariffViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketNameTextView = itemView.findViewById(R.id.ticket_name);
            ticketPriceTextView = itemView.findViewById(R.id.ticket_price);
        }
    }
}
