'use client';

import React, { useRef } from 'react';
import { 
  X, 
  Printer, 
  Download, 
  ShieldCheck, 
  Calendar, 
  User as UserIcon, 
  Building2, 
  Sparkles, 
  CheckCircle2, 
  Send
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Order, Customer, Store } from '@/lib/types';
import { toast } from 'sonner';

interface ContractPdfModalProps {
  isOpen: boolean;
  onClose: () => void;
  order: Order | null;
  customer?: Customer | null;
  store?: Store | null;
  onSendWhatsApp?: (order: Order) => void;
}

export const ContractPdfModal: React.FC<ContractPdfModalProps> = ({
  isOpen,
  onClose,
  order,
  customer,
  store,
  onSendWhatsApp
}) => {
  const printAreaRef = useRef<HTMLDivElement>(null);

  if (!isOpen || !order) return null;

  const handlePrint = () => {
    window.print();
    toast.success('Abriendo diálogo de impresión y exportación a PDF');
  };

  const storeName = store?.nombre || 'Sede Principal San Isidro';
  const storeAddress = store?.direccion || 'Av. Conquistadores 890, San Isidro, Lima';
  const storePhone = store?.telefono || '+51 1 421-9988';
  const storeRuc = '20608912345';

  const clientName = order.clienteNombreCompleto || customer?.nombreCompleto || 'Cliente Distinguido';
  const clientDoc = order.clienteDocumento || customer?.numeroDocumento || 'DNI/RUC Pendiente';
  const clientPhone = order.clienteTelefono || customer?.telefono || '+51 900 000 000';
  const clientAddress = customer?.direccion || 'Av. Principal San Isidro';

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md overflow-y-auto">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 20 }}
          className="relative w-full max-w-4xl bg-stone-900 border border-stone-800 rounded-2xl shadow-2xl overflow-hidden my-8"
        >
          {/* Barra de Acciones Superior (Oculta al imprimir) */}
          <div className="flex items-center justify-between px-6 py-4 border-b border-stone-800 bg-stone-950/80 print:hidden">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
                <Sparkles className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-lg font-serif font-bold text-stone-100">
                  Contrato Oficial & Recibo de Garantía
                </h3>
                <p className="text-xs text-stone-400">
                  Código: <span className="font-mono text-amber-400 font-semibold">{order.codigoContrato}</span>
                </p>
              </div>
            </div>

            <div className="flex items-center space-x-3">
              {onSendWhatsApp && (
                <button
                  onClick={() => onSendWhatsApp(order)}
                  className="flex items-center space-x-2 px-3 py-2 bg-emerald-600/20 hover:bg-emerald-600/30 text-emerald-400 border border-emerald-500/30 rounded-xl text-xs font-medium transition-all"
                  title="Enviar por WhatsApp"
                >
                  <Send className="w-4 h-4" />
                  <span>Enviar WhatsApp</span>
                </button>
              )}

              <button
                onClick={handlePrint}
                className="flex items-center space-x-2 px-4 py-2 bg-amber-500 hover:bg-amber-400 text-stone-950 rounded-xl font-semibold text-xs transition-all shadow-lg shadow-amber-500/10"
              >
                <Printer className="w-4 h-4" />
                <span>Imprimir / Descargar PDF</span>
              </button>

              <button
                onClick={onClose}
                className="p-2 text-stone-400 hover:text-stone-100 hover:bg-stone-800 rounded-xl transition-all"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
          </div>

          {/* DOCUMENTO IMPRIMIBLE (Formato A4 Oficial) */}
          <div 
            ref={printAreaRef}
            className="p-8 md:p-12 bg-white text-stone-900 font-sans print:p-0 print:m-0"
            id="printable-contract"
          >
            {/* Encabezado Membretado */}
            <div className="border-b-2 border-stone-900 pb-6 mb-8 flex justify-between items-start">
              <div>
                <div className="flex items-center space-x-2">
                  <div className="w-8 h-8 rounded bg-stone-900 text-amber-400 flex items-center justify-center font-serif font-black text-xl">
                    I
                  </div>
                  <span className="font-serif text-2xl font-bold tracking-wider text-stone-950">
                    I.G.A.V. GALA LUXURY
                  </span>
                </div>
                <p className="text-xs uppercase tracking-widest text-stone-600 font-semibold mt-1">
                  Haute Couture & Formalwear Rental SaaS
                </p>
                <div className="text-xs text-stone-600 mt-2 space-y-0.5">
                  <p><strong>Razón Social:</strong> IGAV Gala Luxury S.A.C. | RUC: {storeRuc}</p>
                  <p><strong>Sede Emisora:</strong> {storeName} — {storeAddress}</p>
                  <p><strong>Central Telefónica:</strong> {storePhone} | soporte@igav-gala.pe</p>
                </div>
              </div>

              <div className="text-right border border-stone-900 p-4 rounded-lg bg-stone-50">
                <span className="text-[10px] font-bold tracking-widest uppercase text-stone-500 block">
                  CONTRATO DE ARRENDAMIENTO
                </span>
                <span className="font-mono text-xl font-black text-stone-950 block mt-1">
                  {order.codigoContrato}
                </span>
                <span className="text-xs text-stone-600 mt-1 block">
                  Fecha Emisión: {new Date().toLocaleDateString('es-PE', { day: '2-digit', month: 'long', year: 'numeric' })}
                </span>
              </div>
            </div>

            {/* Datos del Cliente & Fechas del Servicio */}
            <div className="grid grid-cols-2 gap-6 p-4 rounded-lg bg-stone-100 mb-8 border border-stone-200 text-xs">
              <div>
                <h4 className="font-bold text-stone-900 uppercase tracking-wider text-[11px] mb-2 border-b border-stone-300 pb-1">
                  1. Datos del Cliente Arrendatario
                </h4>
                <div className="space-y-1 text-stone-700">
                  <p><strong>Nombre Completo:</strong> {clientName}</p>
                  <p><strong>Doc. Identidad:</strong> {clientDoc}</p>
                  <p><strong>Teléfono Contacto:</strong> {clientPhone}</p>
                  <p><strong>Dirección:</strong> {clientAddress}</p>
                </div>
              </div>

              <div>
                <h4 className="font-bold text-stone-900 uppercase tracking-wider text-[11px] mb-2 border-b border-stone-300 pb-1">
                  2. Fechas Pactadas de Servicio
                </h4>
                <div className="space-y-1 text-stone-700">
                  <p><strong>Fecha de Entrega/Retiro:</strong> {order.fechaEntregaAcordada || '2026-09-28'}</p>
                  <p><strong>Fecha Límite Devolución:</strong> <span className="text-red-700 font-bold">{order.fechaDevolucionAcordada || '2026-10-01'}</span></p>
                  <p><strong>Estado del Contrato:</strong> {order.estado}</p>
                  <p><strong>Tipo de Orden:</strong> {order.tipo}</p>
                </div>
              </div>
            </div>

            {/* Tabla Detallada de Prendas */}
            <div className="mb-8">
              <h4 className="font-bold text-stone-900 uppercase tracking-wider text-xs mb-3">
                3. Detalle de Prendas de Alta Costura Arrendadas
              </h4>
              <table className="w-full text-xs text-left border-collapse">
                <thead>
                  <tr className="bg-stone-900 text-white uppercase text-[10px] tracking-wider">
                    <th className="py-2.5 px-3">Código SKU</th>
                    <th className="py-2.5 px-3">Descripción de la Prenda</th>
                    <th className="py-2.5 px-3">Modalidad</th>
                    <th className="py-2.5 px-3 text-right">Tarifa Alquiler</th>
                    <th className="py-2.5 px-3 text-right">Depósito Custodia</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-stone-200">
                  {(order.items || []).map((item, idx) => (
                    <tr key={idx} className="hover:bg-stone-50">
                      <td className="py-3 px-3 font-mono font-bold text-stone-800">{item.garmentSku}</td>
                      <td className="py-3 px-3">
                        <span className="font-semibold text-stone-900 block">{item.garmentName}</span>
                        <span className="text-[11px] text-stone-500">Ajuste de gala y planchado al vapor incluido</span>
                      </td>
                      <td className="py-3 px-3 font-medium">{item.tipoItem}</td>
                      <td className="py-3 px-3 text-right font-semibold">S/. {item.precioAplicado.toFixed(2)}</td>
                      <td className="py-3 px-3 text-right font-mono text-amber-800 font-bold">
                        S/. {item.garantiaAplicada.toFixed(2)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Resumen Financiero y Depósito en Garantía */}
            <div className="flex justify-end mb-8">
              <div className="w-80 bg-stone-50 border border-stone-300 p-4 rounded-lg text-xs space-y-2">
                <div className="flex justify-between text-stone-700">
                  <span>Subtotal Alquiler:</span>
                  <span className="font-semibold">S/. {order.subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-amber-800 font-bold border-t border-stone-200 pt-2">
                  <span>Depósito de Garantía (En Custodia):</span>
                  <span>S/. {order.montoGarantiaTotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-stone-900 font-black text-sm border-t-2 border-stone-900 pt-2">
                  <span>Total Cobrado en Caja:</span>
                  <span>S/. {order.montoTotal.toFixed(2)}</span>
                </div>
                <p className="text-[10px] text-stone-500 italic text-right mt-1">
                  * La garantía será reembolsada íntegramente tras la inspección técnica pos-evento.
                </p>
              </div>
            </div>

            {/* Cláusulas Legales del Arrendamiento */}
            <div className="border border-stone-300 p-4 rounded-lg bg-stone-50/50 text-[10px] text-stone-600 mb-10 space-y-1.5 leading-relaxed">
              <p className="font-bold text-stone-900 uppercase tracking-wider text-[11px]">
                Términos y Condiciones del Servicio (Cláusulas Resolutivas):
              </p>
              <p>
                <strong>1. Devolución y Horario:</strong> Las prendas deben ser entregadas en la sede emisora a más tardar el <strong>{order.fechaDevolucionAcordada || 'la fecha acordada'}</strong> antes de las 19:00 hrs.
              </p>
              <p>
                <strong>2. Penalidad por Mora:</strong> Todo retraso en la devolución devengará una penalización automática de <strong>S/. 50.00 (Cincuenta y 00/100 Soles) por día calendario de mora</strong>, la cual será descontada de forma directa del depósito en garantía (RF-08).
              </p>
              <p>
                <strong>3. Inspección Técnica y Daños:</strong> Al momento de la entrega física, el personal técnico inspeccionará las prendas. En caso de manchas graves, quemaduras, desgarros o pérdida de accesorios, se aplicarán las tarifas de restauración establecidas en el tarifario de incidencias (RF-13). Si las prendas se entregan en perfecto estado, la garantía se liquidará y devolverá en su totalidad.
              </p>
              <p>
                <strong>4. Cuarentena de Tintorería:</strong> No se requiere que el cliente lave la prenda por cuenta propia. Todo vestuario ingresará a un proceso de desinfección y limpieza en seco en tintorería especializada por un lapso preventivo de 24 a 48 horas (RF-05).
              </p>
            </div>

            {/* Firmas de Conformidad */}
            <div className="grid grid-cols-2 gap-12 pt-8 border-t border-stone-300 text-center text-xs">
              <div>
                <div className="border-b border-stone-400 pb-12 mb-2"></div>
                <p className="font-bold text-stone-900">IGAV GALA LUXURY S.A.C.</p>
                <p className="text-stone-500 text-[11px]">Firma Autorizada y Sello de Sede</p>
              </div>

              <div>
                <div className="border-b border-stone-400 pb-12 mb-2"></div>
                <p className="font-bold text-stone-900">{clientName}</p>
                <p className="text-stone-500 text-[11px]">Arrendatario — Doc: {clientDoc}</p>
              </div>
            </div>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
