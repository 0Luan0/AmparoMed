package com.example.amparomed.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.amparomed.model.RegistroDose;

import java.util.List;

@Dao
public interface RegistroDoseDao {
    @Query("SELECT * FROM registro_dose")
    List<RegistroDose> listar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(RegistroDose r);

    @Query("DELETE FROM registro_dose WHERE lembreteId = :lembreteId AND dataHora = :dataHoraIso")
    void deletarPorLembreteEData(long lembreteId, String dataHoraIso);
}
