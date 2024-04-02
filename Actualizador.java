import java.util.HashSet;

public class Actualizador {
    private int[] rBits;
    private HashSet<Integer> paginasReferenciadas;

    public void actualizarRBits() {
        for (int i = 0; i < rBits.length; i++) {
            rBits[i] = rBits[i]>>1;
            if (paginasReferenciadas.contains(i)) {
                rBits[i] += 1<<31;
            }
        }
        paginasReferenciadas.clear();
    }

    public Actualizador(int[] rBits, HashSet<Integer> paginasReferenciadas) {
        this.rBits = rBits;
        this.paginasReferenciadas = paginasReferenciadas;
    }

}
