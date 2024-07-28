package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.ProjectActivity;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.R;
import aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model.Project;


public class ProjectAdapter extends ArrayAdapter<Project> {

    // Recursos de diseño y contexto
    private int resourceLayout;
    private Context mContext;

    // Bandera para controlar la visibilidad de los botones
    private boolean hideButtons = false;

    // Constructor del adaptador
    public ProjectAdapter(Context context, int resource, List<Project>
            items) {

        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    // Método para establecer si los botones deben ocultarse o
    public void setHideButtons(boolean hide) {
        hideButtons = hide;
    }

    // Método para obtener la vista del adaptador para un elemento específico


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Si la vista es nula, inflar el diseño
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(resourceLayout, parent, false);
        }

        //Obtenet el portafolio actual
        Project project = getItem(position);
        if (project != null) {
// Obtener las vistas
            TextView textViewId = view.findViewById(R.id.textViewId);
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewDescription = view.findViewById(R.id.textViewdescription);
            TextView textViewStartDate = view.findViewById(R.id.textViewStartDate);
            TextView textViewEndDate = view.findViewById(R.id.textViewEndDate);
            TextView textViewStatus = view.findViewById(R.id.textViewStatus);

// Obtener los botones de edición y eliminación
            Button buttonEdit = view.findViewById(R.id.buttonEdit);
            Button buttonDelete = view.findViewById(R.id.buttonDelete);

// Establecer los valores en los TextView
            textViewId.setText(project.getId());
            textViewName.setText(project.getName());
            textViewDescription.setText(project.getDescription());

            textViewStartDate.setText(project.getStringStartDate());
            textViewEndDate.setText(project.getStringEndDate());
            textViewStatus.setText(project.getStatus());

// Controlar la visibilidad de los botones según la variable hideButtons
            if (hideButtons) {
                buttonEdit.setVisibility(View.GONE);
                buttonDelete.setVisibility(View.GONE);
            } else {
                buttonEdit.setVisibility(View.VISIBLE);
                buttonDelete.setVisibility(View.VISIBLE);
            }

// Asignar listeners a los botones
            buttonEdit.setOnClickListener(v -> {
// Llamar al método editProduct de ProjectActivity con el projecto a editar
                if (mContext instanceof ProjectActivity) {
                    ((ProjectActivity) mContext).editProject(project);
                }
            });

            buttonDelete.setOnClickListener(v -> {
// Llamar al método deleteProduct de MainActivity con el projecto a eliminar
                if (mContext instanceof ProjectActivity) {
                    ((ProjectActivity) mContext).deleteProject(project);
                }
            });
        }
        return view;
    }


}
