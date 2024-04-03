import java.util.HashSet;

public class Conteo {
    private int[] rBits;
    private HashSet<Integer> paginasReferenciadas;
    private String estado;

    public synchronized void actualizarRBits() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        estado = "Actualizando";
        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = rBits[i]>>1;
            if (paginasReferenciadas.contains(i)) {
                rBits[i] += 1<<7;
            }
        }
        paginasReferenciadas.clear();
        estado = "Esperando";
        notify();
    }

    public synchronized void pedirActualizar() {
        notify();
    }

    public synchronized int obtenerPaginaAEliminar(int[] tablaPaginas) {
        if (estado.equals("Actualizando")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int idx = 0, menor = 256;
        for (int j = 0; j < tablaPaginas.length; j++) {
            if (tablaPaginas[j] != -1 && this.rBits[j] < menor) {
                menor = this.rBits[j];
                idx = j;
            }
        }
        return idx;
    }

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
