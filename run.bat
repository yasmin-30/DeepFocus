@echo off
set JAVAFX_PATH=C:\Users\theoc\Downloads\openjfx-25.0.1_windows-x64_bin-sdk\javafx-sdk-25.0.1\lib

if not exist bin mkdir bin

echo Compiling...
javac -d bin --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml src/deepfocus/Main.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running...
java -cp bin --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml deepfocus.Main
