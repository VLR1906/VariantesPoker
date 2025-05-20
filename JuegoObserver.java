import java.util.List;

public interface JuegoObserver {
    void actualizarCartas(List<Jugador> jugadores);
    void mostrarMensaje(String mensaje);
    void solicitarAccionesApuesta(List<Jugador> jugadores, int apuestaMinima, int apuestaCompleta, Jugador jugadorInicial);
    void finalizarJuego(String ganador, int fichasGanadas);
}