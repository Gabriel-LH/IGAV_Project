/**
 * Microservicio de Notificaciones WhatsApp con whatsapp-web.js (wsp-js)
 * Sistema SaaS I.G.A.V. - Gestión de Alquiler de Gala y Alta Costura
 * 
 * Funcionalidades:
 * - Generación de código QR para vinculación de sesión WhatsApp de la tienda.
 * - Alerta preventiva 24 horas antes del vencimiento/devolución para evitar cobro de mora.
 * - Recordatorio 24 horas antes del retiro/evento social del cliente.
 * - Notificación instantánea de emisión de contrato y custodia de garantía.
 */

const express = require('express');
const cors = require('cors');
const { Client, LocalAuth } = require('whatsapp-web.js');
const qrcode = require('qrcode-terminal');

const app = express();
const PORT = process.env.PORT || 5001;

app.use(cors());
app.use(express.json());

// Estado de la conexión de WhatsApp
let connectionStatus = 'DISCONNECTED'; // 'DISCONNECTED', 'WAITING_QR', 'CONNECTED'
let lastQrCode = null;

// Inicialización del cliente whatsapp-web.js
console.log('>>> [WSP-JS] Inicializando cliente WhatsApp Web...');

const client = new Client({
    authStrategy: new LocalAuth({
        dataPath: './.wsp_auth'
    }),
    puppeteer: {
        headless: true,
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-dev-shm-usage',
            '--disable-accelerated-2d-canvas',
            '--no-first-run',
            '--no-zygote',
            '--disable-gpu'
        ]
    }
});

client.on('qr', (qr) => {
    connectionStatus = 'WAITING_QR';
    lastQrCode = qr;
    console.log('\n================================================================');
    console.log('>>> [WSP-JS] ESCANEA ESTE CÓDIGO QR CON WHATSAPP EN TU CELULAR:');
    console.log('================================================================\n');
    qrcode.generate(qr, { small: true });
});

client.on('ready', () => {
    connectionStatus = 'CONNECTED';
    lastQrCode = null;
    console.log('\n================================================================');
    console.log('>>> [WSP-JS] ¡WHATSAPP CONECTADO Y LISTO PARA ENVIAR ALERTAS! ✅');
    console.log('================================================================\n');
});

client.on('authenticated', () => {
    console.log('>>> [WSP-JS] Sesión autenticada correctamente.');
});

client.on('auth_failure', (msg) => {
    connectionStatus = 'DISCONNECTED';
    console.error('>>> [WSP-JS] Error de autenticación:', msg);
});

client.on('disconnected', (reason) => {
    connectionStatus = 'DISCONNECTED';
    console.warn('>>> [WSP-JS] Sesión desconectada:', reason);
});

// Iniciar cliente de WhatsApp
client.initialize().catch(err => {
    console.error('>>> [WSP-JS] Error al inicializar cliente:', err.message);
});

/**
 * Normaliza el número de teléfono al formato internacional de WhatsApp (ej: 51987654321@c.us)
 */
function formatWhatsAppNumber(phone) {
    let cleaned = phone.replace(/\D/g, '');
    // Si es un número peruano de 9 dígitos sin código de país, agregar prefijo 51
    if (cleaned.length === 9) {
        cleaned = '51' + cleaned;
    }
    return `${cleaned}@c.us`;
}

/**
 * Genera el texto del mensaje con formato estético de alta costura
 */
