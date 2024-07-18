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

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScheduleRepository scheduleRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        scheduleRepository = new ScheduleRepository(requireContext());

        List<Station> stations = scheduleRepository.findStationsBySeasonAndDayAndLine(1, 1, 1);
        recyclerView.setAdapter(new TimetableAdapter(stations));

        /*String jsonFileName = "schedule.json";
        String jsonString = JsonUtils.loadJSONFromAssets(getContext(), jsonFileName);

        if (jsonString != null) {
            List<Station> stationList = JsonUtils.parseStationListJson(jsonString, 1, 1, 1);

            recyclerView.setAdapter(new TimetableAdapter(stationList));
        }*/

        return view;
    }

    private List<String> generateTimes(String hour) {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            times.add(hour + ":" + String.format("%02d", i));
        }
        return times;
    }
}