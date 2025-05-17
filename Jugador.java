import java.util.*;

public class Jugador {
    private String nombre;
    private boolean humano;
    private List<Carta> mano;
    private int fichas;
    private int apuestaActual;
    private boolean retirado;

    public Jugador(String nombre, boolean humano) {
        this.nombre = nombre;
        this.humano = humano;
        this.mano = new ArrayList<>();
        this.fichas = 100; // fichas iniciales
        this.apuestaActual = 0;
        this.retirado = false;
    }

    public void recibirCartas(List<Carta> cartas) {
        mano.clear();
        mano.addAll(cartas);
    }

    public void cambiarCartas(Baraja baraja, Scanner scanner) {
        if (!humano || retirado) {
            // Bot cambia aleatoriamente 1 o 2 cartas
            Random r = new Random();
            int cambios = r.nextInt(3);
            for (int i = 0; i < cambios; i++) {
                int index = r.nextInt(mano.size());
                mano.set(index, baraja.repartir(1).get(0));
            }
        } else {
            System.out.println("\nTu mano actual:");
            for (int i = 0; i < mano.size(); i++) {
                System.out.println((i + 1) + ". " + mano.get(i));
            }
            System.out.print("¿Cuántas cartas deseas cambiar? (0-5): ");
            int cantidad = scanner.nextInt();
            scanner.nextLine(); // limpia buffer

            for (int i = 0; i < cantidad; i++) {
                System.out.print("Número de carta a cambiar (1-5): ");
                int pos = scanner.nextInt();
                scanner.nextLine();
                if (pos >= 1 && pos <= 5) {
                    mano.set(pos - 1, baraja.repartir(1).get(0));
                }
            }
        }
    }

    public int apostar(int cantidad) {
        int apuesta = Math.min(cantidad, fichas);
        fichas -= apuesta;
        apuestaActual += apuesta;
        return apuesta;
    }

    public void igualar(int maxApuesta) {
        int diferencia = maxApuesta - apuestaActual;
        apostar(diferencia);
    }

    public void retirarse() {
        retirado = true;
    }

    public void reiniciarRonda() {
        apuestaActual = 0;
        retirado = false;
    }

    // Getters

    public String getNombre() {
        return nombre;
    }

    public boolean esHumano() {
        return humano;
    }

    public boolean estaRetirado() {
        return retirado;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public int getFichas() {
        return fichas;
    }

    public int getApuestaActual() {
        return apuestaActual;
    }

    public void ganarFichas(int cantidad) {
        fichas += cantidad;
    }

    //seven card stud

    
public void agregarCarta(Carta carta) {
    mano.add(carta);
}


public List<Carta> getCartasVisibles() {
    List<Carta> visibles = new ArrayList<>();
    for (Carta c : mano) {
        if (c.esVisible()) {
            visibles.add(c);
        }
    }
    return visibles;
}


public void mostrarMano() {
    System.out.println("Cartas de " + nombre + ":");
    for (Carta c : mano) {
        System.out.println(c);
    }
}


public void mostrarCartasVisibles() {
    System.out.println("Cartas visibles de " + nombre + ":");
    for (Carta c : mano) {
        if (c.esVisible()) {
            System.out.println(c);
        }
    }
}
}
