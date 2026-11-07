package com.example.amparomed;

import android.app.Application;

import com.example.amparomed.data.AmparoRepository;

/**
 * Classe Application: roda antes de qualquer tela. Inicializa o repositorio
 * com o banco de dados (Room), carregando ou semeando os dados.
 */
public class AmparoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AmparoRepository.init(this);
    }
}
