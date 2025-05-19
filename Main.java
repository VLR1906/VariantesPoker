import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Jugador> jugadores = new ArrayList<>();

            int cantidad;
            do {
                String input = JOptionPane.showInputDialog("¿Cuántos jugadores participarán? (mínimo 2)");
                if (input == null) return;
                cantidad = Integer.parseInt(input);
            } while (cantidad < 2);

            for (int i = 1; i <= cantidad; i++) {
                String nombre = JOptionPane.showInputDialog("Nombre del jugador " + i + ":");
                if (nombre == null || nombre.isEmpty()) nombre = "Jugador " + i;
                jugadores.add(new Jugador(nombre, true));
            }

            // Inicia la interfaz gráfica
            new InterfazJuego(jugadores);
        });
    }
}
