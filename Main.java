import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String ruta = "referencias.txt";

    public static String getRuta() {
        return ruta;
    }

    public static void main(String[] args) {
        int NF, NC;
        int TP, NR, NP;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el tamaño de pagina: ");
        TP = scanner.nextInt();
        System.out.println("Ingrese el número de filas: ");
        NF = scanner.nextInt();
        System.out.println("Ingrese el número de columnas: ");
        NC = scanner.nextInt();

        NP = (int) Math.ceil((2 * NF * NC + 9.0) / (TP/4));
        NR = 18 * (NF-2) * (NC-2) + NF * NC;

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
}