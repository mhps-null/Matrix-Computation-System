package algeo.utils;

import algeo.modules.Matrix;

public class CekUkuranMtrx {

    public static boolean isPersegiMtrx(Matrix M) {
        try {
            int baris = M.getBaris();
            int kolom = M.getKolom();

            double[][] data = M.getData();
            for (int i = 0; i < baris; i++) {
                if (data[i].length != kolom) {
                    return false;
                }
            }

            return baris == kolom;
        } catch (NullPointerException e) {
            System.err.println("Error: Matriks null!");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Matriks kosong!");
            return false;
        } catch (Exception e) {
            System.err.println("Error tidak terduga: " + e.getMessage());
            return false;
        }
    }
}
