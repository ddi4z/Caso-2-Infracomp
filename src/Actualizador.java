

public class Actualizador extends Thread{
    private Conteo conteo;
    private boolean ejecutando = true;

    /*
     * Este método se encarga de realizar peticiones para actualizar los bits R.
     * Se ejecuta en un thread aparte.
     * Se detiene cuando se terminan de procesar todas las referencias.
    */
    @Override
    public void run() {
        while (ejecutando){
            conteo.actualizarRBits();
        }
    }

    /*
     * Detiene la ejecución del thread.
    */
    public void detener(){
        ejecutando = false;
    }

    /*
     * Constructor de la clase.
     * @param conteo: instancia de conteo que contiene la información necesaria para actualizar los bits R.
    */
    public Actualizador(Conteo conteo) {
        this.conteo = conteo;
    }

}