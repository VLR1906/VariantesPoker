import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Jugador> jugadores = new ArrayList<>();

        int numJugadores = 0;
        while (numJugadores < 2 || numJugadores > 8) {
            String input = JOptionPane.showInputDialog(null, "¿Cuántos jugadores? (mínimo 2, máximo 8):");
            if (input == null) return;
            try {
                numJugadores = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un número válido.");
            }
        }

        for (int i = 1; i <= numJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(null, "Nombre del jugador " + i + ":");
            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Jugador " + i;
            }
            jugadores.add(new Jugador(nombre, true));
        }

        JuegoSieteCartasStud juego = new JuegoSieteCartasStud(jugadores);
        InterfazGUI gui = new InterfazGUI(juego);
        juego.setObserver(gui);

        juego.jugar();
    }
}
