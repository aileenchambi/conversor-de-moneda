import com.google.gson.annotations.SerializedName;

public record RespuestaError(String result, @SerializedName("error-type") String errorType) {


    public String obtenerMensajeError(){
        String mensajeError = switch (errorType){
            case "unsupported-code" -> "Codigo de moneda base no soportada";
            case "malformed-request" -> "Solicitud mal formada.";
            case "invalid-key" -> "Clave API invalida";
            default -> "Error desconocido: " + errorType;
        };
        return "Se ha encontrado un " + result + ": " + mensajeError;
    }

}
