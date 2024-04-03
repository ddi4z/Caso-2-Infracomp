import java.util.HashSet;

public class Conteo {
    private int[] rBits;
    private HashSet<Integer> paginasReferenciadas;
    private String estado;

    /*
     * Este método se encarga de actualizar los bits R de las páginas.
    */
    public synchronized void actualizarRBits() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        estado = "Actualizando";
        for (int i = 0; i < rBits.length; i++) {
            // Desplazar bits a la derecha.
            rBits[i] = rBits[i]>>1;
            if (paginasReferenciadas.contains(i)) {
                // Encender el octavo bit.
                rBits[i] += 1<<7;
            }
        }
        // Limpiar lista de páginas referenciadas.
        paginasReferenciadas.clear();

        estado = "Esperando";
        notify();
    }

    /*
     * Este método se encarga de pedir la actualización de los bits R.
    */
    public synchronized void pedirActualizar() {
        notify();
    }

    /*
     * Este método se encarga de obtener el indice de la página a eliminar.
     * @param tablaPaginas: tabla de páginas.
     * @return índice de la página a eliminar.
    */
    public synchronized int obtenerPaginaAEliminar(int[] tablaPaginas) {
        if (estado.equals("Actualizando")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Encontrar la página con el conteo mas bajo, en caso de empate, se elige la primera.
        int idx = 0, menor = 256;
        for (int j = 0; j < tablaPaginas.length; j++) {
            if (tablaPaginas[j] != -1 && this.rBits[j] < menor) {
                menor = this.rBits[j];
                idx = j;
            }
        }
        return idx;
    }

    /*
     * Este método se encarga de referenciar una página, añadiendo su número a la lista de páginas referenciadas.
     * @param pagina: índice de la página a referenciar.
    */
    public synchronized void referenciarPagina(int pagina) {
        if (estado.equals("Actualizando")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        paginasReferenciadas.add(pagina);
    }

    /*
     * Constructor de la clase.
     * @param np: número de páginas.
    */
    public Conteo(int np) {
        this.rBits = new int[np];
        this.paginasReferenciadas = new HashSet<Integer>();
        this.estado = "Esperando";

        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = 0;
        }
        paginasReferenciadas.clear();
    }
}
