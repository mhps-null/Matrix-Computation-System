package algeo.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import algeo.modules.Matrix;

public class SaveToFile {
    /**
     * Menyimpan hasil SPL, determinan, invers, interpolasi, atau regresi ke file
     * .txt
     * 
     * @param title    judul bagian
     * @param content  isi hasil perhitungan
     * @param fileName nama file tanpa .txt
     */
    public static void saveToTxt(String content, String fileName) {
        try {
            String folderPath = (System.getProperty("user.dir") + "/test/output/");

            String fullPath = folderPath + fileName + "_" + ".txt";

            try (PrintWriter out = new PrintWriter(new FileWriter(fullPath))) {
                out.println("===============================================");
                out.println("           " + fileName);
                out.println("===============================================");
                out.println();
                out.println(content);
                out.println();
                out.println("===============================================");
            }

            System.out.println("File berhasil disimpan di: " + fullPath);

        } catch (IOException e) {
            System.err.println("Gagal menyimpan file hasil: " + e.getMessage());
        }
    }

    public static String contentBuilder(String Metode, String Input, String Hasil) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode: ").append(Metode).append("\n");
        sb.append("Input: ").append("\n").append(Input).append("\n\n");
        sb.append("Hasil: ").append("\n").append(Hasil).append("\n");

        return sb.toString();
    }

    public static String contentBuilderItp(String Metode, String Input, String Domain, String Persamaan) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode: ").append(Metode).append("\n");
        sb.append("Input: ").append("\n").append(Input).append("\n\n");
        sb.append("Domain: ").append("\n").append(Domain).append("\n\n");
        sb.append("Persamaan Hasil: ").append("\n").append(Persamaan).append("\n");

        return sb.toString();
    }

    public static String contentBuilderItpBezier(String Metode, String TitikKontrol) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode: ").append(Metode).append("\n\n");
        sb.append("Titik Kontrol: ").append("\n").append(TitikKontrol).append("\n");

        return sb.toString();
    }

    public static String matrixToString(Matrix M) {
        StringBuilder sb = new StringBuilder();
        double[][] data = M.getData();

        for (int i = 0; i < M.getBaris(); i++) {
            for (int j = 0; j < M.getKolom(); j++) {
                sb.append(String.format("%.3f", data[i][j]));
                if (j < M.getKolom() - 1)
                    sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
