package com.example.amparomed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Vinculo;
import com.example.amparomed.util.Constantes;
import com.example.amparomed.util.InsetsHelper;

/** Perfil do cuidador (tela 11). */
public class PerfilCuidadorActivity extends AppCompatActivity {

    private long cuidadorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cuidador);
        InsetsHelper.aplicarTopo(findViewById(R.id.barra_topo));
        InsetsHelper.aplicarBase(findViewById(R.id.corpo_perfil));
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        cuidadorId = getIntent().getLongExtra(Constantes.EXTRA_CUIDADOR_ID, -1);
        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());

        findViewById(R.id.btn_ligar_perfil).setOnClickListener(v -> {
            Cuidador c = AmparoRepository.get().getCuidador(cuidadorId);
            if (c != null) Dialogos.mostrarLigacao(this, c.getNome(), c.getTelefone(), false);
        });
        findViewById(R.id.btn_editar_perfil).setOnClickListener(v -> {
            Intent i = new Intent(this, FormCuidadorActivity.class);
            i.putExtra(Constantes.EXTRA_CUIDADOR_ID, cuidadorId);
            startActivity(i);
        });
        findViewById(R.id.btn_remover_perfil).setOnClickListener(v -> {
            Cuidador c = AmparoRepository.get().getCuidador(cuidadorId);
            if (c == null) return;
            Dialogos.confirmarRemocao(this, "Remover " + c.getNome() + "?", c.getNome(), c.rotuloVinculo(),
                    "Este contato será removido da lista.",
                    () -> { AmparoRepository.get().removerCuidador(cuidadorId); finish(); });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cuidador c = AmparoRepository.get().getCuidador(cuidadorId);
        if (c == null) { finish(); return; }
        ((TextView) findViewById(R.id.txt_titulo_topo)).setText(c.getVinculo() == Vinculo.CUIDADOR ? "Cuidador" : "Familiar");
        ((TextView) findViewById(R.id.txt_ini_perfil)).setText(c.iniciais());
        ((TextView) findViewById(R.id.txt_nome_perfil)).setText(c.getNome());
        ((TextView) findViewById(R.id.txt_badge_perfil)).setText(c.rotuloVinculo());
        ((TextView) findViewById(R.id.txt_idade_perfil)).setText(c.getIdade() + " anos");
        ((TextView) findViewById(R.id.txt_tel_perfil)).setText(c.getTelefone());
        ((TextView) findViewById(R.id.txt_email_perfil)).setText(c.getEmail());
        ((TextView) findViewById(R.id.txt_cpf_perfil)).setText(c.getCpf());
        ((TextView) findViewById(R.id.btn_ligar_perfil)).setText("Ligar para " + primeiroNome(c.getNome()));
    }

    private String primeiroNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return "";
        return nome.trim().split("\\s+")[0];
    }
}
