@echo off
set LIB_PATH=lib

if not exist bin mkdir bin

echo Compiling...
javac -d bin -cp "%LIB_PATH%\*" --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml src/deepfocus/Main.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running...
java -cp "bin;%LIB_PATH%\*" -Djava.library.path="%LIB_PATH%" --module-path "%LIB_PATH%" --add-modules javafx.controls,javafx.fxml deepfocus.Main
