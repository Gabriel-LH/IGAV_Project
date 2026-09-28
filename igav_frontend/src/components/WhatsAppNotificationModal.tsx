'use client';

import React, { useState } from 'react';
import { 
  X, 
  Send, 
  MessageSquare, 
  Calendar, 
  ShieldCheck, 
  AlertTriangle, 
  Sparkles, 
  Copy, 
  Check, 
  ExternalLink,
  Bot
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Order, Customer } from '@/lib/types';
import { toast } from 'sonner';

interface WhatsAppNotificationModalProps {
  isOpen: boolean;
  onClose: () => void;
  order: Order | null;
  customer?: Customer | null;
  storeName?: string;
}

export type NotificationType = 'EVENT_REMINDER' | 'RETURN_ALERT' | 'CONTRACT_CONFIRMATION';

export const WhatsAppNotificationModal: React.FC<WhatsAppNotificationModalProps> = ({
  isOpen,
  onClose,
  order,
  customer,
  storeName = 'Sede Principal San Isidro'
}) => {
  const [selectedType, setSelectedType] = useState<NotificationType>('RETURN_ALERT');
  const [isSendingBot, setIsSendingBot] = useState(false);
  const [copied, setCopied] = useState(false);

  if (!isOpen || !order) return null;

  const clientName = order.clienteNombreCompleto || customer?.nombreCompleto || 'Cliente Distinguido';
  const clientPhone = order.clienteTelefono || customer?.telefono || '+51 987 654 321';
  const garmentName = (order.items || []).map(i => i.garmentName).join(', ') || 'Vestuario de Gala';
  const depositAmount = order.montoGarantiaTotal || 200.0;
  const eventDate = order.fechaEntregaAcordada || '2026-09-28';
  const returnDate = order.fechaDevolucionAcordada || '2026-10-01';

  // Generador de texto según la plantilla seleccionada
  const getMessageContent = (): string => {
    switch (selectedType) {
      case 'EVENT_REMINDER':
        return `✨ *I.G.A.V. Gala Luxury - Recordatorio de Recojo de Vestuario* ✨\n\n` +
               `Estimada/o *${clientName}*,\n\n` +
               `Le recordamos cordialmente que su vestuario de alta costura (*${garmentName}*) ` +
               `correspondiente al contrato *${order.codigoContrato}* se encuentra planchado al vapor, entallado y listo para retiro ` +
               `en nuestra tienda este *${eventDate}*.\n\n` +
               `📍 *Sede de Retiro:* ${storeName}\n` +
               `📄 *Requisito:* Presentar su DNI o documento de identidad en recepción.\n\n` +
               `_¡Será un honor vestirle para su evento especial!_\n` +
               `*I.G.A.V. Haute Couture & Gala Management*`;

      case 'RETURN_ALERT':
        return `⚠️ *I.G.A.V. Gala Luxury - Alerta Preventiva de Devolución* ⚠️\n\n` +
               `Estimada/o *${clientName}*,\n\n` +
               `Esperamos que haya disfrutado al máximo de su evento. Le recordamos que la fecha límite pactada ` +
               `para la devolución de sus prendas (*${garmentName}*) bajo el contrato *${order.codigoContrato}* ` +
               `es mañana *${returnDate}*.\n\n` +
               `💰 *Depósito en Custodia:* S/. ${depositAmount.toFixed(2)}\n` +
               `🔒 *Liquidación Inmediata:* Al entregar las prendas a tiempo en nuestro módulo de inspección, ` +
               `se procederá a la devolución íntegra de su garantía de inmediato.\n\n` +
               `⏰ *Importante:* Entregas posteriores generarán un recargo por mora de S/. 50.00 por día de retraso (Cláusula 2 del contrato).\n\n` +
               `📍 *Punto de Entrega:* ${storeName}\n` +
               `*I.G.A.V. Concierge & Logistics*`;

      case 'CONTRACT_CONFIRMATION':
        return `🎩 *I.G.A.V. - Confirmación de Contrato de Alquiler de Gala* 📜\n\n` +
               `Estimada/o *${clientName}*,\n\n` +
               `Su contrato oficial *${order.codigoContrato}* ha sido emitido con éxito.\n\n` +
               `👗 *Prendas Reservadas:* ${garmentName}\n` +
               `📅 *Fecha de Retiro:* ${eventDate}\n` +
               `📅 *Fecha de Devolución:* ${returnDate}\n` +
               `🛡️ *Garantía en Custodia:* S/. ${depositAmount.toFixed(2)}\n\n` +
               `Puede descargar su comprobante y contrato digital desde nuestra plataforma.\n` +
               `_¡Gracias por confiar en la distinción de I.G.A.V.!_`;
    }
  };

  const messageText = getMessageContent();

  const handleCopy = () => {
    navigator.clipboard.writeText(messageText);
    setCopied(true);
    toast.success('Mensaje copiado al portapapeles');
    setTimeout(() => setCopied(false), 2000);
  };

  const handleDirectWhatsApp = () => {
    const cleanPhone = clientPhone.replace(/\D/g, '');
    const phoneWithCountry = cleanPhone.length === 9 ? `51${cleanPhone}` : cleanPhone;
    const url = `https://api.whatsapp.com/send?phone=${phoneWithCountry}&text=${encodeURIComponent(messageText)}`;
    window.open(url, '_blank');
    toast.success('Abriendo WhatsApp Web / App...');
  };

  const handleSendViaBot = async () => {
    setIsSendingBot(true);
    try {
      const response = await fetch('http://localhost:5001/api/wsp/send-alert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          phone: clientPhone,
          type: selectedType,
          clientName,
          contractCode: order.codigoContrato,
          date: selectedType === 'RETURN_ALERT' ? returnDate : eventDate,
          garmentName,
          depositAmount,
          storeName
        })
      });

      const data = await response.json();

      if (data.mode === 'WHATSAPP_WEB_JS') {
        toast.success(`¡Alerta enviada exitosamente por whatsapp-web.js a ${clientPhone}!`);
        onClose();
      } else if (data.mode === 'FALLBACK_URL') {
        toast.info('Microservicio listo. Redirigiendo a WhatsApp Web directo para entrega inmediata.');
        window.open(data.directWhatsAppUrl, '_blank');
        onClose();
      } else {
        toast.success('Mensaje despachado');
        onClose();
      }
    } catch (error) {
      toast.warning('Servicio wsp-js en puerto 5001 no detectado. Despachando vía enlace directo Web.');
      handleDirectWhatsApp();
      onClose();
    } finally {
      setIsSendingBot(false);
    }
  };

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md overflow-y-auto">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 20 }}
          className="relative w-full max-w-2xl bg-stone-900 border border-stone-800 rounded-2xl shadow-2xl overflow-hidden my-8"
        >
          {/* Header */}
          <div className="flex items-center justify-between px-6 py-4 border-b border-stone-800 bg-stone-950/80">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400">
                <MessageSquare className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-base font-serif font-bold text-stone-100 flex items-center gap-2">
                  Notificaciones WhatsApp (wsp-js)
                  <span className="text-[10px] font-sans font-medium px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                    Auto Bot + Direct
                  </span>
                </h3>
                <p className="text-xs text-stone-400">
                  Cliente: <span className="text-stone-200 font-semibold">{clientName}</span> ({clientPhone})
                </p>
              </div>
            </div>

            <button
              onClick={onClose}
              className="p-2 text-stone-400 hover:text-stone-100 hover:bg-stone-800 rounded-xl transition-all"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          <div className="p-6 space-y-6">
            {/* Selector de Plantilla */}
            <div>
              <label className="block text-xs font-semibold text-stone-400 uppercase tracking-wider mb-2">
                Seleccione el Tipo de Notificación
              </label>
              <div className="grid grid-cols-3 gap-2.5">
                <button
                  type="button"
                  onClick={() => setSelectedType('RETURN_ALERT')}
                  className={`p-3 rounded-xl border text-left transition-all ${
                    selectedType === 'RETURN_ALERT'
                      ? 'bg-amber-500/10 border-amber-500/40 text-amber-300 shadow-lg shadow-amber-500/5'
                      : 'bg-stone-950/40 border-stone-800 text-stone-400 hover:border-stone-700'
                  }`}
                >
                  <AlertTriangle className="w-4 h-4 mb-1.5 text-amber-400" />
                  <p className="text-xs font-bold">Alerta Devolución (24h)</p>
                  <p className="text-[10px] text-stone-400 mt-0.5">Prevenir cobro por mora</p>
                </button>

                <button
                  type="button"
                  onClick={() => setSelectedType('EVENT_REMINDER')}
                  className={`p-3 rounded-xl border text-left transition-all ${
                    selectedType === 'EVENT_REMINDER'
                      ? 'bg-amber-500/10 border-amber-500/40 text-amber-300 shadow-lg shadow-amber-500/5'
                      : 'bg-stone-950/40 border-stone-800 text-stone-400 hover:border-stone-700'
                  }`}
                >
                  <Calendar className="w-4 h-4 mb-1.5 text-blue-400" />
                  <p className="text-xs font-bold">Recordatorio Recojo (24h)</p>
                  <p className="text-[10px] text-stone-400 mt-0.5">Prenda entallada y lista</p>
                </button>

                <button
                  type="button"
                  onClick={() => setSelectedType('CONTRACT_CONFIRMATION')}
                  className={`p-3 rounded-xl border text-left transition-all ${
                    selectedType === 'CONTRACT_CONFIRMATION'
                      ? 'bg-amber-500/10 border-amber-500/40 text-amber-300 shadow-lg shadow-amber-500/5'
                      : 'bg-stone-950/40 border-stone-800 text-stone-400 hover:border-stone-700'
                  }`}
                >
                  <ShieldCheck className="w-4 h-4 mb-1.5 text-emerald-400" />
                  <p className="text-xs font-bold">Emisión Contrato</p>
                  <p className="text-[10px] text-stone-400 mt-0.5">Custodia de garantía</p>
                </button>
              </div>
            </div>

            {/* Vista Previa del Mensaje Estilo WhatsApp Bubble */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <label className="text-xs font-semibold text-stone-400 uppercase tracking-wider">
                  Vista Previa del Mensaje a Enviar
                </label>
                <button
                  type="button"
                  onClick={handleCopy}
                  className="flex items-center space-x-1 text-xs text-stone-400 hover:text-stone-200 transition-colors"
                >
                  {copied ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{copied ? 'Copiado' : 'Copiar Texto'}</span>
                </button>
              </div>

              <div className="bg-[#0b141a] p-4 rounded-xl border border-stone-800 text-xs font-sans text-stone-200 whitespace-pre-wrap relative shadow-inner leading-relaxed">
                <div className="bg-[#005c4b] text-[#e9edef] p-3 rounded-xl rounded-tl-none shadow max-w-lg">
                  {messageText}
                </div>
                <div className="text-[10px] text-stone-500 mt-2 text-right">
                  Destinatario: <span className="font-mono text-stone-400">{clientPhone}</span>
                </div>
              </div>
            </div>

            {/* Microservicio Bot Info */}
            <div className="bg-stone-950/60 p-3.5 rounded-xl border border-stone-800/80 flex items-center justify-between text-xs">
              <div className="flex items-center space-x-2.5">
                <Bot className="w-4 h-4 text-emerald-400" />
                <div>
                  <span className="font-semibold text-stone-200">Microservicio wsp-js (whatsapp-web.js)</span>
                  <p className="text-[11px] text-stone-400">
                    Puerto: <code className="text-amber-400">http://localhost:5001</code> | Script: <code className="text-stone-300">start_wsp.bat</code>
                  </p>
                </div>
              </div>
              <span className="text-[10px] px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-medium">
                Listo para Conectar
              </span>
            </div>

            {/* Acciones */}
            <div className="flex items-center justify-end space-x-3 pt-2">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-xs font-semibold text-stone-400 hover:text-stone-200 transition-colors"
              >
                Cerrar
              </button>

              <button
                type="button"
                onClick={handleDirectWhatsApp}
                className="flex items-center space-x-2 px-4 py-2 bg-stone-800 hover:bg-stone-700 text-stone-200 rounded-xl text-xs font-semibold transition-all border border-stone-700"
              >
                <ExternalLink className="w-3.5 h-3.5" />
                <span>Abrir en WhatsApp Web</span>
              </button>

              <button
                type="button"
                disabled={isSendingBot}
                onClick={handleSendViaBot}
                className="flex items-center space-x-2 px-5 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl text-xs font-bold transition-all shadow-lg shadow-emerald-600/20 disabled:opacity-50"
              >
                <Send className="w-4 h-4" />
                <span>{isSendingBot ? 'Enviando Alerta...' : 'Enviar con wsp-js (Bot)'}</span>
              </button>
            </div>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
