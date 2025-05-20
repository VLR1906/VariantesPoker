public class CartaOculta extends Carta {
    public CartaOculta(Carta original) {
        super(original.getValor(), original.getPalo());
    }

    @Override
    public boolean esVisible() {
        return false;
    }

    @Override
    public String toString() {
        return "[Carta oculta]";
    }
}
