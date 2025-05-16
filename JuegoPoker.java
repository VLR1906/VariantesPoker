import java.util.List;

public abstract class JuegoPoker {
    protected List<Jugador> jugadores;
    protected Baraja baraja;

    public JuegoPoker(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
    }

    public abstract void jugar();

    protected abstract void repartirCartas();

    protected abstract void gestionarApuestas();

    protected abstract void evaluarGanador();
}

