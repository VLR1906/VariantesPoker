public class CartaVisible extends Carta {
    public CartaVisible(Carta original) {
        super(original.getValor(), original.getPalo());
    }

    @Override
    public boolean esVisible() {
        return true;
    }
}