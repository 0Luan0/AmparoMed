package com.example.amparomed;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.example.amparomed.ui.EstoqueFragment;
import com.example.amparomed.ui.HistoricoFragment;
import com.example.amparomed.ui.InicioFragment;
import com.example.amparomed.ui.PessoasFragment;
import com.example.amparomed.util.Constantes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Casca das 4 abas. Aberta pela LoginActivity. */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            trocarFragment(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) {
            int aba = getIntent().getIntExtra(Constantes.EXTRA_ABA, R.id.nav_inicio);
            bottomNav.setSelectedItemId(aba);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int aba = intent.getIntExtra(Constantes.EXTRA_ABA, -1);
        if (aba != -1) bottomNav.setSelectedItemId(aba);
    }

    public void irParaAba(int itemId) { bottomNav.setSelectedItemId(itemId); }

    private void trocarFragment(int itemId) {
        Fragment fragment;
        boolean iconesEscuros;
        if (itemId == R.id.nav_historico) { fragment = new HistoricoFragment(); iconesEscuros = true; }
        else if (itemId == R.id.nav_estoque) { fragment = new EstoqueFragment(); iconesEscuros = true; }
        else if (itemId == R.id.nav_pessoas) { fragment = new PessoasFragment(); iconesEscuros = true; }
        else { fragment = new InicioFragment(); iconesEscuros = false; }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragmentos, fragment).commit();

        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(iconesEscuros);
    }
}
