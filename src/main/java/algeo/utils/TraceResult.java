package algeo.utils;

import java.util.ArrayList;
import java.util.List;

import algeo.modules.Determinan;
import algeo.modules.Invers;
import algeo.modules.Matrix;
import algeo.modules.SPL;

public class TraceResult {
    private final List<TraceStep> steps = new ArrayList<>();

    public void add(String deskripsi, Matrix kondisi) {
        steps.add(new TraceStep(deskripsi, kondisi));
    }

    public List<TraceStep> getSteps() {
        return new ArrayList<>(steps);
    }

    public int size() {
        return steps.size();
    }

    public Matrix getLastMatrix() {
        if (steps.isEmpty())
            return null;
        return steps.get(steps.size() - 1).getKondisi();
    }

    // METODE PENYELESAIAN SPL DENGAN ELIMINASI GAUSS
    public static TraceResult splGaussLangkah(Matrix M) {
        Matrix matrix = M.copy();
        TraceResult trace = new TraceResult();
        int baris = matrix.getBaris();
        int kolom = matrix.getKolom();
        int batas = Math.min(baris, kolom - 1);

        trace.add("Mulai Gauss (matrix awal)", matrix.copy());

        for (int i = 0; i < batas; i++) {
            double diagonal = matrix.getElmt(i, i);
            if (Math.abs(diagonal) < 1e-12) {
                boolean swapped = false;
                for (int k = i + 1; k < baris; k++) {
                    if (matrix.getElmt(k, i) != 0) {
                        // tukar baris
                        for (int j = 0; j < kolom; j++) {
                            double tmp = matrix.getElmt(i, j);
                            matrix.setElmt(i, j, matrix.getElmt(k, j));
                            matrix.setElmt(k, j, tmp);
                        }
                        trace.add(String.format("Swap baris %d dan %d", i + 1, k + 1), matrix.copy());
                        swapped = true;
                        diagonal = matrix.getElmt(i, i);
                        break;
                    }
                }
                if (!swapped) {
                    trace.add(String.format("Tidak ada pivot pada kolom %d, lanjutkan", i + 1), matrix.copy());
                    continue;
                }
            }

            if (diagonal != 0) {
                double temp = matrix.getElmt(i, i);
                for (int j = i; j < kolom; j++) {
                    matrix.setElmt(i, j, matrix.getElmt(i, j) / diagonal);
                }
                diagonal = matrix.getElmt(i, i);
                trace.add(String.format("Kalikan baris %d dengan 1/(%.3f) sehingga pivot = 1", i + 1, temp),
                        matrix.copy());
            }

            for (int k = i + 1; k < baris; k++) {
                double faktor = matrix.getElmt(k, i) / matrix.getElmt(i, i);
                for (int j = i; j < kolom; j++) {
                    matrix.setElmt(k, j, matrix.getElmt(k, j) - faktor * matrix.getElmt(i, j));
                }
                trace.add(String.format("R%d = R%d - (%.3f) * R%d", k + 1, k + 1, faktor, i + 1), matrix.copy());
            }
        }
        trace.add("Selesai Gauss (hasil)", matrix.copy());

        trace.add(SPL.jenisHasil(matrix), matrix.copy());
        return trace;
    }

    public static TraceResult splGaussJordanLangkah(Matrix M) {
        TraceResult trace = splGaussLangkah(M);
        Matrix matrixGJ = trace.getLastMatrix().copy();

        int bar = matrixGJ.getBaris();
        int kol = matrixGJ.getKolom();

        trace.add("Mulai Gauss-Jordan (meneruskan hasil eliminasi Gauss awal)", matrixGJ.copy());

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
                trace.add(String.format("Kalikan baris %d agar pivot jadi 1", i + 1), matrixGJ.copy());
            }

