public class Carta {
    private String valor;
    private String palo;
    private boolean visible;

    public Carta(String valor, String palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public String getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
    }

    public String getNombreArchivo() {
        return valor + "_de_" + palo;
    }
    public class UtilCarta {
        public static String obtenerRutaImagen(Carta carta) {
            String valor = carta.getValor().toLowerCase();
            String palo = carta.getPalo().toLowerCase(); // "corazones", "picas", etc.
            return "imagenes/" + valor + "_" + palo + ".png";
        }
    }
    public boolean esVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }



    @Override
    public String toString() {
        return (visible ? valor + " de " + palo : "Carta Oculta");

    }

}

