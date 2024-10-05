package com.diasjoao.metrosultejo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleRowAdapter extends RecyclerView.Adapter<ScheduleRowAdapter.ScheduleRowViewHolder> {

    private List<LocalDateTime> times;

    public ScheduleRowAdapter(List<LocalDateTime> times) {
        this.times = times;
    }

    public static class ScheduleRowViewHolder extends RecyclerView.ViewHolder {
        TextView time;

        public ScheduleRowViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
        }
    }

    @NonNull
    @Override
    public ScheduleRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_row_time, parent, false);
        return new ScheduleRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleRowViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.time.setText(times.get(position).format(formatter));

        int totalWidth = (int) holder.time.getPaint().measureText("HH:mm");
        holder.time.setWidth(totalWidth);
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
