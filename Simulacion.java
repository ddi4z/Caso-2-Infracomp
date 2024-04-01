import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Simulacion {
    private int tp,nf, nc;
    private int[][] mat1, mat2, mat3;
    private BufferedWriter escritorArchivo;

    public Simulacion(int tp, int nf, int nc) {
        this.tp = tp;
        this.nf = nf;
        this.nc = nc;
        mat1 = new int[nf][nc];
        mat2 = new int[3][3];
        mat3 = new int[nf][nc];
    }

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

    public void reportarReferencia(int[][] mat,int i, int j, char tipo){
        char tipoMatriz = 'M';
        if(mat == mat2) tipoMatriz = 'F';
        else if(mat == mat3) tipoMatriz = 'R';

        int pagina, desplazamiento;

        if (tipoMatriz == 'M') {
            desplazamiento = (4 + ((nf * i + j) * 4) % (tp - tp % 4));
            pagina = 2 + ((nc * i + j)*4)/(tp - tp % 4) + (desplazamiento / (tp - tp % 4));
            desplazamiento = desplazamiento % (tp - tp % 4);
        } else if (tipoMatriz == 'R') {
            desplazamiento = (4 + ((4* nc * nf) % (tp - tp % 4))  + ((nf * i + j) * 4) % (tp - tp % 4));
            pagina = 2 + ((4 * nc * nf) / (tp - tp % 4)) + ((nc * i + j) * 4)/(tp - tp % 4) + (desplazamiento / (tp - tp % 4));
            desplazamiento = desplazamiento % (tp - tp % 4);
        } else {
            pagina = (4 * (3 * i + j)) / (tp - tp % 4) ;
            desplazamiento = ((3 * i + j) * 4) % (tp - tp % 4);
        }
        try {
            escritorArchivo.write(tipoMatriz + "["+ i +"][" + j + "]," + pagina + "," + desplazamiento +",W\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribir(int[][] mat, int i, int j, int valor){
        reportarReferencia(mat, i, j, 'W');
        mat[i][j] = valor;
    }

    public int leer(int[][] mat, int i, int j){
        reportarReferencia(mat, i, j, 'R');
        return mat[i][j];
    }

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
