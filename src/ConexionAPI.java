import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConexionAPI {

    private final String direccionBaseAPI = "https://v6.exchangerate-api.com/v6/";
    private final String claveAPI = "a9c65d6fe31c34947b38055d";

    public Moneda obtenerDatosMoneda(String monedaBase){
        final String direccionFinalAPI = direccionBaseAPI + claveAPI + "/latest/" + monedaBase;
        Moneda moneda = null;

        HttpClient cliente = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(direccionFinalAPI))
                    .build();
            HttpResponse<String> respuesta = cliente
                    .send(solicitud, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() != 200) {
                RespuestaError error = convertirJsonAClase(respuesta.body(), RespuestaError.class);
                throw new APIExcepcion(error.obtenerMensajeError());

            }

            moneda = convertirJsonAClase(respuesta.body(), Moneda.class);

        } catch (APIExcepcion e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error en la URI, verifique la dirección.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al obtener datos de la API." );
        }
        return moneda;
    }

    public Map<String, String> obtenerMonedasSoportadas(){
        final String direccion = direccionBaseAPI + claveAPI + "/codes";
        Map<String, String> monedas = null;
        HttpClient cliente = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try{
            HttpRequest solicitud = HttpRequest.newBuilder()
                    .uri(URI.create(direccion))
                    .build();
            HttpResponse<String> respuesta = cliente
                    .send(solicitud, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                MonedasSoportadas monedasSoportadas = convertirJsonAClase(respuesta.body(), MonedasSoportadas.class);
                monedas = convertirListaAMap(monedasSoportadas.supported_codes());

            }
        }catch (IOException | InterruptedException e) {
            System.out.println("Error al obtener las monedas soportadas: " + e.getMessage());
        }


        return monedas;
    }

    public <T> T convertirJsonAClase(String json, Class<T> clase) {
        Gson gson = new Gson();
        return gson.fromJson(json, clase);
    }

    public Map <String, String> convertirListaAMap(List<List<String>> codigosSoportados){
        Map<String, String> monedasMap = new HashMap<>();
        for(List<String> parMoneda : codigosSoportados){
            monedasMap.put(parMoneda.get(0), parMoneda.get(1));
        }
        return monedasMap;
    }


}
