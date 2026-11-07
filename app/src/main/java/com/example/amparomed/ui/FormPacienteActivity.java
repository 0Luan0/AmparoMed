package com.example.amparomed.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.Paciente;
import com.example.amparomed.util.InsetsHelper;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Edicao do paciente. Idade sempre calculada, nunca digitada. */
public class FormPacienteActivity extends AppCompatActivity {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate nascimento;
    private TextView txtNasc, txtIdade;
    private EditText editNome, editCpf, editAlergias, editDoencas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_paciente);
        InsetsHelper.aplicarTopo(findViewById(R.id.barra_topo));
        InsetsHelper.aplicarBase(findViewById(R.id.rodape_form_pac));
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);
        ((TextView) findViewById(R.id.txt_titulo_topo)).setText("Editar paciente");

        txtNasc = findViewById(R.id.txt_nascimento_form);
        txtIdade = findViewById(R.id.txt_idade_form);
        editNome = findViewById(R.id.edit_nome_pac);
        editCpf = findViewById(R.id.edit_cpf_pac);
        editAlergias = findViewById(R.id.edit_alergias);
        editDoencas = findViewById(R.id.edit_doencas);

        Paciente p = AmparoRepository.get().getPaciente();
        nascimento = p.getDataNascimento();
        editNome.setText(p.getNome());
        editCpf.setText(p.getCpf());
        editAlergias.setText(juntar(p.getAlergias()));
        editDoencas.setText(juntar(p.getDoencas()));
        atualizarDataIdade();

        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.campo_nascimento_form).setOnClickListener(v -> abrirCalendario());
        findViewById(R.id.btn_salvar_pac).setOnClickListener(v -> salvar());
    }

    private void abrirCalendario() {
        DatePickerDialog d = new DatePickerDialog(this, (view, ano, mes, dia) -> {
            nascimento = LocalDate.of(ano, mes + 1, dia);
            atualizarDataIdade();
        }, nascimento.getYear(), nascimento.getMonthValue() - 1, nascimento.getDayOfMonth());
        d.show();
    }

    private void atualizarDataIdade() {
        txtNasc.setText(nascimento.format(DATA));
        txtIdade.setText(Period.between(nascimento, LocalDate.now()).getYears() + " anos");
    }

    private void salvar() {
        Paciente p = AmparoRepository.get().getPaciente();
        p.setNome(editNome.getText().toString().trim());
        p.setDataNascimento(nascimento);
        p.setCpf(editCpf.getText().toString().trim());
        p.setAlergias(separar(editAlergias.getText().toString()));
        p.setDoencas(separar(editDoencas.getText().toString()));
        AmparoRepository.get().setPaciente(p);
        Toast.makeText(this, "Paciente atualizado", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String juntar(List<String> itens) { return String.join(", ", itens); }

    private List<String> separar(String texto) {
        List<String> res = new ArrayList<>();
        for (String parte : texto.split(",")) {
            String t = parte.trim();
            if (!t.isEmpty()) res.add(t);
        }
        return res;
    }
}
