package com.diasjoao.metrosultejo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.helpers.DateHelper;
import com.diasjoao.metrosultejo.helpers.JsonHelper;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LiveActivity extends AppCompatActivity {

    private AutoCompleteTextView line1Spinner, station1Spinner;
    private CardView cardView1, cardView2, cardView3;
    private TextView time1, time2, time3;
    private TextView countdown1, countdown2, countdown3;

    private Boolean isSummer;
    private String weekday;
    private int lineID, stationID;

    private JSONObject file;

    private List<Long> timeDifferences = new ArrayList<>(3);
    private List<String> departureTimes = new ArrayList<>(3);

    private CountDownTimer firstTimer, secondTimer, thirdTimer;

    private List<Integer> stationsByLine = List.of(
            R.array.linha_11, R.array.linha_12, R.array.linha_21, R.array.linha_22, R.array.linha_31, R.array.linha_32
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        setUpLiveActivityToolbar();
        setUpLiveActivityViews();

        setLineAndStation();
        setTimeSettings();

        setMainJsonFile();

        setLineSpinnerInfo();
        setStationSpinnerInfo();

        //line1Spinner.setSelection(lineID);
        line1Spinner.setText(this.getResources().getStringArray(R.array.linhas)[lineID], false);
    }

    private void setUpLiveActivityToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpLiveActivityViews() {
        line1Spinner = findViewById(R.id.line1);
        station1Spinner = findViewById(R.id.station1);

        cardView1 = findViewById(R.id.cardView1);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);

        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);

        countdown1 = findViewById(R.id.countdown1);
        countdown2 = findViewById(R.id.countdown2);
        countdown3 = findViewById(R.id.countdown3);
    }

    private void setLineAndStation() {
        lineID = getIntent().getIntExtra("line", 0);
        stationID = getIntent().getIntExtra("station", 0);
    }

    private void setTimeSettings() {
        LocalDate currentDate = LocalDateTime.now().minus(3, ChronoUnit.HOURS).toLocalDate();

        isSummer = isSummer(currentDate);
        weekday = getWeekday(currentDate);
    }

    private boolean isSummer(LocalDate currentDate) {
        return (currentDate.getMonthValue() <= Month.SEPTEMBER.getValue() || currentDate.getMonthValue() >= Month.JULY.getValue()) &&
                ((currentDate.getDayOfMonth() >= 15 && currentDate.getMonthValue() == Month.JULY.getValue()) ||
                 (currentDate.getDayOfMonth() <= 7 && currentDate.getMonthValue() == Month.SEPTEMBER.getValue()));
    }

    private String getWeekday(LocalDate currentDate) {
        if (currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY))
            return "SATURDAY";
        if (currentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                DateHelper.isHoliday(this, LocalDate.now()))
            return "SUNDAY";

        return "WEEKDAY";
    }

    private void setMainJsonFile() {
        try {
            file = new JSONObject(Objects.requireNonNull(JsonHelper.getJsonFromAssets(this, "data.json")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLineSpinnerInfo() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, this.getResources().getStringArray(R.array.linhas)
        );

        line1Spinner.setAdapter(adapter);

        line1Spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = LiveActivity.this;
                int selectedItem = new ArrayList<>(
                        Arrays.asList(context.getResources().getStringArray(R.array.linhas))
                ).indexOf(charSequence.toString());

                try {
                    ArrayAdapter<CharSequence> stationAdapter = new ArrayAdapter<>(
                            context,
                            R.layout.support_simple_spinner_dropdown_item,
                            context.getResources().getStringArray(stationsByLine.get(selectedItem))
                    );

                    stationID = Math.min(stationID, stationAdapter.getCount() - 1);
                    lineID = selectedItem;

                    station1Spinner.setAdapter(stationAdapter);
                    station1Spinner.setText(context.getResources().getStringArray(stationsByLine.get(selectedItem))[stationID], false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setStationSpinnerInfo() {
        station1Spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Context context = LiveActivity.this;

                stationID = new ArrayList<>(
                        Arrays.asList(context.getResources().getStringArray(stationsByLine.get(lineID)))).indexOf(charSequence.toString()
                );

                timeDifferences.clear();
                departureTimes.clear();

                cancelCountDownTimers();

                try {
                    JSONObject lineInfoJson = JsonHelper.getLineInfo(file, lineID);

                    int stationOffset = JsonHelper.getStationOffset(lineInfoJson, stationID);
                    JSONArray stationTimes = JsonHelper.getStationTimes(lineInfoJson, isSummer, weekday);

                    List<LocalTime> stationTimesDate = DateHelper.convertStationTimesToDate(stationTimes, stationOffset);
                    setRelevantStationTimes(stationTimesDate);

                    time1.setText(Objects.requireNonNullElse(departureTimes.get(0), ""));
                    firstTimer = setCountDownTimer(cardView1, countdown1, timeDifferences.get(0));

                    time2.setText(Objects.requireNonNullElse(departureTimes.get(1), ""));
                    secondTimer = setCountDownTimer(cardView2, countdown2, timeDifferences.get(1));

                    time3.setText(Objects.requireNonNullElse(departureTimes.get(2), ""));
                    thirdTimer = setCountDownTimer(cardView3, countdown3, timeDifferences.get(2));

                    startCountDownTimers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setRelevantStationTimes(List<LocalTime> stationTimes) {
        LocalTime currentTime = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()).minus(3, ChronoUnit.HOURS);

        int indexNextDeparture = -1;
        for (LocalTime departureTime : stationTimes) {
            if (departureTime.compareTo(currentTime) >= 0) {
                indexNextDeparture = stationTimes.indexOf(departureTime);
                break;
            }
        }

        if (indexNextDeparture == 0) {
            setDepartureTimes(null, currentTime);
            setDepartureTimes(stationTimes.get(indexNextDeparture), currentTime);
            setDepartureTimes(stationTimes.get(indexNextDeparture + 1), currentTime);
            return ;
        }

        if (indexNextDeparture == -1) {
            setDepartureTimes(stationTimes.get(stationTimes.size() - 1), currentTime);
            setDepartureTimes(null, currentTime);
            setDepartureTimes(null, currentTime);
            return;
        }

        if (indexNextDeparture == (stationTimes.size() - 1)) {
            setDepartureTimes(stationTimes.get(indexNextDeparture - 1), currentTime);
            setDepartureTimes(stationTimes.get(indexNextDeparture), currentTime);
            setDepartureTimes(null, currentTime);
            return;
        }

        setDepartureTimes(stationTimes.get(indexNextDeparture - 1), currentTime);
        setDepartureTimes(stationTimes.get(indexNextDeparture), currentTime);
        setDepartureTimes(stationTimes.get(indexNextDeparture + 1), currentTime);
    }

    private void setDepartureTimes(LocalTime departureTime, LocalTime currentTime) {
        if (departureTime == null) {
            timeDifferences.add(null);
            departureTimes.add(null);
            return;
        }

        timeDifferences.add(Duration.between(currentTime, departureTime).toMillis());
        departureTimes.add(departureTime.plus(3, ChronoUnit.HOURS).toString());
    }

    private CountDownTimer setCountDownTimer(CardView cardView, TextView countdown, Long timeDifference) {
        if (timeDifference == null) {
            cardView.setVisibility(View.GONE);
            return null;
        }
        cardView.setVisibility(View.VISIBLE);

        return new CountDownTimer(timeDifference < 0 ? 1000000 : timeDifference, 1000) {
            Long tempDifference = Math.abs(timeDifference);

            @Override
            public void onTick(long millisUntilFinished) {
                if (timeDifference < 0) {
                    tempDifference += 1000;
                    countdown.setText("+" + DateHelper.minutesToString(DateHelper.millisecondsToMinutes(tempDifference)));
                } else {
                    countdown.setText(DateHelper.minutesToString(DateHelper.millisecondsToMinutes(millisUntilFinished)));
                }

            }

            @Override
            public void onFinish() { }
        };
    }

    private void cancelCountDownTimers() {
        if (firstTimer != null) firstTimer.cancel();
        if (secondTimer != null) secondTimer.cancel();
        if (thirdTimer != null) thirdTimer.cancel();
    }

    private void startCountDownTimers() {
        if (firstTimer != null) firstTimer.start();
        if (secondTimer != null) secondTimer.start();
        if (thirdTimer != null) thirdTimer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.removeItem(R.id.miInfo);
        return true;
    }
}