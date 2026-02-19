package algeo.modules;

import algeo.utils.MatrixParser;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Locale;
import java.io.File;

public class RegresiPolinomGanda {

    // Membuat fitur polinomial
    // Ex : derajat 2 maka fitur polinomnya 1, x1, x2, x1^2, x1.x2, x2^2
    public static Matrix fiturPolinom(Matrix X, int derajat) {
        int baris = X.getBaris();
        int numVars = X.getKolom();

        List<int[]> exponents = generateExponents(numVars, derajat);
        int numFeatures = exponents.size();

        double[][] hasil = new double[baris][numFeatures];

        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < numFeatures; j++) {
                int[] pangkat = exponents.get(j);
                double val = 1.0;
                for (int k = 0; k < numVars; k++) {
                    val *= Math.pow(X.getElmt(i, k), pangkat[k]);
                }
                hasil[i][j] = val;
            }
        }
        return new Matrix(hasil);
    }

    // Fungsi pembantu generate exponent
    private static List<int[]> generateExponents(int numVars, int degree) {
        List<int[]> result = new ArrayList<>();
        generateLexicographic(result, new int[numVars], 0, degree);
        return result;
    }

    private static void generateLexicographic(List<int[]> result, int[] current, int index, int remaining) {
        if (index == current.length) {
            result.add(current.clone());
            return;
        }
        for (int i = 0; i <= remaining; i++) {
            current[index] = i;
            generateLexicographic(result, current, index + 1, remaining - i);
        }
    }

    // Menghitung nilai regresi polinom berganda (beta)
    public static Matrix regresiPolinom(Matrix X, double[] Y, int derajat) {
        // Konversi terlebih dahulu Y menjadi matrix
        double[][] yMatrixData = new double[Y.length][1];
        for (int i = 0; i < Y.length; i++) {
            yMatrixData[i][0] = Y[i];
        }
        Matrix Y_matrix = new Matrix(yMatrixData);

        Matrix X_poly = fiturPolinom(X, derajat);

        Matrix XT = X_poly.transpose();
        Matrix XTX = XT.perkalian(X_poly);
        Matrix XTX_inv = Invers.inversAugment(XTX);
        Matrix XTY = XT.perkalian(Y_matrix);
        Matrix beta = XTX_inv.perkalian(XTY);

        return beta;
    }

    // Persamaan y(x)
    public static String bentukPersamaan(Matrix beta, int numVars, int derajat) {
        List<int[]> exponents = generateExponents(numVars, derajat);
        StringBuilder sb = new StringBuilder("y(x) = ");

        for (int i = 0; i < beta.getBaris(); i++) {
            double coeff = beta.getElmt(i, 0);
            if (Math.abs(coeff) < 1e-9)
                continue;
            int[] exp = exponents.get(i);

            if (i > 0) { // Untuk suku kedua dan seterusnya
                if (coeff > 0)
                    sb.append(" + ");
                else
                    sb.append(" - ");
            } else { // Untuk suku pertama (konstan)
                if (coeff < 0)
                    sb.append("-"); // Hanya tambahkan '-' jika negatif
            }

            sb.append(String.format("%.3f", Math.abs(coeff)));

            for (int j = 0; j < exp.length; j++) {
                if (exp[j] > 0) {
                    sb.append("*x").append(j + 1);
                    if (exp[j] > 1)
                        sb.append("^").append(exp[j]);
                }
            }
        }

        return sb.toString();
    }

    // Evaluasi nilai Yt
    public static double hitungY(Matrix beta, double[] xInput, int derajat) {
        List<int[]> exponents = generateExponents(xInput.length, derajat);
        double y = 0.0;

        for (int i = 0; i < beta.getBaris(); i++) {
            double term = beta.getElmt(i, 0);
            int[] exp = exponents.get(i);
            for (int j = 0; j < exp.length; j++) {
                term *= Math.pow(xInput[j], exp[j]);
            }
            y += term;
        }

        return y;
    }

    // MAIN

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useLocale(Locale.US); // pastikan titik digunakan untuk desimal

            // Pastikan path benar (relatif ke root proyek)
            String path = "test/test1.txt";
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File tidak ditemukan: " + file.getAbsolutePath());
            }

            Object[] hasil = MatrixParser.matrixParserRegresi(
                    "/home/mhps-null/Documents/AlGeo/java/algeo1-yakin-kau-bung/test/input/regresi.txt");
            Matrix data = (Matrix) hasil[0];
            int degree = (int) hasil[1];

            Matrix X = data.ambilMatrixKoef();
            double[] Y = data.ambilMatrixKons();

            Matrix beta = regresiPolinom(X, Y, degree);

            System.out.println("\nKoefisien regresi polinomial berganda:");
            beta.print();

            String persamaan = bentukPersamaan(beta, X.getKolom(), degree);
            System.out.println("\nPersamaan hasil regresi:");
            System.out.println(persamaan);

            // Input nilai variabel
            double[] xt = new double[X.getKolom()];
            System.out.println("\nMasukkan nilai xt (total " + X.getKolom() + " variabel):");
            for (int i = 0; i < X.getKolom(); i++) {
                System.out.print("x" + (i + 1) + " = ");
                xt[i] = sc.nextDouble();
            }

            double yt = hitungY(beta, xt, degree);
            System.out.println("\nHasil prediksi yt = " + yt);

            sc.close();

        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

}