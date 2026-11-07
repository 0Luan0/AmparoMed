package com.example.amparomed.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.amparomed.model.Lembrete;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LembreteDao_Impl implements LembreteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Lembrete> __insertionAdapterOfLembrete;

  private final SharedSQLiteStatement __preparedStmtOfDeletar;

  private final SharedSQLiteStatement __preparedStmtOfDeletarPorMedicamento;

  public LembreteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLembrete = new EntityInsertionAdapter<Lembrete>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `lembrete` (`id`,`medicamentoId`,`quantidadePorDose`,`intervaloHoras`,`horarioInicial`,`dataInicio`,`duracaoDias`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Lembrete entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMedicamentoId());
        statement.bindDouble(3, entity.getQuantidadePorDose());
        statement.bindLong(4, entity.getIntervaloHoras());
        final String _tmp = Conversores.fromLocalTime(entity.getHorarioInicial());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final String _tmp_1 = Conversores.fromLocalDate(entity.getDataInicio());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        if (entity.getDuracaoDias() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDuracaoDias());
        }
      }
    };
    this.__preparedStmtOfDeletar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM lembrete WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeletarPorMedicamento = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM lembrete WHERE medicamentoId = ?";
        return _query;
      }
    };
  }

  @Override
  public void inserir(final Lembrete l) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLembrete.insert(l);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletar(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletar.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletar.release(_stmt);
    }
  }

  @Override
  public void deletarPorMedicamento(final long medId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletarPorMedicamento.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, medId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletarPorMedicamento.release(_stmt);
    }
  }

  @Override
  public List<Lembrete> listar() {
    final String _sql = "SELECT * FROM lembrete";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfMedicamentoId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicamentoId");
      final int _cursorIndexOfQuantidadePorDose = CursorUtil.getColumnIndexOrThrow(_cursor, "quantidadePorDose");
      final int _cursorIndexOfIntervaloHoras = CursorUtil.getColumnIndexOrThrow(_cursor, "intervaloHoras");
      final int _cursorIndexOfHorarioInicial = CursorUtil.getColumnIndexOrThrow(_cursor, "horarioInicial");
      final int _cursorIndexOfDataInicio = CursorUtil.getColumnIndexOrThrow(_cursor, "dataInicio");
      final int _cursorIndexOfDuracaoDias = CursorUtil.getColumnIndexOrThrow(_cursor, "duracaoDias");
      final List<Lembrete> _result = new ArrayList<Lembrete>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Lembrete _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpMedicamentoId;
        _tmpMedicamentoId = _cursor.getLong(_cursorIndexOfMedicamentoId);
        final double _tmpQuantidadePorDose;
        _tmpQuantidadePorDose = _cursor.getDouble(_cursorIndexOfQuantidadePorDose);
        final int _tmpIntervaloHoras;
        _tmpIntervaloHoras = _cursor.getInt(_cursorIndexOfIntervaloHoras);
        final LocalTime _tmpHorarioInicial;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfHorarioInicial)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfHorarioInicial);
        }
        _tmpHorarioInicial = Conversores.toLocalTime(_tmp);
        final LocalDate _tmpDataInicio;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDataInicio)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfDataInicio);
        }
        _tmpDataInicio = Conversores.toLocalDate(_tmp_1);
        final Integer _tmpDuracaoDias;
        if (_cursor.isNull(_cursorIndexOfDuracaoDias)) {
          _tmpDuracaoDias = null;
        } else {
          _tmpDuracaoDias = _cursor.getInt(_cursorIndexOfDuracaoDias);
        }
        _item = new Lembrete(_tmpId,_tmpMedicamentoId,_tmpQuantidadePorDose,_tmpIntervaloHoras,_tmpHorarioInicial,_tmpDataInicio,_tmpDuracaoDias);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
