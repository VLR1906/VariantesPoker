import java.text.Normalizer;

public class Carta {
    private String valor;
    private String palo;
    private boolean visible;

    public Carta(String valor, String palo) {
        this(valor, palo, true);
    }

    public Carta(String valor, String palo, boolean visible) {
        this.valor = valor;
        this.palo = palo;
        this.visible = visible;
    }

    public String getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
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

    public String getNombreArchivo() {
        String valorFormateado;
        switch (valor.toUpperCase()) {
            case "A": valorFormateado = "as"; break;
            case "J": valorFormateado = "jota"; break;
            case "Q": valorFormateado = "reina"; break;
            case "K": valorFormateado = "rey"; break;
            default:  valorFormateado = valor.toLowerCase();
        }


        String paloNormalizado = normalizarTexto(palo.toLowerCase());

        return valorFormateado + "_" + paloNormalizado + ".png";
    }
    private String normalizarTexto(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replace("ñ", "n");
    }
}

