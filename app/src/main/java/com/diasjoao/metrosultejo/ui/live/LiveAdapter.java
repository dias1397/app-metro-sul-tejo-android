package com.diasjoao.metrosultejo.ui.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.adapters.TimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveViewHolder> {

    private Context context;
    private List<LocalDateTime> times;

    public LiveAdapter(Context context, List<LocalDateTime> times) {
        this.context = context;
        this.times = times;
    }

    public static class LiveViewHolder extends RecyclerView.ViewHolder {
        TextView textViewArrivalTime;
        TextView textViewTimeLeft;

        public LiveViewHolder(View itemView) {
            super(itemView);
            textViewArrivalTime = itemView.findViewById(R.id.textViewArrivalTime);
            textViewTimeLeft = itemView.findViewById(R.id.textViewTimeLeft);
        }
    }

    @NonNull
    @Override
    public LiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_recycler_view_row, parent, false);
        return new LiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveViewHolder holder, int position) {
        LocalDateTime currentTime = times.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.textViewArrivalTime.setText(currentTime.format(formatter));

        long timeLeft = Duration.between(LocalDateTime.now(), currentTime).toMinutes();
        if (timeLeft < 0) {
            holder.textViewTimeLeft.setText(String.format(Locale.getDefault(), "%02d'", Math.abs(timeLeft)));
            holder.textViewTimeLeft.setBackgroundColor(context.getColor(R.color.FireBrick));
            holder.textViewTimeLeft.setVisibility(View.VISIBLE);
        } else if (timeLeft < 60) {
            holder.textViewTimeLeft.setText(String.format(Locale.getDefault(), "%02d'", timeLeft));
            holder.textViewTimeLeft.setBackgroundColor(context.getColor(R.color.ForestGreen));
            holder.textViewTimeLeft.setVisibility(View.VISIBLE);
        } else {
            holder.textViewTimeLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
