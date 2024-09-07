package com.diasjoao.metrosultejo.ui.routes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diasjoao.metrosultejo.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoutesFragment extends Fragment {

    private static final String LINE_NAME = "line_name";

    public static RoutesFragment newInstance(String lineName) {
        RoutesFragment fragment = new RoutesFragment();
        Bundle args = new Bundle();
        args.putString(LINE_NAME, lineName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        // Retrieve the line name from the arguments
        String lineName = getArguments().getString(LINE_NAME);

        // Initialize RecyclerView or other UI components
        setupRecyclerView(view, lineName);

        return view;
    }

    private void setupRecyclerView(View view, String lineName) {
        TextView lineNameTextview = view.findViewById(R.id.line_name);
        lineNameTextview.setText(getLineName(lineName));
        lineNameTextview.setBackgroundColor(getLineColor(lineName));

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Retrieve stations for the line and set up the adapter
        List<String> stations = getStationsForLine(lineName);
        RoutesAdapter adapter = new RoutesAdapter(stations, getLineColor(lineName), station -> {
            // Handle station click
            /*Intent intent = new Intent(getActivity(), StationDetailActivity.class);
            intent.putExtra("station_name", station);
            startActivity(intent);*/
        });

        recyclerView.setAdapter(adapter);
    }

    private String getLineName(String lineName) {
        switch (lineName) {
            case "LINHA 1":
                return getResources().getString(R.string.line_1_name);
            case "LINHA 2":
                return getResources().getString(R.string.line_2_name);
            case "LINHA 3":
                return getResources().getString(R.string.line_3_name);
            default:
                return null;
        }
    }

    private List<String> getStationsForLine(String lineName) {
        // Return the list of stations based on the line name
        switch (lineName) {
            case "LINHA 1":
                return Arrays.asList(getResources().getStringArray(R.array.line_11_stations));
            case "LINHA 2":
                return Arrays.asList(getResources().getStringArray(R.array.line_21_stations));
            case "LINHA 3":
                return Arrays.asList(getResources().getStringArray(R.array.line_31_stations));
            default:
                return Collections.emptyList();
        }
    }

    private int getLineColor(String lineName) {
        // Return color based on line name
        switch (lineName) {
            case "LINHA 1":
                return getResources().getColor(R.color.linha1, null);
            case "LINHA 2":
                return getResources().getColor(R.color.linha2, null);
            case "LINHA 3":
                return getResources().getColor(R.color.linha3, null);
            default:
                return Color.BLACK;
        }
    }
}