package algeo.modules;

import java.util.HashSet;

import algeo.utils.MatrixParser;

public class Interpolasi {

    public static String cariDomain(Matrix M) {
        double[][] data = M.getData();
        double xkecil = data[0][0];
        double xbesar = data[0][0];

        for (int i = 0; i < M.getBaris(); i++) {
            for (int j = 0; j < M.getKolom() - 1; j++) {
                xkecil = Math.min(xkecil, data[i][j]);
                xbesar = Math.max(xbesar, data[i][j]);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("x ∈ (").append(xkecil).append(",").append(xbesar).append(")");
        return sb.toString();
    }

    public static Matrix interpolasiPolinomial(Matrix M) {
        cekXY(M);
        int n = M.getBaris();
        Matrix a = new Matrix(n, n + 1);
        for (int i = 0; i < n; i++) {
            double xi = M.getElmt(i, 0);
            double yi = M.getElmt(i, 1);
            double pangkat = 1.0;
            for (int j = 0; j < n; j++) {
                a.setElmt(i, j, pangkat);
                pangkat *= xi;
            }
            a.setElmt(i, n, yi);
        }
        Matrix b = SPL.splGaussJordan(a);
        Matrix koefisien = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            koefisien.setElmt(i, 0, b.getElmt(i, n));
        }
        return koefisien;
    }

    public static double evaluasiPolinomial(Matrix koefisien, double t) {
        int n = koefisien.getBaris();
        double hasil = koefisien.getElmt(n - 1, 0);
        for (int i = n - 2; i >= 0; i--) {
            hasil = koefisien.getElmt(i, 0) + t * hasil;
        }
        return Math.round(hasil);
    }

    private static void cekXY(Matrix M) {
        if (M == null || M.getKolom() != 2 || M.getBaris() == 0) {
            throw new IllegalArgumentException("Matrix harus n x 2, n > 0");
        }
        HashSet<Double> s = new HashSet<>();
        for (int i = 0; i < M.getBaris(); i++) {
            double xi = M.getElmt(i, 0);
            if (!s.add(xi)) {
                throw new IllegalArgumentException("Nilai x harus berbeda");
            }
        }
    }

    public static Matrix interpolasiSplinaBezier(Matrix M) {
        if (M == null || M.getKolom() != 2 || M.getBaris() < 2) {
            throw new IllegalArgumentException("Matrix harus berukuran (n+1) x 2 dengan n >= 1");
        }
        int n = M.getBaris();
        int segmen = n - 1;
        if (segmen == 1) {
            double x0 = M.getElmt(0, 0);
            double y0 = M.getElmt(0, 1);
            double x1 = M.getElmt(1, 0);
            double y1 = M.getElmt(1, 1);
            Matrix c = new Matrix(1, 4);
            c.setElmt(0, 0, (2 * x0 + x1) / 3.0);
            c.setElmt(0, 1, (2 * y0 + y1) / 3.0);
            c.setElmt(0, 2, (2 * x1 + x0) / 3.0);
            c.setElmt(0, 3, (2 * y1 + y0) / 3.0);
            return c;
        }
        HashSet<Double> xs = new HashSet<>();
        for (int i = 0; i < n; i++) {
            double xi = M.getElmt(i, 0);
            if (!xs.add(xi)) {
                throw new IllegalArgumentException("Nilai x harus distinct");
            }
        }
        int m = segmen - 1;
        double[] ax = new double[m], bx = new double[m], cx = new double[m], rx = new double[m];
        double[] ay = new double[m], by = new double[m], cy = new double[m], ry = new double[m];
        if (m == 1) {
            bx[0] = 4;
            by[0] = 4;
            ax[0] = ay[0] = 0;
            cx[0] = cy[0] = 0;
            rx[0] = 6 * M.getElmt(1, 0) - M.getElmt(0, 0) - M.getElmt(2, 0);
            ry[0] = 6 * M.getElmt(1, 1) - M.getElmt(0, 1) - M.getElmt(2, 1);
        } else {
            bx[0] = 4;
            cx[0] = 1;
            by[0] = 4;
            cy[0] = 1;
            rx[0] = 6 * M.getElmt(1, 0) - M.getElmt(0, 0);
            ry[0] = 6 * M.getElmt(1, 1) - M.getElmt(0, 1);
            for (int i = 1; i < m - 1; i++) {
                ax[i] = 1;
                bx[i] = 4;
                cx[i] = 1;
                ay[i] = 1;
                by[i] = 4;
                cy[i] = 1;
                rx[i] = 6 * M.getElmt(i + 1, 0);
                ry[i] = 6 * M.getElmt(i + 1, 1);
            }
            ax[m - 1] = 1;
            bx[m - 1] = 4;
            ay[m - 1] = 1;
            by[m - 1] = 4;
            rx[m - 1] = 6 * M.getElmt(segmen - 1, 0) - M.getElmt(segmen, 0);
            ry[m - 1] = 6 * M.getElmt(segmen - 1, 1) - M.getElmt(segmen, 1);
        }
        double[] bxu = tridiagonal(ax, bx, cx, rx);
        double[] byu = tridiagonal(ay, by, cy, ry);
        Matrix kontrol = new Matrix(segmen, 4);
        for (int i = 0; i < segmen; i++) {
            double six = M.getElmt(i, 0), siy = M.getElmt(i, 1);
            double sip1x = M.getElmt(i + 1, 0), sip1y = M.getElmt(i + 1, 1);
            int index = Math.min(i, m - 1);
            double bix = bxu[index];
            double biy = byu[index];
            kontrol.setElmt(i, 0, (2 * six + bix) / 3.0);
            kontrol.setElmt(i, 1, (2 * siy + biy) / 3.0);
            kontrol.setElmt(i, 2, (2 * sip1x + bix) / 3.0);
            kontrol.setElmt(i, 3, (2 * sip1y + biy) / 3.0);
        }
        return kontrol;
    }

    private static double[] tridiagonal(double[] a, double[] b, double[] c, double[] r) {
        int n = b.length;
        double[] cp = new double[n];
        double[] rp = new double[n];
        cp[0] = c[0] / b[0];
        rp[0] = r[0] / b[0];
        for (int i = 1; i < n; i++) {
            double d = b[i] - a[i] * cp[i - 1];
            cp[i] = (i < n - 1) ? c[i] / d : 0.0;
            rp[i] = (r[i] - a[i] * rp[i - 1]) / d;
        }
        double[] x = new double[n];
        x[n - 1] = rp[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = rp[i] - cp[i] * x[i + 1];
        }
        return x;
    }

    public static String bentukPersamaan(Matrix koef) {
        StringBuilder sb = new StringBuilder("f(x) = ");
        int n = koef.getBaris();
        boolean firstTerm = true;

        for (int i = 0; i < n; i++) {
            double c = koef.getElmt(i, 0);
            if (Math.abs(c) < 1e-9)
                continue;

            if (!firstTerm) {
                sb.append(c >= 0 ? " + " : " - ");
            } else if (c < 0) {
                sb.append("-");
            }

            double absC = Math.abs(c);

            if (!(absC == 1 && i > 0)) {
                sb.append(absC % 1 == 0 ? String.format("%d", (int) absC) : String.format("%.3f", absC));
            }

            if (i == 1) {
                sb.append("x");
            } else if (i > 1) {
                sb.append("x^").append(i);
            }

            firstTerm = false;
        }

        if (firstTerm)
            sb.append("0");
        return sb.toString();
    }

    public static void main(String[] args) {
        // === TEST 1: Interpolasi Polinomial ===
        System.out.println("=== TEST 1: Interpolasi Polinomial ===");
        // Misal titik: (1, 1), (2, 4), (3, 9)
        double[][] data1 = {
                { 0, 1 },
                { 1, 3 },
                { 2, 2 },
                { 3, 5 }

        };
        Matrix M1 = new Matrix(data1);

        try {
            MatrixParser
                    .parseMatrixFromFileTxt(
                            "/home/mhps-null/Documents/AlGeo/java/algeo1-yakin-kau-bung/test/input/itpPolinom.txt")
                    .print();
            Matrix koef = Interpolasi.interpolasiPolinomial(M1);
            System.out.println("Koefisien hasil interpolasi:");
            koef.print();
            System.out.println(bentukPersamaan(koef));

            // Uji evaluasi polinomial di t = 4
            double hasil = Interpolasi.evaluasiPolinomial(koef, 3);
            System.out.println("Nilai f(3) = " + hasil);
        } catch (Exception e) {
            System.out.println("Error pada interpolasi polinomial: " + e.getMessage());
        }

        // === TEST 2: Interpolasi Splina Bezier (Cubic Spline) ===
        // System.out.println("\n=== TEST 2: Interpolasi Splina Bezier ===");
        // double[][] data2 = {
        // { 0, 1 },
        // { 1, 2 },
        // { 2, 3 },
        // { 3, 2 }
        // };
        // Matrix M2 = new Matrix(data2);

        // try {
        // Matrix kontrol = Interpolasi.interpolasiSplinaBezier(M2);
        // System.out.println("Titik kontrol Bezier per segmen:");
        // kontrol.print();
        // } catch (Exception e) {
        // System.out.println("Error pada interpolasi splina bezier: " +
        // e.getMessage());
        // }

        // // === TEST 3: Kasus Error (duplikat X) ===
        // System.out.println("\n=== TEST 3: Error Case (Duplikat X) ===");
        // double[][] data3 = {
        // { 1, 1 },
        // { 1, 2 }
        // };
        // Matrix M3 = new Matrix(data3);

        // try {
        // Interpolasi.interpolasiPolinomial(M3);
        // } catch (Exception e) {
        // System.out.println("Error terdeteksi dengan benar: " + e.getMessage());
        // }
    }
}
