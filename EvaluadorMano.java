import java.util.*;
import java.util.List;

public class EvaluadorMano {
    public static final Map<String, Integer> valorCarta = new HashMap<>();
    private static String ultimaCombinacion = "";


    static {
        valorCarta.put("2", 2);
        valorCarta.put("3", 3);
        valorCarta.put("4", 4);
        valorCarta.put("5", 5);
        valorCarta.put("6", 6);
        valorCarta.put("7", 7);
        valorCarta.put("8", 8);
        valorCarta.put("9", 9);
        valorCarta.put("10", 10);
        valorCarta.put("J", 11);
        valorCarta.put("Q", 12);
        valorCarta.put("K", 13);
        valorCarta.put("A", 14);
    }

    public static Jugador determinarGanador(List<Jugador> jugadores) {
        Jugador ganador = null;
        int mejorPuntaje = -1;

        for (Jugador j : jugadores) {
            int puntuacion = evaluarMano(j.getMano()); // Usamos el método que ya tienes
            if (puntuacion > mejorPuntaje) {
                mejorPuntaje = puntuacion;
                ganador = j;
            }
        }


        return ganador;
    }


    public static int evaluarMano(List<Carta> mano) {
        Map<String, Integer> valorCount = new HashMap<>();
        Map<String, Integer> paloCount = new HashMap<>();
        List<Integer> valores = new ArrayList<>();

        for (Carta c : mano) {
            valorCount.put(c.getValor(), valorCount.getOrDefault(c.getValor(), 0) + 1);
            paloCount.put(c.getPalo(), paloCount.getOrDefault(c.getPalo(), 0) + 1);
            valores.add(valorCarta.get(c.getValor()));
        }

        Collections.sort(valores);

        boolean escalera = true;
        for (int i = 1; i < valores.size(); i++) {
            if (valores.get(i) != valores.get(i - 1) + 1) {
                escalera = false;
                break;
            }
        }

        boolean color = paloCount.containsValue(5);
        boolean poker = valorCount.containsValue(4);
        boolean fullHouse = valorCount.containsValue(3) && valorCount.containsValue(2);
        boolean trio = valorCount.containsValue(3);
        long pares = valorCount.values().stream().filter(v -> v == 2).count();

        if (escalera && color) return 800 + valores.get(4); // Escalera de color
        if (poker) return 700;
        if (fullHouse) return 600;
        if (color) return 500;
        if (escalera) return 400;
        if (trio) return 300;
        if (pares == 2) return 200;
        if (pares == 1) return 100;
        return valores.get(4);
    }



    public static String getUltimaCombinacion() {
        return ultimaCombinacion;
    }

    public static int evaluarMejorManoDeSiete(List<Carta> cartas) {
        List<List<Carta>> combinaciones = obtenerCombinacionesDe5(cartas);
        int mejorPuntaje = -1;

        for (List<Carta> combinacion : combinaciones) {
            int puntuacion = evaluarMano(combinacion);
            if (puntuacion > mejorPuntaje) {
                mejorPuntaje = puntuacion;
            }
        }

        return mejorPuntaje;
    }
    //seven card stud

    public static Jugador determinarGanadorSieteCartas(List<Jugador> jugadores) {
        Jugador ganador = null;
        int mejorPuntaje = -1;

        for (Jugador j : jugadores) {
            List<Carta> cartasVisibles = j.getMano();
            int puntuacion = evaluarMejorManoDeSiete(cartasVisibles);

            if (puntuacion > mejorPuntaje) {
                mejorPuntaje = puntuacion;
                ganador = j;
            }
        }

        return ganador;
    }

    private static List<List<Carta>> obtenerCombinacionesDe5(List<Carta> cartas) {
        List<List<Carta>> resultado = new ArrayList<>();
        combinar(cartas, 0, new ArrayList<>(), resultado);
        return resultado;
    }

    private static void combinar(List<Carta> cartas, int inicio, List<Carta> actual, List<List<Carta>> resultado) {
        if (actual.size() == 5) {
            resultado.add(new ArrayList<>(actual));
            return;
        }

        for (int i = inicio; i < cartas.size(); i++) {
            actual.add(cartas.get(i));
            combinar(cartas, i + 1, actual, resultado);
            actual.remove(actual.size() - 1);
        }
    }

    private static int obtenerValor(String valor) {
        return valorCarta.getOrDefault(valor, 0);
    }
    public static int compararManosVisibles(List<Carta> mano1, List<Carta> mano2) {

        List<Carta> copia1 = new ArrayList<>(mano1);
        List<Carta> copia2 = new ArrayList<>(mano2);


        copia1.sort((a, b) -> obtenerValor(b.getValor()) - obtenerValor(a.getValor()));
        copia2.sort((a, b) -> obtenerValor(b.getValor()) - obtenerValor(a.getValor()));


        for (int i = 0; i < Math.min(copia1.size(), copia2.size()); i++) {
            int valor1 = obtenerValor(copia1.get(i).getValor());
            int valor2 = obtenerValor(copia2.get(i).getValor());

            if (valor1 > valor2) {
                return 1;
            } else if (valor1 < valor2) {
                return -1;
            }
        }


        return 0;
    }

}

