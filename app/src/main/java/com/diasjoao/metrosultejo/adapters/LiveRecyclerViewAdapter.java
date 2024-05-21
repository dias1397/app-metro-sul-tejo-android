package com.diasjoao.metrosultejo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.model.Station;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LiveRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_VIEW = 1;

    private Context context;
    private List<Station> stations;
    private List<Station> visibleStations;

    public LiveRecyclerViewAdapter(Context context, List<Station> stations) {
        this.context = context;
        this.stations = stations;
        this.visibleStations = stations.stream()
                .filter(x -> x.getTimeDifference() < 3600)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.live_recycler_view_footer, parent, false);
            return new FooterViewHolder(view);
        }

        view = LayoutInflater.from(context)
                .inflate(R.layout.live_recycler_view_row, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;

                vh.bindView(position);
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (visibleStations == null) {
            return 0;
        }

        if (visibleStations.size() == 0) {
            return 1;
        }

        return visibleStations.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == visibleStations.size()) {
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public void refreshDataSet(List<Station> stationTimes) {
        this.stations = stationTimes;
        this.visibleStations = stations.stream()
                .filter(x -> x.getTimeDifference() < 3600)
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {
                if (visibleStations.size() == stations.size()) {
                    visibleStations = stations.stream()
                            .filter(x -> x.getTimeDifference() < 3600)
                            .collect(Collectors.toList());
                } else {
                    visibleStations = stations;
                }
                notifyDataSetChanged();
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStopTime, tvTimeDifference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStopTime = itemView.findViewById(R.id.stopTime);
            tvTimeDifference = itemView.findViewById(R.id.timeDifference);
        }

        public void bindView(int position) {
            Long stopTime = visibleStations.get(position).getStopTime();
            tvStopTime.setText(LocalTime.ofSecondOfDay(stopTime).plus(3, ChronoUnit.HOURS).toString());

            Long timeDifference = visibleStations.get(position).getTimeDifference();
            if (timeDifference < 3600) {
                tvTimeDifference.setText(String.format(Locale.getDefault(), "%02d'", Math.abs(timeDifference) / 60));
                tvTimeDifference.setBackgroundColor(timeDifference < 0 ? context.getColor(R.color.FireBrick) : context.getColor(R.color.ForestGreen));
                tvTimeDifference.setVisibility(View.VISIBLE);
            } else {
                tvTimeDifference.setVisibility(View.GONE);
            }
        }
    }
}
