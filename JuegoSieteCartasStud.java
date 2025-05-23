import java.util.*;

public class JuegoSieteCartasStud extends JuegoPoker {
    private int pozo;
    private JuegoObserver observer;
    private int rondaActual = 3;
    private int rondasJugadas = 0;
    private final int MAX_RONDAS = 7;

    public JuegoSieteCartasStud(List<Jugador> jugadores) {
        super(jugadores);
        this.pozo = 0;
    }

    @Override
    public void jugar() {
        if (rondasJugadas >= MAX_RONDAS) {
            terminarJuego();
            return;
        }

        rondasJugadas++;

        cobrarAnte();
        repartirCartas();
        observer.actualizarCartas(jugadores);

        mostrarCartasVisibles();
        gestionarApuestas();
    }

    public void avanzarRonda() {
        rondaActual++;

        if (rondaActual <= 7) {
            boolean esUltimaOculta = (rondaActual == 7);
            repartirUnaCarta(esUltimaOculta);
            observer.actualizarCartas(jugadores);
            mostrarCartasVisibles();
            gestionarApuestas();
        } else {
            evaluarGanador();
            for (Jugador j : jugadores) {
                for (Carta c : j.getCartas()) {
                    c.setVisible(true);
                }
            }
            observer.actualizarCartas(jugadores);
            rondasJugadas++;

            if (rondasJugadas >= MAX_RONDAS) {
                terminarJuego();
            }
        }
    }

    @Override
    protected void repartirCartas() {
        for (Jugador j : jugadores) {
            List<Carta> cartas = baraja.repartir(3);

            j.recibirCartas(new ArrayList<>());
            j.agregarCarta(new CartaOculta(cartas.get(0)));
            j.agregarCarta(new CartaOculta(cartas.get(1)));
            j.agregarCarta(new CartaVisible(cartas.get(2)));
        }
    }

    private void repartirUnaCarta(boolean esUltimaOculta) {
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                Carta carta = baraja.repartir(1).get(0);
                if (esUltimaOculta) {
                    j.agregarCarta(new CartaOculta(carta));
                } else {
                    j.agregarCarta(new CartaVisible(carta));
                }
            }
        }
    }

    @Override
    protected void gestionarApuestas() {
        int bringIn = 5;
        int apuestaCompleta = 10;

        boolean esThirdStreet = jugadores.get(0).getCartas().length == 3;

        if (esThirdStreet) {
            Jugador bringInJugador = obtenerJugadorConCartaVisibleMasBaja();

            observer.mostrarMensaje("\n" + bringInJugador.getNombre() + " debe pagar el bring-in de " + bringIn + " fichas.");
            pozo += bringInJugador.apostar(bringIn);

            observer.solicitarAccionesApuesta(jugadores, bringIn, apuestaCompleta, bringInJugador); // ← GUI debe mostrar y decidir
        } else {
            observer.solicitarAccionesApuesta(jugadores, apuestaCompleta, apuestaCompleta, null); // para cuarta calle en adelante
        }
    }

    public void procesarDecisiones(Map<Jugador, String> decisiones, int apuestaMinima, int apuestaCompleta) {
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                String decision = decisiones.get(j);
                switch (decision.toLowerCase()) {
                    case "call":
                        pozo += j.apostar(apuestaMinima);
                        break;
                    case "raise":
                        pozo += j.apostar(apuestaCompleta);
                        break;
                    case "fold":
                        j.retirarse();
                        break;
                    default:
                        observer.mostrarMensaje("Decisión inválida para " + j.getNombre());
                }
            }
        }
        avanzarRonda();
    }

    @Override
    protected Jugador evaluarGanador() {
        List<Jugador> enJuego = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                j.revelarCartas();
                j.mostrarMano();
                enJuego.add(j);
            }
        }

        Jugador ganador = EvaluadorMano.determinarGanador(enJuego);
        if (ganador != null) {
            observer.mostrarMensaje("\n\u00a1" + ganador.getNombre() + " gana el pozo de " + pozo + " fichas!");
            ganador.ganarFichas(pozo);
        } else {
            observer.mostrarMensaje("Ning\u00fan jugador v\u00e1lido.");
        }

        for (Jugador j : jugadores) {
            j.reiniciarRonda();
        }
        pozo = 0;
        return ganador;
    }

    private Jugador obtenerJugadorConCartaVisibleMasBaja() {
        Jugador resultado = null;
        CartaVisible cartaMasBaja = null;

        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                for (Carta c : j.getCartas()) {
                    if (c instanceof CartaVisible) {
                        CartaVisible cv = (CartaVisible) c;
                        if (cartaMasBaja == null || compararCartas(cv, cartaMasBaja) < 0) {
                            cartaMasBaja = cv;
                            resultado = j;
                        }
                        break;
                    }
                }
            }
        }
        return resultado;
    }
    private int valorNumerico(Carta carta) {
        return EvaluadorMano.valorCarta.getOrDefault(carta.getValor(), 0);
    }

    private int compararCartas(CartaVisible a, CartaVisible b) {
        int valorA = valorNumerico(a);
        int valorB = valorNumerico(b);

        if (valorA != valorB) {
            return Integer.compare(valorA, valorB);
        } else {
            return a.getPalo().compareTo(b.getPalo());
        }
    }

    private void cobrarAnte() {
        int ante = 5;
        for (Jugador j : jugadores) {
            pozo += j.apostar(ante);
        }
    }

    private void mostrarCartasVisibles() {
        observer.mostrarMensaje("\n=== Cartas visibles de los jugadores ===");
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) {
                j.mostrarCartasVisibles();
            }
        }
        observer.mostrarMensaje("========================================");
    }

    public void setObserver(JuegoObserver observer) {
        this.observer = observer;
    }

    private void terminarJuego() {
        Jugador ganadorFinal = jugadores.get(0);
        for (Jugador j : jugadores) {
            if (j.getFichas() > ganadorFinal.getFichas()) {
                ganadorFinal = j;
            }
        }

        if (observer != null) {
            observer.finalizarJuego(ganadorFinal.getNombre(), ganadorFinal.getFichas());
        }
    }
}
