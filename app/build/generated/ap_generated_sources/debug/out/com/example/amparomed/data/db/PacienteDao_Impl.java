package com.example.amparomed.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.amparomed.model.Paciente;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PacienteDao_Impl implements PacienteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Paciente> __insertionAdapterOfPaciente;

  public PacienteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPaciente = new EntityInsertionAdapter<Paciente>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `paciente` (`id`,`nome`,`dataNascimento`,`cpf`,`alergias`,`doencas`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Paciente entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNome() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNome());
        }
        final String _tmp = Conversores.fromLocalDate(entity.getDataNascimento());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        if (entity.getCpf() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCpf());
        }
        final String _tmp_1 = Conversores.fromLista(entity.getAlergias());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = Conversores.fromLista(entity.getDoencas());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
      }
    };
  }

  @Override
  public void inserir(final Paciente p) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPaciente.insert(p);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Paciente obter() {
    final String _sql = "SELECT * FROM paciente WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "nome");
      final int _cursorIndexOfDataNascimento = CursorUtil.getColumnIndexOrThrow(_cursor, "dataNascimento");
      final int _cursorIndexOfCpf = CursorUtil.getColumnIndexOrThrow(_cursor, "cpf");
      final int _cursorIndexOfAlergias = CursorUtil.getColumnIndexOrThrow(_cursor, "alergias");
      final int _cursorIndexOfDoencas = CursorUtil.getColumnIndexOrThrow(_cursor, "doencas");
      final Paciente _result;
      if (_cursor.moveToFirst()) {
        final String _tmpNome;
        if (_cursor.isNull(_cursorIndexOfNome)) {
          _tmpNome = null;
        } else {
          _tmpNome = _cursor.getString(_cursorIndexOfNome);
        }
        final LocalDate _tmpDataNascimento;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfDataNascimento)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfDataNascimento);
        }
        _tmpDataNascimento = Conversores.toLocalDate(_tmp);
        final String _tmpCpf;
        if (_cursor.isNull(_cursorIndexOfCpf)) {
          _tmpCpf = null;
        } else {
          _tmpCpf = _cursor.getString(_cursorIndexOfCpf);
        }
        final List<String> _tmpAlergias;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfAlergias)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfAlergias);
        }
        _tmpAlergias = Conversores.toLista(_tmp_1);
        final List<String> _tmpDoencas;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfDoencas)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfDoencas);
        }
        _tmpDoencas = Conversores.toLista(_tmp_2);
        _result = new Paciente(_tmpNome,_tmpDataNascimento,_tmpCpf,_tmpAlergias,_tmpDoencas);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
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
