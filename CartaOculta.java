public class CartaOculta extends Carta {
    private Carta original;

    public CartaOculta(Carta original) {
        super(original.getValor(), original.getPalo());
        this.original = original;
    }

    public Carta getOriginal() {
        return original;
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
