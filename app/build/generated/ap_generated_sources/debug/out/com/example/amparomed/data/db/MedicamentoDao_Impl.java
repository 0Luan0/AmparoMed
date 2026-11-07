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
import com.example.amparomed.model.FormaFarmaceutica;
import com.example.amparomed.model.Medicamento;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MedicamentoDao_Impl implements MedicamentoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Medicamento> __insertionAdapterOfMedicamento;

  private final SharedSQLiteStatement __preparedStmtOfDeletar;

  public MedicamentoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicamento = new EntityInsertionAdapter<Medicamento>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medicamento` (`id`,`nome`,`dosagem`,`forma`,`tipo`,`quantidadeEstoque`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Medicamento entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNome() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNome());
        }
        if (entity.getDosagem() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDosagem());
        }
        final String _tmp = Conversores.fromForma(entity.getForma());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getTipo() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTipo());
        }
        statement.bindDouble(6, entity.getQuantidadeEstoque());
      }
    };
    this.__preparedStmtOfDeletar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medicamento WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void inserir(final Medicamento m) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMedicamento.insert(m);
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
  public List<Medicamento> listar() {
    final String _sql = "SELECT * FROM medicamento";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "nome");
      final int _cursorIndexOfDosagem = CursorUtil.getColumnIndexOrThrow(_cursor, "dosagem");
      final int _cursorIndexOfForma = CursorUtil.getColumnIndexOrThrow(_cursor, "forma");
      final int _cursorIndexOfTipo = CursorUtil.getColumnIndexOrThrow(_cursor, "tipo");
      final int _cursorIndexOfQuantidadeEstoque = CursorUtil.getColumnIndexOrThrow(_cursor, "quantidadeEstoque");
      final List<Medicamento> _result = new ArrayList<Medicamento>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Medicamento _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpNome;
        if (_cursor.isNull(_cursorIndexOfNome)) {
          _tmpNome = null;
        } else {
          _tmpNome = _cursor.getString(_cursorIndexOfNome);
        }
        final String _tmpDosagem;
        if (_cursor.isNull(_cursorIndexOfDosagem)) {
          _tmpDosagem = null;
        } else {
          _tmpDosagem = _cursor.getString(_cursorIndexOfDosagem);
        }
        final FormaFarmaceutica _tmpForma;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfForma)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfForma);
        }
        _tmpForma = Conversores.toForma(_tmp);
        final String _tmpTipo;
        if (_cursor.isNull(_cursorIndexOfTipo)) {
          _tmpTipo = null;
        } else {
          _tmpTipo = _cursor.getString(_cursorIndexOfTipo);
        }
        final double _tmpQuantidadeEstoque;
        _tmpQuantidadeEstoque = _cursor.getDouble(_cursorIndexOfQuantidadeEstoque);
        _item = new Medicamento(_tmpId,_tmpNome,_tmpDosagem,_tmpForma,_tmpTipo,_tmpQuantidadeEstoque);
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
