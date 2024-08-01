package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.presenter;

import android.content.Context;
import android.view.View;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.repository.ProjectDAO;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view.IProjectView;

//@TODO PDT
public class ProjectPresenterImpl implements IProjectPresenter{


    private IProjectView view;
    private ProjectDAO dao;


    public ProjectPresenterImpl(IProjectView view, Context context) {
        this.view = view;
        this.dao=new ProjectDAO(context);
    }

    @Override
    public void showProjects() {
        List<Project> projectList =dao.getAllProjects();
        if(projectList.isEmpty()){
            view.showMessage("Actualmente no hay proyectos registrados");
        }
        view.showProjects(projectList);
    }



    @Override
    public void addProject(Project obj) {
        long rowInserted = dao.addProject(obj);
        if(rowInserted>0){
            view.showMessage("Proyecto agregado exitosamente");
        }else {
            view.showError("Error al agregar proyecto");
        }
        loadProjects();



    }

    @Override
    public void updateProject(Project project) {
        int result = dao.updateProject(project);
        if (result>0) {
            view.showMessage("Proyecto actualizado exitosamente");
        } else {
            view.showError("Error al actualizar el Proyecto");
        }
        loadProjects();
    }

    @Override
    public void deleteProject(Project project) {
        if (project.isDeleted()) {
            view.showMessage("El proyecto ya está eliminado");
        } else {
            dao.deleteProject(project.getId());
            view.showMessage("Proyecto eliminado exitosamente");
            loadProjects();
        }
    }
    @Override
    public void loadProjects() {
        List<Project> projects = dao.getAllProjects();
        view.showProjects(projects);
    }

    @Override
    public void synchronizeData() {

    }
}
