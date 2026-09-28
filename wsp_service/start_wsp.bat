@echo off
REM =========================================================================
REM Script para Iniciar el Microservicio de WhatsApp wsp-js (IGAV SaaS)
REM =========================================================================

echo [1/2] Verificando dependencias de whatsapp-web.js...
if not exist "node_modules" (
    echo Instalando librerias de whatsapp-web.js y express...
    call npm install
)

echo [2/2] Iniciando Microservicio de WhatsApp en puerto 5001...
echo Escanee el codigo QR que aparecera en pantalla con su celular.
node index.js
pause
