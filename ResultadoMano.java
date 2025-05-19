public class ResultadoMano {
    private int puntuacion;
    private String combinacion;

    public ResultadoMano(int puntuacion, String combinacion) {
        this.puntuacion = puntuacion;
        this.combinacion = combinacion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getCombinacion() {
        return combinacion;
    }
}

