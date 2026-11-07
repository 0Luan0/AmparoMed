package com.example.amparomed.ui;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amparomed.R;
import com.example.amparomed.model.Ocorrencia;
import com.example.amparomed.model.StatusDose;
import com.example.amparomed.util.Formatos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Lista de lembretes do dia na tela Inicio, com os 3 estados de dose. */
public class LembreteAdapter extends RecyclerView.Adapter<LembreteAdapter.VH> {

    public interface Acao {
        void aoTocarPendente(Ocorrencia o);
        void aoRemover(Ocorrencia o);
    }

    private final List<Ocorrencia> itens = new ArrayList<>();
    private LocalDateTime proximo;
    private final Acao acao;

    public LembreteAdapter(Acao acao) { this.acao = acao; }

    public void setDados(List<Ocorrencia> novos, LocalDateTime proximo) {
        itens.clear();
        itens.addAll(novos);
        this.proximo = proximo;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lembrete, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Ocorrencia o = itens.get(position);
        String dose = o.getMedicamento().unidadeDose(o.getLembrete().getQuantidadePorDose());
        String hora = Formatos.hora(o.getDataHora().toLocalTime());
        String subBase = dose + " · " + hora;

        h.txtTitulo.setText(o.getMedicamento().nomeCompleto());
        h.txtTitulo.setPaintFlags(h.txtTitulo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        h.pill.setVisibility(View.GONE);

        StatusDose status = o.getStatus();
        if (status == StatusDose.TOMADO) {
            h.raiz.setBackgroundResource(R.drawable.bg_card_tomado);
            h.barra.setBackgroundResource(R.drawable.bg_barra_tomado);
            h.txtTitulo.setPaintFlags(h.txtTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            h.txtTitulo.setTextColor(cor(h, R.color.texto_riscado));
            h.txtSub.setText(subBase);
            h.txtSub.setTextColor(cor(h, R.color.texto_secundario));
            h.btnLixeira.setVisibility(View.GONE);
            h.imgStatus.setVisibility(View.VISIBLE);
            h.imgStatus.setImageResource(R.drawable.ic_check_circle);
            h.imgStatus.setImageTintList(ColorStateList.valueOf(cor(h, R.color.verde_principal)));
        } else if (status == StatusDose.NAO_TOMADO) {
            h.raiz.setBackgroundResource(R.drawable.bg_card);
            h.barra.setBackgroundResource(R.drawable.bg_barra_nao_tomado);
            h.txtTitulo.setTextColor(cor(h, R.color.verde_tinta));
            h.txtSub.setText(comSufixoVermelho(h, subBase, " · não tomado"));
            h.btnLixeira.setVisibility(View.GONE);
            h.imgStatus.setVisibility(View.VISIBLE);
            h.imgStatus.setImageResource(R.drawable.ic_x_circle);
            h.imgStatus.setImageTintList(ColorStateList.valueOf(cor(h, R.color.vermelho)));
        } else {
            h.raiz.setBackgroundResource(R.drawable.bg_card);
            h.barra.setBackgroundResource(R.drawable.bg_barra_pendente);
            h.txtTitulo.setTextColor(cor(h, R.color.verde_tinta));
            h.txtSub.setText(subBase);
            h.txtSub.setTextColor(cor(h, R.color.texto_secundario));
            h.btnLixeira.setVisibility(View.VISIBLE);
            h.imgStatus.setVisibility(View.GONE);
            if (proximo != null && o.getDataHora().isEqual(proximo)) h.pill.setVisibility(View.VISIBLE);
        }

        h.raiz.setOnClickListener(v -> { if (o.getStatus() == StatusDose.PENDENTE) acao.aoTocarPendente(o); });
        h.btnLixeira.setOnClickListener(v -> acao.aoRemover(o));
    }

    @Override
    public int getItemCount() { return itens.size(); }

    private int cor(VH h, int idCor) { return ContextCompat.getColor(h.itemView.getContext(), idCor); }

    private CharSequence comSufixoVermelho(VH h, String base, String sufixo) {
        SpannableStringBuilder sb = new SpannableStringBuilder(base);
        int inicio = sb.length();
        sb.append(sufixo);
        sb.setSpan(new ForegroundColorSpan(cor(h, R.color.vermelho)), inicio, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    static class VH extends RecyclerView.ViewHolder {
        final View raiz, barra;
        final TextView txtTitulo, txtSub, pill;
        final ImageButton btnLixeira;
        final ImageView imgStatus;
        VH(@NonNull View v) {
            super(v);
            raiz = v.findViewById(R.id.raiz_item);
            barra = v.findViewById(R.id.barra);
            txtTitulo = v.findViewById(R.id.txt_titulo);
            txtSub = v.findViewById(R.id.txt_sub);
            pill = v.findViewById(R.id.pill_proximo);
            btnLixeira = v.findViewById(R.id.btn_lixeira);
            imgStatus = v.findViewById(R.id.img_status);
        }
    }
}
