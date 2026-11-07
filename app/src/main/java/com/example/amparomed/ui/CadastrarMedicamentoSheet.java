package com.example.amparomed.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.util.Formatos;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

/** Bottom sheet "Cadastrar medicamento" (tela 08). */
public class CadastrarMedicamentoSheet extends BottomSheetDialogFragment {

    public interface OnSalvoListener { void aoSalvar(); }

    private static final String[] TIPOS_SOLIDO = {"Comprimido", "Cápsula", "Drágea"};
    private static final String[] TIPOS_LIQUIDO = {"Xarope", "Gotas", "Solução oral"};

    private OnSalvoListener listener;
    private FormaFarmaceutica forma = FormaFarmaceutica.SOLIDA;
    private double quantidade = 30;

    private TextView segSolida, segLiquida, txtQtd;
    private Spinner spinnerTipo;
    private EditText editNome, editDosagem;
    private MaterialButton btnCadastrar;

    public void setListener(OnSalvoListener listener) { this.listener = listener; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_cadastrar_medicamento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        segSolida = v.findViewById(R.id.seg_solida);
        segLiquida = v.findViewById(R.id.seg_liquida);
        txtQtd = v.findViewById(R.id.txt_qtd_cad);
        spinnerTipo = v.findViewById(R.id.spinner_tipo);
        editNome = v.findViewById(R.id.edit_nome);
        editDosagem = v.findViewById(R.id.edit_dosagem);
        btnCadastrar = v.findViewById(R.id.btn_cadastrar);

        segSolida.setOnClickListener(x -> selecionarForma(FormaFarmaceutica.SOLIDA));
        segLiquida.setOnClickListener(x -> selecionarForma(FormaFarmaceutica.LIQUIDA));

        ImageButton menos = v.findViewById(R.id.btn_qcad_menos);
        ImageButton mais = v.findViewById(R.id.btn_qcad_mais);
        menos.setOnClickListener(x -> { double passo = passoAtual(); quantidade = Math.max(passo, quantidade - passo); atualizarQtd(); });
        mais.setOnClickListener(x -> { quantidade += passoAtual(); atualizarQtd(); });

        editNome.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { btnCadastrar.setEnabled(!s.toString().trim().isEmpty()); }
        });

        v.findViewById(R.id.btn_cancelar_cad).setOnClickListener(x -> dismiss());
        btnCadastrar.setOnClickListener(x -> cadastrar());

        selecionarForma(FormaFarmaceutica.SOLIDA);
    }

    private void selecionarForma(FormaFarmaceutica f) {
        forma = f;
        segSolida.setSelected(f == FormaFarmaceutica.SOLIDA);
        segLiquida.setSelected(f == FormaFarmaceutica.LIQUIDA);
        String[] tipos = (f == FormaFarmaceutica.SOLIDA) ? TIPOS_SOLIDO : TIPOS_LIQUIDO;
        ArrayAdapter<String> ad = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tipos);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(ad);
        quantidade = (f == FormaFarmaceutica.SOLIDA) ? 30 : 100;
        atualizarQtd();
    }

    private void atualizarQtd() {
        String unidade = (forma == FormaFarmaceutica.SOLIDA) ? " unidades" : " ml";
        txtQtd.setText(Formatos.numero(quantidade) + unidade);
    }

    private double passoAtual() { return (forma == FormaFarmaceutica.LIQUIDA) ? 10 : 1; }

    private void cadastrar() {
        String nome = editNome.getText().toString().trim();
        if (nome.isEmpty()) return;
        String dosagem = editDosagem.getText().toString().trim();
        String tipo = (String) spinnerTipo.getSelectedItem();
        AmparoRepository.get().addMedicamento(new Medicamento(0, nome, dosagem, forma, tipo, quantidade));
        if (listener != null) listener.aoSalvar();
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog d = (BottomSheetDialog) getDialog();
        if (d != null) {
            View bs = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bs != null) {
                BottomSheetBehavior<View> b = BottomSheetBehavior.from(bs);
                b.setState(BottomSheetBehavior.STATE_EXPANDED);
                b.setSkipCollapsed(true);
            }
        }
    }
}
