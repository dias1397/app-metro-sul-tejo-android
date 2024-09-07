package com.diasjoao.metrosultejo.ui.routes;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;

import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.StationViewHolder> {

    private final List<String> stations;
    private final int lineColor;
    private final OnRoutesClickListener onRoutesClickListener;

    // Interface for handling station click events
    public interface OnRoutesClickListener {
        void onStationClick(String stationName);
    }

    // Constructor
    public RoutesAdapter(List<String> stations, int lineColor, OnRoutesClickListener onRoutesClickListener) {
        this.stations = stations;
        this.lineColor = lineColor;
        this.onRoutesClickListener = onRoutesClickListener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each station item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_recycler_view_row, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        // Get the station name for the current position
        String stationName = stations.get(position);

        // Set the station name and color the background based on the line color
        holder.stationNameTextView.setText(stationName);
        holder.stationImageView.setBackgroundColor(lineColor);

        // Handle the click event for each station
        holder.itemView.setOnClickListener(v -> onRoutesClickListener.onStationClick(stationName));
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    // ViewHolder class for each station item
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
