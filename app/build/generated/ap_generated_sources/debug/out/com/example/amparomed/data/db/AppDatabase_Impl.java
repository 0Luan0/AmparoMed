package com.example.amparomed.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MedicamentoDao _medicamentoDao;

  private volatile LembreteDao _lembreteDao;

  private volatile RegistroDoseDao _registroDoseDao;

  private volatile CuidadorDao _cuidadorDao;

  private volatile PacienteDao _pacienteDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `medicamento` (`id` INTEGER NOT NULL, `nome` TEXT, `dosagem` TEXT, `forma` TEXT, `tipo` TEXT, `quantidadeEstoque` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `lembrete` (`id` INTEGER NOT NULL, `medicamentoId` INTEGER NOT NULL, `quantidadePorDose` REAL NOT NULL, `intervaloHoras` INTEGER NOT NULL, `horarioInicial` TEXT, `dataInicio` TEXT, `duracaoDias` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `registro_dose` (`id` INTEGER NOT NULL, `lembreteId` INTEGER NOT NULL, `medicamentoNome` TEXT, `doseTexto` TEXT, `dataHora` TEXT, `tomado` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cuidador` (`id` INTEGER NOT NULL, `nome` TEXT, `idade` INTEGER NOT NULL, `vinculo` TEXT, `parentesco` TEXT, `email` TEXT, `telefone` TEXT, `cpf` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `paciente` (`id` INTEGER NOT NULL, `nome` TEXT, `dataNascimento` TEXT, `cpf` TEXT, `alergias` TEXT, `doencas` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'df3d1e47fb8a75df36073bacea41dd86')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `medicamento`");
        db.execSQL("DROP TABLE IF EXISTS `lembrete`");
        db.execSQL("DROP TABLE IF EXISTS `registro_dose`");
        db.execSQL("DROP TABLE IF EXISTS `cuidador`");
        db.execSQL("DROP TABLE IF EXISTS `paciente`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMedicamento = new HashMap<String, TableInfo.Column>(6);
        _columnsMedicamento.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamento.put("nome", new TableInfo.Column("nome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamento.put("dosagem", new TableInfo.Column("dosagem", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamento.put("forma", new TableInfo.Column("forma", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamento.put("tipo", new TableInfo.Column("tipo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicamento.put("quantidadeEstoque", new TableInfo.Column("quantidadeEstoque", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedicamento = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedicamento = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedicamento = new TableInfo("medicamento", _columnsMedicamento, _foreignKeysMedicamento, _indicesMedicamento);
        final TableInfo _existingMedicamento = TableInfo.read(db, "medicamento");
        if (!_infoMedicamento.equals(_existingMedicamento)) {
          return new RoomOpenHelper.ValidationResult(false, "medicamento(com.example.amparomed.model.Medicamento).\n"
                  + " Expected:\n" + _infoMedicamento + "\n"
                  + " Found:\n" + _existingMedicamento);
        }
        final HashMap<String, TableInfo.Column> _columnsLembrete = new HashMap<String, TableInfo.Column>(7);
        _columnsLembrete.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("medicamentoId", new TableInfo.Column("medicamentoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("quantidadePorDose", new TableInfo.Column("quantidadePorDose", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("intervaloHoras", new TableInfo.Column("intervaloHoras", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("horarioInicial", new TableInfo.Column("horarioInicial", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("dataInicio", new TableInfo.Column("dataInicio", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLembrete.put("duracaoDias", new TableInfo.Column("duracaoDias", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLembrete = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLembrete = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLembrete = new TableInfo("lembrete", _columnsLembrete, _foreignKeysLembrete, _indicesLembrete);
        final TableInfo _existingLembrete = TableInfo.read(db, "lembrete");
        if (!_infoLembrete.equals(_existingLembrete)) {
          return new RoomOpenHelper.ValidationResult(false, "lembrete(com.example.amparomed.model.Lembrete).\n"
                  + " Expected:\n" + _infoLembrete + "\n"
                  + " Found:\n" + _existingLembrete);
        }
        final HashMap<String, TableInfo.Column> _columnsRegistroDose = new HashMap<String, TableInfo.Column>(6);
        _columnsRegistroDose.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroDose.put("lembreteId", new TableInfo.Column("lembreteId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroDose.put("medicamentoNome", new TableInfo.Column("medicamentoNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroDose.put("doseTexto", new TableInfo.Column("doseTexto", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroDose.put("dataHora", new TableInfo.Column("dataHora", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistroDose.put("tomado", new TableInfo.Column("tomado", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegistroDose = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRegistroDose = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRegistroDose = new TableInfo("registro_dose", _columnsRegistroDose, _foreignKeysRegistroDose, _indicesRegistroDose);
        final TableInfo _existingRegistroDose = TableInfo.read(db, "registro_dose");
        if (!_infoRegistroDose.equals(_existingRegistroDose)) {
          return new RoomOpenHelper.ValidationResult(false, "registro_dose(com.example.amparomed.model.RegistroDose).\n"
                  + " Expected:\n" + _infoRegistroDose + "\n"
                  + " Found:\n" + _existingRegistroDose);
        }
        final HashMap<String, TableInfo.Column> _columnsCuidador = new HashMap<String, TableInfo.Column>(8);
        _columnsCuidador.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("nome", new TableInfo.Column("nome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("idade", new TableInfo.Column("idade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("vinculo", new TableInfo.Column("vinculo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("parentesco", new TableInfo.Column("parentesco", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("telefone", new TableInfo.Column("telefone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCuidador.put("cpf", new TableInfo.Column("cpf", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCuidador = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCuidador = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCuidador = new TableInfo("cuidador", _columnsCuidador, _foreignKeysCuidador, _indicesCuidador);
        final TableInfo _existingCuidador = TableInfo.read(db, "cuidador");
        if (!_infoCuidador.equals(_existingCuidador)) {
          return new RoomOpenHelper.ValidationResult(false, "cuidador(com.example.amparomed.model.Cuidador).\n"
                  + " Expected:\n" + _infoCuidador + "\n"
                  + " Found:\n" + _existingCuidador);
        }
        final HashMap<String, TableInfo.Column> _columnsPaciente = new HashMap<String, TableInfo.Column>(6);
        _columnsPaciente.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaciente.put("nome", new TableInfo.Column("nome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaciente.put("dataNascimento", new TableInfo.Column("dataNascimento", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaciente.put("cpf", new TableInfo.Column("cpf", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaciente.put("alergias", new TableInfo.Column("alergias", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaciente.put("doencas", new TableInfo.Column("doencas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPaciente = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPaciente = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPaciente = new TableInfo("paciente", _columnsPaciente, _foreignKeysPaciente, _indicesPaciente);
        final TableInfo _existingPaciente = TableInfo.read(db, "paciente");
        if (!_infoPaciente.equals(_existingPaciente)) {
          return new RoomOpenHelper.ValidationResult(false, "paciente(com.example.amparomed.model.Paciente).\n"
                  + " Expected:\n" + _infoPaciente + "\n"
                  + " Found:\n" + _existingPaciente);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "df3d1e47fb8a75df36073bacea41dd86", "1444ba00bf149c9556b7f6210bf7422d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "medicamento","lembrete","registro_dose","cuidador","paciente");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `medicamento`");
      _db.execSQL("DELETE FROM `lembrete`");
      _db.execSQL("DELETE FROM `registro_dose`");
      _db.execSQL("DELETE FROM `cuidador`");
      _db.execSQL("DELETE FROM `paciente`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MedicamentoDao.class, MedicamentoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LembreteDao.class, LembreteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RegistroDoseDao.class, RegistroDoseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CuidadorDao.class, CuidadorDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PacienteDao.class, PacienteDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MedicamentoDao medicamentoDao() {
    if (_medicamentoDao != null) {
      return _medicamentoDao;
    } else {
      synchronized(this) {
        if(_medicamentoDao == null) {
          _medicamentoDao = new MedicamentoDao_Impl(this);
        }
        return _medicamentoDao;
      }
    }
  }

  @Override
  public LembreteDao lembreteDao() {
    if (_lembreteDao != null) {
      return _lembreteDao;
    } else {
      synchronized(this) {
        if(_lembreteDao == null) {
          _lembreteDao = new LembreteDao_Impl(this);
        }
        return _lembreteDao;
      }
    }
  }

  @Override
  public RegistroDoseDao registroDao() {
    if (_registroDoseDao != null) {
      return _registroDoseDao;
    } else {
      synchronized(this) {
        if(_registroDoseDao == null) {
          _registroDoseDao = new RegistroDoseDao_Impl(this);
        }
        return _registroDoseDao;
      }
    }
  }

  @Override
  public CuidadorDao cuidadorDao() {
    if (_cuidadorDao != null) {
      return _cuidadorDao;
    } else {
      synchronized(this) {
        if(_cuidadorDao == null) {
          _cuidadorDao = new CuidadorDao_Impl(this);
        }
        return _cuidadorDao;
      }
    }
  }

  @Override
  public PacienteDao pacienteDao() {
    if (_pacienteDao != null) {
      return _pacienteDao;
    } else {
      synchronized(this) {
        if(_pacienteDao == null) {
          _pacienteDao = new PacienteDao_Impl(this);
        }
        return _pacienteDao;
      }
    }
  }
}
