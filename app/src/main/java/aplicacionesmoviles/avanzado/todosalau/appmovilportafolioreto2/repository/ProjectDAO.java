package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.utils.ProjectDatabaseHelper;

public class ProjectDAO {

    private SQLiteDatabase db;// Objeto para interactuar con la base de datos
    private ProjectDatabaseHelper dbHelper;// Instancia de DatabaseHelper para crear y actualizar la base de datos

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    // Constructor que recibe el contexto de la aplicación y crea una instancia de DatabaseHelper
    public ProjectDAO(Context context) {
        dbHelper = new ProjectDatabaseHelper(context);

    }

    // Método para abrir la conexión con la base de datos en modo escritura
    public void open() {
        db = dbHelper.getWritableDatabase();

    }

    // Método para cerrar la conexión con la base de datos
    public void close() {
        dbHelper.close();
    }


    // Método para agregar un producto a la base de datos
    public long addProject(Project project) {
        long insertedId = -1; // Valor por defecto si ocurre un error
        // Verificar si el producto ya existe antes de agregarlo
        /*
        if (projectExists(project.getId())) { Siempre lelga nulo
            Log.i("ProjectDatabaseHelper", "Project con ID " + project.getId() + " ya existe.");
            return; // Detener la ejecución si el project ya existe
        }*/
        try {
            if (db == null) {
                this.open(); // Asegurar que la base de datos esté abierta
            }
            ContentValues values = new ContentValues();
            values.put(ProjectDatabaseHelper.COLUMN_NAME, project.getName());
            values.put(ProjectDatabaseHelper.COLUMN_DESCRIPTION, project.getDescription());
            values.put(ProjectDatabaseHelper.COLUMN_START_DATE, dateToString(project.getStartDate()));
            values.put(ProjectDatabaseHelper.COLUMN_END_DATE, dateToString(project.getEndDate()));
            values.put(ProjectDatabaseHelper.COLUMN_STATUS, project.getStatus());

            insertedId =  db.insertOrThrow(ProjectDatabaseHelper.TABLE_PROJECTS, null, values);
        } catch (Exception e) {
            Log.e("ProductDatabaseHelper", "Error al agregar producto", e);

        }
        return insertedId;
    }


    // Método para obtener todos los productos de la base de datos
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        try {
            if (db == null) {
                this.open();
            }
            Cursor cursor = db.query(ProjectDatabaseHelper.TABLE_PROJECTS, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
// Índices de columnas
                int idIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_NAME);
                int descriptionIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_DESCRIPTION);
                int startDateIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_START_DATE);
                int endDateIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_END_DATE);
                int statusIndex = cursor.getColumnIndex(ProjectDatabaseHelper.COLUMN_STATUS);

                if (idIndex != -1 && nameIndex != -1 && startDateIndex != -1 && endDateIndex != -1 && statusIndex != -1) {
                    do {
// Obtener datos de cada producto
                        String id = cursor.getString(idIndex);
                        String name = cursor.getString(nameIndex);
                        String description = cursor.getString(descriptionIndex);
                        Date startDate = stringToDate(cursor.getString(startDateIndex));
                        Date endDate = stringToDate(cursor.getString(endDateIndex));

                        String status = cursor.getString(statusIndex);


                        projects.add(new Project(id, name, description, startDate, endDate, status));
                    } while (cursor.moveToNext());
                } else {
                    Log.e("ProjectDAO", "Columnas no encontradas.");
                }
            }
// Cerrar el cursor y la base de datos
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("SQLite", "Error al obtener todos los productos: " + e.getMessage());
        }
        return projects;
    }

    // Método para actualizar un producto en la base de datos
    public int updateProject(Project project) {
        int rowsAffected = -1; // Valor por defecto
        try {
            if (db == null) {
                this.open();
            }

            ContentValues values = new ContentValues();
            values.put(ProjectDatabaseHelper.COLUMN_ID, project.getId());
            values.put(ProjectDatabaseHelper.COLUMN_NAME, project.getName());
            values.put(ProjectDatabaseHelper.COLUMN_DESCRIPTION, project.getDescription());
            values.put(ProjectDatabaseHelper.COLUMN_START_DATE, project.getStringStartDate());
            values.put(ProjectDatabaseHelper.COLUMN_END_DATE, project.getStringEndDate());
            values.put(ProjectDatabaseHelper.COLUMN_STATUS, project.getStatus());

            // Actualizar el producto en la base de datos
            rowsAffected = db.update(ProjectDatabaseHelper.TABLE_PROJECTS, values, ProjectDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(project.getId())});


            if (rowsAffected == 0) {
                Log.e("ProjectDAO", "No se actualizó ninguna fila.ID de producto inválido.");
            }
        } catch (SQLException e) {
            Log.e("SQLite", "Error al actualizar producto: " + e.getMessage());
        }
        return rowsAffected;
    }


    // Método para eliminar un producto de la base de datos
    public int deleteProject(String id) {
        int rowsDeleted = -1; // Valor por defecto

        try {
            if (db == null) {
                this.open();
            }

            // Eliminar el producto
            rowsDeleted = db.delete(ProjectDatabaseHelper.TABLE_PROJECTS, ProjectDatabaseHelper.COLUMN_ID + "=?", new String[]{id});
            // Verificar si la eliminación fue exitosa
            if (rowsDeleted > 0) {
                Log.i("ProjectDAO", "Project eliminado exitosamente.");
            } else {
                Log.e("ProjectDAO", "No se eliminó ningún Project.ID no válido:" + id);
            }
        } catch (Exception e) {
            Log.e("ProjectDAO", "Error al eliminar el Project", e);
        }
        return rowsDeleted;
    }


    // Método para verificar si un producto existe en la base de datos
    public boolean projectExists(String id) {
        boolean exists = false;
        try {
            if (db == null) {
                this.open();
            }

            // Consulta para verificar si el producto existe
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + ProjectDatabaseHelper.TABLE_PROJECTS + " WHERE " + ProjectDatabaseHelper.COLUMN_ID + "=?", new String[]{id});
            // Si el cursor tiene datos, obtener el conteo
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    exists = count > 0;
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e("ProjectDAO", "Error verificando existencia de project", e);
        }
        return exists;
    }

    // Convertir Date a String
    private String dateToString(Date date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return dateFormat.format(date);
        } catch (Exception e) {
            Log.e("ProjectDAO", "Error convirtiendo Date a String", e);
            return null;
        }

    }


    // Convertir String a Date
    private Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e("ProjectDAO", "Error convirtiendo string a fecha", e);
            e.printStackTrace();
        }


        return null;
        }
    }



