import java.util.*;

public class CincoCartasDraw extends JuegoPoker {
    private Scanner scanner = new Scanner(System.in);
    private int pozo;
    private final int apuestaMinima = 10;

    public CincoCartasDraw(List<Jugador> jugadores) {
        super(jugadores);
    }

    @Override
    public void jugar() {
        while (jugadores.size() > 1) {
            System.out.println("\n======== NUEVA RONDA ========");
            baraja = new Baraja();
            pozo = 0;

            for (Jugador j : jugadores) {
                j.reiniciarRonda();
            }

            repartirCartas();
            mostrarFichas();

            System.out.println("\n💰 Primera ronda de apuestas:");
            gestionarApuestas();

            cambiarCartas();

            System.out.println("\n💰 Segunda ronda de apuestas:");
            gestionarApuestas();

            mostrarManos();

            evaluarGanador();
            eliminarSinFichas();
        }

        System.out.println("🎉 El ganador final es: " + jugadores.get(0).getNombre());
    }

    @Override
    protected void repartirCartas() {
        for (Jugador j : jugadores) {
            j.recibirCartas(baraja.repartir(5));
        }
    }

    @Override
    protected void gestionarApuestas() {
        int apuestaMaxima = 0;

        for (Jugador j : jugadores) {
            if (j.estaRetirado()) continue;

            System.out.println("\nTurno de " + j.getNombre());
            System.out.println("Tu mano: " + j.getMano());
            System.out.println("Fichas: " + j.getFichas());
            System.out.println("Apuesta actual a igualar: " + apuestaMaxima);
            System.out.print("¿Retirarse (r), Igualar (i), Apostar (a)? ");

            String opcion = scanner.nextLine();
            switch (opcion.toLowerCase()) {
                case "r":
                    j.retirarse();
                    break;
                case "i":
                    j.igualar(apuestaMaxima);
                    pozo += j.getApuestaActual();
                    break;
                case "a":
                    System.out.print("¿Cuánto deseas apostar? ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine();
                    int cantidadReal = j.apostar(cantidad);
                    pozo += cantidadReal;
                    if (j.getApuestaActual() > apuestaMaxima) {
                        apuestaMaxima = j.getApuestaActual();
                    }
                    break;
                default:
                    System.out.println("Entrada inválida. Te retiras.");
                    j.retirarse();
            }
        }
    }

    @Override
    protected void evaluarGanador() {
        List<Jugador> activos = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                activos.add(j);
            }
        }

        Jugador ganador = EvaluadorMano.determinarGanador(activos);
        System.out.println("🏆 Ganador de la ronda: " + ganador.getNombre());
        ganador.ganarFichas(pozo);
    }

    private void cambiarCartas() {
        System.out.println("\n🔁 Ronda de cambio de cartas:");
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                j.cambiarCartas(baraja, scanner);
            }
        }
    }

    private void mostrarManos() {
        System.out.println("\n🃏 Manos finales:");
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                System.out.println(j.getNombre() + ": " + j.getMano());
            }
        }
    }

    private void mostrarFichas() {
        for (Jugador j : jugadores) {
            System.out.println(j.getNombre() + " → " + j.getFichas() + " fichas");
        }
    }

    private void eliminarSinFichas() {
        jugadores.removeIf(j -> j.getFichas() <= 0);
    }
}

