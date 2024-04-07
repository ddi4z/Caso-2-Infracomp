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

    /*
     * Constructor de la clase Calculo.
     * @param NMP Número de marcos de página.
     * @param ruta Ruta del archivo de texto que contiene las referencias.
    */
    public Calculo(int NMP, String ruta){
        this.NMP = NMP;
        this.marcosPagina = new boolean[NMP];
        this.ruta = ruta;
    }

    /*
     * Este método se encarga de cargar el archivo de texto que contiene las referencias
     * y demas valores de interés dentro del archivo.
    */
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

    /*
     * Este método se encarga de inicializar las estructuras de datos que se utilizarán
     * para realizar los cálculos.
    */
    public void inicializarEstructuras() {
        for (int i = 0; i < NMP; i++) {
            this.marcosPagina[i] = false;
        }
        for (int i = 0; i < this.np; i++) {
            this.tablaPaginas[i] = -1;
            this.tablaAuxiliar[i] = i;
        }
    }

    /*
     * Este método se encarga de imprimir los resultados obtenidos al realizar los cálculos
     * necesarios para determinar la cantidad de hits y misses que se obtienen al realizar
     * las referencias contenidas en el archivo de texto.
     * @param hits Cantidad de hits obtenidos.
     * @param misses Cantidad de misses obtenidos.
    */
    public void imprimirResultados(int hits, int misses){
        System.out.println("Hits: " + hits + " = " + (hits*100.0/this.nr) + "%");
        System.out.println("Fallas: " + misses + " = " + (misses*100.0/this.nr) + "%");
        System.out.println("Tiempo de ejecución: (hits * 30) ns + (misses * 10000000) ns = " + hits*30 + " + " + misses*10000000 + " = " + (hits*30 + misses*10000000) + " ns");
        System.out.println("Tiempo si todo fuera fallos: " + this.nr*10000000 + " ns");
        System.out.println("Tiempo si todo fuera hits: " + this.nr*30 + " ns");
    }

    /*
     * Este método se encarga de realizar los cálculos necesarios para determinar
     * la cantidad de hits y misses que se obtienen al realizar las referencias
     * contenidas en el archivo de texto.
    */
    public void realizarCalculos(){
        cargarArchivo();
        inicializarEstructuras();

        String[] referencia;
        int pagina;
        boolean escritura;
        int hits = 0;
        int misses = 0;
        Actualizador actualizador = new Actualizador(this.conteo);
        actualizador.start();

        int i = 0;
        while (i < this.nr) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            referencia = this.referencias[i].split(",");

            pagina = Integer.parseInt(referencia[1]);
            escritura = referencia[3].equals("W");
            System.out.println(pagina +""+  referencia[3] + "" + escritura);
            
            if ((i+1)%4 == 0) conteo.pedirActualizar();

            if (this.tablaPaginas[pagina] == -1) {
                misses++;
                int marco = 0;
                while (marco < this.NMP && this.marcosPagina[marco]) {
                    marco++;
                }
                if (marco < this.NMP) {
                    this.tablaPaginas[pagina] = marco;
                    this.marcosPagina[marco] = true;
                    this.conteo.referenciarPagina(pagina, escritura);
                } else {
                    int idx  = this.conteo.obtenerPaginaAEliminar(this.tablaPaginas);

                    this.tablaPaginas[pagina] = this.tablaPaginas[idx];
                    this.tablaPaginas[idx] = -1;
                    this.conteo.referenciarPagina(pagina, escritura);
                }
            } else {
                hits++;
                this.conteo.referenciarPagina(pagina, escritura);
            }
            i++;
        }
        while (i%4 != 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

        conteo.pedirActualizar();
        actualizador.detener();
        imprimirResultados(hits, misses);
    }
}
