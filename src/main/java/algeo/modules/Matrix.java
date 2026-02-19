package algeo.modules;

public class Matrix {
    // === Atribut ===
    private int baris;
    private int kolom;
    private double[][] data;

    // === Konstruktor ===
    public Matrix(int baris, int kolom) {
        this.baris = baris;
        this.kolom = kolom;
        this.data = new double[baris][kolom];
    }

    public Matrix(double[][] arr2d) {
        this.baris = arr2d.length;
        this.kolom = arr2d[0].length;
        this.data = new double[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                this.data[i][j] = arr2d[i][j];
            }
        }
    }

    public Matrix(Matrix other) {
        this(other.data);
    }

    // === Getter & Setter ===
    // Get jumlah baris matriks
    public int getBaris() {
        return baris;
    }

    // Get jumlah kolom matriks
    public int getKolom() {
        return kolom;
    }

    // Get matriks berupa array 2 dimensi
    public double[][] getData() {
        return data;
    }

    // Get elemen matriks pada baris i, kolom j
    public double getElmt(int i, int j) {
        return data[i][j];
    }

    // Set elemen matriks pada baris i, kolom j
    public void setElmt(int i, int j, double val) {
        data[i][j] = val;
    }

    // === Method ===
    // Copy matrix ke matrix lain
    public Matrix copy() {
        return new Matrix(this);
    }

    // Tambah matriks
    public Matrix penjumlahan(Matrix other) {
        if (this.baris != other.baris || this.kolom != other.kolom) {
            throw new IllegalArgumentException("Ukuran matriks harus sama untuk penjumlahan");
        }
        double[][] result = new double[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                result[i][j] = this.data[i][j] + other.data[i][j];
            }
        }
        return new Matrix(result);
    }

    // Kurang matriks
    public Matrix pengurangan(Matrix other) {
        if (this.baris != other.baris || this.kolom != other.kolom) {
            throw new IllegalArgumentException("Ukuran matriks harus sama untuk pengurangan");
        }
        double[][] result = new double[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                result[i][j] = this.data[i][j] - other.data[i][j];
            }
        }
        return new Matrix(result);
    }

    // Kali dengan skalar
    public Matrix perkalian(double k) {
        double[][] result = new double[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                result[i][j] = this.data[i][j] * k;
            }
        }
        return new Matrix(result);
    }

    // Kali dengan matriks lain
    public Matrix perkalian(Matrix other) {
        if (this.kolom != other.baris) {
            throw new IllegalArgumentException(
                    "Jumlah kolom matriks pertama harus sama dengan jumlah baris matriks kedua");
        }
        double[][] result = new double[this.baris][other.kolom];
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < other.kolom; j++) {
                double sum = 0;
                for (int k = 0; k < this.kolom; k++) {
                    sum += this.data[i][k] * other.data[k][j];
                }
                result[i][j] = sum;
            }
        }
        return new Matrix(result);
    }

    // Mengganti kolom matrix
    public Matrix gantiKolom(double[] kolDari, int kolomKe) {
        Matrix result = this.copy();
        for (int i = 0; i < baris; i++) {
            result.data[i][kolomKe] = kolDari[i];
        }
        return result;
    }

    // Ambil matriks koefisien (semua kecuali kolom terakhir)
    public Matrix ambilMatrixKoef() {
        double[][] newMatrix = new double[baris][kolom - 1];
        for (int i = 0; i < baris; i++) {
            System.arraycopy(this.data[i], 0, newMatrix[i], 0, kolom - 1);
        }
        return new Matrix(newMatrix);
    }

    // Ambil vektor konstanta (kolom terakhir)
    public double[] ambilMatrixKons() {
        double[] newMatrix = new double[baris];
        for (int i = 0; i < baris; i++) {
            newMatrix[i] = this.data[i][kolom - 1];
        }
        return newMatrix;
    }

    // Transpose
    public Matrix transpose() {
        double[][] tp = new double[kolom][baris];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                tp[j][i] = data[i][j];
            }
        }
        return new Matrix(tp);
    }

    // Ambil minor
    public Matrix getMinorEntri(int barisMinorEntri, int kolomMinorEntri) {
        double[][] minorEntri = new double[baris - 1][kolom - 1];
        int r = 0;
        for (int a = 0; a < baris; a++) {
            if (a == barisMinorEntri)
                continue;
            int c = 0;
            for (int b = 0; b < kolom; b++) {
                if (b == kolomMinorEntri)
                    continue;
                minorEntri[r][c] = data[a][b];
                c++;
            }
            r++;
        }
        return new Matrix(minorEntri);
    }

    // Matriks kofaktor
    public Matrix kofaktorMatrix() {
        double[][] kof = new double[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                kof[i][j] = Math.pow(-1, i + j) *
                        Determinan.determinanOBE(this.getMinorEntri(i, j));
            }
        }
        return new Matrix(kof);
    }

    // Matriks adjoin
    public Matrix adjoin() {
        return this.kofaktorMatrix().transpose();
    }

    // Hitung banyak leading non-zero row (reduksi gauss)
    public int hitungLead() {
        int lead = 0;
        for (int i = 0; i < baris; i++) {
            boolean semuaNol = true;
            for (int j = 0; j < kolom; j++) {
                if (Math.abs(data[i][j]) > 1e-9) {
                    semuaNol = false;
                    break;
                }
            }
            if (!semuaNol) {
                lead++;
            }
        }
        return lead;
    }

    // Cetak matrix
    public void print() {
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                System.out.printf("%8.2f ", data[i][j]);
            }
            System.out.println();
        }
    }

    // Menghasilkan matrix identitas
    public static Matrix identitas(int n) {
        Matrix M = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            M.data[i][i] = 1.0;
        }
        return M;
    }

    // Menukar baris
    public void tukarBaris(int baris1, int baris2) {
        if (baris1 == baris2) return;
        if (baris1 < 0 || baris1 >= baris || baris2 < 0 || baris2 >= baris) {
            throw new IndexOutOfBoundsException("Indeks baris di luar batas");
        }
        double[] temp = data[baris1];
        data[baris1] = data[baris2];
        data[baris2] = temp;
    }
}