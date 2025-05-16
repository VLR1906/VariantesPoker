import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Jugador> jugadores = new ArrayList<>();

        System.out.print("¿Cuántos jugadores participarán? (mínimo 2): ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        while (cantidad < 2) {
            System.out.print("Debe haber al menos 2 jugadores. Ingresa de nuevo: ");
            cantidad = scanner.nextInt();
            scanner.nextLine();
        }

        for (int i = 1; i <= cantidad; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            jugadores.add(new Jugador(nombre, true));
        }

        CincoCartasDraw juego = new CincoCartasDraw(jugadores);
        juego.jugar();
    }
}

