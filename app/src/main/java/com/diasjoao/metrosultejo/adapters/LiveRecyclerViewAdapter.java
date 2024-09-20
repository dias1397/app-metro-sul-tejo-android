package com.diasjoao.metrosultejo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.model.StationOld;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LiveRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_VIEW = 1;

    private Context context;
    private List<StationOld> stationOlds;
    private List<StationOld> visibleStationOlds;

    public LiveRecyclerViewAdapter(Context context, List<StationOld> stationOlds) {
        this.context = context;
        this.stationOlds = stationOlds;
        this.visibleStationOlds = stationOlds.stream()
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
                .inflate(R.layout.item_live_arrival, parent, false);
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
        if (visibleStationOlds == null) {
            return 0;
        }

        if (visibleStationOlds.size() == 0) {
            return 1;
        }

        return visibleStationOlds.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == visibleStationOlds.size()) {
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public void refreshDataSet(List<StationOld> stationOldTimes) {
        this.stationOlds = stationOldTimes;
        this.visibleStationOlds = stationOlds.stream()
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
                if (visibleStationOlds.size() == stationOlds.size()) {
                    visibleStationOlds = stationOlds.stream()
                            .filter(x -> x.getTimeDifference() < 3600)
                            .collect(Collectors.toList());
                } else {
                    visibleStationOlds = stationOlds;
                }
                notifyDataSetChanged();
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStopTime, tvTimeDifference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStopTime = itemView.findViewById(R.id.textViewArrivalTime);
            tvTimeDifference = itemView.findViewById(R.id.textViewTimeLeft);
        }

        public void bindView(int position) {
            Long stopTime = visibleStationOlds.get(position).getStopTime();
            tvStopTime.setText(LocalTime.ofSecondOfDay(stopTime).plus(3, ChronoUnit.HOURS).toString());

            Long timeDifference = visibleStationOlds.get(position).getTimeDifference();
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
