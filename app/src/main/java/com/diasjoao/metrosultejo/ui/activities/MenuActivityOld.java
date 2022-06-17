package com.diasjoao.metrosultejo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diasjoao.metrosultejo.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;


public class MenuActivityOld extends AppCompatActivity {
    ImageView line1status;
    ImageView line2status;
    ImageView line3status;
    TextView timeOfUpdate;

    private Calendar rightNow;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_old);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MenuActivityOld.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        line1status = findViewById(R.id.line1);
        line2status = findViewById(R.id.line2);
        line3status = findViewById(R.id.line3);
        timeOfUpdate =findViewById(R.id.atualizado);

        new RetrieveLineState().execute();

        Button proximo = findViewById(R.id.proximo);
        Button horarios = findViewById(R.id.horarios);
        Button mapa = findViewById(R.id.mapa);
        Button info = findViewById(R.id.informacao);

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LiveActivity.class);
                intent.putExtra("line", 0);
                intent.putExtra("station", 0);
                startActivity(intent);
            }
        });

        horarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ScheduleActivity.class);
                intent.putExtra("line", 0);
                startActivity(intent);
            }
        });

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        timeOfUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveLineState().execute();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    class RetrieveLineState extends AsyncTask<String, Void, int[]> {

        private Exception exception;

        protected int[] doInBackground(String... urls) {
            int[] states = new int[]{-1,-1,-1};
            try {
                Document doc = Jsoup.connect("http://www.mts.pt/").get();
                Elements lineStates = doc.getElementsByClass("estado-linhas__btn");

                for (int i = 0; i<lineStates.size(); i++) {
                    if (lineStates.get(i).text().equals("Ok")) {
                        states[i] = 1;
                    } else {
                        states[i] = -1;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return states;
        }

        protected void onPostExecute(int[] result) {
            if (isNetworkConnected()) {
                line1status.setImageResource(result[0]==1 ? R.drawable.ic_check_black_24dp : R.drawable.ic_warning_black_24dp);
                line2status.setImageResource(result[1]==1 ? R.drawable.ic_check_black_24dp : R.drawable.ic_warning_black_24dp);
                line3status.setImageResource(result[2]==1 ? R.drawable.ic_check_black_24dp : R.drawable.ic_warning_black_24dp);

                rightNow = Calendar.getInstance();

                timeOfUpdate.setText("Atualizado em " + rightNow.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", rightNow.get(Calendar.MINUTE)));
            } else {
                line1status.setImageResource(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp);
                line2status.setImageResource(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp);
                line3status.setImageResource(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp);

                timeOfUpdate.setText("Por favor conecte-se à Internet");
            }
        }
    }
}
