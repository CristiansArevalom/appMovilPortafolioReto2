package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.monitor.NetworkMonitor;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.presenter.ProjectPresenterImpl;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.repository.ProjectDAO;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.utils.ProjectDatabaseHelper;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.utils.ProjectFirebaseHelper;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view.IProjectView;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view.adapters.ProjectAdapter;

public class ProjectActivity extends AppCompatActivity implements IProjectView {

    // Componentes de la interfaz de usuario
    private EditText editTextId, editTextName, editTextDescription,editTextStartDate,editTextEndDate,editTextStatus;

    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

    private Button buttonAdd, buttonGetFirebase, buttonSichronized, buttonGetSqlite;
    private ListView listViewProjects;


    // Objetos de ayuda

    private ProjectDAO dao; // validar se deme mirar si s epued hacer con el presenter
    private ProjectPresenterImpl presenter; //validar si se improta la imp o la intefaz
    private ProjectAdapter projectAdapter;
    private ProjectFirebaseHelper firebaseHelper;
    private NetworkMonitor networkMonitor;

    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_project);

        initializeUIComponents();
        initializeHelpers();
        setUpEventListeners();
        authenticateFirebaseUser();


        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();



    }

    // Inicializa los componentes de la interfaz de usuario
    private void initializeUIComponents() {
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editDescription);
        editTextStartDate = findViewById(R.id.editStartDate);
        editTextEndDate = findViewById(R.id.editEndDate);
        editTextStatus = findViewById(R.id.editTextStatus);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonGetFirebase = findViewById(R.id.buttonGetFirebase);
        buttonSichronized = findViewById(R.id.buttonSichronized);
        buttonGetSqlite = findViewById(R.id.buttonGetSqlite);
        listViewProjects= findViewById(R.id.listViewProjects);

        // Inicializar la base de datos y el adaptador
        this.dao= new ProjectDAO(this);//validar

       presenter = new ProjectPresenterImpl(this, this); // @TODO validar para mover logica de projectActivity a presenter

        List<Project> projects = dao.getAllProjects();
        projectAdapter = new ProjectAdapter(this, R.layout.list_item_project, projects);
        listViewProjects.setAdapter(projectAdapter);


        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());



    }

    // Inicializa los objetos de ayuda
    private void initializeHelpers() {
        networkMonitor = new NetworkMonitor(this);
        firebaseHelper = new ProjectFirebaseHelper();
    }


    // Autentica al usuario de Firebase
    private void authenticateFirebaseUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                // Puedes usar el user.getUid() para identificar al usuario si es necesario
            } else {
                Toast.makeText(ProjectActivity.this, "Error al iniciar sesión anónimo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Maneja la operación de agregar o actualizar producto
    private void handleAddOrUpdateProduct() {
        if (buttonAdd.getText().toString().equalsIgnoreCase("Agregar")) {
            addProject();
        } else {
            saveProject();
        }
    }

    // Configura los eventos de los botones
    private void setUpEventListeners() {
        buttonGetSqlite.setOnClickListener(v -> loadProjectsFromDatabase());
        buttonSichronized.setOnClickListener(v -> synchronizeData());
        buttonGetFirebase.setOnClickListener(v -> loadProjectsFromFirebase(true));
        buttonAdd.setOnClickListener(v -> handleAddOrUpdateProduct());

        editTextStartDate.setOnClickListener(v -> showDatePicker(startDateCalendar, editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePicker(endDateCalendar, editTextEndDate));
    }


    // Métodos relacionados con la carga de datos
    private void showDatePicker(final Calendar calendar, final EditText editText) {
        new DatePickerDialog(ProjectActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void loadProjectsFromDatabase() {
        List<Project> projects = dao.getAllProjects();
        updateProjectList(projects, false);
    }
    private void loadProjectsFromFirebase(boolean hideButtons) { //
        Log.d("ProjectActivit", "Entro a loadPorjectsFromFirebase");
        firebaseHelper.getAllProjects(new ProjectFirebaseHelper.GetProjectsCallback() {

            @Override
            public void onProjectsRetrieved(List<Project> projects) {
                updateProjectList(projects, hideButtons);
            }

            @Override
            public void onError() {
                Toast.makeText(ProjectActivity.this, "Error al obtener productos de Firebase", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateProjectList(List<Project> projects, boolean hideButtons) {
        projectAdapter.clear();
        projectAdapter.addAll(projects);
        projectAdapter.setHideButtons(hideButtons);
        projectAdapter.notifyDataSetChanged();
    }


    // Métodos relacionados con la sincronización de datos
    private void synchronizeData() {
        if (!networkMonitor.isNetworkAvailable()) {
            Toast.makeText(ProjectActivity.this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            return;
        }
        synchronizeAndRemoveData();
        synchronizeAndLoadData();
    }

    private void synchronizeAndLoadData() {
        List<Project> projectsFromSQLite = dao.getAllProjects();
        synchronizeProjectsToFirebase(projectsFromSQLite);
        loadProjectsFromFirebase(true);
    }
    private void synchronizeAndRemoveData() {
        firebaseHelper.getAllProjects(new ProjectFirebaseHelper.GetProjectsCallback() {
            @Override
            public void onProjectsRetrieved(List<Project> projectsFromFirebase) {
                List<Project> projectsFromSQLite = dao.getAllProjects();
                Set<String> sqliteProjectIds = new HashSet<>();

                for (Project sqliteProduct : projectsFromSQLite) {
                    sqliteProjectIds.add(sqliteProduct.getId());
                }

                List<Project> projectsToDeleteFromFirebase = new ArrayList<>();
                for (Project firebaseProject : projectsFromFirebase) {
                    if (!sqliteProjectIds.contains(firebaseProject.getId())) {
                        projectsToDeleteFromFirebase.add(firebaseProject);
                    }
                }

                deleteProjectsFromFirebase(projectsToDeleteFromFirebase);
            }

            @Override
            public void onError() {
                Toast.makeText(ProjectActivity.this, "Error al obtener proyectos de Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void synchronizeProjectsToFirebase(List<Project> projectsFromSQLite) {
        for (Project project : projectsFromSQLite) {
            firebaseHelper.checkIfProjectExists(project.getId(), new ProjectFirebaseHelper.ProjectExistsCallback() {
                @Override
                public void onProjectExists(boolean exists) {
                    if (exists) {
                        firebaseHelper.updateProject(project);
                    } else {
                        firebaseHelper.addProject(project, new ProjectFirebaseHelper.AddProjectCallback() {
                            @Override
                            public void onSuccess() {
                                // Producto agregado exitosamente

                            }
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(ProjectActivity.this, "Error al agregar project a Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(ProjectActivity.this, "Error al verificar existencia del producto en Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void deleteProjectsFromFirebase(List<Project> projectsToDeleteFromFirebase) {
        for (Project projectToDelete : projectsToDeleteFromFirebase) {
            firebaseHelper.deleteProject(projectToDelete.getId(), new ProjectFirebaseHelper.DeleteProjectCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ProjectActivity.this, "Producto eliminado de Firebase", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(ProjectActivity.this, "Error al eliminar producto de Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        loadProjectsFromFirebase(true);
    }


    // Métodos de utilidad @todo completar los filtros
    private boolean areFieldsEmpty() {
        if (editTextName.getText().toString().trim().isEmpty() || editTextDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void clearInputFields() {
        editTextId.setText("");
        editTextName.setText("");
        editTextDescription.setText("");
        editTextStartDate.setText("");
        editTextEndDate.setText("");
        editTextStatus.setText("");
    }

    // Métodos relacionados con la manipulación de productos
    private void addProject() {
        if (areFieldsEmpty()) {
            return;
        }
        String name = editTextName.getText().toString();
        String description= editTextDescription.getText().toString();
        String startDate= editTextStartDate.getText().toString();
        String endDate=editTextEndDate.getText().toString();
        String status=editTextStatus.getText().toString();
        Project newProject = new Project(name, description,stringToDate(startDate),stringToDate(endDate),status);
        presenter.addProject(newProject);
        loadProjectsFromDatabase();
        clearInputFields();
    }

    private void saveProject() {
        if (areFieldsEmpty()) {
            return;
        }
        String id = editTextId.getText().toString();
        String name = editTextName.getText().toString();
        String description= editTextDescription.getText().toString();
        String startDate= editTextStartDate.getText().toString();
        String endDate=editTextEndDate.getText().toString();
        String status=editTextStatus.getText().toString();

        Project updatedProject  = new Project(id,name, description,stringToDate(startDate),stringToDate(endDate),status);
        presenter.updateProject(updatedProject);
        loadProjectsFromDatabase();
        buttonAdd.setText("Agregar");
        clearInputFields();
    }

    public void deleteProject(Project project) {
            presenter.deleteProject(project);
            loadProjectsFromDatabase();
    }
    /*
    private void addProject() {
        if (areFieldsEmpty()) {
            return;
        }

        String name = editTextName.getText().toString();
        String description= editTextDescription.getText().toString();
        String startDate= editTextStartDate.getText().toString();
        String endDate=editTextEndDate.getText().toString();
        String status=editTextStatus.getText().toString();

        Project newProject = new Project(name, description,stringToDate(startDate),stringToDate(endDate),status);

        long rowInserted = dao.addProject(newProject);
        if(rowInserted>0){
            Toast.makeText(this, "Proyecto agregado exitosamente", Toast.LENGTH_SHORT).show();
        }
        loadProjectsFromDatabase();
        clearInputFields();
    }


    private void saveProject() {
        if (areFieldsEmpty()) {
            return;
        }
        String id = editTextId.getText().toString();
        String name = editTextName.getText().toString();
        String description= editTextDescription.getText().toString();
        String startDate= editTextStartDate.getText().toString();
        String endDate=editTextEndDate.getText().toString();
        String status=editTextStatus.getText().toString();

        Project newProject = new Project(id,name, description,stringToDate(startDate),stringToDate(endDate),status);

        dao.updateProject(newProject);
        loadProjectsFromDatabase();
        buttonAdd.setText("Agregar");
        clearInputFields();
        Toast.makeText(this, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
    }
        public void deleteProject(Project project) {
        if (project.isDeleted()) {
            Toast.makeText(this, "project ya está eliminado", Toast.LENGTH_SHORT).show();
        } else {
            dao.deleteProject(project.getId());
            loadProjectsFromDatabase();
            Toast.makeText(this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }
*/



    public void editProject(Project project) {
        editTextId.setText(project.getId());
        editTextName.setText(project.getName());
        editTextDescription.setText(project.getDescription());
        editTextStartDate.setText(dateToString(project.getStartDate())); //PDT FECHA
        editTextEndDate.setText(dateToString(project.getEndDate())); //PDT FECHA
        editTextStatus.setText(project.getStatus());
        buttonAdd.setText("Guardar");
    }





    // Convertir String a Date
    private Date stringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String dateToString(Date date) {
        try {
           return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void showProjects(List<Project> projectList) {

        projectAdapter.clear();
        projectAdapter.addAll(projectList);
        projectAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }


}