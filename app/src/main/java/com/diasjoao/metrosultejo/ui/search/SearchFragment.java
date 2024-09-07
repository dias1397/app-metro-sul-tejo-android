package com.diasjoao.metrosultejo.ui.search;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

    private Map<String, List<String>> lineStationMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the views
        spinnerLine = view.findViewById(R.id.spinner_line);
        spinnerStation = view.findViewById(R.id.spinner_station);
        buttonSearch = view.findViewById(R.id.button_search);

        // Set up the data
        setupData();

        // Set up the Spinners
        setupLineSpinner();

        // Set up the Search button click listener
        buttonSearch.setOnClickListener(v -> {
            String selectedLine = spinnerLine.getSelectedItem().toString();
            String selectedStation = spinnerStation.getSelectedItem().toString();
            performSearch(selectedLine, selectedStation);
        });

        Bundle args = getArguments();
        if (args != null) {
            spinnerLine.setSelection(args.getInt("lineId"));
        }

        return view;
    }

    private void setupData() {
        Resources res = requireContext().getResources();
        String[] lines = {"line_11", "line_12", "line_21", "line_22", "line_31", "line_32"};

        lineStationMap = new LinkedHashMap<>();
        for (String line : lines) {
            String[] stations = res.getStringArray(res.getIdentifier(line + "_stations", "array", requireContext().getPackageName()));

            List<String> stationList = Arrays.asList(stations);

            String lineName = res.getString(res.getIdentifier(line + "_name", "string", requireContext().getPackageName()));
            lineStationMap.put(lineName, stationList);
        }
    }

    private void setupLineSpinner() {
        // Populate the Line Spinner with lines
        final List<String> lines = new ArrayList<>(lineStationMap.keySet());
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lines);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLine.setAdapter(lineAdapter);

        // Set an item selected listener to update the Station Spinner when a line is selected
        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLine = lines.get(position);
                updateStationSpinner(selectedLine);

                if (getArguments() != null) {
                    spinnerStation.setSelection(getArguments().getInt("stationId"));
                    getArguments().clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initialize the Station Spinner with the first line's stations
        if (!lines.isEmpty()) {
            updateStationSpinner(lines.get(0));
        }
    }

    private void updateStationSpinner(String selectedLine) {
        // Get the stations corresponding to the selected line
        List<String> stations = lineStationMap.get(selectedLine);

        // Populate the Station Spinner with stations
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, stations);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStation.setAdapter(stationAdapter);
    }

    private void performSearch(String line, String station) {
        Toast.makeText(getContext(), "Searching for " + line + " at " + station, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), LiveActivity.class);
        intent.putExtra("lineId", spinnerLine.getSelectedItemPosition());
        intent.putExtra("stationId", spinnerStation.getSelectedItemPosition());
        startActivity(intent);
    }
}