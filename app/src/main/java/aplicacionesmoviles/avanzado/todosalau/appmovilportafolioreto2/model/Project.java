package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Project {
    private String id;                // Identificador único del proyecto
    private String name;           // Nombre del proyecto
    private String description;    // Descripción del proyecto
    private Date startDate;        // Fecha de inicio del proyecto
    private Date endDate;          // Fecha de finalización del proyecto (opcional)
    private String status;         // Estado del proyecto (e.g., "En progreso", "Completado")
    private boolean deleted; // Indica si el producto ha sido


    public Project() {

    }

    public Project(String id, String name, String description, Date startDate, Date endDate, String status) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    public Project(String name, String description, Date startDate, Date endDate, String status) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStringStartDate(){
        return this.dateToString(this.startDate);
    }

    public String getStringEndDate(){
        return this.dateToString(this.endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Convierte las propiedades del producto en un mapa para almacenamiento o envío
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id); // Agrega el ID al mapa
        result.put("name", name); // Agrega el nombre al mapa
        result.put("description", description);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("status", status);
        return result;

    }

        private String dateToString(Date date) {
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
