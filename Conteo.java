public class Conteo {
    private int[] rBits;
    private int[] mBits;

    /*
     * Este método se encarga de actualizar los bits R y M de las páginas.
    */
    public synchronized void actualizarRBits() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = 0;
        }
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
     * Este método se encarga de referenciar una página.
     * @param pagina: índice de la página a referenciar.
    */
    public synchronized void referenciarPagina(int pagina, boolean modificado) {
        rBits[pagina] = 1;
        if (modificado) {
            mBits[pagina] = 1;
        }
    }

    /*
     * Constructor de la clase.
     * @param np: número de páginas.
    */
    public Conteo(int np) {
        this.rBits = new int[np];
        this.mBits = new int[np];
        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = 0;
            mBits[i] = 0;
        }
    }
}
