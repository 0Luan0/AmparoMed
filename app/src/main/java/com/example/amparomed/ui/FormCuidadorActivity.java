package com.example.amparomed.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Vinculo;
import com.example.amparomed.util.Constantes;
import com.example.amparomed.util.InsetsHelper;
import com.google.android.material.button.MaterialButton;

/** Cadastro/edicao de cuidador (tela 12). */
public class FormCuidadorActivity extends AppCompatActivity {

    private long cuidadorId = -1;
    private Vinculo vinculo = Vinculo.FAMILIAR;

    private TextView segFamiliar, segCuidador;
    private View grupoParentesco;
    private EditText editNome, editParentesco, editIdade, editCpf, editTel, editEmail;
    private MaterialButton btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cuidador);
        InsetsHelper.aplicarTopo(findViewById(R.id.barra_topo));
        InsetsHelper.aplicarBase(findViewById(R.id.rodape_form));
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        segFamiliar = findViewById(R.id.seg_familiar);
        segCuidador = findViewById(R.id.seg_cuidador);
        grupoParentesco = findViewById(R.id.grupo_parentesco);
        editNome = findViewById(R.id.edit_nome_cuid);
        editParentesco = findViewById(R.id.edit_parentesco);
        editIdade = findViewById(R.id.edit_idade);
        editCpf = findViewById(R.id.edit_cpf);
        editTel = findViewById(R.id.edit_tel);
        editEmail = findViewById(R.id.edit_email);
        btnSalvar = findViewById(R.id.btn_salvar_cuid);

        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
        segFamiliar.setOnClickListener(v -> selecionarVinculo(Vinculo.FAMILIAR));
        segCuidador.setOnClickListener(v -> selecionarVinculo(Vinculo.CUIDADOR));

        TextWatcher validador = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { validar(); }
        };
        editNome.addTextChangedListener(validador);
        editTel.addTextChangedListener(validador);

        cuidadorId = getIntent().getLongExtra(Constantes.EXTRA_CUIDADOR_ID, -1);
        Cuidador existente = (cuidadorId != -1) ? AmparoRepository.get().getCuidador(cuidadorId) : null;
        if (existente != null) {
            ((TextView) findViewById(R.id.txt_titulo_topo)).setText("Editar cuidador");
            btnSalvar.setText("Salvar alterações");
            editNome.setText(existente.getNome());
            editParentesco.setText(existente.getParentesco());
            editIdade.setText(String.valueOf(existente.getIdade()));
            editCpf.setText(existente.getCpf());
            editTel.setText(existente.getTelefone());
            editEmail.setText(existente.getEmail());
            selecionarVinculo(existente.getVinculo());
        } else {
            ((TextView) findViewById(R.id.txt_titulo_topo)).setText("Novo familiar ou cuidador");
            btnSalvar.setText("Cadastrar");
            selecionarVinculo(Vinculo.FAMILIAR);
        }

        btnSalvar.setOnClickListener(v -> salvar());
        validar();
    }

    private void selecionarVinculo(Vinculo v) {
        vinculo = v;
        segFamiliar.setSelected(v == Vinculo.FAMILIAR);
        segCuidador.setSelected(v == Vinculo.CUIDADOR);
        grupoParentesco.setVisibility(v == Vinculo.FAMILIAR ? View.VISIBLE : View.GONE);
    }

    private void validar() {
        boolean ok = !editNome.getText().toString().trim().isEmpty()
                && !editTel.getText().toString().trim().isEmpty();
        btnSalvar.setEnabled(ok);
    }

    private void salvar() {
        String nome = editNome.getText().toString().trim();
        String tel = editTel.getText().toString().trim();
        if (nome.isEmpty() || tel.isEmpty()) return;
        int idade = 0;
        try {
            String i = editIdade.getText().toString().trim();
            if (!i.isEmpty()) idade = Integer.parseInt(i);
        } catch (NumberFormatException ignored) { }
        String parentesco = (vinculo == Vinculo.FAMILIAR) ? editParentesco.getText().toString().trim() : "";
        String email = editEmail.getText().toString().trim();
        String cpf = editCpf.getText().toString().trim();

        AmparoRepository repo = AmparoRepository.get();
        if (cuidadorId == -1) repo.addCuidador(new Cuidador(0, nome, idade, vinculo, parentesco, email, tel, cpf));
        else repo.atualizarCuidador(new Cuidador(cuidadorId, nome, idade, vinculo, parentesco, email, tel, cpf));
        Toast.makeText(this, "Contato salvo", Toast.LENGTH_SHORT).show();
        finish();
    }
}
