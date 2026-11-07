package com.example.amparomed.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Lembrete;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.model.Ocorrencia;
import com.example.amparomed.model.StatusDose;
import com.example.amparomed.util.Constantes;
import com.example.amparomed.util.Formatos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Tela cheia de alarme (tela 04). Voltar pelo sistema fecha SEM registrar. */
public class AlarmeActivity extends AppCompatActivity {

    private Ocorrencia ocorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        AmparoRepository repo = AmparoRepository.get();
        long lembreteId = getIntent().getLongExtra(Constantes.EXTRA_LEMBRETE_ID, -1);
        String iso = getIntent().getStringExtra(Constantes.EXTRA_DATA_HORA);
        Lembrete l = repo.getLembrete(lembreteId);

        if (l == null || iso == null) {
            Toast.makeText(this, "Lembrete não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Medicamento m = repo.getMedicamento(l.getMedicamentoId());
        LocalDateTime dataHora = LocalDateTime.parse(iso);
        ocorrencia = new Ocorrencia(l, m, dataHora, StatusDose.PENDENTE);
        preencher(m, l, dataHora);

        findViewById(R.id.btn_confirmar).setOnClickListener(v -> confirmar());
        findViewById(R.id.btn_nao_tomei).setOnClickListener(v -> naoTomei());
    }

    private void preencher(Medicamento m, Lembrete l, LocalDateTime dataHora) {
        ((TextView) findViewById(R.id.txt_hora)).setText(Formatos.hora(dataHora.toLocalTime()));
        ((TextView) findViewById(R.id.txt_nome_alarme)).setText(m.getNome());
        String sub = (m.getDosagem() == null || m.getDosagem().isEmpty())
                ? m.getTipo().toLowerCase(Formatos.PT)
                : m.getDosagem() + " · " + m.getTipo().toLowerCase(Formatos.PT);
        ((TextView) findViewById(R.id.txt_dose_sub)).setText(sub);
        ((TextView) findViewById(R.id.txt_tomar_agora)).setText(m.unidadeDose(l.getQuantidadePorDose()));
        ((TextView) findViewById(R.id.txt_restam)).setText((int) m.getQuantidadeEstoque() + " " + m.unidadeCurta());
        ImageView icone = findViewById(R.id.img_forma);
        icone.setImageResource(m.getForma() == FormaFarmaceutica.LIQUIDA ? R.drawable.ic_drop : R.drawable.ic_pill);
        ((TextView) findViewById(R.id.txt_nota)).setText("Se não tomar, o próximo horário é " + textoProximo(l, dataHora) + ".");
    }

    private void confirmar() {
        boolean zerou = AmparoRepository.get().confirmarDose(ocorrencia);
        String msg = zerou ? "Dose confirmada. O estoque acabou."
                : "Dose confirmada. Estoque: " + (int) ocorrencia.getMedicamento().getQuantidadeEstoque()
                    + " " + ocorrencia.getMedicamento().unidadeCurta();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void naoTomei() {
        AmparoRepository.get().rejeitarDose(ocorrencia);
        Toast.makeText(this, "Registrado. Próximo horário: "
                + textoProximo(ocorrencia.getLembrete(), ocorrencia.getDataHora()), Toast.LENGTH_SHORT).show();
        finish();
    }

    private String textoProximo(Lembrete l, LocalDateTime base) {
        LocalDateTime prox = AmparoRepository.get().proximoHorarioDepois(l, base);
        if (prox == null) return "—";
        String hora = Formatos.hora(prox.toLocalTime());
        LocalDate hoje = LocalDate.now();
        LocalDate d = prox.toLocalDate();
        if (d.isEqual(hoje)) return "hoje às " + hora;
        if (d.isEqual(hoje.plusDays(1))) return "amanhã às " + hora;
        return "em " + d.format(DateTimeFormatter.ofPattern("dd/MM")) + " às " + hora;
    }
}
