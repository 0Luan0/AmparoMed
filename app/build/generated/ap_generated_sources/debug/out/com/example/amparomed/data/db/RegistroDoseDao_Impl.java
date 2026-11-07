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
import com.example.amparomed.model.RegistroDose;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RegistroDoseDao_Impl implements RegistroDoseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RegistroDose> __insertionAdapterOfRegistroDose;

  private final SharedSQLiteStatement __preparedStmtOfDeletarPorLembreteEData;

  public RegistroDoseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRegistroDose = new EntityInsertionAdapter<RegistroDose>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `registro_dose` (`id`,`lembreteId`,`medicamentoNome`,`doseTexto`,`dataHora`,`tomado`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RegistroDose entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getLembreteId());
        if (entity.getMedicamentoNome() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMedicamentoNome());
        }
        if (entity.getDoseTexto() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDoseTexto());
        }
        final String _tmp = Conversores.fromLocalDateTime(entity.getDataHora());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        final int _tmp_1 = entity.isTomado() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
      }
    };
    this.__preparedStmtOfDeletarPorLembreteEData = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM registro_dose WHERE lembreteId = ? AND dataHora = ?";
        return _query;
      }
    };
  }

  @Override
  public void inserir(final RegistroDose r) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRegistroDose.insert(r);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deletarPorLembreteEData(final long lembreteId, final String dataHoraIso) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletarPorLembreteEData.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, lembreteId);
    _argIndex = 2;
    if (dataHoraIso == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, dataHoraIso);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletarPorLembreteEData.release(_stmt);
    }
  }

  @Override
  public List<RegistroDose> listar() {
    final String _sql = "SELECT * FROM registro_dose";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLembreteId = CursorUtil.getColumnIndexOrThrow(_cursor, "lembreteId");
      final int _cursorIndexOfMedicamentoNome = CursorUtil.getColumnIndexOrThrow(_cursor, "medicamentoNome");
      final int _cursorIndexOfDoseTexto = CursorUtil.getColumnIndexOrThrow(_cursor, "doseTexto");
      final int _cursorIndexOfDataHora = CursorUtil.getColumnIndexOrThrow(_cursor, "dataHora");
      final int _cursorIndexOfTomado = CursorUtil.getColumnIndexOrThrow(_cursor, "tomado");
      final List<RegistroDose> _result = new ArrayList<RegistroDose>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RegistroDose _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpLembreteId;
        _tmpLembreteId = _cursor.getLong(_cursorIndexOfLembreteId);
        final String _tmpMedicamentoNome;
        if (_cursor.isNull(_cursorIndexOfMedicamentoNome)) {
          _tmpMedicamentoNome = null;
        } else {
          _tmpMedicamentoNome = _cursor.getString(_cursorIndexOfMedicamentoNome);
        }
        final String _tmpDoseTexto;
        if (_cursor.isNull(_cursorIndexOfDoseTexto)) {
          _tmpDoseTexto = null;
        } else {
          _tmpDoseTexto = _cursor.getString(_cursorIndexOfDoseTexto);
        }
        final LocalDateTime _tmpDataHora;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfDataHora)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfDataHora);
        }
        _tmpDataHora = Conversores.toLocalDateTime(_tmp);
        final boolean _tmpTomado;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfTomado);
        _tmpTomado = _tmp_1 != 0;
        _item = new RegistroDose(_tmpId,_tmpLembreteId,_tmpMedicamentoNome,_tmpDoseTexto,_tmpDataHora,_tmpTomado);
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
