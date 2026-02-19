# !/bin/bash

# FOR LINUX
# change JAVA_FX to your path of JDK
# change JAR_FILE to this path of jar file

# Directory of this script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

JAVA_FX="$SCRIPT_DIR/javafx-sdk-25/lib"
JAR_FILE="$SCRIPT_DIR/bin/matrify-with-dependencies.jar"

java --enable-native-access=javafx.graphics \
     --module-path "$JAVA_FX" \
     --add-modules javafx.controls,javafx.fxml \
     -jar "$JAR_FILE"