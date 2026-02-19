package algeo.modules;

import algeo.utils.CekUkuranMtrx;
import algeo.utils.MatrixParser;
import algeo.utils.OutputFormatter;

public class SPL {

    // METODE PENYELESAIAN SPL DENGAN ELIMINASI GAUSS
    public static Matrix splGauss(Matrix A) {
        Matrix matrix = A.copy();
        int baris = matrix.getBaris();
        int kolom = matrix.getKolom();
        int batas = Math.min(baris, kolom - 1);

        for (int i = 0; i < batas; i++) {
            double diagonal = matrix.getElmt(i, i);
            if (diagonal == 0) {
                for (int k = i + 1; k < baris; k++) {
                    if (matrix.getElmt(k, i) != 0) {
                        // tukar baris
                        for (int j = 0; j < kolom; j++) {
                            double tmp = matrix.getElmt(i, j);
                            matrix.setElmt(i, j, matrix.getElmt(k, j));
                            matrix.setElmt(k, j, tmp);
                        }
                        diagonal = matrix.getElmt(i, i);
                        break;
                    }
                }
            }
            if (matrix.getElmt(i, i) == 0)
                continue;

            if (diagonal != 0) {
                for (int j = i; j < kolom; j++) {
                    matrix.setElmt(i, j, matrix.getElmt(i, j) / diagonal);
                }
            }
            for (int k = i + 1; k < baris; k++) {
                double faktor = matrix.getElmt(k, i) / matrix.getElmt(i, i);
                for (int j = i; j < kolom; j++) {
                    matrix.setElmt(k, j, matrix.getElmt(k, j) - faktor * matrix.getElmt(i, j));
                }
            }
        }
        return matrix;
    }

    public static Matrix splGaussJordan(Matrix A) {
        Matrix matrixGJ = splGauss(A).copy();
        int bar = matrixGJ.getBaris();
        int kol = matrixGJ.getKolom();

        for (int i = bar - 1; i >= 0; i--) {
            int kolAcuan = -1;
            for (int j = 0; j < kol - 1; j++) {
                if (Math.abs(matrixGJ.getElmt(i, j)) > 1e-9) {
                    kolAcuan = j;
                    break;
                }
            }
            if (kolAcuan == -1)
                continue;

            double acuan = matrixGJ.getElmt(i, kolAcuan);
            if (acuan != 1) {
                for (int k = kolAcuan; k < kol; k++) {
                    matrixGJ.setElmt(i, k, matrixGJ.getElmt(i, k) / acuan);
                }
            }
            for (int l = 0; l < i; l++) {
                double faktor = matrixGJ.getElmt(l, kolAcuan);
                if (faktor == 0)
                    continue;
                for (int m = kolAcuan; m < kol; m++) {
                    matrixGJ.setElmt(l, m, matrixGJ.getElmt(l, m) - faktor * matrixGJ.getElmt(i, m));
                }
            }
        }
        return matrixGJ;
    }

    // CRAMER
    public static Matrix splCramer(Matrix M) {
        Matrix A = M.ambilMatrixKoef();
        double[] B = M.ambilMatrixKons();

        if (!CekUkuranMtrx.isPersegiMtrx(A)) {
            return null;
        }

        double detA = Determinan.determinanOBE(A);

        if (detA == 0) {
            return null;
        }

        int n = A.getBaris();
        double[] solusi = new double[n];

        for (int i = 0; i < n; i++) {
            Matrix Ai = A.gantiKolom(B, i);
            double detAi = Determinan.determinanOBE(Ai);
            solusi[i] = detAi / detA;
        }

        Matrix hasilCr = new Matrix(solusi.length, 1);
        for (int i = 0; i < solusi.length; i++) {
            hasilCr.setElmt(i, 0, solusi[i]);
        }

        return hasilCr;
    }

    // INVERS
    public static Matrix splInvers(Matrix M) {
        Matrix A = M.ambilMatrixKoef();

        if (!CekUkuranMtrx.isPersegiMtrx(A)) {
            return null;
        }

        double detA = Determinan.determinanOBE(A);

        if (detA == 0) {
            return null;
        }

        double[] B = M.ambilMatrixKons();
        Matrix inv = Invers.inversAdjoin(A);

        double[] hasil = new double[inv.getBaris()];
        for (int i = 0; i < inv.getBaris(); i++) {
            double sum = 0;
            for (int j = 0; j < inv.getKolom(); j++) {
                sum += inv.getElmt(i, j) * B[j];
            }
            hasil[i] = sum;
        }

        Matrix hasilInv = new Matrix(hasil.length, 1);
        for (int i = 0; i < hasil.length; i++) {
            hasilInv.setElmt(i, 0, hasil[i]);
        }

        return hasilInv;
    }

    // cek jenis hasil
    public static String jenisHasil(Matrix M) {
        Matrix A = M.ambilMatrixKoef();
        int nVar = A.getKolom();
        int leadAug = M.hitungLead();
        int leadA = A.hitungLead();

        if (leadA == leadAug && leadA < nVar) {
            System.out.println("BANYAK");
            return solusiBanyak(M);
        } else if (leadA == leadAug && leadA == nVar) {
            System.out.println("UNIK");
            return solusiUnik(M);
        } else
            return "tidak ada solusi";
    }

