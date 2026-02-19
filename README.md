<p align="center">
  <img src="logo.jpeg" width="60%"/>
</p>

This template is the starter file structure for Tubes 1 Algeo 2025/2026. This template is a Java project using Maven Build tool with JavaFX GUI already configured in pom.xml.

In this project, you can choose if you want to develop to CLI or GUI app. The default is CLI, if you want GUI do uncomment the necessary part in App.java to run the template GUI test then run the commands below to run the app.

## Requirements

Before building and running the **Matrix Calculator**, make sure you have the following installed:

### Java

- **Version:** 17 or higher
- **Download links:**
  - [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads)

### Maven

- **Version:** 3.2.5 or higher (recommended 3.6.3+)
- **Download links:**
  - [Direct Apache Maven Official Downloads](https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip)

### Additional installation info

### Windows

For maven installation, download the .zip and it should contain a directory with

```
apache-maven-<version>/
├── bin/               <-- executable scripts (mvn, mvn.cmd)
├── boot/
├── conf/
├── lib/
├── NOTICE
├── LICENSE
├── README.txt
```

Put bin/ in environment PATH to use in terminal. [Add folder to PATH tutorial](https://www.youtube.com/watch?v=pGRw1bgb1gU)

### Linux

```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
sudo apt install maven -y
```

### MacOS

```bash
brew install openjdk@17
brew install maven
```

## How to develop

Using maven, the root development directory is `src/main/java/algeo`
There should not be any coding outside of that directory other than `test` using JUnit or other libraries.

Inside `src/main/java/algeo`, develop modules that are modular to use in the main program (`App.java`)

When running `mvn exec:java` later, `App.java` main program will be the one that is run.

## How to run

1. Compiling the program
   The following command will produce a `target` directory with `matrix-calculator-1.0-SNAPSHOT.jar` in it

```bash
mvn clean package
```

alternatively, if you don't want to make a .jar file, you can use

```bash
mvn clean compile
```

2. Running the program
   To run CLI, run:

```bash
mvn exec:java
```

To run GUI, be sure to uncomment the main GUI and run:

```bash
mvn clean javafx:run
```

when the program is first run, it should print in terminal:

```bash
Hai
Halo Algeo!
```

## Using the program as a library

Copy the .jar file that is in the `target` file (from running `mvn clean compile`) to `bin` for submission, this .jar file can be used in other projects to import modules in this current project

# Matrify - Kalkulator Matriks (JavaFX)

# Anggota Kelompok 3 (Yakin Kau Bung!)

1. Muhammad Haris Putra Sulastianto 13524053
2. Keisha Rizka Syofyani 13524073
3. Helena Kristela Sarhawa 13524109

# Penjelasan Singkat Program

Program Matrify ini merupakan kalkulator matriks interaktif yang dikembangkan untuk membantu pengguna melakukan berbagai operasi dan analisis berbasis matriks. Program ini menyediakan lima fitur utama, yaitu:

1. Penyelesaian Sistem Persamaan Linier (SPL)
2. Perhitungan Determinan Matriks
3. Perhitungan Invers Matriks
4. Interpolasi
5. Regresi Polinomial Berganda

Pengguna dapat memasukkan data matriks atau titik sampel secara manual melalui antarmuka program, atau memuatnya dari file teks (.txt). Hasil perhitungan juga dapat disimpan kembali ke file teks untuk dokumentasi.

# Alur Program

1. Pemilihan Fitur
   Setelah program dijalankan, pengguna akan diminta memilih salah satu fitur utama: SPL, Determinan, Invers, Interpolasi, atau Regresi.

2. Pemasukan Data

- Untuk fitur SPL, pengguna menginputkan ukuran matriks dan matriks augmented (matriks koefisien dan matriks konstanta)
- Untuk Determinan, dan Invers, pengguna menginputkan ukuran matriks dan matriks
- Untuk fitur Interpolasi, pengguna memasukkan jumlah titik sampel dan titik-titik data (x, y) untuk dianalisis
- Untuk fitur Regresi, pengguna memasukkan jumlah titik dan derajat polinom serta titik-titik data (x, y) untuk dianalisis

Data dapat dimasukkan secara langsung atau diimpor dari file teks (.txt) dengan format tertentu.

3. Validasi File Input
   Jika pengguna memilih input dari file, program akan melakukan validasi format file terlebih dahulu untuk memastikan kesesuaiannya dengan fitur yang dipilih.

4. Pemrosesan dan Perhitungan
   Setelah data berhasil dimasukkan, pengguna dapat memilih metode perhitungan yang tersedia sesuai jenis operasi (misalnya metode eliminasi Gauss, kaidah Cramer, atau inverse matrix untuk SPL).
   Program akan menampilkan langkah-langkah perhitungan secara terstruktur hingga menghasilkan nilai akhir.

5. Penyimpanan Hasil
   Pengguna dapat memilih untuk menyimpan hasil perhitungan ke file teks (.txt) dengan mencentang opsi penyimpanan dan menentukan nama file keluaran.

6. Reset dan Penggunaan Ulang
   Setelah satu sesi perhitungan selesai, pengguna dapat mengatur ulang program melalui tombol Reset untuk kembali ke menu utama dan memulai perhitungan baru.

# Tata Cara Menjalankan Program

Sebuah aplikasi kalkulator matriks dengan antarmuka grafis (GUI) yang dibangun menggunakan JavaFX.

## Prerequisites (Prasyarat)

Sebelum menjalankan aplikasi, pastikan sistem Anda telah memenuhi persyaratan berikut:

1.  **Java Development Kit (JDK)**
    - Pastikan JDK (versi 11 atau lebih tinggi) sudah terinstall. Anda bisa memeriksanya dengan membuka Command Prompt/Terminal dan mengetik:
      ```bash
      java -version
      ```
    - Jika belum terinstall, unduh dari [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) atau [OpenJDK](https://jdk.java.net/).

2.  **JavaFX SDK**
    - Aplikasi ini memerlukan JavaFX SDK.
    - Unduh SDK sesuai dengan sistem operasi dan versi JDK Anda dari [**GluonHQ**](https://gluonhq.com/products/javafx/).
    - Ekstrak file ZIP yang telah diunduh ke lokasi yang mudah diakses (misalnya, `C:\javafx-sdk-21` atau `~/javafx-sdk-21`).

3.  **File JAR Aplikasi**
    - Pastikan file `matrify-dependencies.jar` (atau nama file JAR hasil build lainnya) sudah tersedia di dalam direktori proyek, misalnya di dalam folder `bin/`.

## How to Run (Cara Menjalankan)

Pilih instruksi yang sesuai dengan sistem operasi Anda.

### 🖥️ Windows

1.  Buat file baru bernama `run.bat` di direktori utama proyek Anda.

2.  Buka file tersebut dengan teks editor (seperti Notepad) dan salin-tempel kode di bawah ini.

    ```bat
    @echo off

    :: 1. Sesuaikan path ke folder 'lib' di dalam direktori JavaFX SDK Anda.
    set JAVA_FX=C:\path\to\your\javafx-sdk\lib

    :: 2. Sesuaikan path ke file JAR aplikasi Anda.
    set JAR_FILE="bin\matrify-dependencies.jar"

    echo Menjalankan aplikasi Matrify...

    java --enable-native-access=javafx.graphics ^
    --module-path "%JAVA_FX%" ^
    --add-modules javafx.controls,javafx.fxml ^
    -jar %JAR_FILE%

    pause
    ```

3.  **Penting:** Ubah nilai variabel `JAVA_FX` dan `JAR_FILE` agar sesuai dengan lokasi file di komputer Anda.

4.  Simpan file `run.bat`.

5.  Jalankan aplikasi dengan **mengklik dua kali (double-click)** file `run.bat` di File Explorer.

### 🐧 Linux & 🍎 macOS

1.  Buat file baru bernama `run.sh` di direktori utama proyek Anda.

2.  Buka file tersebut dengan teks editor dan salin-tempel kode di bawah ini.

    ```sh
    #!/bin/bash

    # 1. Sesuaikan path ke folder 'lib' di dalam direktori JavaFX SDK Anda.
    JAVA_FX=/path/to/your/javafx-sdk/lib

    # 2. Sesuaikan path ke file JAR aplikasi Anda.
    JAR_FILE="bin/matrify-dependencies.jar"

    echo "Menjalankan aplikasi Matrify..."

    java --enable-native-access=javafx.graphics \
    --module-path "$JAVA_FX" \
    --add-modules javafx.controls,javafx.fxml \
    -jar "$JAR_FILE"
    ```

3.  **Penting:** Ubah nilai variabel `JAVA_FX` dan `JAR_FILE` agar sesuai dengan lokasi file di komputer Anda.

4.  Simpan file `run.sh`.

5.  Buka Terminal di direktori proyek, lalu berikan izin eksekusi (`execute permission`) pada file tersebut dengan perintah:

    ```sh
    chmod +x run.sh
    ```

6.  Jalankan aplikasi dengan perintah:
    ```sh
    ./run.sh
    ```
