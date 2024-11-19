import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class Principal {

    public static void main(String[] args) {
        int opcion = 1;
        Scanner lectura = new Scanner(System.in);
        String monedaBase;
        String monedaDestino;
        double monto = 0;
        ConexionAPI conexion = new ConexionAPI();
        Map<String, String> monedasDisponibles = conexion.obtenerMonedasSoportadas();

        while (opcion != 3) {
            System.out.println("""
                    
                    ***** MENU *****
                    1. Ver lista de monedas soportadas
                    2. Convertir moneda
                    3. Salir
                    """);
            System.out.print("Elije una opción: ");
            String entradaUsuario = lectura.nextLine().trim();
            try {
                opcion = Integer.parseInt(entradaUsuario);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese una opcion valida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("*****MONEDAS SOPORTADAS*****");
                    if (monedasDisponibles == null || monedasDisponibles.isEmpty()) {
                        System.out.println("No se pudieron obtener las monedas soportadas.");
                    } else {
                        monedasDisponibles.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
                    }
                    break;
                case 2:
                    try {
                        System.out.println("*****CONVERSOR DE MONEDAS*****");
                        System.out.println("Ingrese el codigo de la moneda base (Ej: USD): ");
                        monedaBase = lectura.nextLine().trim().toUpperCase();
                        validarMoneda(monedasDisponibles, monedaBase);

                        System.out.println("Ingrese el codigo de la moneda destino (Ej: BOB): ");
                        monedaDestino = lectura.nextLine().trim().toUpperCase();
                        validarMoneda(monedasDisponibles, monedaDestino);

                        System.out.println("Ingrese el monto a convertir: ");
                        String montoTexto = lectura.nextLine().trim();
                        try {
                            monto = Double.parseDouble(montoTexto);
                            if (monto <= 0) {
                                throw new IllegalArgumentException("El monto debe ser mayor a 0.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("El monto ingresado no es válido.");
                            continue;
                        }
                        ConversorMoneda conversor = new ConversorMoneda(conexion, monedaBase, monedaDestino, monto);
                        double resultado = conversor.convertirMoneda();

                        System.out.println("*****DATOS DE CONVERSION*****");
                        System.out.println("Moneda base: " + conversor.getMonedaBase());
                        System.out.println("Moneda destino: " + conversor.getMonedaDestino());
                        System.out.println("Monto: " + conversor.getMonto());
                        System.out.println("Tasa de conversión: " + conversor.getTasaDeConversion());
                        System.out.println("Resultado: " + resultado);
                    } catch (InputMismatchException e){
                        System.out.println("El monto ingresado no es valido");
                        lectura.next();
                    }catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (APIExcepcion e) {
                        System.out.println("Error en la API: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error inesperado: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Elije una opcion valida.");
            }
        }
        lectura.close();
    }


    private static void validarMoneda(Map<String, String> monedasValidas, String moneda) {
        if (monedasValidas == null) {
            throw new IllegalArgumentException("Error al obtener la lista de monedas disponibles.");
        }

        if (!monedasValidas.containsKey(moneda)) {
            throw new IllegalArgumentException("Error: " + moneda + " no es una moneda valida.");
        }
    }
}
