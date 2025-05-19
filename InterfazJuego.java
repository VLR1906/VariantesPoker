import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InterfazJuego extends JFrame {
    private List<Jugador> jugadores;
    private Jugador jugadorActual;
    private int indiceJugador = 0;
    private JLabel turnoLabel, dineroLabel;
    private JPanel cartasPanel;
    private JButton apostarBtn, igualarBtn, retirarseBtn, cambiarCartasBtn, siguienteBtn;
    private JTextField apuestaField;
    private Baraja baraja;
    private boolean faseCambio = false;
    private boolean rondaTerminada = false;
    private int apuestaMaxima = 0;


    public InterfazJuego(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        inicializarJugadores();
        configurarVentana();
        mostrarInformacionJugador();

    }

    private void inicializarJugadores() {
        for (Jugador j : jugadores) {
            j.reiniciarRonda();
            j.recibirCartas(baraja.repartir(5));
        }
    }

    private void configurarVentana() {
        setTitle("5 Card Draw Poker");
        setSize(900, 600);
        setLayout(null);
        getContentPane().setBackground(new Color(38, 142, 66));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        turnoLabel = new JLabel();
        turnoLabel.setForeground(Color.WHITE);
        turnoLabel.setBounds(350, 10, 400, 30);
        add(turnoLabel);

        dineroLabel = new JLabel();
        dineroLabel.setForeground(Color.WHITE);
        dineroLabel.setBounds(20, 30, 200, 30);
        add(dineroLabel);

        cartasPanel = new JPanel();
        cartasPanel.setBounds(100, 350, 700, 150);  // más ancho para mostrar 5 cartas
        cartasPanel.setOpaque(false);
        add(cartasPanel);

        // Inicializa botones y campo
        apostarBtn = new JButton("Apostar");
        igualarBtn = new JButton("Igualar");
        retirarseBtn = new JButton("Retirarse");
        cambiarCartasBtn = new JButton("Cambiar Cartas");
        siguienteBtn = new JButton("Siguiente");
        apuestaField = new JTextField(6);
        Dimension campoDimension = new Dimension(80, 20);  // más angosto y bajo
        apuestaField.setMaximumSize(campoDimension);
        apuestaField.setPreferredSize(campoDimension);

        // Panel lateral para controles
        JPanel controlesPanel = new JPanel();
        controlesPanel.setLayout(new BoxLayout(controlesPanel, BoxLayout.Y_AXIS));
        controlesPanel.setBounds(60, 150, 160, 300);  // colocación en la ventana
        controlesPanel.setOpaque(false);

        // Espaciado vertical entre componentes
        controlesPanel.add(new JLabel("Apuesta:"));
        controlesPanel.add(apuestaField);
        controlesPanel.add(Box.createVerticalStrut(10));
        controlesPanel.add(apostarBtn);
        controlesPanel.add(Box.createVerticalStrut(10));
        controlesPanel.add(igualarBtn);
        controlesPanel.add(Box.createVerticalStrut(10));
        controlesPanel.add(retirarseBtn);
        controlesPanel.add(Box.createVerticalStrut(10));
        controlesPanel.add(cambiarCartasBtn);
        controlesPanel.add(Box.createVerticalStrut(10));
        controlesPanel.add(siguienteBtn);

        add(controlesPanel);

// 🔽 Agrega esto justo aquí
        String[] combinaciones = {
                "     Par", "     Doble Par", "     Tercia", "     Escalera", "     Color",
                "     Full House", "     Póker", "     Escalera de color", "     Escalera real"
        };

        JList<String> combinacionesList = new JList<>(combinaciones);
        combinacionesList.setCellRenderer(new CombinacionRenderer());
        combinacionesList.setBackground(Color.WHITE);
        combinacionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane combinacionesScroll = new JScrollPane(combinacionesList);
        combinacionesScroll.setBounds(680, 20, 180, 200); // parte superior derecha
        add(combinacionesScroll);


        // Listeners
        apostarBtn.addActionListener(e -> realizarApuesta());
        igualarBtn.addActionListener(e -> igualarApuesta());
        retirarseBtn.addActionListener(e -> {
            jugadorActual.retirarse();
            mostrarMensaje("Te has retirado.");
            siguienteJugador();
        });

        cambiarCartasBtn.addActionListener(e -> cambiarCartas());
        siguienteBtn.addActionListener(e -> siguienteJugador());

        setVisible(true);
    }


    private JButton crearBoton(String texto, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(60, 400 + y - 60, 140, 30);
        return btn;
    }

    private void mostrarInformacionJugador() {
        jugadorActual = jugadores.get(indiceJugador);
        turnoLabel.setText("Turno de: " + jugadorActual.getNombre());
        dineroLabel.setText("Fichas: $" + jugadorActual.getFichas());
        mostrarCartas();
        actualizarBotones();
    }

    private void mostrarCartas() {
        cartasPanel.removeAll();
        cartasPanel.setLayout(new FlowLayout());

        System.out.println("Cartas del jugador " + jugadorActual.getNombre() + ":");

        for (Carta carta : jugadorActual.getMano()) {
            String nombreArchivo = carta.getNombreArchivo() + ".png";
            System.out.println("- " + nombreArchivo);  // DEBUG

            ImageIcon icon = new ImageIcon("C:\\Users\\victo\\IdeaProjects\\VariantesDePoker\\src\\imagenes\\" + nombreArchivo);
            Image image = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            JLabel cartaLabel = new JLabel(new ImageIcon(image));
            cartasPanel.add(cartaLabel);
        }

        cartasPanel.revalidate();
        cartasPanel.repaint();
    }


    private void actualizarBotones() {
        boolean activo = !jugadorActual.estaRetirado();
        apostarBtn.setEnabled(activo && !faseCambio);
        igualarBtn.setEnabled(activo && !faseCambio);
        retirarseBtn.setEnabled(activo);
        cambiarCartasBtn.setEnabled(activo && faseCambio);
        siguienteBtn.setEnabled(true);
    }

    private class CombinacionRenderer extends JLabel implements ListCellRenderer<String> {
        public CombinacionRenderer() {
            setOpaque(true);
            setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            switch (value) {
                case "     Par":
                    setForeground(Color.BLUE); break;
                case "     Doble Par":
                    setForeground(new Color(30, 144, 255)); break;
                case "     Tercia":
                    setForeground(Color.MAGENTA); break;
                case "     Escalera":
                    setForeground(Color.blue); break;
                case "     Color":
                    setForeground(new Color(0, 128, 0)); break;
                case "     Full House":
                    setForeground(new Color(138, 43, 226)); break;
                case "     Póker":
                    setForeground(Color.RED); break;
                case "     Escalera de color":
                    setForeground(new Color(0, 139, 139)); break;
                case "     Escalera real":
                    setForeground(new Color(188, 162, 24)); break;
                default:
                    setForeground(Color.BLACK);
            }
            setBackground(isSelected ? new Color(255, 255, 255) : Color.white);
            return this;
        }
    }

    private void realizarApuesta() {
        try {
            int cantidad = Integer.parseInt(apuestaField.getText());

            if (cantidad <= jugadorActual.getFichas() && cantidad > apuestaMaxima) {
                int diferencia = cantidad - jugadorActual.getApuestaActual();
                jugadorActual.apostar(diferencia);
                apuestaMaxima = cantidad;
                mostrarMensaje("Has subido la apuesta a $" + cantidad);
                siguienteJugador();
            } else {
                mostrarMensaje("La apuesta debe ser mayor que la actual (" + apuestaMaxima + ") y menor o igual a tus fichas.");
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingresa una cantidad válida.");
        }
    }


    private void igualarApuesta() {
        int diferencia = apuestaMaxima - jugadorActual.getApuestaActual();

        if (diferencia > 0 && diferencia <= jugadorActual.getFichas()) {
            jugadorActual.apostar(diferencia);
            mostrarMensaje("Has igualado con $" + apuestaMaxima);
        } else if (diferencia == 0) {
            mostrarMensaje("Ya has igualado la apuesta.");
        } else {
            mostrarMensaje("No tienes suficientes fichas para igualar.");
        }

        siguienteJugador();
    }


    private void cambiarCartas() {
        List<Carta> mano = jugadorActual.getMano();
        String entrada = JOptionPane.showInputDialog(this, "¿Qué cartas cambiar? (ej: 1,3,5)");
        if (entrada != null && !entrada.trim().isEmpty()) {
            String[] indices = entrada.split(",");
            for (String s : indices) {
                try {
                    int pos = Integer.parseInt(s.trim()) - 1;
                    if (pos >= 0 && pos < mano.size()) {
                        mano.set(pos, baraja.repartir(1).get(0));
                    }
                } catch (Exception ignored) {}
            }
        }

        mostrarCartas();  // Mostrar cartas actualizadas

        // Ahora mostramos mensaje SIN avanzar de inmediato
        JOptionPane.showMessageDialog(this, "Cartas cambiadas. Presiona 'Siguiente' para continuar.");

        cambiarCartasBtn.setEnabled(false);  // Ya no puede volver a cambiar
    }


    private void siguienteJugador() {
        if (rondaTerminada) {
            mostrarGanador();
            return;
        }

        indiceJugador++;
        if (indiceJugador >= jugadores.size()) {
            if (!faseCambio) {
                faseCambio = true;
                indiceJugador = 0;
                mostrarMensaje("Comienza la fase de cambio de cartas.");
            } else {
                rondaTerminada = true;
                mostrarGanador();
                return;
            }
        }

        mostrarInformacionJugador();
    }

    private void mostrarGanador() {
        Jugador ganador = EvaluadorMano.determinarGanador(jugadores);
        ganador.ganarFichas(jugadores.stream().mapToInt(Jugador::getApuestaActual).sum());

        // Obtener la combinación ganadora desde EvaluadorMano
        String combinacion = EvaluadorMano.getUltimaCombinacion();

        // Mostrar la mano gráficamente en el cartasPanel
        cartasPanel.removeAll();
        cartasPanel.setLayout(new FlowLayout());

        for (Carta carta : ganador.getMano()) {
            String nombreArchivo = carta.getNombreArchivo() + ".png";
            ImageIcon icon = new ImageIcon("C:\\Users\\victo\\IdeaProjects\\VariantesDePoker\\src\\imagenes\\" + nombreArchivo);
            Image image = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            JLabel cartaLabel = new JLabel(new ImageIcon(image));
            cartasPanel.add(cartaLabel);
        }

        cartasPanel.revalidate();
        cartasPanel.repaint();

        // Mostrar mensaje con el nombre y combinación
        mostrarMensaje("¡Ganador: " + ganador.getNombre() + "!\nCombinación ganadora: " + combinacion);

        int opcion = JOptionPane.showConfirmDialog(this, "¿Jugar otra ronda?");
        if (opcion == JOptionPane.YES_OPTION) {
            nuevaRonda();
        } else {
            System.exit(0);
        }
    }




    private void nuevaRonda() {
        this.baraja = new Baraja();
        faseCambio = false;
        rondaTerminada = false;
        indiceJugador = 0;
        apuestaMaxima = 0;

        for (Jugador j : jugadores) {
            j.reiniciarRonda();  // también reinicia su apuesta actual a 0
            j.recibirCartas(baraja.repartir(5));
        }

        mostrarInformacionJugador();
    }


    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