function buildMessageText({ type, clientName, contractCode, date, garmentName, depositAmount, storeName }) {
    const store = storeName || 'Sede Principal San Isidro';
    
    switch (type) {
        case 'EVENT_REMINDER':
            return `✨ *I.G.A.V. Gala Luxury - Recordatorio de Recojo de Vestuario* ✨\n\n` +
                   `Estimada/o *${clientName}*,\n\n` +
                   `Le recordamos cordialmente que su vestuario de alta costura (*${garmentName || 'Prenda de Gala'}*) ` +
                   `correspondiente al contrato *${contractCode}* se encuentra planchado al vapor, entallado y listo para retiro ` +
                   `en nuestra tienda este *${date}*.\n\n` +
                   `📍 *Sede de Retiro:* ${store}\n` +
                   `📄 *Requisito:* Presentar su DNI / Documento de Identidad en recepción.\n\n` +
                   `_¡Será un honor vestirle para su evento especial!_\n` +
                   `*I.G.A.V. Haute Couture & Gala Management*`;

        case 'RETURN_ALERT':
            return `⚠️ *I.G.A.V. Gala Luxury - Alerta Preventiva de Devolución* ⚠️\n\n` +
                   `Estimada/o *${clientName}*,\n\n` +
                   `Esperamos que haya disfrutado al máximo de su evento. Le recordamos que la fecha límite pactada ` +
                   `para la devolución de sus prendas (*${garmentName || 'Vestuario de Gala'}*) bajo el contrato *${contractCode}* ` +
                   `es mañana *${date}*.\n\n` +
                   `💰 *Depósito en Custodia:* S/. ${depositAmount ? Number(depositAmount).toFixed(2) : '0.00'}\n` +
                   `🔒 *Liquidación Inmediata:* Al entregar las prendas a tiempo en nuestro módulo de inspección, ` +
                   `se procederá a la devolución íntegra de su garantía de inmediato.\n\n` +
                   `⏰ *Importante:* Entregas posteriores generarán un recargo por mora de S/. 50.00 por día de retraso (Cláusula 2 del contrato).\n\n` +
                   `📍 *Punto de Entrega:* ${store}\n` +
                   `*I.G.A.V. Concierge & Logistics*`;

        case 'CONTRACT_CONFIRMATION':
            return `🎩 *I.G.A.V. - Confirmación de Contrato de Alquiler de Gala* 📜\n\n` +
                   `Estimada/o *${clientName}*,\n\n` +
                   `Su contrato oficial *${contractCode}* ha sido emitido con éxito.\n\n` +
                   `👗 *Prendas Reservadas:* ${garmentName || 'Vestuario de Gala'}\n` +
                   `📅 *Fecha de Retiro:* ${date}\n` +
                   `🛡️ *Garantía en Custodia:* S/. ${depositAmount ? Number(depositAmount).toFixed(2) : '0.00'}\n\n` +
                   `Puede descargar su comprobante y contrato digital desde nuestra plataforma.\n` +
                   `_¡Gracias por confiar en la distinción de I.G.A.V.!_`;

        default:
            return `✨ *I.G.A.V. Notificaciones* ✨\n\nEstimada/o ${clientName}, le saludamos de I.G.A.V. respecto a su contrato ${contractCode}.`;
    }
}

// ================= ENDPOINTS DE LA API =================

/**
 * Consulta el estado actual de la conexión de WhatsApp
 */
app.get('/api/wsp/status', (req, res) => {
    res.json({
        status: connectionStatus,
        hasQr: !!lastQrCode,
        timestamp: new Date().toISOString()
    });
});

/**
 * Obtiene el código QR en texto para visualización
 */
app.get('/api/wsp/qr', (req, res) => {
    res.json({
        status: connectionStatus,
        qr: lastQrCode
    });
});

/**
 * Envía una notificación programada o bajo demanda por WhatsApp
 */
app.post('/api/wsp/send-alert', async (req, res) => {
    try {
        const { phone, type, clientName, contractCode, date, garmentName, depositAmount, storeName } = req.body;

        if (!phone || !clientName || !contractCode) {
            return res.status(400).json({
                error: 'Faltan parámetros obligatorios: phone, clientName, contractCode'
            });
        }

        const messageText = buildMessageText({
            type,
            clientName,
            contractCode,
            date,
            garmentName,
            depositAmount,
            storeName
        });

        const formattedPhone = formatWhatsAppNumber(phone);

        // Si el cliente está conectado, enviar por whatsapp-web.js
        if (connectionStatus === 'CONNECTED') {
            const response = await client.sendMessage(formattedPhone, messageText);
            console.log(`>>> [WSP-JS] Mensaje enviado a ${phone} (${contractCode}): ID ${response.id.id}`);
            return res.json({
                success: true,
                mode: 'WHATSAPP_WEB_JS',
                messageId: response.id.id,
                phone: formattedPhone,
                type,
                contractCode,
                preview: messageText
            });
        }

        // Si aún no está vinculado por QR, devolver el mensaje preparado y enlace web directo como fallback
        const directUrl = `https://api.whatsapp.com/send?phone=${phone.replace(/\D/g, '')}&text=${encodeURIComponent(messageText)}`;
        console.warn(`>>> [WSP-JS] Cliente WhatsApp no conectado. Generando fallback directo: ${directUrl}`);

        return res.json({
            success: true,
            mode: 'FALLBACK_URL',
            note: 'El bot whatsapp-web.js no está escaneado en terminal, pero el mensaje fue generado exitosamente.',
            directWhatsAppUrl: directUrl,
            phone,
            type,
            contractCode,
            preview: messageText
        });
    } catch (error) {
        console.error('>>> [WSP-JS] Error al enviar mensaje WhatsApp:', error);
        res.status(500).json({
            error: 'Error interno al procesar el envío de WhatsApp',
            details: error.message
        });
    }
});

app.listen(PORT, () => {
    console.log(`>>> [WSP-JS] Servidor API de Notificaciones WhatsApp escuchando en http://localhost:${PORT}`);
});
