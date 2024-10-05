package com.diasjoao.metrosultejo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.model.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.diasjoao.metrosultejo.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.TimetableViewHolder> {

    private List<Station> stationList;
    private Set<RecyclerView> timesRecyclerViews = new HashSet<>();
    private boolean isSyncing = false;

    public ScheduleAdapter(List<Station> stationList) {
        this.stationList = stationList;
    }

    public static class TimetableViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;
        RecyclerView timesRecyclerView;

        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stopName);
            timesRecyclerView = itemView.findViewById(R.id.timesRecyclerView);
        }
    }

    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timetable_item, parent, false);
        return new TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        Station station = stationList.get(position);
        holder.stopName.setText(station.getName());

        int totalWidth = (int) holder.stopName.getPaint().measureText("XXXXXXXXXXXXXXX");
        holder.stopName.setWidth(totalWidth);

        if (position % 2 == 0) {
            holder.stopName.setBackgroundColor(holder.itemView.getContext().getColor(R.color.rowPrimary));
        } else {
            holder.stopName.setBackgroundColor(holder.itemView.getContext().getColor(R.color.rowAlternate));
        }

        holder.timesRecyclerView.setHasFixedSize(true);
        holder.timesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.timesRecyclerView.setAdapter(new ScheduleRowAdapter(station.getConvertedTimes()));

        timesRecyclerViews.add(holder.timesRecyclerView);
        holder.timesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isSyncing) return;

                isSyncing = true;
                for (RecyclerView syncedRecyclerView : timesRecyclerViews) {
                    if (syncedRecyclerView != recyclerView) {
                        syncedRecyclerView.scrollBy(dx, dy);
                    }
                }
                isSyncing = false;
            }
        });
        if (position % 2 == 0) {
            holder.timesRecyclerView.setBackgroundColor(holder.itemView.getContext().getColor(R.color.rowPrimary));
        } else {
            holder.timesRecyclerView.setBackgroundColor(holder.itemView.getContext().getColor(R.color.rowAlternate));
        }
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }
}
