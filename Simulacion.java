import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Simulacion {
    private int tp,nf, nc;
    private int[][] mat1, mat2, mat3;
    private BufferedWriter escritorArchivo;

    /*
     * Constructor de la clase Simulacion.
     * @param tp: tamaño de página.
     * @param nf: número de filas.
     * @param nc: número de columnas.
    */
    public Simulacion(int tp, int nf, int nc) {
        this.tp = tp;
        this.nf = nf;
        this.nc = nc;
        mat1 = new int[nf][nc];
        mat2 = new int[3][3];
        mat3 = new int[nf][nc];
    }

    /*
     * Inicializa las 2 matrices con valores aleatorios.
     * Inicializa el atributos de escritura.
     * Iniciliza el proceso.
    */
    public void iniciarSimulacion() {
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nc; j++) {
                mat1[i][j] = (int) (Math.random() * 255);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mat2[i][j] = (int) (Math.random() * 255);
            }
        }
        try {
            FileWriter archivo = new FileWriter(Main.getRuta(), true);
            BufferedWriter escritor = new BufferedWriter(archivo);
            this.escritorArchivo = escritor;

            proceso();

            escritorArchivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Calcula el desplazamiento de una matriz en base a sus dimensiones y sus indices.
     * @param nf: número de filas de la matriz.
     * @param nc: número de columnas de la matriz.
     * @param i: índice de la fila, indexado en 0.
     * @param j: índice de la columna, indexado en 0.
     * @return: el desplazamiento de la matriz.
    */
    public int calcularDesplazamiento(int nf, int nc, int i, int j){
        return ((nc * i + j) * 4) % (tp - tp % 4);
    }

    /*
     * Calcula la cantidad de paginas de una matriz en base a sus dimensiones y sus indices.
     * @param nf: número de filas de la matriz.
     * @param nc: número de columnas de la matriz.
     * @param i: índice de la fila, indexado en 0.
     * @param j: índice de la columna, indexado en 0.
     * @param desplazamiento: desplazamiento acumulado de la matriz.
     * @return: el desplazamiento de la matriz.
    */
    public int calcularPaginas(int nf, int nc, int i, int j, int desplazamiento){
        return ((nc * i + j) * 4) / (tp - tp % 4) + (desplazamiento / (tp - tp % 4));
    }

    /*
     * Reporta la referencia de una matriz en el archivo de salida.
     * @param mat: matriz a la que se le reportará la referencia.
     * @param i: índice de la fila, indexado en 0.
     * @param j: índice de la columna, indexado en 0.
     * @param tipo: tipo de referencia, 'R' para lectura y 'W' para escritura.
    */
    public void reportarReferencia(int[][] mat,int i, int j, char tipo){
        char tipoMatriz = 'M';
        if (mat == mat2) tipoMatriz = 'F';
        else if (mat == mat3) tipoMatriz = 'R';

        // Las siguientes 4 líneas de código calculan el desplazamiento y paginas del filtro y la matriz para realizar calculos acumulativos.
        // Los indices de una matriz estan indexados desde 0, por lo que se le resta 1 a los indices de la matriz.
        // Sin embargo, necesitamos la referencia al final de la matriz, por lo que se le suma 1 a j.
        int desplazamientoDelFiltroTotal = calcularDesplazamiento(3,3,(3-1),(3));
        int paginasDelFiltroTotal = calcularPaginas(3,3,(3-1),(3) ,0);
        int desplazamientoDeMatrizTotal = calcularDesplazamiento(nf,nc,(nf-1),(nc));
        int paginasDeMatrizTotal = calcularPaginas(nf,nc,(nf-1),(nc) ,0);

        // Calcula el desplazamiento y la página de la referencia según el tipo de matriz.
        int pagina, desplazamiento;
        if (tipoMatriz == 'M') {
            desplazamiento = desplazamientoDelFiltroTotal + calcularDesplazamiento(nf,nc,i,j);
            pagina = paginasDelFiltroTotal + calcularPaginas(nf,nc,i,j,desplazamiento);
        } else if (tipoMatriz == 'R') {
            desplazamiento = desplazamientoDelFiltroTotal + desplazamientoDeMatrizTotal + calcularDesplazamiento(nf,nc,i,j);
            pagina = paginasDelFiltroTotal + paginasDeMatrizTotal + calcularPaginas(nf,nc,i,j,desplazamiento);
        } else {
            desplazamiento = calcularDesplazamiento(3,3,i,j);
            pagina = calcularPaginas(3,3,i,j,desplazamiento);
        }
        desplazamiento = desplazamiento % (tp - tp % 4);

        // Escribe la referencia en el archivo de salida.
        try {
            escritorArchivo.write(tipoMatriz + "["+ i +"][" + j + "]," + pagina + "," + desplazamiento +"," + tipo +"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Escribe un valor en una matriz y reporta la referencia en el archivo de salida.
     * @param mat: matriz en la que se escribirá el valor.
     * @param i: índice de la fila, indexado en 0.
     * @param j: índice de la columna, indexado en 0.
     * @param valor: valor que se escribirá en la matriz.
    */
    public void escribir(int[][] mat, int i, int j, int valor){
        reportarReferencia(mat, i, j, 'W');
        mat[i][j] = valor;
    }

    /*
     * Lee un valor de una matriz y reporta la referencia en el archivo de salida.
     * @param mat: matriz de la que se leerá el valor.
     * @param i: índice de la fila, indexado en 0.
     * @param j: índice de la columna, indexado en 0.
     * @return: el valor leído de la matriz.
    */
    public int leer(int[][] mat, int i, int j){
        reportarReferencia(mat, i, j, 'R');
        return mat[i][j];
    }

    /*
     * Ejecuta el algoritmo especificado.
     * Para el caso, se trata de una convolución de matrices.
    */
    public void proceso() {
        for (int i = 1; i < nf - 1; i++) {
            for (int j = 1; j < nc - 1; j++) {
                int acum = 0;
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        int i2 = i + a;
                        int j2 = j + b;
                        int i3 = 1 + a;
                        int j3 = 1 + b;
                        acum += (leer(mat1,i2,j2) * leer(mat2,i3,j3));
                    }
                }
                if (acum >= 0 && acum <= 255) {
                    escribir(mat3,i,j,acum);
                } else if (acum < 0) {
                    escribir(mat3,i,j,0);
                } else {
                    escribir(mat3,i,j,255);
                }
            }
        }
        for (int i = 0; i < nc; i++) {
            escribir(mat3,0,i,0);
            escribir(mat3,nf - 1,i,255);
        }
        for (int i = 1; i < nf - 1; i++) {
            escribir(mat3,i,0,0);
            escribir(mat3,i,nc - 1,255);
        }
    }
}
