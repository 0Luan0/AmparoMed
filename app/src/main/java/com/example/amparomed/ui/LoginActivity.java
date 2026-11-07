package com.example.amparomed.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.amparomed.MainActivity;
import com.example.amparomed.R;

/**
 * Tela de login — APENAS VISUAL. Nao ha autenticacao: o botao Entrar
 * simplesmente abre o app (MainActivity) e encerra a tela de login, para
 * que o botao "voltar" nao retorne a ela.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true); // fundo claro -> icones escuros

        findViewById(R.id.btn_entrar).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // "Criar conta" abre a tela de cadastro.
        findViewById(R.id.btn_criar_conta).setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class)));
    }
}
