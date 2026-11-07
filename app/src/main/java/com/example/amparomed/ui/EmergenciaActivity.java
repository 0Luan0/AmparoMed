package com.example.amparomed.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.util.InsetsHelper;

/**
 * Tela de emergencia (tela 05). Ligacao SIMULADA. Num app real, ligar para o
 * 192 usaria Intent.ACTION_DIAL (o Android nao permite ACTION_CALL direto para
 * numeros de emergencia): o discador abriria com o numero e a pessoa confirmaria.
 */
public class EmergenciaActivity extends AppCompatActivity implements ContatoAdapter.Acao {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);
        InsetsHelper.aplicarTopo(findViewById(R.id.barra_topo));
        InsetsHelper.aplicarBase(findViewById(R.id.corpo_emergencia));
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        ((TextView) findViewById(R.id.txt_titulo_topo)).setText("Emergência");
        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.nucleo_samu).setOnClickListener(v -> Dialogos.mostrarLigacao(this, "SAMU", "192", true));

        RecyclerView lista = findViewById(R.id.lista_emergencia);
        ContatoAdapter adapter = new ContatoAdapter(this);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);
        adapter.setDados(AmparoRepository.get().getCuidadores());
    }

    @Override public void aoAbrir(Cuidador c) { Dialogos.mostrarLigacao(this, c.getNome(), c.getTelefone(), false); }
    @Override public void aoLigar(Cuidador c) { Dialogos.mostrarLigacao(this, c.getNome(), c.getTelefone(), false); }
}
