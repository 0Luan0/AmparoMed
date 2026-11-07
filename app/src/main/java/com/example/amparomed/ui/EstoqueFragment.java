package com.example.amparomed.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.data.EstoqueCalculadora;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.util.InsetsHelper;

import java.util.ArrayList;
import java.util.List;

/** Aba Estoque (tela 07). */
public class EstoqueFragment extends Fragment
        implements MedicamentoAdapter.Acao, CadastrarMedicamentoSheet.OnSalvoListener {

    private MedicamentoAdapter adapter;
    private TextView txtContagem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estoque, container, false);
        InsetsHelper.aplicarTopo(v.findViewById(R.id.barra_topo));
        ((TextView) v.findViewById(R.id.txt_titulo_topo)).setText("Estoque");
        v.findViewById(R.id.btn_voltar).setOnClickListener(x ->
                ((MainActivity) requireActivity()).irParaAba(R.id.nav_inicio));

        txtContagem = v.findViewById(R.id.txt_contagem);
        RecyclerView lista = v.findViewById(R.id.lista_estoque);
        adapter = new MedicamentoAdapter(this);
        lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        lista.setAdapter(adapter);

        v.findViewById(R.id.btn_adicionar_med).setOnClickListener(x -> {
            CadastrarMedicamentoSheet sheet = new CadastrarMedicamentoSheet();
            sheet.setListener(this);
            sheet.show(getChildFragmentManager(), "cadastrar_med");
        });
        return v;
    }

    @Override
    public void onResume() { super.onResume(); recarregar(); }

    private void recarregar() {
        List<Medicamento> meds = new ArrayList<>(AmparoRepository.get().getMedicamentos());
        meds.sort((a, b) -> {
            Double da = EstoqueCalculadora.diasRestantes(a);
            Double db = EstoqueCalculadora.diasRestantes(b);
            double va = (da == null) ? Double.MAX_VALUE : da;
            double vb = (db == null) ? Double.MAX_VALUE : db;
            return Double.compare(va, vb);
        });
        txtContagem.setText(meds.size() + (meds.size() == 1 ? " MEDICAMENTO" : " MEDICAMENTOS"));
        adapter.setDados(meds);
    }

    @Override
    public void aoRepor(Medicamento m) { Dialogos.mostrarRepor(requireContext(), m, this::recarregar); }

    @Override
    public void aoRemover(Medicamento m) {
        Dialogos.confirmarRemocao(requireContext(),
                "Remover " + m.nomeCompleto() + "?", m.nomeCompleto(),
                EstoqueCalculadora.textoDuracao(m),
                "Os lembretes deste remédio também serão removidos.",
                () -> {
                    AmparoRepository.get().removerMedicamento(m.getId());
                    recarregar();
                    Toast.makeText(requireContext(), "Medicamento removido", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void aoSalvar() { recarregar(); }
}
