package com.diasjoao.metrosultejo.ui.tariffs;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.model.Tariff;

import java.util.ArrayList;
import java.util.List;

public class TariffsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TariffRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tariffs);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVariant, null));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Tariff> tariffList = new ArrayList<>();
        tariffList.add(new Tariff("Bilhete Simples", "0,85 €"));
        tariffList.add(new Tariff("Zapping", "0,85 €"));
        tariffList.add(new Tariff("Pré-Comprado (10 Bilhetes)", "0.75 €"));
        tariffList.add(new Tariff("Passe MTS ", "16.75 €"));
        tariffList.add(new Tariff("Passe MTS 4_18@escola.tp", "Gratuito"));
        tariffList.add(new Tariff("Passe MTS Sub23@superior.tp", "Gratuito"));
        tariffList.add(new Tariff("Passe MTS 3ª Idade", "9,20 €"));
        tariffList.add(new Tariff("Passe MTS Reformado/Pensionista", "9,20 €"));
        tariffList.add(new Tariff("Complemento MTS", "9 €"));
        tariffList.add(new Tariff("Complemento MTS 4_18@escola.tp", "Gratuito"));
        tariffList.add(new Tariff("Complemento MTS Sub23@superior.tp", "Gratuito"));
        tariffList.add(new Tariff("Complemento MTS 3ª Idade", "4,95 €"));
        tariffList.add(new Tariff("Complemento MTS Reformado/Pensionista", "4,95 €"));
        tariffList.add(new Tariff("Navegante Metropolitano", "40 €"));
        tariffList.add(new Tariff("Navegante Municipal", "30 €"));
        tariffList.add(new Tariff("Navegante 12", "Gratuito"));
        tariffList.add(new Tariff("Navegante +65", "20 €"));
        tariffList.add(new Tariff("Navegante Metropolitano 4_18 e sub23", "Gratuito"));
        tariffList.add(new Tariff("Navegante Metropolitano Social + (A)", "20 €"));
        tariffList.add(new Tariff("Navegante Municipal Social + (A)", "15 €"));
        tariffList.add(new Tariff("Navegante Metropolitano Social", "30 €"));
        tariffList.add(new Tariff("Navegante Municipal Social + (B)", "22,50 €"));
        tariffList.add(new Tariff("Navegante Metropolitano Família", "80 €"));
        tariffList.add(new Tariff("Navegante Municipal Família", "60 €"));

        adapter = new TariffRecyclerViewAdapter(tariffList, this);
        recyclerView.setAdapter(adapter);
    }
}