            for (int l = 0; l < i; l++) {
                double faktor = matrixGJ.getElmt(l, kolAcuan);
                if (Math.abs(faktor) < 1e-9)
                    continue;
                for (int m = kolAcuan; m < kol; m++) {
                    matrixGJ.setElmt(l, m, matrixGJ.getElmt(l, m) - faktor * matrixGJ.getElmt(i, m));
                }
                trace.add(String.format("Eliminasi ke atas: R%d = R%d - (%.3f) * R%d",
                        l + 1, l + 1, faktor, i + 1), matrixGJ.copy());
            }
        }

        trace.add("Selesai Gauss-Jordan (hasil)", matrixGJ.copy());

        trace.add(SPL.jenisHasil(matrixGJ), matrixGJ.copy());
        return trace;
    }

    public static TraceResult splCramerLangkah(Matrix M) {
        TraceResult trace = new TraceResult();
        trace.add("Mulai metode Cramer", M.copy());

        Matrix A = M.ambilMatrixKoef();
        double[] B = M.ambilMatrixKons();

        if (!CekUkuranMtrx.isPersegiMtrx(A)) {
            trace.add("Matrix A bukan persegi, metode Cramer tidak dapat diterapkan", M.copy());
            return trace;
        }

        double detA = Determinan.determinanOBE(A);
        trace.add(String.format("Hitung determinan utama: det(A) = %.3f", detA), A.copy());

        if (Math.abs(detA) < 1e-9) {
            trace.add("det(A) = 0, SPL tidak memiliki solusi tunggal (tidak dapat lanjut dengan Cramer)", A.copy());
            return trace;
        }

        int n = A.getBaris();
        double[] solusi = new double[n];

        for (int i = 0; i < n; i++) {
            Matrix Ai = A.gantiKolom(B, i);
            double detAi = Determinan.determinanOBE(Ai);
            solusi[i] = detAi / detA;

            trace.add(String.format(
                    "Ganti kolom ke-%d dengan matriks B, hitung det(A%d) = %.3f, lalu x%d = det(A%d)/det(A) = %.3f",
                    i + 1, i + 1, detAi, i + 1, i + 1, solusi[i]), Ai.copy());
        }

        Matrix hasilCr = new Matrix(solusi.length, 1);
        for (int i = 0; i < solusi.length; i++) {
            hasilCr.setElmt(i, 0, solusi[i]);
        }

        trace.add("Selesai metode Cramer (hasil)", hasilCr.copy());

        trace.add(SPL.jenisHasilCrInv(M.copy(), "cramer"), hasilCr.copy());
        return trace;
    }

    public static TraceResult splInversLangkah(Matrix M) {
        Matrix A = M.ambilMatrixKoef();
        double[] B = M.ambilMatrixKons();
        TraceResult trace = new TraceResult();

        trace.add("Mulai metode matriks balikan (matriks koefisien)", A.copy());

        if (!CekUkuranMtrx.isPersegiMtrx(A)) {
            trace.add("Matriks tidak persegi, metode invers tidak dapat diterapkan", A.copy());
            return trace;
        }

        double detA = Determinan.determinanOBE(A);

        if (Math.abs(detA) < 1e-12) {
            trace.add("Determinan = 0, metode invers tidak dapat diterapkan", A.copy());
            return trace;
        }

        TraceResult inversTrace = inversAdjoinLangkah(A);
        for (TraceStep step : inversTrace.getSteps()) {
            trace.add(String.format("Langkah invers: %s", step.getDeskripsi()),
                    step.getKondisi() != null ? step.getKondisi().copy() : null);
        }

        Matrix inv = Invers.inversAdjoin(A);
        trace.add("Matriks invers A", inv.copy());

        double[] hasil = new double[inv.getBaris()];
        for (int i = 0; i < inv.getBaris(); i++) {
            double sum = 0;
            StringBuilder step = new StringBuilder(String.format("Hitung x%d = ", i + 1));
            for (int j = 0; j < inv.getKolom(); j++) {
                double prod = inv.getElmt(i, j) * B[j];
                sum += prod;
                step.append(String.format("(%.3f * %.3f)", inv.getElmt(i, j), B[j]));
                if (j < inv.getKolom() - 1)
                    step.append(" + ");
            }
            hasil[i] = sum;
            step.append(" = ").append(String.format("%.3f", sum));
            Matrix tempMat = new Matrix(hasil.length, 1);
            for (int k = 0; k <= i; k++) {
                tempMat.setElmt(k, 0, hasil[k]);
            }

            trace.add(step.toString(), tempMat);
        }

        Matrix hasilInv = new Matrix(hasil.length, 1);
        for (int i = 0; i < hasil.length; i++) {
            hasilInv.setElmt(i, 0, hasil[i]);
        }

        trace.add("Hasil akhir solusi menggunakan invers", hasilInv.copy());

        trace.add(SPL.jenisHasilCrInv(M.copy(), "inverse"), hasilInv.copy());
        return trace;
    }

    public static TraceResult determinanOBELangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }

        Matrix mat = M.copy();

        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Determinan hanya bisa dihitung jika matriks persegi!", mat);
            return trace;
        }

        trace.add("Mulai OBE untuk determinan (matrix awal)", mat.copy());

        int n = mat.getBaris();
        double[][] data = mat.getData();
        int tukarBaris = 0;

        for (int i = 0; i < n; i++) {
            double diagonal = data[i][i];

            if (Math.abs(diagonal) < 1e-9) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (Math.abs(data[k][i]) > 1e-9) {
                        double[] temp = data[k];
                        data[k] = data[i];
                        data[i] = temp;
                        diagonal = data[i][i];
                        tukarBaris++;
                        swapped = true;
                        trace.add(String.format("Swap baris %d dengan baris %d", i + 1, k + 1), mat.copy());
                        break;
                    }
                }
                if (!swapped) {
                    trace.add(String.format("Tidak ada pivot di kolom %d, lanjutkan", i + 1), mat.copy());
                    continue;
                }
            }

            if (Math.abs(data[i][i]) < 1e-9)
                continue;

            for (int k = i + 1; k < n; k++) {
                double temp1 = data[k][i];
                double temp2 = data[i][i];
                double faktor = data[k][i] / data[i][i];
                for (int j = i; j < n; j++) {
                    data[k][j] -= faktor * data[i][j];
                }
                trace.add(
                        String.format("Eliminasi: R%d = R%d - (%.3f / %.3f) * R%d", k + 1, k + 1, temp1, temp2, i + 1),
                        mat.copy());
            }
        }

        int tanda = (tukarBaris % 2 == 0) ? 1 : -1;
        double kaliDiagonal = 1.0;
        for (int i = 0; i < n; i++) {
            kaliDiagonal *= data[i][i];
        }
        double det = tanda * kaliDiagonal;
        trace.add(
                String.format("Swap baris: %d kali, perkalian diagonal: %.3f", tukarBaris, kaliDiagonal),
                mat.copy());
        trace.add(
                String.format("Determinan = ((-1)^%d) * (%.3f) = %.3f", tukarBaris, kaliDiagonal, det),
                mat.copy());

        trace.add(String.format("Selesai OBE, determinan = %.3f", det), mat.copy());
        return trace;
    }

    public static TraceResult determinanEkspansiKofaktorLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }
        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Matriks bukan persegi, determinan tidak dapat dihitung", M.copy());
            return trace;
        }
        trace.add("Mulai mencari determinan dengan metode ekspansi kofaktor (Matrix awal)", M.copy());

        traceDeterKofaktor(M, trace, "Root");
        return trace;
    }

    private static double traceDeterKofaktor(Matrix M, TraceResult trace, String path) {
        int n = M.getBaris();
        if (n == 1) {
            trace.add(String.format("%s: Determinan 1x1 = %.3f", path, M.getElmt(0, 0)), M.copy());
            return M.getElmt(0, 0);
        }
        if (n == 2) {
            double deter2 = M.getElmt(0, 0) * M.getElmt(1, 1) - M.getElmt(0, 1) * M.getElmt(1, 0);
            trace.add(String.format("%s: Determinan 2x2 = %.3f * %.3f - %.3f * %.3f = %.3f",
                    path, M.getElmt(0, 0), M.getElmt(1, 1), M.getElmt(0, 1), M.getElmt(1, 0), deter2), M.copy());
            return deter2;
        }

        double deter = 0.0;
        for (int k = 0; k < n; k++) {
            Matrix minor = M.getMinorEntri(0, k);
            double tanda = ((k % 2) == 0) ? 1 : -1;
            double subDeter = traceDeterKofaktor(minor, trace, path + " -> Cofaktor(0," + k + ")");
            double kontribusi = tanda * M.getElmt(0, k) * subDeter;
            trace.add(String.format("%s: Tambah kontribusi dari elemen (0,%d) = %.3f * %.3f * %.3f = %.3f",
                    path, k, tanda, M.getElmt(0, k), subDeter, kontribusi), M.copy());
            deter += kontribusi;
        }
        trace.add(String.format("%s: Determinan total = %.3f", path, deter), M.copy());
        return deter;
    }

    public static TraceResult inversAdjoinLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }

        trace.add("Mulai inverse dengan metode adjoin (Matrix awal)", M.copy());

        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Matriks tidak persegi, tidak dapat dihitung invers", M.copy());
            return trace;
        }

        TraceResult detTrace = determinanEkspansiKofaktorLangkah(M);
        double det = Determinan.determinanEkspansiKofaktor(M);
        for (TraceStep step : detTrace.getSteps()) {
            trace.add(String.format("Determinasi: %s", step.getDeskripsi()), step.getKondisi().copy());
        }

        if (Math.abs(det) < 1e-9) {
            trace.add("Determinan = 0, matriks tidak dapat diinvers", M.copy());
            return trace;
        }
        trace.add(String.format("Determinan matriks = %.3f", det), M.copy());

        TraceResult adjTrace = adjoinLangkah(M);
        for (TraceStep step : adjTrace.getSteps()) {
            trace.add(String.format("Langkah adjoin: %s", step.getDeskripsi()),
                    step.getKondisi() != null ? step.getKondisi().copy() : null);
        }
        Matrix adj = M.adjoin();
        trace.add("Matriks adjoin", adj.copy());

        Matrix hasil = adj.perkalian(1.0 / det);
        trace.add(String.format("Kalikan adjoin dengan 1/%.3f = (%.3f) untuk mendapatkan invers",
                det, (1.0 / det)), hasil.copy());

        trace.add("Selesai Invers metode adjoin",
                hasil.copy());

        return trace;
    }

    public static TraceResult inversAugmentLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }
        trace.add("Mulai inverse dengan metode augment (Matrix awal)", M.copy());

        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Matriks tidak persegi, tidak dapat dihitung invers", M.copy());
            return trace;
        }

        int n = M.getBaris();
        Matrix L = M.copy();
        Matrix R = Matrix.identitas(n);
        Matrix gabungan = new Matrix(n, 2 * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                gabungan.setElmt(i, j, L.getElmt(i, j));
                gabungan.setElmt(i, j + n, R.getElmt(i, j));
            }
        }
        trace.add("Matrix L awal (salinan) dan R awal (identitas)", gabungan.copy());

        for (int kol = 0; kol < n; kol++) {
            // Cari pivot maksimal
            int barisAcuan = kol;
            double maxAbs = Math.abs(L.getElmt(barisAcuan, kol));
            for (int i = kol + 1; i < n; i++) {
                double val = Math.abs(L.getElmt(i, kol));
                if (val > maxAbs) {
                    maxAbs = val;
                    barisAcuan = i;
                }
            }

            if (maxAbs == 0.0) {
                gabungan = new Matrix(n, 2 * n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        gabungan.setElmt(i, j, L.getElmt(i, j));
                        gabungan.setElmt(i, j + n, R.getElmt(i, j));
                    }
                }
                trace.add("Pivot = 0, matriks singular, tidak dapat diinvers", gabungan.copy());
                return trace;
            }

            // Tukar baris jika perlu
            if (barisAcuan != kol) {
                L.tukarBaris(barisAcuan, kol);
                R.tukarBaris(barisAcuan, kol);
                gabungan = new Matrix(n, 2 * n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        gabungan.setElmt(i, j, L.getElmt(i, j));
                        gabungan.setElmt(i, j + n, R.getElmt(i, j));
                    }
                }
                trace.add(String.format("Swap baris %d dengan %d", kol + 1, barisAcuan + 1), gabungan.copy());
            }

            // Kalikan pivot menjadi 1
            double pivot = L.getElmt(kol, kol);
            for (int k = 0; k < n; k++) {
                L.setElmt(kol, k, L.getElmt(kol, k) / pivot);
                R.setElmt(kol, k, R.getElmt(kol, k) / pivot);
            }
            gabungan = new Matrix(n, 2 * n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    gabungan.setElmt(i, j, L.getElmt(i, j));
                    gabungan.setElmt(i, j + n, R.getElmt(i, j));
                }
            }
            trace.add(String.format("Kalikan baris %d dengan 1/%.3f agar pivot = 1", kol + 1, pivot), gabungan.copy());//

            for (int l = 0; l < n; l++) {
                if (l == kol)
                    continue;
                double faktor = L.getElmt(l, kol);
                if (Math.abs(faktor) < 1e-9)
                    continue;
                for (int m = 0; m < n; m++) {
                    L.setElmt(l, m, L.getElmt(l, m) - faktor * L.getElmt(kol, m));
                    R.setElmt(l, m, R.getElmt(l, m) - faktor * R.getElmt(kol, m));
                }
                gabungan = new Matrix(n, 2 * n);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        gabungan.setElmt(i, j, L.getElmt(i, j));
                        gabungan.setElmt(i, j + n, R.getElmt(i, j));
                    }
                }
                trace.add(String.format("Eliminasi baris %d: R%d = R%d - (%.3f)*R%d", kol + 1, l + 1, l + 1, faktor,
                        kol + 1), gabungan.copy());
            }
        }

        gabungan = new Matrix(n, 2 * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                gabungan.setElmt(i, j, L.getElmt(i, j));
                gabungan.setElmt(i, j + n, R.getElmt(i, j));
            }
        }
        trace.add("Selesai invers (R adalah invers)", R.copy());

        return trace;
    }

    public static TraceResult ekspansiKofaktorLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }

        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Matriks bukan persegi, tidak dapat dihitung kofaktor.", M.copy());
            return trace;
        }

        trace.add("Mulai menghitung ekspansi kofaktor (Matrix awal)", M.copy());

        int n = M.getBaris();
        Matrix kofaktor = new Matrix(n, n);

        // Proses hitung kofaktor per elemen
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minor = M.getMinorEntri(i, j);
                double detMinor = Determinan.determinanEkspansiKofaktor(minor);
                double tanda = ((i + j) % 2 == 0) ? 1 : -1;
                double nilaiKof = tanda * detMinor;
                kofaktor.setElmt(i, j, nilaiKof);

                trace.add(String.format(
                        "Cofaktor[%d,%d] = (-1)^(%d+%d) * det(Minor[%d,%d]) = %.3f * (%.3f) = %.3f",
                        i, j, i, j, i, j, tanda, detMinor, nilaiKof),
                        minor.copy());
            }
        }

        trace.add("Matriks kofaktor selesai dihitung:", kofaktor.copy());
        return trace;
    }

    public static TraceResult transposeLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }

        trace.add("Mulai transpose matriks (Matrix awal)", M.copy());

        int bar = M.getBaris();
        int kol = M.getKolom();
        Matrix hasil = new Matrix(kol, bar);

        for (int i = 0; i < bar; i++) {
            for (int j = 0; j < kol; j++) {
                hasil.setElmt(j, i, M.getElmt(i, j));
                trace.add(String.format("Tukar elemen M[%d,%d] → T[%d,%d] = %.3f",
                        i, j, j, i, M.getElmt(i, j)), hasil.copy());
            }
        }

        trace.add("Selesai transpose matriks", hasil.copy());
        return trace;
    }

    public static TraceResult adjoinLangkah(Matrix M) {
        TraceResult trace = new TraceResult();

        if (M == null) {
            trace.add("Matriks null!", null);
            return trace;
        }

        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            trace.add("Matriks tidak persegi, adjoin tidak dapat dihitung", M.copy());
            return trace;
        }

        trace.add("Mulai menghitung matriks adjoin (Matrix awal)", M.copy());

        // Langkah 1: hitung kofaktor matrix
        TraceResult kofTrace = ekspansiKofaktorLangkah(M);
        Matrix kofaktor = new Matrix(M.getBaris(), M.getKolom());
        for (int i = 0; i < kofTrace.getSteps().size(); i++) {
            trace.add(String.format("Langkah kofaktor: %s", kofTrace.getSteps().get(i).getDeskripsi()),
                    kofTrace.getSteps().get(i).getKondisi().copy());
        }

        kofaktor = M.kofaktorMatrix();
        trace.add("Matriks kofaktor", kofaktor.copy());

        // Langkah 2: transpose hasil kofaktor
        TraceResult transTrace = transposeLangkah(kofaktor);
        for (TraceStep step : transTrace.getSteps()) {
            trace.add(String.format("Langkah transpose: %s", step.getDeskripsi()), step.getKondisi().copy());
        }

        Matrix adjoin = kofaktor.transpose();
        trace.add("Matriks adjoin (transpose dari kofaktor)", adjoin.copy());

        trace.add("Selesai menghitung adjoin", adjoin.copy());
        return trace;
    }

    public static void main(String[] args) {
        double[][] data = {
                { 5, 15, 55 },
                { 15, 55, 225 },
                { 55, 225, 979 }
        };

        Matrix M = new Matrix(data);
        TraceResult trace = inversAdjoinLangkah(M);

        int step = 1;
        for (TraceStep s : trace.getSteps()) {
            System.out.printf("Langkah %d: %s%n", step++, s.getDeskripsi());
            if (s.getKondisi() != null) {
                s.getKondisi().print();
            } else {
                System.out.println("null");
            }
            System.out.println("--------------------------------");
        }
    }
}