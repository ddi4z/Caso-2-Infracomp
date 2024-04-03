public class Actualizador {
    private Conteo conteo;

    public void start() {
        conteo.actualizarRBits();
    }

    public Actualizador(Conteo conteo) {
        this.conteo = conteo;
    }

}
