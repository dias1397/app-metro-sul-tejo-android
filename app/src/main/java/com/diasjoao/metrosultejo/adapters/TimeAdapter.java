package com.diasjoao.metrosultejo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {

    private List<String> times;

    public TimeAdapter(List<String> times) {
        this.times = times;
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView time;

        public TimeViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
        }
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timetable_item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.time.setText(times.get(position));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
}
