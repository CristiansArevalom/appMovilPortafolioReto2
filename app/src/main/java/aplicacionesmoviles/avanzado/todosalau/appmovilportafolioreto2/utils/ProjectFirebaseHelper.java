package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;

//@TODO, isse con el tipo de dato de fechas, saca un warning pero funciona ; issue seguridad, en el firebase se dejo todo true a nivel de regñas,
//ESta clase es para manejar sqllite
public class ProjectFirebaseHelper {
    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    // Constructor que inicializa la referencia a la base de datos
    public ProjectFirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("projects");
    }

    // Interfaz para el callback al agregar un Project
    public interface AddProjectCallback {
        void onSuccess();
        void onError(Exception e);

    }

    // Interfaz para el callback al eliminar un Project
    public interface DeleteProjectCallback {
        void onSuccess();
        void onError(Exception e);
    }


    // Método para agregar un project a la base de datos
    public void addProject(Project project, AddProjectCallback callback) {
        // Si el Project no tiene un ID, se genera uno nuevo
        if (project.getId() == null || project.getId().isEmpty()) {
            String newId = databaseReference.push().getKey();
            project.setId(newId);
        }

        // Agregar el producto a Firebase
        databaseReference.child(project.getId()).setValue(project)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }


    // Método para actualizar un Project en la base de datos
    public void updateProject(Project project) {
        // Verificar si el ID del Project es nulo o está vacío
        if (project == null || project.getId() == null || project.getId().isEmpty()) {
            System.out.println("El ID del project es nulo o vacío. No se puede actualizar el project.");
            return; // Detener la ejecución si el ID es nulo o vacío
        }

        // Actualizar el Project en Firebase
        databaseReference.child(project.getId()).updateChildren(project.toMap());
    }

    // Método para eliminar un Project de la base de datos
    public void deleteProject(String id, DeleteProjectCallback callback) {
        // Verificar si el ID es nulo o está vacío
        if (id == null || id.isEmpty()) {
            callback.onError(new IllegalArgumentException("ID del Project es nulo o vacío."));
            return;
        }

        // Eliminar el Project de Firebase
        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }
    // Interfaz para el callback de obtención de Project
    public interface GetProjectsCallback {
        void onProjectsRetrieved(List<Project> project);
        void onError();
    }

    // Método para obtener todos los Project de la base de datos
    public void getAllProjects(final GetProjectsCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    Project project = projectSnapshot.getValue(Project.class);
                    if (project != null) {
                        projects.add(project);
                        Log.d("MainActivity", "Proyecto: " + project.getStartDate());
                        Log.d("MainActivity", "Proyecto: " + project.getEndDate());
                    }
                }
                callback.onProjectsRetrieved(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError();
            }
        });
    }

    // Método para obtener un projects específico por ID

    public void getProjectById(String productId, GetProjectByIdCallback callback) {
        // Obtener una referencia al producto específico por ID
        DatabaseReference productReference = databaseReference.child(productId);

        // Añadir un oyente para obtener el producto
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Obtener el producto
                Project project = dataSnapshot.getValue(Project.class);

                // Llamar al callback con el producto obtenido
                callback.onProjectRetrieved(project);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // En caso de error, llamar al callback de error
                callback.onError(databaseError.toException());
            }
        });
    }
    // Interfaz para el callback de obtener un projecto por ID
    public interface GetProjectByIdCallback {
        void onProjectRetrieved(@Nullable Project project);
        void onError(Exception e);
    }


    // Método para verificar si un Project existe en la base de datos
    public void checkIfProjectExists(String projectId, ProjectExistsCallback callback) {
        DatabaseReference projectRef = databaseReference.child(projectId);

        projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean exists = snapshot.exists();
                callback.onProjectExists(exists);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError();
            }
        });
    }

    // Interfaz para el callback de existencia de un producto
    public interface ProjectExistsCallback {
        void onProjectExists(boolean exists);
        void onError();
    }
}

