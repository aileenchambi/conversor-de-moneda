import java.util.Map;

public class ConversorMoneda {
    private ConexionAPI conexionAPI;
    private String monedaBase;
    private String monedaDestino;
    private double tasaDeConversion;
    private double monto;

    public ConversorMoneda(ConexionAPI conexionAPI,String monedaBase, String monedaDestino, double monto) {
        this.conexionAPI = conexionAPI;
        this.monedaBase = monedaBase;
        this.monedaDestino = monedaDestino;
        this.monto = monto;
    }

    public double convertirMoneda() {
        Moneda moneda = conexionAPI.obtenerDatosMoneda(monedaBase);
        if (moneda == null) {
            throw new APIExcepcion("No se obtuvo los datos de la moneda base: " + monedaBase);
        }

        Map<String, Double> tasasDeConversion = moneda.conversion_rates();
        if (!tasasDeConversion.containsKey(monedaDestino)) {
            throw new APIExcepcion("No se encontró la tasa de conversión para la moneda destino: " + monedaDestino);
        }

        tasaDeConversion = tasasDeConversion.get(monedaDestino);

        return monto * tasaDeConversion;
    }


    public String getMonedaBase() {
        return monedaBase;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public double getTasaDeConversion() {
        return tasaDeConversion;
    }

    public double getMonto() {
        return monto;
    }
}

