package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.model;

public class UserModel {

    private String userId;           // ID único del usuario
    private String email;            // Correo electrónico del usuario
    private String name;             // Nombre del usuario


    // Constructor vacío necesario para Firebase
    public UserModel() {
    }


    // Constructor para inicializar un UserModel con ID, correo electrónico y nombre
    public UserModel(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    // Getters y setters para los demás atributos
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
