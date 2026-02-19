package algeo.modules;

import algeo.utils.CekUkuranMtrx;

public class Invers {
    // Menghitung invers matriks menggunakan metode adjoin
    public static Matrix inversAdjoin(Matrix M) {

        if (M == null) {
            throw new IllegalArgumentException("Matriks null!");
        }
        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            throw new IllegalArgumentException("Matriks tidak persegi, tidak dapat diinvers");
        }

        double det = Determinan.determinanEkspansiKofaktor(M);
        if (Math.abs(det) < 1e-9) {
            throw new ArithmeticException("Determinan matriks = 0, tidak dapat diinvers");
        }

        Matrix adj = M.adjoin();

        // Kalikan adjoin dengan 1/det
        Matrix hasil = adj.perkalian(1.0 / det);

        return hasil;
    }
        // Menghitung invers matriks menggunakan metode augment
    public static Matrix inversAugment(Matrix M) {

        if (M == null) {
            throw new IllegalArgumentException("Matriks null!");
        }
        if (!CekUkuranMtrx.isPersegiMtrx(M)) {
            throw new IllegalArgumentException("Matriks tidak persegi, tidak dapat diinvers");
        }

        int n = M.getBaris();
        Matrix L = M.copy();
        Matrix R = Matrix.identitas(n);

        for (int kol = 0; kol < n; kol++) {
            int barisAcuan = kol;
            double maxAbs = Math.abs(L.getElmt(barisAcuan,kol));
            for (int i = kol + 1; i < n; i++) {
                double j = Math.abs(L.getElmt(i, kol));
                if (j > maxAbs) {
                    maxAbs = j;
                    barisAcuan = i;
                }
            }
            if (maxAbs == 0.0) {
                throw new ArithmeticException("Determinan 0: matriks singular, tidak dapat diinvers");
            }
            if(barisAcuan != kol) {
                L.tukarBaris(barisAcuan, kol);
                R.tukarBaris(barisAcuan, kol);
            }
            double pivot = L.getElmt(kol, kol);
            if (pivot == 0.0) {
                throw new ArithmeticException("Pivot 0: matriks singular");
            }
            for (int k = 0; k < n; k++) {
                L.setElmt(kol, k, L.getElmt(kol, k) / pivot);
                R.setElmt(kol, k, R.getElmt(kol, k) / pivot);
            }
            for (int l = 0; l < n; l++) {
                if (l == kol) continue;
                double faktor = L.getElmt(l, kol);
                if (faktor == 0.0) continue;
                for (int m = 0; m < n; m++) {
                    L.setElmt(l, m, L.getElmt(l,m) - faktor*L.getElmt(kol, m));
                    R.setElmt(l, m, R.getElmt(l,m) - faktor*R.getElmt(kol, m));
                }
            }
        }
        return R;
    }
}
