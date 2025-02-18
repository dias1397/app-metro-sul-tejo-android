package com.diasjoao.metrosultejo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;

import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.StationViewHolder> {

    private final List<String> stations;
    private final int lineColor;
    private final OnRoutesClickListener onRoutesClickListener;

    public LineAdapter(List<String> stations, int lineColor, OnRoutesClickListener onRoutesClickListener) {
        this.stations = stations;
        this.lineColor = lineColor;
        this.onRoutesClickListener = onRoutesClickListener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        String stationName = stations.get(position);

        holder.stationNameTextView.setText(stationName);
        holder.stationImageView.setBackgroundColor(lineColor);

        holder.itemView.setOnClickListener(v -> onRoutesClickListener.onStationClick(stationName));
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    public interface OnRoutesClickListener {
        void onStationClick(String stationName);
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {
        ImageView stationImageView;
        TextView stationNameTextView;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            stationImageView = itemView.findViewById(R.id.station_image_view);
            stationNameTextView = itemView.findViewById(R.id.station_name_text_view);
        }
    }
}
