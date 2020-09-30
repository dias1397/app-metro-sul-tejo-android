package com.diasjoao.metrosultejo.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    String dayOfTheWeek = null;
    String[] stops = null;
    Boolean isSummer;
    int lineId;

    ArrayList<Date> stationTimes = new ArrayList<>();
    ArrayList<Integer> offsets = new ArrayList<>();

    CheckBox util;
    CheckBox sabados;
    CheckBox domingo;

    TableLayout table1;
    TableLayout table2;
    Spinner lineSpinner;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        util = (CheckBox) findViewById(R.id.ut);
        sabados = (CheckBox) findViewById(R.id.sab);
        domingo = (CheckBox) findViewById(R.id.dom);

        table1 = (TableLayout)findViewById(R.id.tabela1);
        table2 = (TableLayout)findViewById(R.id.tabela2);
        lineSpinner = (Spinner) findViewById(R.id.linhas);

        final ScrollView scroll1 = (ScrollView) findViewById(R.id.scroll1);
        final ScrollView scroll2 = (ScrollView) findViewById(R.id.scroll2);

        Calendar rightNow = Calendar.getInstance();

        Intent intent = getIntent();
        lineId = intent.getIntExtra("line", 0);
        dayOfTheWeek = intent.getStringExtra("day");
        isSummer = setStationOfTheYear(rightNow);

        if (dayOfTheWeek == null) {
            dayOfTheWeek = setDayOfTheWeek(rightNow);
        }

        switch (dayOfTheWeek) {
            case "weekdays":
                util.setChecked(true);
                sabados.setChecked(false);
                domingo.setChecked(false);

                util.setClickable(false);
                break;
            case "saturdays":
                util.setChecked(false);
                sabados.setChecked(true);
                domingo.setChecked(false);

                sabados.setClickable(false);
                break;
            case "sundays":
                util.setChecked(false);
                sabados.setChecked(false);
                domingo.setChecked(true);

                domingo.setClickable(false);
                break;
        }

        util.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!dayOfTheWeek.equals("weekdays") && util.isChecked()){
                    Intent reset = new Intent(getBaseContext(), ScheduleActivity.class);
                    reset.putExtra("day", "weekdays");
                    reset.putExtra("line", lineId);
                    reset.setFlags(reset.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(reset);
                }
            }
        });

        sabados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!dayOfTheWeek.equals("saturdays") && sabados.isChecked()){
                    Intent reset = new Intent(getBaseContext(), ScheduleActivity.class);
                    reset.putExtra("day", "saturdays");
                    reset.putExtra("line", lineId);
                    reset.setFlags(reset.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(reset);
                }
            }
        });

        domingo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!dayOfTheWeek.equals("sundays") && domingo.isChecked()){
                    Intent reset = new Intent(getBaseContext(), ScheduleActivity.class);
                    reset.putExtra("day", "sundays");
                    reset.putExtra("line", lineId);
                    reset.setFlags(reset.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(reset);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(this,
                R.array.linhas, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lineSpinner.setAdapter(adapter_1);
        lineSpinner.setSelection(lineId);

        lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lineId = i;

                if (i == 0){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_11);
                    populate();
                }
                if (i == 1){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_12);
                    populate();
                }
                if (i == 2){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_21);
                    populate();
                }
                if (i == 3){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_22);
                    populate();
                }
                if (i == 4){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_31);
                    populate();
                }
                if (i == 5){
                    table1.removeAllViews();
                    table2.removeAllViews();
                    stops = null;
                    stops = getResources().getStringArray(R.array.linha_32);
                    populate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        scroll1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                scroll2.scrollTo(i,i1);
            }
        });

        scroll2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                scroll1.scrollTo(i, i1);
            }
        });
    }

    public void populate() {
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(1, 0xFF000000);

        int k = 0;
        for (String s : stops){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView texto = new TextView(this);
            texto.setTextSize(17.0f);
            texto.setBackground(gd);
            texto.setTextColor(getResources().getColor(R.color.colorBlack));
            texto.setPadding(10, 0, 10, 0);
            texto.setText(s + "  ");
            row.addView(texto);
            table1.addView(row, k);
            k++;
        }

        Calendar rightNow = Calendar.getInstance();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            offsets = getOffsets(obj, lineId);
            stationTimes = getStationsTimes(obj, lineId, isSummer, dayOfTheWeek);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int offsetSum = 0, a = 0;
        for (int off : offsets) {
            offsetSum += off;
            String temp = "";

            for (Date d : stationTimes) {
                rightNow.setTime(new Date(d.getTime() + (offsetSum * DateUtils.ONE_MINUTE_IN_MILLIS)));
                temp += String.format("%02d", rightNow.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", rightNow.get(Calendar.MINUTE));
                temp += "  |  ";
            };

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView texto = new TextView(this);
            texto.setTextSize(17.0f);
            texto.setBackground(gd);
            texto.setTextColor(getResources().getColor(R.color.colorBlack));
            texto.setPadding(10, 0, 10, 0);
            texto.setText(temp);
            row.addView(texto);

            table2.addView(row, a);
            a++;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.datareal);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public ArrayList<Integer> getOffsets(JSONObject jsonFile, int lineId) throws JSONException {
        ArrayList<Integer> result = new ArrayList<>();
        JSONArray offsets = jsonFile.getJSONArray("directions").getJSONObject(lineId).getJSONArray("offsets");

        for (int i = 0; i < offsets.length(); i++) {
            result.add(offsets.getInt(i));
        }

        return result;
    }
    public ArrayList<Date> getStationsTimes(JSONObject jsonFile, int lineId, Boolean isSummer, String dayOfTheWeek) throws JSONException {
        ArrayList<Date> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        JSONArray offsets = jsonFile.getJSONArray("directions").getJSONObject(lineId).getJSONArray("departures");
        int scheduleId = 0;

        if (isSummer) {
            if (dayOfTheWeek.equals("weekdays")) {
                scheduleId = 0;
            } else if (dayOfTheWeek.equals("saturdays")) {
                scheduleId = 1;
            } else {
                scheduleId = 2;
            }
        } else {
            if (dayOfTheWeek.equals("weekdays")) {
                scheduleId = 3;
            } else if (dayOfTheWeek.equals("saturdays")) {
                scheduleId = 4;
            } else {
                scheduleId = 5;
            }
        }

        JSONArray timetable = offsets.getJSONObject(scheduleId).getJSONObject("times").getJSONArray("times");

        for (int i = 0; i < timetable.length(); i++) {
            try {
                result.add(new Date(dateFormat.parse(timetable.getString(i)).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Boolean setStationOfTheYear(Calendar rightNow) {
        if (rightNow.get(Calendar.DAY_OF_MONTH) >= 8 && rightNow.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
            return false;
        }

        if (rightNow.get(Calendar.DAY_OF_MONTH) <= 14 && rightNow.get(Calendar.MONTH) == Calendar.JULY){
            return false;
        }

        if (rightNow.get(Calendar.MONTH) > Calendar.SEPTEMBER || rightNow.get(Calendar.MONTH) < Calendar.JULY) {
            return false;
        }

        return true;
    }
    public String setDayOfTheWeek(Calendar rightNow) {
        switch (rightNow.get(Calendar.DAY_OF_WEEK)) {
            case 7:
                dayOfTheWeek = "saturdays";
                break;
            case 1:
                dayOfTheWeek = "sundays";
                break;
            default:
                dayOfTheWeek = "weekdays";
                break;
        }

        return dayOfTheWeek = checkHoliday(rightNow, Arrays.asList(getResources().getStringArray(R.array.feriados))) ? "sundays" : dayOfTheWeek;
    }

    private static Boolean checkHoliday(Calendar date, List<String> feriados){
        String dia = String.format("%02d",date.get(Calendar.DAY_OF_MONTH)) +
                "-" + String.format("%02d", date.get(Calendar.MONTH));

        for (String feriado : feriados) {
            if (feriado.equals(dia))
                return true;
        }

        return false;
    }
}
