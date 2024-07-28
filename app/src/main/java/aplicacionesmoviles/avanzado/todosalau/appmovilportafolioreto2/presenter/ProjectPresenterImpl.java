package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.presenter;

import android.content.Context;

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

    }

    @Override
    public void updateProject(Project obj) {

    }

    @Override
    public void deleteProject(Project obj) {

    }
}
