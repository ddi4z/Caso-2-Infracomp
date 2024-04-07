import java.util.HashMap;

public class Conteo {
    private int[] rBits;
    private int[] mBits;
    private HashMap<Integer,Boolean> paginasReferenciadas;
    private String estado;

    /*
     * Este método se encarga de actualizar los bits R y M de las páginas.
    */
    public synchronized void actualizarRBits() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        estado = "Actualizando";
        for (int i = 0; i < rBits.length; i++) {
            // Establecer numeros en 0
            rBits[i] = 0;
            if (paginasReferenciadas.containsKey(i)) {
                rBits[i] = 1;
            }
            if (paginasReferenciadas.containsKey(i) && paginasReferenciadas.get(i)) {
                mBits[i] = 1;
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

        // Encontrar la página con la clase mas baja
        int idx = 0, menor = 4;
        for (int j = 0; j < tablaPaginas.length; j++) {
            if (tablaPaginas[j] != -1) {
                int currValue = 0;
                if (rBits[j] == 0 && mBits[j] == 0) {
                    currValue = 0;
                } else if (rBits[j] == 0 && mBits[j] == 1) {
                    currValue = 1;
                } else if (rBits[j] == 1 && mBits[j] == 0) {
                    currValue = 2;
                } else if (rBits[j] == 1 && mBits[j] == 1) {
                    currValue = 3;
                }
                if (currValue < menor) {
                    menor = currValue;
                    idx = j;
                }
            }
        }
        return idx;
    }

    /*
     * Este método se encarga de referenciar una página, añadiendo su número a la lista de páginas referenciadas.
     * @param pagina: índice de la página a referenciar.
    */
    public synchronized void referenciarPagina(int pagina, boolean modificado) {
        if (estado.equals("Actualizando")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        paginasReferenciadas.put(pagina, modificado);
    }

    /*
     * Constructor de la clase.
     * @param np: número de páginas.
    */
    public Conteo(int np) {
        this.rBits = new int[np];
        this.mBits = new int[np];
        this.paginasReferenciadas = new HashMap<>();
        this.estado = "Esperando";

        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = 0;
            mBits[i] = 0;
        }
        paginasReferenciadas.clear();
    }
}
