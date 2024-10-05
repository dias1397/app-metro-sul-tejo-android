package com.diasjoao.metrosultejo.ui.fragment;

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
import com.diasjoao.metrosultejo.ui.adapter.LineAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LineFragment extends Fragment {

    private static final String LINE_NAME = "line_name";

    private TextView lineNameTextView;
    private RecyclerView stationsRecyclerView;

    private String lineName;
    private int lineColor;
    private LineAdapter lineAdapter;

    public static LineFragment newInstance(String lineName) {
        LineFragment fragment = new LineFragment();
        Bundle args = new Bundle();
        args.putString(LINE_NAME, lineName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line, container, false);

        initVars();
        initViews(view);

        setupUI();

        return view;
    }

    private void initVars() {
        String lineNameId = requireArguments().getString(LINE_NAME, null);

        lineName = getLineName(lineNameId);
        lineColor = getLineColor(lineNameId);

        lineAdapter = new LineAdapter(getStationsForLine(lineNameId), lineColor, null);
    }

    private void initViews(View view) {
        lineNameTextView = view.findViewById(R.id.line_name);
        stationsRecyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setupUI() {
        lineNameTextView.setText(lineName);
        lineNameTextView.setBackgroundColor(lineColor);

        stationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        stationsRecyclerView.setAdapter(lineAdapter);
    }

    private String getLineName(String lineName) {
        return switch (lineName) {
            case "LINHA 1" -> getResources().getString(R.string.line11_name);
            case "LINHA 2" -> getResources().getString(R.string.line21_name);
            case "LINHA 3" -> getResources().getString(R.string.line31_name);
            default -> null;
        };
    }

    private int getLineColor(String lineName) {
        return switch (lineName) {
            case "LINHA 1" -> getResources().getColor(R.color.linha1, null);
            case "LINHA 2" -> getResources().getColor(R.color.linha2, null);
            case "LINHA 3" -> getResources().getColor(R.color.linha3, null);
            default -> Color.BLACK;
        };
    }

    private List<String> getStationsForLine(String lineName) {
        return switch (lineName) {
            case "LINHA 1" ->
                    Arrays.asList(getResources().getStringArray(R.array.line_11_stations));
            case "LINHA 2" ->
                    Arrays.asList(getResources().getStringArray(R.array.line_21_stations));
            case "LINHA 3" ->
                    Arrays.asList(getResources().getStringArray(R.array.line_31_stations));
            default -> Collections.emptyList();
        };
    }
}