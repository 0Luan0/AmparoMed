package com.example.amparomed.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;

/**
 * Tela de cadastro — APENAS VISUAL. Nao persiste usuario: o botao Cadastrar
 * abre o app (MainActivity) e encerra o fluxo de entrada (login + cadastro).
 */
public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);

        findViewById(R.id.btn_cadastrar).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity(); // fecha login + cadastro; o app abre limpo
        });

        // "Ja tenho conta" volta para o login.
        findViewById(R.id.btn_ja_tenho).setOnClickListener(v -> finish());
    }
}
