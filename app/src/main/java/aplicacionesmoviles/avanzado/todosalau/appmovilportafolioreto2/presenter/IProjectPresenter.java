package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.presenter;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;

//@TODO PDT
public interface IProjectPresenter {

    void showProjects();

    void addProject(Project obj);

    void updateProject(Project obj);

    void deleteProject(Project obj);

    void loadProjects();
    void synchronizeData();
}
