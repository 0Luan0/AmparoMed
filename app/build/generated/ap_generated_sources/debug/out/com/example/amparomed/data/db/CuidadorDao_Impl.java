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
import com.example.amparomed.model.Cuidador;
import com.example.amparomed.model.Vinculo;
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
public final class CuidadorDao_Impl implements CuidadorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Cuidador> __insertionAdapterOfCuidador;

  private final SharedSQLiteStatement __preparedStmtOfDeletar;

  public CuidadorDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCuidador = new EntityInsertionAdapter<Cuidador>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cuidador` (`id`,`nome`,`idade`,`vinculo`,`parentesco`,`email`,`telefone`,`cpf`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Cuidador entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNome() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNome());
        }
        statement.bindLong(3, entity.getIdade());
        final String _tmp = Conversores.fromVinculo(entity.getVinculo());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getParentesco() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getParentesco());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEmail());
        }
        if (entity.getTelefone() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getTelefone());
        }
        if (entity.getCpf() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCpf());
        }
      }
    };
    this.__preparedStmtOfDeletar = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cuidador WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void inserir(final Cuidador c) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCuidador.insert(c);
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
  public List<Cuidador> listar() {
    final String _sql = "SELECT * FROM cuidador";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "nome");
      final int _cursorIndexOfIdade = CursorUtil.getColumnIndexOrThrow(_cursor, "idade");
      final int _cursorIndexOfVinculo = CursorUtil.getColumnIndexOrThrow(_cursor, "vinculo");
      final int _cursorIndexOfParentesco = CursorUtil.getColumnIndexOrThrow(_cursor, "parentesco");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "telefone");
      final int _cursorIndexOfCpf = CursorUtil.getColumnIndexOrThrow(_cursor, "cpf");
      final List<Cuidador> _result = new ArrayList<Cuidador>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Cuidador _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpNome;
        if (_cursor.isNull(_cursorIndexOfNome)) {
          _tmpNome = null;
        } else {
          _tmpNome = _cursor.getString(_cursorIndexOfNome);
        }
        final int _tmpIdade;
        _tmpIdade = _cursor.getInt(_cursorIndexOfIdade);
        final Vinculo _tmpVinculo;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfVinculo)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfVinculo);
        }
        _tmpVinculo = Conversores.toVinculo(_tmp);
        final String _tmpParentesco;
        if (_cursor.isNull(_cursorIndexOfParentesco)) {
          _tmpParentesco = null;
        } else {
          _tmpParentesco = _cursor.getString(_cursorIndexOfParentesco);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmpTelefone;
        if (_cursor.isNull(_cursorIndexOfTelefone)) {
          _tmpTelefone = null;
        } else {
          _tmpTelefone = _cursor.getString(_cursorIndexOfTelefone);
        }
        final String _tmpCpf;
        if (_cursor.isNull(_cursorIndexOfCpf)) {
          _tmpCpf = null;
        } else {
          _tmpCpf = _cursor.getString(_cursorIndexOfCpf);
        }
        _item = new Cuidador(_tmpId,_tmpNome,_tmpIdade,_tmpVinculo,_tmpParentesco,_tmpEmail,_tmpTelefone,_tmpCpf);
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
