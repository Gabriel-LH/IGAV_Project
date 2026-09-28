@echo off
REM =========================================================================
REM Script de Generación Automática del Manual Técnico con JavaDoc (IGAV SaaS)
REM =========================================================================

echo [1/3] Configurando entorno JDK 21...
set JAVA_HOME=C:\Users\jony_\.vscode\extensions\redhat.java-1.53.0-win32-x64\jre\21.0.10-win32-x86_64
set PATH=%JAVA_HOME%\bin;%PATH%

echo [2/3] Ejecutando Maven Javadoc Plugin...
call ./mvnw.cmd javadoc:javadoc -DskipTests

echo [3/3] Verificando manual generado...
if exist "target\site\apidocs\index.html" (
    echo Manual generado con exito en target\site\apidocs\index.html
    start target\site\apidocs\index.html
) else if exist "target\reports\apidocs\index.html" (
    echo Manual generado con exito en target\reports\apidocs\index.html
    start target\reports\apidocs\index.html
) else (
    echo Revise la salida de la consola de Maven.
)
pause
