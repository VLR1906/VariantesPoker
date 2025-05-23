import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class InterfazGUI extends JFrame implements JuegoObserver {

    private JuegoSieteCartasStud juego;
    private JTextArea areaMensajes;
    private JPanel panelAcciones;

    private Map<Jugador, String> decisionesTemporales = new HashMap<>();
    private Iterator<Jugador> iteradorJugadores;
    private int apuestaMinima;
    private int apuestaCompleta;

    public InterfazGUI(JuegoSieteCartasStud juego) {
        this.juego = juego;
        juego.setObserver(this);

        setTitle("Siete Cartas Stud");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaMensajes);
        add(scroll, BorderLayout.EAST);

        panelAcciones = new JPanel();
        add(panelAcciones, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");


        if (mensaje.contains("gana el pozo")) {
            JOptionPane.showMessageDialog(this, mensaje, "¡Ganador!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void solicitarAccionesApuesta(List<Jugador> jugadores, int apuestaMinima, int apuestaCompleta, Jugador bringInJugador) {
        this.apuestaMinima = apuestaMinima;
        this.apuestaCompleta = apuestaCompleta;
        decisionesTemporales.clear();
        iteradorJugadores = jugadores.iterator();
        siguienteJugadorParaApostar();
    }

    private void siguienteJugadorParaApostar() {
        while (iteradorJugadores.hasNext()) {
            Jugador jugador = iteradorJugadores.next();
            if (!jugador.estaRetirado()) {
                mostrarOpcionesPara(jugador);
                return;
            }
        }

        juego.procesarDecisiones(disciplinarDecisiones(), apuestaMinima, apuestaCompleta);
    }

    private void mostrarOpcionesPara(Jugador jugador) {
        panelAcciones.removeAll();
        panelAcciones.setLayout(new FlowLayout());

        JLabel etiqueta = new JLabel("Turno de " + jugador.getNombre() + ":");
        JButton btnCall = new JButton("Igualar");
        JButton btnRaise = new JButton("Subir");
        JButton btnFold = new JButton("Retirarse");

        btnCall.addActionListener(e -> registrarDecision(jugador, "call"));
        btnRaise.addActionListener(e -> registrarDecision(jugador, "raise"));
        btnFold.addActionListener(e -> registrarDecision(jugador, "fold"));

        panelAcciones.add(etiqueta);
        panelAcciones.add(btnCall);
        panelAcciones.add(btnRaise);
        panelAcciones.add(btnFold);

        panelAcciones.revalidate();
        panelAcciones.repaint();
    }

    private void registrarDecision(Jugador jugador, String decision) {
        String decisionTraducida;
        switch (decision) {
            case "call":
                decisionTraducida = "Igualar";
                break;
            case "raise":
                decisionTraducida = "Subir";
                break;
            case "fold":
                decisionTraducida = "Retirarse";
                break;
            default:
                decisionTraducida = decision;
        }

        mostrarMensaje(jugador.getNombre() + " eligió: " + decisionTraducida);
        decisionesTemporales.put(jugador, decision);
        siguienteJugadorParaApostar();
    }

    private Map<Jugador, String> disciplinarDecisiones() {
        return new HashMap<>(decisionesTemporales);
    }

    @Override
    public void actualizarCartas(List<Jugador> jugadores) {
        areaMensajes.setText("");
        getContentPane().removeAll();
        add(new JScrollPane(areaMensajes), BorderLayout.EAST);

        JPanel panelJugadores = new JPanel();
        panelJugadores.setLayout(new GridLayout(jugadores.size(), 1));

        for (Jugador j : jugadores) {
            JPanel panelCartas = new JPanel();
            String texto = j.getNombre() + " - Apuesta: $" + j.getApuestaActual();
            panelCartas.setBorder(BorderFactory.createTitledBorder(texto));

            for (Carta c : j.getCartas()) {
                JLabel etiqueta = new JLabel(obtenerIconoCarta(c));
                panelCartas.add(etiqueta);
            }

            panelJugadores.add(panelCartas);
        }

        add(panelJugadores, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private ImageIcon obtenerIconoCarta(Carta carta) {
        String nombreArchivo = carta.esVisible() ? carta.getNombreArchivo() + ".png" : "reverso.png";
        String rutaRelativa = "imagenes/" + nombreArchivo;

        java.net.URL url = getClass().getClassLoader().getResource(rutaRelativa);

        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        } else {
            System.err.println("No se encontró la imagen: " + rutaRelativa);
            return null;
        }
    }

    @Override
    public void finalizarJuego(String ganador, int fichasGanadas) {
        panelAcciones.removeAll();
        panelAcciones.revalidate();
        panelAcciones.repaint();


        JOptionPane.showMessageDialog(this,
                "¡Juego terminado!\nGanador: " + ganador + "\nFichas ganadas: " + fichasGanadas,
                "Resultado Final",
                JOptionPane.INFORMATION_MESSAGE
        );

        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
            System.exit(0);
        });
    }
}
