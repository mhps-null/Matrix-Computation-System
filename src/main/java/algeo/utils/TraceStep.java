package algeo.utils;

import algeo.modules.Matrix;

public class TraceStep {
    private final String deskripsi;
    private final Matrix kondisi;

    public TraceStep(String deskripsi, Matrix kondisi) {
        this.deskripsi = deskripsi;
        this.kondisi = kondisi.copy();
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Matrix getKondisi() {
        return kondisi;
    }

    @Override
    public String toString() {
        return deskripsi;
    }
}
