package com.diasjoao.metrosultejo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class LiveActivity extends AppCompatActivity {

    Spinner spinner_1, spinner_2;
    ConstraintLayout board1, board2, board3;
    TextView hours1, hours2, hours3;
    TextView minutes1, minutes2, minutes3;

    private int line;
    private int station;

    private int stationId, stationNumber, stationOffset;
    private int lineId;
    private Boolean inverse = true;
    private ArrayList<Date> stationTimes = new ArrayList<>();
    private ArrayList<Long> timeDiferences = new ArrayList<>(3);
    private ArrayList<String> realTimes = new ArrayList<>(3);
    private int[] timerOnOff = new int[]{-1,-1,-1};

    private Calendar rightNow;
    private Boolean summer = true;
    private String dayOfTheWeek;

    CountDownTimer firstTimer = null;
    CountDownTimer secondTimer = null;
    CountDownTimer thirdTimer = null;
   
    private AdView mAdView;

    static final long ONE_MINUTE_IN_MILLIS=60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        line = intent.getIntExtra("line", 0);
        station = intent.getIntExtra("station", 0);

        spinner_1 = (Spinner) findViewById(R.id.linhas);
        spinner_2 = (Spinner) findViewById(R.id.estacoes);
        board1 = findViewById(R.id.board1);
        board2 = findViewById(R.id.board2);
        board3 = findViewById(R.id.board3);
        hours1 = findViewById(R.id.hours1);
        hours2 = findViewById(R.id.hours2);
        hours3 = findViewById(R.id.hours3);
        minutes1 = findViewById(R.id.minutes1);
        minutes2 = findViewById(R.id.minutes2);
        minutes3 = findViewById(R.id.minutes3);

        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(this,
                R.array.linhas, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter_1);

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter_2 = null;

                if (i == 0) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_11, android.R.layout.simple_spinner_item);
                }
                if (i == 1) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_12, android.R.layout.simple_spinner_item);
                }
                if (i == 2) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_21, android.R.layout.simple_spinner_item);
                }
                if (i == 3) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_22, android.R.layout.simple_spinner_item);
                }
                if (i == 4) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_31, android.R.layout.simple_spinner_item);
                }
                if (i == 5) {
                    adapter_2 = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.linha_32, android.R.layout.simple_spinner_item);
                }

                adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_2.setAdapter(adapter_2);
                station = station > adapter_2.getCount() ? adapter_2.getCount() - 1 : station;
                spinner_2.setSelection(station);

                line = i;
                lineId = getLineId(line);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("Nothing Selected on spinner 2");
            }
        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                rightNow = Calendar.getInstance();
                rightNow.add(Calendar.HOUR, -3);

                setTimeSettings(rightNow);

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAsset());

                    stationId = getStationId(obj, (String) adapterView.getItemAtPosition(i));
                    stationNumber = getStationNumber(obj, lineId, stationId);
                    stationOffset = getStationOffset(obj, lineId, stationNumber);

                    stationTimes = getStationTimes(obj, stationOffset);
                    timeDiferences = setTimeDifferences(rightNow, stationTimes);

                    if (timerOnOff[0] != -1) {
                        firstTimer.cancel();
                        timerOnOff[0] = -1;
                    }
                    if (timerOnOff[1] != -1) {
                        secondTimer.cancel();
                        timerOnOff[1] = -1;
                    }
                    if (timerOnOff[2] != -1) {
                        thirdTimer.cancel();
                        timerOnOff[2] = -1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Timer #1
                if (timeDiferences.get(0) != -1) {
                    board1.setVisibility(View.VISIBLE);
                    hours1.setText(realTimes.get(0));
                    timerOnOff[0] = 1;
                    try{
                        firstTimer = new CountDownTimer(1000000, 1000) {

                            Long temp = -timeDiferences.get(0);

                            public void onTick(long millisUntilFinished) {
                                temp = temp + 1000;
                                minutes1.setText('+' + millisecondsToString(temp));
                            }

                            public void onFinish() {

                            }
                        }.start();
                    }catch (Exception e){

                    }
                } else {
                    board1.setVisibility(View.GONE);
                }

                // Timer #2
                if (timeDiferences.get(1) != -1) {
                    hours2.setText(realTimes.get(1));
                    timerOnOff[1] = 1;
                    try{
                        secondTimer = new CountDownTimer(timeDiferences.get(1), 1000) {

                            public void onTick(long millisUntilFinished) {
                                minutes2.setText(millisecondsToString(millisUntilFinished));
                            }

                            public void onFinish() {
                                Intent intent = new Intent(getBaseContext(), LiveActivity.class);
                                intent.putExtra("line", line);
                                intent.putExtra("station", i);
                                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            }
                        }.start();
                    }catch (Exception e){

                    }
                } else {
                    hours2.setText("Volte Amanhã");
                    hours2.setBackgroundColor(Color.TRANSPARENT);
                    minutes2.setText("    ");
                    minutes2.setBackgroundColor(Color.TRANSPARENT);
                }

                // Timer #3
                if (timeDiferences.get(2) != -1) {
                    board3.setVisibility(View.VISIBLE);
                    hours3.setText(realTimes.get(2));
                    timerOnOff[2] = 1;
                    try{
                        thirdTimer = new CountDownTimer(timeDiferences.get(2), 1000) {

                            public void onTick(long millisUntilFinished) {
                                minutes3.setText(millisecondsToString(millisUntilFinished));
                            }

                            public void onFinish() {

                            }
                        }.start();
                    }catch (Exception e){

                    }
                } else {
                    board3.setVisibility(View.GONE);
                }

                station = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_1.setSelection(line);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (timerOnOff[0] != -1) {
            firstTimer.cancel();
            timerOnOff[0] = -1;
        }
        if (timerOnOff[1] != -1) {
            secondTimer.cancel();
            timerOnOff[1] = -1;
        }
        if (timerOnOff[2] != -1) {
            thirdTimer.cancel();
            timerOnOff[2] = -1;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.data);
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

    public int getLineId(int line) {
        switch (line) {
            case 0:
                inverse = false;
                return 0;
            case 1:
                inverse = true;
                return 0;
            case 2:
                inverse = false;
                return 1;
            case 3:
                inverse = true;
                return 1;
            case 4:
                inverse = false;
                return 2;
            case 5:
                inverse = true;
                return 2;
            default:
                return -1;
        }
    }

    public int getStationId(JSONObject jsonFile, String stationName) throws JSONException {
        JSONArray stationsList = jsonFile.getJSONArray("stations");

        for (int i = 0; i < stationsList.length(); i++) {
            if (stationsList.getJSONObject(i).getString("name").equals(stationName)) {
                return Integer.parseInt(stationsList.getJSONObject(i).getString("id"));
            }
        }

        return -1;
    }

    public int getStationNumber(JSONObject jsonFile, int lineId, int stationId) throws JSONException {
        JSONObject line = jsonFile.getJSONArray("lines").getJSONObject(lineId);
        JSONArray stationOrder = line.getJSONArray("stations");

        for (int i = 0; i < stationOrder.length(); i++) {
            if (Integer.parseInt(stationOrder.getString(i)) == stationId) {
                if (inverse == false)
                    return i;
                else
                    return (stationOrder.length() - 1) - i;
            }
        }

        return -1;
    }

    public int getStationOffset(JSONObject jsonFile, int lineId, int stationNumber) throws JSONException {
        JSONObject line = jsonFile.getJSONArray("lines").getJSONObject(lineId);
        JSONObject lineWay = line.getJSONArray("directions").getJSONObject(inverse ? 1 : 0);
        JSONArray offsets = lineWay.getJSONArray("offsets");

        int cont = 0;

        for (int i = 0; i < stationNumber; i++) {
            cont += Integer.parseInt(offsets.getString(i));
        }

        return cont;
    }

    public ArrayList<Date> getStationTimes(JSONObject jsonFile, int stationOffset) throws JSONException {
        ArrayList<Date> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        JSONObject line = jsonFile.getJSONArray("lines").getJSONObject(lineId);
        JSONObject directions = line.getJSONArray("directions").getJSONObject(inverse ? 1 : 0);

        int combination = 0;
        if (summer) {
            if (dayOfTheWeek.equals("weekdays")) {
                combination = 0;
            } else if (dayOfTheWeek.equals("saturdays")) {
                combination = 1;
            } else {
                combination = 2;
            }
        } else {
            if (dayOfTheWeek.equals("weekdays")) {
                combination = 3;
            } else if (dayOfTheWeek.equals("saturdays")) {
                combination = 4;
            } else {
                combination = 5;
            }
        }

        JSONObject departures = directions.getJSONArray("departures").getJSONObject(combination);
        JSONArray times = departures.getJSONObject("times").getJSONArray("times");

        for (int i = 0; i < times.length(); i++) {
            try {
                result.add(new Date(dateFormat.parse(times.getString(i)).getTime() + (stationOffset * ONE_MINUTE_IN_MILLIS)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void setTimeSettings(Calendar rightNow) {
        if (rightNow.get(Calendar.DAY_OF_MONTH) >= 8 && rightNow.get(Calendar.MONTH) == 9) {
            summer = false;
        }
        if (rightNow.get(Calendar.DAY_OF_MONTH) <= 14 && rightNow.get(Calendar.MONTH) == 7){
            summer = false;
        }
        if (rightNow.get(Calendar.MONTH) > 9 || rightNow.get(Calendar.MONTH) < 7) {
            summer = false;
        }

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

        dayOfTheWeek = checkHoliday(rightNow, Arrays.asList(getResources().getStringArray(R.array.feriados))) ? "sundays" : dayOfTheWeek;
    }

    public ArrayList<Long> setTimeDifferences(Calendar rightNowCalendar, ArrayList<Date> stationTimes) throws ParseException {
        ArrayList<Long> result = new ArrayList<>(3);
        String temp = rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + rightNowCalendar.get(Calendar.MINUTE);
        Date rightNow = new SimpleDateFormat("HH:mm").parse(temp);

        realTimes.clear();

        // Apenas metros no futuro
        if (stationTimes.get(0).getTime() - rightNow.getTime() > 0) {
            result.add((long) -1);
            realTimes.add("-1");
            result.add(stationTimes.get(0).getTime() - rightNow.getTime());
            rightNowCalendar.setTime(stationTimes.get(0));
            rightNowCalendar.add(Calendar.HOUR, 3);
            realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));
            result.add(stationTimes.get(1).getTime() - rightNow.getTime());
            rightNowCalendar.setTime(stationTimes.get(1));
            rightNowCalendar.add(Calendar.HOUR, 3);
            realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));
        } else {
            int cont = 0;
            int position = -1;
            for (Date d : stationTimes) {
                if (d.getTime() - rightNow.getTime() > 0 && position == -1) {
                    position = cont;
                }
                cont++;
            }

            if (position != -1) {
                if (position == stationTimes.size()-1) {
                    // falta apenas um metro no dia
                    result.add(stationTimes.get(position-1).getTime() - rightNow.getTime());
                    rightNowCalendar.setTime(stationTimes.get(position-1));
                    rightNowCalendar.add(Calendar.HOUR, 3);
                    realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                    result.add(stationTimes.get(position).getTime() - rightNow.getTime());
                    rightNowCalendar.setTime(stationTimes.get(position));
                    rightNowCalendar.add(Calendar.HOUR, 3);
                    realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                    result.add((long) -1);
                    realTimes.add("-1");
                } else {
                    // horarios normais
                    result.add(stationTimes.get(position-1).getTime() - rightNow.getTime());
                    rightNowCalendar.setTime(stationTimes.get(position-1));
                    rightNowCalendar.add(Calendar.HOUR, 3);
                    realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                    result.add(stationTimes.get(position).getTime() - rightNow.getTime());
                    rightNowCalendar.setTime(stationTimes.get(position));
                    rightNowCalendar.add(Calendar.HOUR, 3);
                    realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                    result.add(stationTimes.get(position+1).getTime() - rightNow.getTime());
                    rightNowCalendar.setTime(stationTimes.get(position+1));
                    rightNowCalendar.add(Calendar.HOUR, 3);
                    realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                }
            } else {
                // Não vão passar mais metros
                result.add(stationTimes.get(stationTimes.size()-1).getTime() - rightNow.getTime());
                rightNowCalendar.setTime(stationTimes.get(stationTimes.size()-1));
                rightNowCalendar.add(Calendar.HOUR, 3);
                realTimes.add(rightNowCalendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNowCalendar.get(Calendar.MINUTE)));

                result.add((long) -1);
                realTimes.add("-1");

                result.add((long) -1);
                realTimes.add("-1");
            }
        }

        return result;
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

    private static String millisecondsToString(Long milliseconds){
        return String.format("%1$3s", (((milliseconds / (1000*60))) + "'"));
    }
}
