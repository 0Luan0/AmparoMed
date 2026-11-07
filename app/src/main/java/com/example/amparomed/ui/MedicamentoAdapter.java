package com.example.amparomed.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.R;
import com.example.amparomed.data.EstoqueCalculadora;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.util.Formatos;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/** Lista do estoque, com barra de duracao e estado "baixo" em terracota. */
public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.VH> {

    public interface Acao {
        void aoRepor(Medicamento m);
        void aoRemover(Medicamento m);
    }

    private final List<Medicamento> itens = new ArrayList<>();
    private final Acao acao;

    public MedicamentoAdapter(Acao acao) { this.acao = acao; }

    public void setDados(List<Medicamento> novos) {
        itens.clear();
        itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estoque, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Medicamento m = itens.get(position);
        boolean liquido = m.getForma() == FormaFarmaceutica.LIQUIDA;
        boolean baixo = EstoqueCalculadora.estoqueBaixo(m);

        h.frameForma.setBackgroundResource(liquido ? R.drawable.bg_chip_pessego : R.drawable.bg_chip_roxo);
        h.imgForma.setImageResource(liquido ? R.drawable.ic_drop : R.drawable.ic_pill);
        h.imgForma.setImageTintList(ColorStateList.valueOf(cor(h, liquido ? R.color.pessego : R.color.roxo)));

        h.txtNome.setText(m.nomeCompleto());
        h.txtForma.setText(descricaoForma(m, liquido));
        h.txtQtd.setText((int) m.getQuantidadeEstoque() + " " + m.unidadeCurta());

        int corTexto = cor(h, baixo ? R.color.terracota : R.color.verde_tinta);
        h.txtNome.setTextColor(corTexto);
        h.txtQtd.setTextColor(corTexto);
        h.imgAlerta.setVisibility(baixo ? View.VISIBLE : View.GONE);

        h.barra.setProgressDrawable(ContextCompat.getDrawable(h.itemView.getContext(),
                baixo ? R.drawable.progress_estoque_baixo : R.drawable.progress_estoque_ok));
        h.barra.setProgress(EstoqueCalculadora.percentualBarra(m));

        h.txtDuracao.setText(EstoqueCalculadora.textoDuracao(m));

        if (baixo) {
            h.btnRepor.setBackgroundTintList(ColorStateList.valueOf(cor(h, R.color.verde_principal)));
            h.btnRepor.setTextColor(cor(h, R.color.branco));
            h.btnRepor.setIconTint(ColorStateList.valueOf(cor(h, R.color.branco)));
        } else {
            h.btnRepor.setBackgroundTintList(ColorStateList.valueOf(cor(h, R.color.branco)));
            h.btnRepor.setTextColor(cor(h, R.color.verde_principal));
            h.btnRepor.setIconTint(ColorStateList.valueOf(cor(h, R.color.verde_principal)));
        }

        h.btnRepor.setOnClickListener(v -> acao.aoRepor(m));
        h.btnLixeira.setOnClickListener(v -> acao.aoRemover(m));
    }

    @Override
    public int getItemCount() { return itens.size(); }

    private String descricaoForma(Medicamento m, boolean liquido) {
        String base = liquido ? "Líquido" : m.getTipo();
        double consumo = EstoqueCalculadora.consumoDiario(m);
        if (consumo <= 0) return base;
        String porDia = liquido ? Formatos.numero(consumo) + " ml por dia" : Formatos.numero(consumo) + " por dia";
        return base + " · " + porDia;
    }

    private int cor(VH h, int idCor) { return ContextCompat.getColor(h.itemView.getContext(), idCor); }

    static class VH extends RecyclerView.ViewHolder {
        final View frameForma;
        final ImageView imgForma, imgAlerta;
        final TextView txtNome, txtForma, txtQtd, txtDuracao;
        final ProgressBar barra;
        final ImageButton btnLixeira;
        final MaterialButton btnRepor;
        VH(@NonNull View v) {
            super(v);
            frameForma = v.findViewById(R.id.frame_forma);
            imgForma = v.findViewById(R.id.img_forma);
            txtNome = v.findViewById(R.id.txt_nome);
            txtForma = v.findViewById(R.id.txt_forma);
            txtQtd = v.findViewById(R.id.txt_qtd);
            imgAlerta = v.findViewById(R.id.img_alerta);
            barra = v.findViewById(R.id.barra_progresso);
            txtDuracao = v.findViewById(R.id.txt_duracao);
            btnLixeira = v.findViewById(R.id.btn_lixeira);
            btnRepor = v.findViewById(R.id.btn_repor);
        }
    }
}
