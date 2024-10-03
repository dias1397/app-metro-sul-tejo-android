package com.diasjoao.metrosultejo.ui.search;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.ui.live.LiveActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    private Spinner spinnerLine;
    private Spinner spinnerStation;
    private Button buttonSearch;

    private int lineId = 0;
    private ArrayAdapter<String> lineAdapter;
    private ArrayAdapter<String> stationAdapter;
    private List<String> lines = new ArrayList<>();
    private Map<String, List<String>> stationsByLineMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initVars();
        initViews(view);

        setupUI();
        setupListeners();

        return view;
    }

    private void initVars() {
        Bundle args = getArguments();
        if (args != null) {
            lineId = args.getInt("lineId");
        }

        stationsByLineMap = fetchLineStationMap();
        lines = new ArrayList<>(stationsByLineMap.keySet());

        lineAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lines);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        updateStationAdapter(lines.get(lineId));
    }

    private Map<String, List<String>> fetchLineStationMap() {
        Resources res = requireContext().getResources();
        String[] lines = {"line_11", "line_12", "line_21", "line_22", "line_31", "line_32"};

        Map<String, List<String>> lineStationMap = new LinkedHashMap<>();
        for (String line : lines) {
            String lineName = res.getString(res.getIdentifier(line + "_name", "string", requireContext().getPackageName()));
            String[] stations = res.getStringArray(res.getIdentifier(line + "_stations", "array", requireContext().getPackageName()));

            lineStationMap.put(lineName, Arrays.asList(stations));
        }

        return lineStationMap;
    }

    private void initViews(View view) {
        spinnerLine = view.findViewById(R.id.spinner_line);
        spinnerStation = view.findViewById(R.id.spinner_station);

        buttonSearch = view.findViewById(R.id.button_search);
    }

    private void setupUI() {
        spinnerLine.setAdapter(lineAdapter);
        spinnerStation.setAdapter(stationAdapter);
    }

    private void setupListeners() {
        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLine = lines.get(position);
                updateStationAdapter(selectedLine);

                if (getArguments() != null) {
                    spinnerStation.setSelection(getArguments().getInt("stationId"));
                    getArguments().clear();
                } else {
                    spinnerStation.setAdapter(stationAdapter);
                    spinnerStation.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        spinnerLine.setSelection(lineId);

        buttonSearch.setOnClickListener(v -> performSearch());
    }

    private void updateStationAdapter(String line) {
        stationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                stationsByLineMap.get(line));
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void performSearch() {
        Intent intent = new Intent(requireActivity(), LiveActivity.class);
        intent.putExtra("lineId", spinnerLine.getSelectedItemPosition() + 1);
        intent.putExtra("stationId", spinnerStation.getSelectedItemPosition());
        intent.putExtra("stationName", spinnerStation.getSelectedItem().toString());
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}