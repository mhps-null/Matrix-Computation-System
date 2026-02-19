@REM FOR WINDOWS
@REM change JAVA_FX to your path of JDK
@REM change JAR_FILE to this path of jar file

@echo off
set "JAVA_FX=%~dp0\javafx-sdk-25\lib"
set "JAR_FILE=%~dp0\algeo1-yakin-kau-bung\bin\matrify-dependencies.jar"

java --enable-native-access=javafx.graphics ^
     --module-path "%JAVA_FX%" ^
     --add-modules javafx.controls,javafx.fxml ^
     -jar %JAR_FILE%
pause