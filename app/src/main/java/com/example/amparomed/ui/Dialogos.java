package com.example.amparomed.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.amparomed.R;
import com.example.amparomed.data.AmparoRepository;
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Medicamento;
import com.example.amparomed.util.Formatos;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/** Dialogos reutilizaveis (repor estoque, confirmar remocao, ligacao simulada). */
public final class Dialogos {

    private Dialogos() { }

    public static void mostrarRepor(Context ctx, Medicamento m, Runnable aoConcluir) {
        boolean liquido = m.getForma() == FormaFarmaceutica.LIQUIDA;
        final double passo = liquido ? 10 : 1;
        final double[] valor = { liquido ? 100 : 30 };

        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_repor_estoque, null);
        ((TextView) v.findViewById(R.id.txt_repor_titulo)).setText("Repor " + m.nomeCompleto());
        ((TextView) v.findViewById(R.id.txt_repor_pergunta)).setText(
                liquido ? "Quantos ml chegaram?" : "Quantos comprimidos chegaram?");

        TextView txtQtd = v.findViewById(R.id.txt_repor_qtd);
        TextView txtAtuais = v.findViewById(R.id.txt_repor_atuais);
        TextView txtTotal = v.findViewById(R.id.txt_repor_total);

        Runnable atualiza = () -> {
            txtQtd.setText("+ " + Formatos.numero(valor[0]));
            txtAtuais.setText((int) m.getQuantidadeEstoque() + " atuais → novo total");
            txtTotal.setText((int) (m.getQuantidadeEstoque() + valor[0]) + " " + m.unidadeCurta());
        };
        atualiza.run();

        AlertDialog d = new MaterialAlertDialogBuilder(ctx).setView(v).create();
        if (d.getWindow() != null) d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        v.findViewById(R.id.btn_repor_menos).setOnClickListener(x -> { valor[0] = Math.max(passo, valor[0] - passo); atualiza.run(); });
        v.findViewById(R.id.btn_repor_mais).setOnClickListener(x -> { valor[0] += passo; atualiza.run(); });
        v.findViewById(R.id.btn_repor_cancelar).setOnClickListener(x -> d.dismiss());
        v.findViewById(R.id.btn_repor_ok).setOnClickListener(x -> {
            AmparoRepository.get().reporEstoque(m.getId(), valor[0]);
            d.dismiss();
            Toast.makeText(ctx, "Estoque atualizado: " + (int) m.getQuantidadeEstoque() + " " + m.unidadeCurta(),
                    Toast.LENGTH_SHORT).show();
            if (aoConcluir != null) aoConcluir.run();
        });
        d.show();
    }

    public static void confirmarRemocao(Context ctx, String titulo, String nome, String sub,
                                        String corpo, Runnable aoRemover) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_remover_lembrete, null);
        ((TextView) v.findViewById(R.id.txt_dlg_titulo)).setText(titulo);
        ((TextView) v.findViewById(R.id.txt_dlg_nome)).setText(nome);
        ((TextView) v.findViewById(R.id.txt_dlg_sub)).setText(sub);
        ((TextView) v.findViewById(R.id.txt_dlg_corpo)).setText(corpo);

        AlertDialog d = new MaterialAlertDialogBuilder(ctx).setView(v).create();
        if (d.getWindow() != null) d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        v.findViewById(R.id.btn_cancelar).setOnClickListener(x -> d.dismiss());
        v.findViewById(R.id.btn_remover).setOnClickListener(x -> { d.dismiss(); if (aoRemover != null) aoRemover.run(); });
        d.show();
    }

    public static void mostrarLigacao(Context ctx, String nome, String numero, boolean emergencia) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_ligacao_simulada, null);
        ((TextView) v.findViewById(R.id.txt_lig_titulo)).setText(
                emergencia ? "Chamando " + nome + " " + numero : "Ligando para " + nome);
        ((TextView) v.findViewById(R.id.txt_lig_numero)).setText(numero);

        View nucleo = v.findViewById(R.id.circulo_ligacao);
        nucleo.setBackgroundResource(emergencia ? R.drawable.bg_nucleo_emergencia : R.drawable.bg_oval_verde_claro);
        ((ImageView) v.findViewById(R.id.img_lig_fone)).setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(ctx, emergencia ? R.color.branco : R.color.verde_principal)));

        AlertDialog d = new MaterialAlertDialogBuilder(ctx).setView(v).create();
        if (d.getWindow() != null) d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        v.findViewById(R.id.btn_encerrar).setOnClickListener(x -> d.dismiss());
        d.show();
    }
}
