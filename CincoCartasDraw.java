import java.util.*;

public class CincoCartasDraw extends JuegoPoker {
    public CincoCartasDraw(List<Jugador> jugadores) {
        super(jugadores);
    }

    @Override
    public void jugar() {
        new InterfazJuego(jugadores); // Muestra la interfaz al final
    }

    @Override
    protected void repartirCartas() {
        for (Jugador jugador : jugadores) {
            jugador.reiniciarRonda();
            jugador.recibirCartas(baraja.repartir(5));
        }
    }

    @Override
    protected void gestionarApuestas() {
        for (Jugador jugador : jugadores) {
            if (!jugador.estaRetirado()) {
                jugador.apostar(10); // Apuesta fija simple
            }
        }
    }

    @Override
    protected void evaluarGanador() {
        Jugador ganador = EvaluadorMano.determinarGanador(jugadores);
        ganador.ganarFichas(50);
        System.out.println("Ganador: " + ganador.getNombre() + " con $" + ganador.getFichas());
    }
}


