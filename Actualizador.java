public class Actualizador extends Thread{
    private Conteo conteo;
    private boolean ejecutando = true;

    @Override
    public void run() {
        while (ejecutando){
            conteo.actualizarRBits();
        }
    }

    public void detener(){
        ejecutando = false;
    }

    public Actualizador(Conteo conteo) {
        this.conteo = conteo;
    }

}
