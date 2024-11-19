public class APIExcepcion extends RuntimeException{
    private String mensaje;

    public APIExcepcion(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMessage() {
        return this.mensaje;
    }
}
