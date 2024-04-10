import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String ruta = "referencias.txt";

    /*
     * Obtiene la ruta del archivo de referencias.
     * @return: la ruta del archivo de referencias.
    */
    public static String getRuta() {
        return ruta;
    }

    /*
     * Simula referencias a memoria de un proceso especificado.
     * Corresponde a la parte 1 del enunciado.
    */
    public void simularRefencias(){
        int NF, NC;
        int TP, NR, NP;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el tamaño de pagina: ");
        TP = scanner.nextInt();
        System.out.println("Ingrese el número de filas: ");
        NF = scanner.nextInt();
        System.out.println("Ingrese el número de columnas: ");
        NC = scanner.nextInt();

        NP = (int) Math.ceil((2 * NF * NC + 9.0) / (TP / 4));
        NR = 18 * (NF - 2) * (NC - 2) + NF * NC;

        try {
            FileWriter archivo = new FileWriter(ruta);
            BufferedWriter escritorArchivo = new BufferedWriter(archivo);
            escritorArchivo.write("TP=" + TP + "\n");
            escritorArchivo.write("NF=" + NF + "\n");
            escritorArchivo.write("NC=" + NC + "\n");
            escritorArchivo.write("NF_NC_Filtro=3\n");
            escritorArchivo.write("NR=" + NR + "\n");
            escritorArchivo.write("NP=" + NP +"\n");
            escritorArchivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Simulacion simulacion = new Simulacion(TP, NF, NC);
        simulacion.iniciarSimulacion();

        scanner.close();
    }

    /*
     * Corresponde a la parte 2 del enunciado.
    */
    public void realizarCalculos(){
        int NMP;
        String ruta;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el número de marcos de página: ");
        NMP = scanner.nextInt();
        System.out.println("Ingrese la ruta del archivo de referencias: ");
        ruta = scanner.next();
        scanner.close();

        Calculo calculo = new Calculo(NMP, ruta);
        calculo.realizarCalculos();
    }

    /*
     * Método principal.
     * Se encarga de mostrar el menú de opciones y ejecutar la opción seleccionada.
    */
    public static void main(String[] args) {
        int opcion;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIngrese la opción deseada: \n");
        System.out.println("1. Simular referencias");
        System.out.println("2. Realizar cálculos");
        System.out.println("0. Salir");
        opcion = scanner.nextInt();

        Main main = new Main();

        switch (opcion) {
            case 1:
                main.simularRefencias();
                break;
            case 2:
                main.realizarCalculos();
                break;
            default:
                break;
        }
        scanner.close();
    }
}