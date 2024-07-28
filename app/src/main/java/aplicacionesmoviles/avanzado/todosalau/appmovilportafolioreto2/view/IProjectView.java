package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;

public interface IProjectView {

    void showProjects(List<Project> projectList);

    void showMessage(String text);
    void showError(String text);
}
