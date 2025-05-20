import java.util.*;

public class CincoCartasDraw extends JuegoPoker {
    private List<Jugador> jugadores;
    private Baraja baraja = new Baraja();

    private int indiceJugador;
    private int ronda;
    private int apuestaMaxima;
    private int bote;
    private boolean rondaTerminada;
    public CincoCartasDraw(List<Jugador> jugadores) {
        super(jugadores);
        this.jugadores = jugadores;

    }

    @Override
    public void jugar() {

    }

    @Override
    protected void repartirCartas() {

    }

    @Override
    protected void gestionarApuestas() {

    }

    @Override
    public Jugador evaluarGanador() {
        List<Jugador> activos = jugadores.stream()
                .filter(j -> !j.estaRetirado())
                .toList();
        Jugador ganador = EvaluadorMano.determinarGanador(activos);
        if (ganador != null) {
            ganador.ganarFichas(bote);
            bote = 0;
        }
        return ganador;
    }


    // Mover lógica desde InterfazJuego.inicializarJugadores()
    public void iniciarNuevaRonda() {
        baraja = new Baraja();
        bote = 0;
        ronda = 1;
        apuestaMaxima = 0;
        indiceJugador = 0;

        for (Jugador j : jugadores) {
            j.reiniciarRonda();
            j.recibirCartas(baraja.repartir(5));
        }
    }

    public String realizarApuesta(Jugador jugador, int cantidad) {
        if (cantidad > jugador.getFichas()) {
            return "No puedes apostar más de lo que tienes.";
        }

        if (cantidad < apuestaMaxima) {
            return "Debes igualar o superar la apuesta máxima actual: $" + apuestaMaxima;
        }

        jugador.apostar(cantidad);
        bote += cantidad;
        apuestaMaxima = cantidad;
        return "Apostaste $" + cantidad;
    }


    public boolean igualarApuesta(Jugador jugador) {
        int diferencia = apuestaMaxima - jugador.getApuestaActual();

        if (diferencia > jugador.getFichas()) {
            return false;
        }

        jugador.apostar(diferencia);
        bote += diferencia;
        return true;
    }


    public void cambiarCartas(Jugador jugador, List<Integer> indicesACambiar) {
        List<Carta> mano = jugador.getMano();
        for (int i : indicesACambiar) {
            if (i >= 0 && i < mano.size()) {
                mano.set(i, baraja.repartir(1).get(0));
            }
        }
    }



    public int getBote() {
        return bote;
    }

    public int getApuestaMaxima() {
        return apuestaMaxima;
    }



    public Jugador entregarBoteAlGanador() {
        Jugador ganador = evaluarGanador();
        if (ganador != null) {
            ganador.ganarFichas(bote);
        }
        return ganador;
    }



    public void reiniciarBote() {
        bote = 0;
    }


}


