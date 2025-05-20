import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] opciones = {"5 Cards Draw", "7 Cards-Stud"};
            int eleccion = JOptionPane.showOptionDialog(
                    null,
                    "¿Qué juego quieres jugar?",
                    "Menú Principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (eleccion == 0) {
                iniciarCincoCartasDraw();
            } else if (eleccion == 1) {
                iniciarSieteCartasStud();
            }
        });
    }

    private static void iniciarCincoCartasDraw() {
        List<Jugador> jugadores = new ArrayList<>();
        int cantidad = 0;

        do {
            String input = JOptionPane.showInputDialog("¿Cuántos jugadores participarán? (mínimo 2, máximo 7)");
            if (input == null) return;
            try {
                cantidad = Integer.parseInt(input);
                if (cantidad < 2 || cantidad > 7) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar un número entre 2 y 7.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (cantidad < 2 || cantidad > 7);

        for (int i = 1; i <= cantidad; i++) {
            String nombre = JOptionPane.showInputDialog("Nombre del jugador " + i + ":");
            if (nombre == null || nombre.isEmpty()) nombre = "Jugador " + i;
            jugadores.add(new Jugador(nombre, true));
        }

        new InterfazJuego(jugadores);
    }

    private static void iniciarSieteCartasStud() {
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
