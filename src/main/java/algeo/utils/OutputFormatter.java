package algeo.utils;

public class OutputFormatter {

    // Membulatkan setiap elemen array dengan n angka di belakang koma
    public static double[] roundArr(double[] arr, int n) {
        double[] result = new double[arr.length];
        double factor = Math.pow(10, n);

        for (int i = 0; i < arr.length; i++) {
            result[i] = Math.round(arr[i] * factor) / factor;
        }

        return result;
    }

    public static double[][] roundMtrx(double[][] matrix, int n) {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            result[i] = roundArr(matrix[i], n);
        }

        return result;
    }

    // Fungsi print array dengan setiap elemen yang sudah dibulatkan
    public static void printArr(double[] arr, int n) {

        String format = "%." + n + "f";
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < arr.length; i++) {
            sb.append(String.format(format, arr[i]));
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        System.out.println(sb.toString());
    }

    public static void printMtrx(double[][] matrix, int n) {
        for (double[] row : matrix) {
            printArr(row, n);
        }
    }

}