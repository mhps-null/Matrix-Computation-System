package algeo.utils;

import java.io.*;
import java.util.*;
import algeo.modules.Matrix;

public class MatrixParser {

    public static double parseFraction(String input) {
        input = input.trim();
        input = input.replace(",", ".");
        if (input.contains("/")) {
            String[] parts = input.split("/");
            if (parts.length == 2) {
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);
                if (denominator == 0)
                    throw new ArithmeticException("Pembagi tidak boleh 0");
                return numerator / denominator;
            } else {
                throw new NumberFormatException("Format pecahan tidak valid: " + input);
            }
        } else {
            return Double.parseDouble(input);
        }
    }

    public static Matrix parseMatrixFromFileTxt(String namaFile) {
        File file = new File(namaFile);
        if (!file.exists()) {
            return null;
        }

        List<double[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(',', '.');
                String[] tokens = line.trim().split("\\s+");

                double[] row = new double[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = parseFraction(tokens[i]);
                }
                rows.add(row);
            }
        }

        catch (IOException e) {
            System.err.println("Error membaca file: " + e.getMessage());
        }

        double[][] matrix = new double[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i] = rows.get(i);
        }
        return new Matrix(matrix);
    }

    // Matrix parser khusus regresi polinomial berganda
    public static Object[] matrixParserRegresi(String namaFile) {
        File file = new File(namaFile);
        if (!file.exists()) {
            return null;
        }

        List<double[]> rows = new ArrayList<>();
        int degree = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(',', '.').trim();
                if (line.isEmpty())
                    continue;

                String[] tokens = line.split("\\s+");

                // 🔹 Jika hanya 1 angka di baris → dianggap sebagai derajat polinomial
                if (tokens.length == 1) {
                    degree = (int) parseFraction(tokens[0]);
                } else {
                    double[] row = new double[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        row[i] = parseFraction(tokens[i]);
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            System.err.println("Error membaca file: " + e.getMessage());
        }

        // 🔹 Validasi
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("File kosong atau format tidak sesuai: " + namaFile);
        }

        // 🔹 Konversi List<double[]> → double[][]
        double[][] matrix = new double[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i] = rows.get(i);
        }

        // 🔹 Return matrix dan degree
        return new Object[] { new Matrix(matrix), degree };
    }

    public static String polynomialToString(Matrix coef, int decimals) {
        StringBuilder sb = new StringBuilder("y(x) = ");
        String fmt = "%." + decimals + "f";
        int n = coef.getBaris();
        boolean first = true;

        for (int i = 0; i < n; i++) {
            double a = coef.getElmt(i, 0);
            if (Math.abs(a) < 1e-12)
                continue;
            String term;
            if (i == 0)
                term = String.format(fmt, a);
            else if (i == 1)
                term = String.format(fmt, a) + "x";
            else
                term = String.format(fmt, a) + "x^" + i;

            if (first) {
                sb.append(term);
                first = false;
            } else {
                sb.append(a >= 0 ? " + " : " - ");
                sb.append(term.replace("-", ""));
            }
        }
        if (first)
            sb.append("0");
        return sb.toString();
    }

    public static void savePolynomialResult(
            String outFile, Matrix points, String methodName,
            double minX, double maxX, String polyString,
            double[] xts, double[] yts) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outFile))) {
            pw.println("Metode: " + methodName);
            pw.println("Input (x, y):");
            for (int i = 0; i < points.getBaris(); i++) {
                pw.printf("  %.3f %.3f%n", points.getElmt(i, 0), points.getElmt(i, 1));
            }
            pw.printf("Domain interpolasi: [%.3f, %.3f]%n", minX, maxX);
            pw.println("Persamaan:");
            pw.println("  " + polyString);

            // Evaluasi
            if (xts != null && yts != null && xts.length == yts.length && xts.length > 0) {
                pw.println("Evaluasi:");
                for (int i = 0; i < xts.length; i++) {
                    pw.printf("  x_t = %.3f%n", xts[i]);
                    pw.printf("  y_t = %.3f%n", yts[i]);
                }
            } else {
                pw.println("Evaluasi:");
                pw.println("  (tidak ada nilai x_t yang diberikan)");
            }
        }
    }

    public static void savePolynomialResult(
            String outFile, Matrix points, String methodName,
            double minX, double maxX, String polyString) throws IOException {
        savePolynomialResult(outFile, points, methodName, minX, maxX, polyString,
                null, null);
    }

    public static void saveBezierResult(
            String outFile, Matrix kontrol, Matrix points) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outFile))) {
            pw.println("Metode: Splina Bézier Kubik");
            pw.println("Input (x, y):");
            for (int i = 0; i < points.getBaris(); i++) {
                pw.printf("  %.3f %.3f%n", points.getElmt(i, 0), points.getElmt(i, 1));
            }
            pw.println("Titik kontrol per segmen (P0,P1,P2,P3):");
            for (int i = 0; i < kontrol.getBaris(); i++) {
                double x0 = points.getElmt(i, 0), y0 = points.getElmt(i, 1);
                double x1 = points.getElmt(i + 1, 0), y1 = points.getElmt(i + 1, 1);
                double c1x = kontrol.getElmt(i, 0), c1y = kontrol.getElmt(i, 1);
                double c2x = kontrol.getElmt(i, 2), c2y = kontrol.getElmt(i, 3);
                pw.printf("Segmen %d: P0=(%.3f,%.3f), P1=(%.3f,%.3f), P2=(%.3f,%.3f), P3=(%.3f,%.3f)%n",
                        i, x0, y0, c1x, c1y, c2x, c2y, x1, y1);
            }
        }
    }

    public static void main(String[] args) {
        Matrix matrix = parseMatrixFromFileTxt(
                "test/test.txt");

        System.out.println("Matrix hasil parsing:");
        matrix.print();
    }
}
