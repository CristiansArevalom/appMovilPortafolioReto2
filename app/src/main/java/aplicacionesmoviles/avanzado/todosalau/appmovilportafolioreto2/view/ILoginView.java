package aplicacionesmoviles.avanzado.todosalau.appmovilportafolioreto2.view;

public interface ILoginView {
    /**
     * Muestra un mensaje de notificación en la interfaz de usuario.
     * @param message Mensaje a mostrar.
     */
    void showToast(String message);
    /**
     * Navega a la pantalla principal de la aplicación.
     */
    void navigateToHome();
    /**
     * Obtiene el correo electrónico ingresado por el usuario.
     * @return Correo electrónico ingresado.
     */
    String getEmail();

    /**
     * Obtiene la contraseña ingresada por el usuario.
     * @return Contraseña ingresada.
     */
    String getPassword();

}
