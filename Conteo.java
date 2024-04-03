import java.util.HashSet;

public class Conteo {
    private int[] rBits;
    private HashSet<Integer> paginasReferenciadas;

    public synchronized void actualizarRBits() {
        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = rBits[i]>>1;
            if (paginasReferenciadas.contains(i)) {
                rBits[i] += 1<<7;
            }
        }
        paginasReferenciadas.clear();
    }

    public synchronized int obtenerPaginaAEliminar(int[] tablaPaginas) {
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
        paginasReferenciadas.add(pagina);
    }

    public Conteo(int np) {
        this.rBits = new int[np];
        this.paginasReferenciadas = new HashSet<Integer>();

        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = 0;
        }
        paginasReferenciadas.clear();
    }
}
