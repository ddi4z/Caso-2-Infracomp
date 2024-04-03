import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Calculo {
    private int NMP;
    private String ruta;

    private int nr, np;
    private String[] referencias;
    private Conteo conteo;

    private int[] tablaPaginas;
    private int[] tablaAuxiliar;
    private boolean[] marcosPagina;


    public Calculo(int NMP, String ruta){
        this.NMP = NMP;
        this.marcosPagina = new boolean[NMP];
        this.ruta = ruta;
    }

    public void cargarArchivo() {
        int lineasProcesadas = 0;
        try {
            Scanner scanner = new Scanner(new File(ruta));
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                if (linea.trim().isEmpty()) {
                    break;
                }
                if  (lineasProcesadas == 4) {
                    this.nr = Integer.parseInt(linea.split("=")[1]);
                    this.referencias = new String[this.nr];
                }
                if  (lineasProcesadas == 5){
                    this.np = Integer.parseInt(linea.split("=")[1]);
                    this.tablaPaginas = new int[this.np];
                    this.tablaAuxiliar = new int[this.np];
                    this.conteo = new Conteo(this.np);
                }
                if  (lineasProcesadas > 5){
                    this.referencias[lineasProcesadas - 6] = linea;
                }
                lineasProcesadas++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void inicializarEstructuras() {
        for (int i = 0; i < NMP; i++) {
            this.marcosPagina[i] = false;
        }
        for (int i = 0; i < this.np; i++) {
            this.tablaPaginas[i] = -1;
            this.tablaAuxiliar[i] = i;
        }
    }

    public void realizarCalculos(){
        cargarArchivo();
        inicializarEstructuras();

        String[] referencia;
        int pagina;
        int hits = 0;
        int misses = 0;
        Actualizador actualizador = new Actualizador(this.conteo);
        for (int i = 0; i < this.nr; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            referencia = this.referencias[i].split(",");
            pagina = Integer.parseInt(referencia[1]);

            if ((i+1)%4 == 0) actualizador.start();

            if (this.tablaPaginas[pagina] == -1) {
                misses++;
                int marco = 0;
                while (marco < this.NMP && this.marcosPagina[marco]) {
                    marco++;
                }
                if (marco < this.NMP) {
                    this.tablaPaginas[pagina] = marco;
                    this.marcosPagina[marco] = true;
                    this.conteo.referenciarPagina(pagina);
                } else {
                    int idx  = this.conteo.obtenerPaginaAEliminar(this.tablaPaginas);

                    this.tablaPaginas[pagina] = this.tablaPaginas[idx];
                    this.tablaPaginas[idx] = -1;
                    this.conteo.referenciarPagina(pagina);
                }
            } else {
                hits++;
                this.conteo.referenciarPagina(pagina);
            }

        }
        System.out.println("Hits: " + hits + " = " + (hits*100.0/this.nr) + "%");
        System.out.println("Fallas: " + misses + " = " + (misses*100.0/this.nr) + "%");
        System.out.println("Tiempo de ejecución: (hits * 30) ns + (misses * 10000000) ns = " + hits*30 + " + " + misses*10000000 + " = " + (hits*30 + misses*10000000) + " ns");
        System.out.println("Tiempo si todo fuera fallos: " + this.nr*10000000 + " ns");
        System.out.println("Tiempo si todo fuera hits: " + this.nr*30 + " ns");
    }
}
