package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.utils;

//ESta clase es para Los datos en firebase
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProjectDatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 1;


    // Nombre de la tabla y columnas
// Nombre de la tabla y columnas
    public static final String TABLE_PROJECTS = "projects";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_START_DATE = "startDate"; //se debe ajustar desde el DAO manejando las fechas como texto en formato ISO 8601 (yyyy-MM-dd). YA QUE SLLITE NO TIENE TIPO DATE
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_STATUS = "status";

    // Constructor
    public ProjectDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_PROJECTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +  // Espacio añadido
                COLUMN_END_DATE + " TEXT, " +    // Espacio añadido
                COLUMN_STATUS + " TEXT" +        // Espacio añadido
                ");";
        db.execSQL(createTableSQL);
        Log.d("DataBaseHelper", "Tabla 'products' creada correctamente");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        onCreate(db);
    }




}