    // cek jenis hasil Cramer dan Inv
    public static String jenisHasilCrInv(Matrix M, String jenis) {
        Matrix A = M.ambilMatrixKoef();

        if (!CekUkuranMtrx.isPersegiMtrx(A)) {
            return ("Matriks A tidak persegi, " + jenis + " tidak dapat diterapkan");
        }

        double detA = Determinan.determinanOBE(A);

        if (detA == 0) {
            return ("Determinan A = 0, " + jenis + " tidak dapat diterapkan");
        }

        Matrix solusi = new Matrix(M.getBaris(), 1);
        if (jenis.equals("cramer")) {
            solusi = splCramer(M);
        } else if (jenis.equals("inverse")) {
            solusi = splInvers(M);
        }

        StringBuilder sb = new StringBuilder();
        double[][] temp = OutputFormatter.roundMtrx(solusi.getData(), 3);
        for (int i = 0; i < solusi.getBaris(); i++) {
            sb.append("x").append(i + 1).append(" = ").append(temp[i][0]).append("\n");
        }
        return sb.toString();
    }

    // Solusi unik (substitusi mundur) untuk matriks hasil Gauss
    public static String solusiUnik(Matrix M) {
        Matrix temp = M.copy();
        int baris = temp.getBaris();
        int kolom = temp.getKolom();
        double[][] matrixCpy = temp.getData();

        for (int i = 0; i < baris; i++) {
            boolean allZero = true;
            for (int j = 0; j < kolom; j++) {
                if (Math.abs(matrixCpy[i][j]) > 1e-9) {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                baris--;
            }
        }

        double[] solusi = new double[baris];

        for (int i = baris - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < baris; j++) {
                sum += matrixCpy[i][j] * solusi[j];
            }
            solusi[i] = (matrixCpy[i][kolom - 1] - sum) / matrixCpy[i][i];
        }

        StringBuilder sb = new StringBuilder();
        solusi = OutputFormatter.roundArr(solusi, 3);
        for (int i = 0; i < solusi.length; i++) {
            sb.append("x").append(i + 1).append(" = ").append(solusi[i]).append("\n");
        }
        return sb.toString();
    }

    // Solusi banyak (sistem tak unik) dengan notasi parameter
    public static String solusiBanyak(Matrix M) {
        Matrix temp = M.copy();
        double[][] matrixCpy = temp.getData();
        int baris = temp.getBaris();
        int kolom = temp.getKolom() - 1; // jumlah variabel tanpa konstanta
        matrixCpy = OutputFormatter.roundMtrx(matrixCpy, 3);

        boolean[] isPivot = new boolean[kolom];
        int row = 0;

        for (int j = 0; j < kolom && row < baris; j++) {
            if (Math.abs(matrixCpy[row][j]) > 1e-9) {
                isPivot[j] = true;
                row++;
            }
        }

        // Assign parameter untuk variabel bebas
        String[] solusi = new String[kolom];
        int paramCount = 1;
        for (int j = 0; j < kolom; j++) {
            if (!isPivot[j]) {
                solusi[j] = "t" + paramCount;
                paramCount++;
            }
        }

        // Hitung nilai variabel pivot dengan substitusi mundur
        for (int i = row - 1; i >= 0; i--) {
            int pivotCol = -1;
            for (int j = 0; j < kolom; j++) {
                if (Math.abs(matrixCpy[i][j]) > 1e-9) {
                    pivotCol = j;
                    break;
                }
            }
            if (pivotCol == -1)
                continue; // baris nol

            String expr = String.valueOf(matrixCpy[i][kolom]); // konstanta
            for (int j = pivotCol + 1; j < kolom; j++) {
                if (Math.abs(matrixCpy[i][j]) > 1e-9) {
                    if (matrixCpy[i][j] != 1) {
                        expr += " - (" + matrixCpy[i][j] + " * " + solusi[j] + ")";
                    } else {
                        expr += " - (" + solusi[j] + ")";
                    }
                }
            }

            if (matrixCpy[i][pivotCol] != 1) {
                expr = "(" + expr + ") / " + matrixCpy[i][pivotCol];
            } else {
                expr = "(" + expr + ")";
            }

            solusi[pivotCol] = expr;
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < kolom; j++) {
            sb.append("x").append(j + 1).append(" = ").append(solusi[j]).append("\n");
        }

        return sb.toString();
    }

    // MAIN DEMO
    public static void main(String[] args) {
        Matrix M = new Matrix(MatrixParser.parseMatrixFromFileTxt("test/input/test5.txt"));

        Matrix hasilGauss = splGauss(M);
        System.out.println("Matrix hasil Gauss:");
        hasilGauss.print();
        System.out.println(jenisHasil(hasilGauss));

        Matrix hasilGJ = splGaussJordan(M);
        System.out.println("Matrix hasil Gauss-Jordan:");
        hasilGJ.print();
        System.out.println(jenisHasil(hasilGJ));

        // double[] hasilCramer = splCramer(M);
        // System.out.println("Solusi Cramer:");
        // OutputFormatter.printArr(hasilCramer, 2);

        // double[] hasilInvers = splInvers(M);
        // System.out.println("Solusi Invers:");
        // OutputFormatter.printArr(hasilInvers, 2);
    }
}