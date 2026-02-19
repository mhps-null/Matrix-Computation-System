package algeo.modules;

import algeo.utils.MatrixParser;
import algeo.utils.CekUkuranMtrx;

public class Determinan {

    // Determinan metode OBE
    public static double determinanOBE(Matrix M) {
        if (M == null) {
            throw new IllegalArgumentException("Matriks null!");
        }
        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            throw new IllegalArgumentException("Determinan hanya bisa dihitung jika matriks persegi!");
        }

        Matrix mat = M.copy();
        int n = mat.getBaris();
        double[][] data = mat.getData();

        int tukarBaris = 0;

        for (int i = 0; i < n; i++) {
            double diagonal = data[i][i];

            if (Math.abs(diagonal) < 1e-9) {
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(data[k][i]) > 1e-9) {
                        double[] temp = data[k];
                        data[k] = data[i];
                        data[i] = temp;
                        diagonal = data[i][i];
                        tukarBaris++;
                        break;
                    }
                }
            }

            if (Math.abs(data[i][i]) < 1e-9)
                continue;

            for (int k = i + 1; k < n; k++) {
                double faktor = data[k][i] / data[i][i];
                for (int j = i; j < n; j++) {
                    data[k][j] -= faktor * data[i][j];
                }
            }
        }

        int tanda = (tukarBaris % 2 == 0) ? 1 : -1;

        double kaliDiagonal = 1.0;
        for (int i = 0; i < n; i++) {
            kaliDiagonal *= data[i][i];
        }

        return tanda * kaliDiagonal;
    }

    // Determinan metode ekspansi kofaktor
    public static double determinanEkspansiKofaktor(Matrix M) {
        if (M == null) {
            throw new IllegalArgumentException("Matriks null!");
        }
        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            throw new IllegalArgumentException("Determinan hanya bisa dihitung jika matriks persegi!");
        }

        int n = M.getBaris();
        if (n == 1)
            return M.getElmt(0, 0);
        if (n == 2)
            return M.getElmt(0, 0) * M.getElmt(1, 1) - M.getElmt(0, 1) * M.getElmt(1, 0);

        double deter = 0.0;
        for (int k = 0; k < n; k++) {
            Matrix minor = M.getMinorEntri(0, k);
            double tanda = ((k % 2) == 0) ? 1 : -1;
            deter += tanda * M.getElmt(0, k) * determinanEkspansiKofaktor(minor);
        }
        return deter;
    }

    // MAIN
    public static void main(String[] args) {
        double determinanOBE = determinanOBE(MatrixParser
                .parseMatrixFromFileTxt("test/test.txt"));

        System.out.println("Hasil determinan menggunakan metode reduksi baris (OBE):");
        System.out.println(determinanOBE);

        double detEksKof = determinanEkspansiKofaktor(MatrixParser.parseMatrixFromFileTxt("test/test.txt"));
        System.out.println("Hasil determinan menggunakan metode Ekspansi Kofaktor:");
        System.out.println(detEksKof);
    }

}