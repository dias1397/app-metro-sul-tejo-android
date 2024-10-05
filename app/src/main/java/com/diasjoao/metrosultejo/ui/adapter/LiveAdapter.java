package com.diasjoao.metrosultejo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.google.android.material.card.MaterialCardView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveViewHolder> {

    private final Context context;
    private final List<LocalDateTime> times;
    private final String route;

    public LiveAdapter(Context context, List<LocalDateTime> times, String route) {
        this.context = context;
        this.times = times;
        this.route = route;
    }

    @NonNull
    @Override
    public LiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_arrival, parent, false);
        return new LiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveViewHolder holder, int position) {
        LocalDateTime currentTime = times.get(position);
        long timeLeft = Duration.between(LocalDateTime.now(), currentTime).toMinutes();

        holder.textViewHeader.setVisibility(View.GONE);
        holder.textViewArrivalTime.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        holder.textViewDestination.setText(route);

        if (timeLeft < 0) {
            setTimeLeftUI(holder, "+%02d min", Math.abs(timeLeft), R.color.FireBrick);
        } else if (timeLeft < 60) {
            if (position > 0 && Duration.between(LocalDateTime.now(), times.get(position - 1)).toMinutes() < 0) {
                holder.textViewHeader.setVisibility(View.VISIBLE);
            }

            setTimeLeftUI(holder, "%02d min", timeLeft, R.color.ForestGreen);
        } else {
            holder.cardViewTimeLeft.setVisibility(View.GONE);
        }
    }

    private void setTimeLeftUI(LiveViewHolder holder, String timeFormat, long timeLeft, int colorRes) {
        holder.cardViewTimeLeft.setVisibility(View.VISIBLE);
        holder.textViewTimeLeft.setText(String.format(Locale.getDefault(), timeFormat, timeLeft));
        holder.textViewTimeLeft.setBackgroundColor(context.getColor(colorRes));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public static class LiveViewHolder extends RecyclerView.ViewHolder {
        TextView textViewArrivalTime;
        TextView textViewTimeLeft;
        TextView textViewHeader;
        TextView textViewDestination;
        MaterialCardView cardViewTimeLeft;

        public LiveViewHolder(View itemView) {
            super(itemView);
            textViewArrivalTime = itemView.findViewById(R.id.textViewArrivalTime);
            textViewTimeLeft = itemView.findViewById(R.id.textViewTimeLeft);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            cardViewTimeLeft = itemView.findViewById(R.id.cardViewTimeLeft);
        }
    }
}
