import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
    private CincoCartasDraw cincoCartasDraw;
    private boolean faseCambio = false;
    private boolean rondaTerminada = false;
    private int bote = 0;
    private int ronda = 1; // 1 = ronda a ciegas, 2 = ronda con cartas visibles
    private int apuestaMaxima = 0;
    private ImageIcon imagenReverso = new ImageIcon("C:\\Users\\victo\\IdeaProjects\\VariantesDePoker\\src\\imagenes\\reverso.png");

    public InterfazJuego(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        inicializarJugadores(); // ya los tienes
        this.cincoCartasDraw = new CincoCartasDraw(jugadores);
        inicializarJugadores();
        configurarVentana();
        mostrarInformacionJugador();
        mostrarMensaje("Primera ronda de apuestas. No se muestran cartas.");
        cambiarCartasBtn.setEnabled(false);


    }

    public void inicializarJugadores() {
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
        Dimension campoDimension = new Dimension(150, 30);  // más angosto y bajo
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

        String[] combinaciones = {
                "     Par", "     Doble Par", "     Tercia", "     Escalera", "     Color",
                "     Full House", "     Póker", "     Escalera de color", "     Escalera real"
        };

        JList<String> combinacionesList = new JList<>(combinaciones);
        combinacionesList.setCellRenderer(new CombinacionRenderer());
        combinacionesList.setBackground(Color.WHITE);
        combinacionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane combinacionesScroll = new JScrollPane(combinacionesList);
        combinacionesScroll.setBounds(680, 20, 180, 150); // parte superior derecha
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


    public void mostrarInformacionJugador() {
        jugadorActual = jugadores.get(indiceJugador);
        turnoLabel.setText("Turno de: " + jugadorActual.getNombre());
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Cambiar tamaño a 20

        dineroLabel.setText("Fichas: $" + jugadorActual.getFichas());
        dineroLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Cambiar tamaño a 18

        if (ronda == 2) {
            mostrarCartas();
        } else {
            ocultarCartas();
        }

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

    private void ocultarCartas() {
        cartasPanel.removeAll();
        for (int i = 0; i < 5; i++) {
            ImageIcon icon = new ImageIcon("C:\\Users\\victo\\IdeaProjects\\VariantesDePoker\\src\\imagenes\\reverso.png"); // Usa una imagen de carta oculta
            Image image = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            JLabel cartaLabel = new JLabel(new ImageIcon(image));
            cartasPanel.add(cartaLabel);
        }
        cartasPanel.revalidate();
        cartasPanel.repaint();
    }



    private void actualizarBotones() {
        boolean activo = !jugadorActual.estaRetirado();
        retirarseBtn.setEnabled(activo);
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
            Jugador jugadorActual = jugadores.get(indiceJugador);

            String resultado = cincoCartasDraw.realizarApuesta(jugadorActual, cantidad);
            mostrarMensaje(resultado);

            if (resultado.startsWith("Apostaste")) {
                mostrarInformacionJugador(); // si usas un método para actualizar la vista
                siguienteJugador();   // o avanzarTurno()
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingresa una cantidad válida.");
        }
    }




    private void igualarApuesta() {
        Jugador jugadorActual = jugadores.get(indiceJugador);

        if (cincoCartasDraw.igualarApuesta(jugadorActual)) {
            mostrarMensaje("Has igualado la apuesta.");
            mostrarInformacionJugador();
            actualizarBote(); // <-- agrega esto
            siguienteJugador();
        } else {
            mostrarMensaje("No tienes suficientes fichas para igualar.");
        }
    }
    private void actualizarBote() {
        Label boteLabel = new Label();
        boteLabel.setText("Bote: $" + cincoCartasDraw.getBote());
    }






    private void cambiarCartas() {
        mostrarCartas();

        String entrada = JOptionPane.showInputDialog(this, "¿Qué cartas cambiar? (ej: 1,3,5)");
        if (entrada != null && !entrada.trim().isEmpty()) {
            String[] indices = entrada.split(",");
            List<Integer> indicesACambiar = new ArrayList<>();

            for (String s : indices) {
                try {
                    int pos = Integer.parseInt(s.trim()) - 1; // el jugador usa 1-based, internamente usamos 0-based
                    indicesACambiar.add(pos);
                } catch (NumberFormatException ignored) {
                    // O puedes mostrar un error si prefieres
                }
            }

            // Llama al método de la clase lógica
            cincoCartasDraw.cambiarCartas(jugadorActual, indicesACambiar);

            mostrarCartas();  // Mostrar cartas actualizadas
            JOptionPane.showMessageDialog(this, "Cartas cambiadas. Presiona 'Siguiente' para continuar.");
        }
    }



    private void siguienteJugador() {
        indiceJugador++;

        // Saltar jugadores retirados
        while (indiceJugador < jugadores.size() && jugadores.get(indiceJugador).estaRetirado()) {
            indiceJugador++;
        }

        if (indiceJugador >= jugadores.size()) {
            if (ronda == 1) {

                // Pasar a segunda ronda de apuestas: cartas visibles
                ronda = 2;
                indiceJugador = 0;
                while (indiceJugador < jugadores.size() && jugadores.get(indiceJugador).estaRetirado()) {
                    indiceJugador++;
                }

                mostrarMensaje("Cartas reveladas. Segunda ronda de apuestas.");
                mostrarCartas(); // ahora sí se ven
                apostarBtn.setEnabled(true);
                igualarBtn.setEnabled(true);
                cambiarCartasBtn.setEnabled(false);
            } else if (ronda == 2) {
                // Pasar a ronda de cambio de cartas
                ronda = 3;
                indiceJugador = 0;
                while (indiceJugador < jugadores.size() && jugadores.get(indiceJugador).estaRetirado()) {
                    indiceJugador++;
                }

                cambiarCartasBtn.setEnabled(true);
                apostarBtn.setEnabled(false);
                igualarBtn.setEnabled(false);
                retirarseBtn.setEnabled(true);
                mostrarCartas();

                mostrarMensaje("Ronda de cambio de cartas. Cada jugador puede cambiar cartas.");

                // NO llames cambiarCartas() aquí. Espera a que el jugador presione el botón para cambiar.

                mostrarCartas(); // Mostrar cartas del jugador actual

            } else {
                // Después de la ronda de cambio, termina el juego y se muestra ganador
                rondaTerminada = true;
                mostrarGanador();
                return;
            }
        }

        mostrarInformacionJugador();
    }




    private void mostrarGanador() {
        int boteGanado = cincoCartasDraw.getBote(); // Obtener el monto antes de reiniciar
        Jugador ganador = cincoCartasDraw.entregarBoteAlGanador();
        cincoCartasDraw.reiniciarBote(); // Ahora sí lo reinicias

        if (ganador == null) {
            mostrarMensaje("No hay un ganador claro.");
            return;
        }

        String combinacion = EvaluadorMano.getUltimaCombinacion();

        StringBuilder manoGanadora = new StringBuilder();
        for (Carta carta : ganador.getMano()) {
            manoGanadora.append(carta.toString()).append(" ");
        }

        mostrarMensaje("¡Ganador: " + ganador.getNombre() + "!\n" +
                "Combinación ganadora: " + combinacion + "\n" +
                "Ganó $" + boteGanado + " en el bote.");

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
        bote = 0;
        ronda = 1;
        apuestaMaxima = 0;

        cincoCartasDraw.iniciarNuevaRonda();  // DELEGA la reinicialización

        mostrarInformacionJugador();
        cambiarCartasBtn.setEnabled(false);
        apostarBtn.setEnabled(true);
        igualarBtn.setEnabled(true);
        retirarseBtn.setEnabled(true);
    }



    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
