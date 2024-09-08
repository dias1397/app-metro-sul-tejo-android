package com.diasjoao.metrosultejo.ui.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.adapters.TimetableAdapter;
import com.diasjoao.metrosultejo.data.repository.ScheduleRepository;
import com.diasjoao.metrosultejo.data.model.Station;

import java.util.List;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleRepository scheduleRepository;

    private int seasonId, dayId, lineId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        if (getArguments() != null) {
            seasonId = getArguments().getInt("seasonId", 1);
            dayId = getArguments().getInt("dayId", 1);
            lineId = getArguments().getInt("lineId", 1);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        scheduleRepository = new ScheduleRepository(requireContext());

        List<Station> stations = scheduleRepository.findStationsBySeasonAndDayAndLine(seasonId, dayId, lineId + lineId - 1);
        recyclerView.setAdapter(new TimetableAdapter(stations));

        return view;
    }
}