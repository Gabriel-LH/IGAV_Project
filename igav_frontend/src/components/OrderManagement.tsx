'use client';

import React, { useState, useEffect } from 'react';
import { 
  CalendarCheck, 
  Plus, 
  Calendar, 
  ShieldCheck, 
  AlertOctagon, 
  CheckCircle2, 
  DollarSign, 
  X,
  Sparkles,
  Clock,
  Printer,
  FileText,
  Send,
  MessageSquare,
  User,
  Scissors,
  Check
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Garment, Order, Customer } from '../lib/types';
import { toast } from 'sonner';

interface OrderManagementProps {
  orders: Order[];
  garments: Garment[];
  customers: Customer[];
  isOpenNewModal: boolean;
  setIsOpenNewModal: (open: boolean) => void;
  onAddOrder: (order: Order) => void;
  onDownloadPdf?: (order: Order) => void;
  onSendWhatsApp?: (order: Order) => void;
}

export const OrderManagement: React.FC<OrderManagementProps> = ({
  orders,
  garments,
  customers,
  isOpenNewModal,
  setIsOpenNewModal,
  onAddOrder,
  onDownloadPdf,
  onSendWhatsApp
}) => {
  // Form State
  const [selectedCustomerId, setSelectedCustomerId] = useState<number>(
    customers[0]?.id || 1
  );
  const [clienteNombre, setClienteNombre] = useState(customers[0]?.nombreCompleto || '');
  const [clienteDocumento, setClienteDocumento] = useState(customers[0]?.numeroDocumento || '');
  const [clienteTelefono, setClienteTelefono] = useState(customers[0]?.telefono || '');

  const [selectedGarmentId, setSelectedGarmentId] = useState<number>(
    garments[0]?.id || 1
  );
  const [fechaEntrega, setFechaEntrega] = useState('2026-10-16');
  const [fechaDevolucion, setFechaDevolucion] = useState('2026-10-19');

  // Bespoke Fitting / Sastrería Fina state
  const [bustoCm, setBustoCm] = useState(88);
  const [cinturaCm, setCinturaCm] = useState(68);
  const [caderaCm, setCaderaCm] = useState(94);
  const [alturaConTaconCm, setAlturaConTaconCm] = useState(172);
  const [solicitarConserjeSastre, setSolicitarConserjeSastre] = useState(false);

  const selectedGarment = garments.find((g) => g.id === Number(selectedGarmentId)) || garments[0];

  const handleCustomerChange = (customerId: number) => {
    setSelectedCustomerId(customerId);
    const c = customers.find((cust) => cust.id === Number(customerId)) as any;
    if (c) {
      const telStr = typeof c.telefono === 'object' && c.telefono !== null ? (c.telefono.value || '') : (c.telefono || '');
      const numDocStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null ? (c.documentoIdentidad.numero || '') : (c.numeroDocumento || '');
      const nameStr = c.nombreCompleto || `${c.nombres || ''} ${c.apellidos || ''}`.trim() || 'Cliente';
      setClienteNombre(nameStr);
      setClienteDocumento(numDocStr);
      setClienteTelefono(telStr);
    }
  };

  useEffect(() => {
    if (customers && customers.length > 0) {
      const match = customers.find((c) => c.id === selectedCustomerId) || customers[0];
      if (match) {
        handleCustomerChange(match.id);
      }
    }
  }, [customers]);

  useEffect(() => {
    if (garments && garments.length > 0 && !garments.some((g) => g.id === Number(selectedGarmentId))) {
      setSelectedGarmentId(garments[0].id);
    }
  }, [garments]);

  // RF-04 Date Collision Validation Check Logic
  const checkDateCollision = (): { isCollision: boolean; reason?: string } => {
    if (!fechaEntrega || !fechaDevolucion) return { isCollision: false };

    const startReq = new Date(fechaEntrega).getTime();
    const endReq = new Date(fechaDevolucion).getTime();

    if (endReq <= startReq) {
      return { isCollision: true, reason: 'La fecha de devolución debe ser posterior a la fecha de entrega.' };
    }

    // Check existing orders for this garment
    for (const order of orders) {
      if (order.items?.some((i) => i.garmentId === selectedGarment?.id)) {
        const orderStart = new Date(order.fechaEntregaAcordada).getTime();
        // Add 24h-48h laundry block buffer (RF-05) to return date
        const bufferHours = selectedGarment?.horasTintoreriaBloqueo || 24;
        const orderEndWithLaundry = new Date(order.fechaDevolucionAcordada).getTime() + (bufferHours * 3600 * 1000);

        if (startReq < orderEndWithLaundry && endReq > orderStart) {
          return {
            isCollision: true,
            reason: `Conflicto de Fechas (RF-04): La pieza ya posee una reserva asignada (${order.codigoContrato}) o se encontrará en ciclo de vaporizado y regeneración artesanal hasta el ${new Date(orderEndWithLaundry).toLocaleDateString('es-ES')}.`
          };
        }
      }
    }

    return { isCollision: false };
  };

  const collisionResult = checkDateCollision();

  const handleCreateOrder = (e: React.FormEvent) => {
    e.preventDefault();
    if (collisionResult.isCollision) {
      toast.error(collisionResult.reason);
      return;
    }
    const targetCust = customers.find((c) => c.id === selectedCustomerId) || customers[0];
    const finalNombre = clienteNombre || (targetCust ? (targetCust.nombreCompleto || `${targetCust.nombres || ''} ${targetCust.apellidos || ''}`.trim()) : 'Cliente Registrado');
    const finalDoc = clienteDocumento || (targetCust?.numeroDocumento || '71029384');
    const finalTel = clienteTelefono || (targetCust?.telefono || '+51 987 654 321');

    const bespokeNote = `[Bespoke Fitting: Busto ${bustoCm}cm, Cintura ${cinturaCm}cm, Cadera ${caderaCm}cm, Altura/Tacón ${alturaConTaconCm}cm${solicitarConserjeSastre ? ' · Asistencia VIP de Sastre en Suite Solicitada' : ''}]`;

    const newOrder: Order = {
      id: Date.now(),
      codigoContrato: `ORD-2026-${Math.floor(1000 + Math.random() * 9000)}`,
      customerId: targetCust ? targetCust.id : selectedCustomerId,
      clienteDocumento: finalDoc,
      clienteNombreCompleto: finalNombre,
      clienteTelefono: finalTel,
      storeId: 1,
      tipo: 'ALQUILER',
      fechaEntregaAcordada: `${fechaEntrega}T10:00:00`,
      fechaDevolucionAcordada: `${fechaDevolucion}T18:00:00`,
      subtotal: selectedGarment ? Number(selectedGarment.precioAlquiler) : 280,
      montoGarantiaTotal: selectedGarment ? Number(selectedGarment.depositoGarantia) : 100,
      descuentoGarantia: 0,
      montoPenalizacion: 0,
      montoTotal: selectedGarment ? Number(selectedGarment.precioAlquiler) : 280,
      garantiaDevueltaNeta: selectedGarment ? Number(selectedGarment.depositoGarantia) : 100,
      estado: 'CONFIRMADA',
      items: selectedGarment ? [
        {
          garmentId: selectedGarment.id,
          garmentName: selectedGarment.nombre,
          garmentSku: selectedGarment.codigoUnico,
          tipoItem: 'ALQUILER',
          precioAplicado: Number(selectedGarment.precioAlquiler),
          garantiaAplicada: Number(selectedGarment.depositoGarantia)
        }
      ] : []
    };

    onAddOrder(newOrder);
    setIsOpenNewModal(false);
  };

  return (
    <div className="space-y-8">
      {/* Editorial Header */}
      <div className="border-b border-[var(--border-subtle)] pb-6 pt-2 flex flex-col sm:flex-row sm:items-end justify-between gap-4">
        <div className="space-y-2">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]"></span>
            <p className="text-[10px] uppercase tracking-[0.24em] font-mono text-[var(--accent-gold)]">
              Protocolo de Custodia Temporal · Alta Costura
            </p>
          </div>
          <h2 className="font-serif-editorial text-3xl sm:text-4xl text-[var(--text-primary)] italic font-normal tracking-wide">
            Contratos de Alquiler & Reservas de Gala
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-light max-w-xl leading-relaxed">
            Detección de solapamiento de fechas en tiempo real (RF-04), asignación de fianza patrimonial (RF-06) 
            y especificación de medidas para la costura fina de atelier.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsOpenNewModal(true)}
          className="px-5 py-2.5 text-xs font-mono font-medium tracking-wider uppercase bg-[#1A1817] dark:bg-[#EDE9E1] text-[#F6F4EE] dark:text-[#1A1817] hover:opacity-90 transition-all flex items-center gap-2 shadow-sm self-start sm:self-auto"
        >
          <Plus className="w-3.5 h-3.5 text-[var(--accent-gold)]" />
          <span>Formalizar Nueva Reserva</span>
        </motion.button>
      </div>

      {/* Orders List Table */}
      <div className="border border-[var(--border-subtle)] bg-[var(--surface-card)] overflow-hidden">
        <div className="p-4 border-b border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <CalendarCheck className="w-4 h-4 text-[var(--accent-gold)]" />
            <h3 className="font-serif-editorial text-lg italic text-[var(--text-primary)]">
              Libro de Veladas & Contratos Registrados
            </h3>
          </div>
          <span className="text-[11px] font-mono text-[var(--text-secondary)] tracking-wider uppercase">
            {orders.length} Contratos en Registro
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="border-b border-[var(--border-subtle)] bg-transparent text-[var(--text-secondary)] uppercase font-mono text-[9px] tracking-[0.18em]">
                <th className="p-3.5">Código Contrato</th>
                <th className="p-3.5">Titular de la Custodia</th>
                <th className="p-3.5">Creación de Archivo</th>
                <th className="p-3.5">Entrega Acordada</th>
                <th className="p-3.5">Devolución / Velada</th>
                <th className="p-3.5 text-right">Custodia (S/)</th>
                <th className="p-3.5 text-right">Fianza Retorno</th>
                <th className="p-3.5 text-center">Estado</th>
                <th className="p-3.5 text-center">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border-subtle)] font-mono text-[11px]">
              {orders.length === 0 ? (
                <tr>
                  <td colSpan={9} className="p-12 text-center text-[var(--text-secondary)] font-serif-editorial text-lg italic">
                    No existen contratos de gala registrados en este momento.
                  </td>
                </tr>
              ) : (
                orders.map((order) => {
                  const item = order.items?.[0];
                  return (
                    <tr key={order.id} className="hover:bg-[var(--surface-elevated)] transition-colors">
                      <td className="p-3.5 font-bold text-[var(--accent-gold)] tracking-wider">
                        {order.codigoContrato}
                      </td>
                      <td className="p-3.5 font-sans-editorial text-[var(--text-primary)] font-medium">
                        <div>{order.clienteNombreCompleto}</div>
                        {order.clienteDocumento && (
                          <div className="text-[10px] text-[var(--text-tertiary)] font-mono">DNI: {order.clienteDocumento}</div>
                        )}
                      </td>
                      <td className="p-3.5 text-[var(--text-secondary)] max-w-xs truncate font-serif-editorial text-sm italic">
                        {item?.garmentName || 'Creación de Gala'}
                      </td>
                      <td className="p-3.5 text-[var(--text-secondary)]">
                        {new Date(order.fechaEntregaAcordada).toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: 'numeric' })}
                      </td>
                      <td className="p-3.5 text-[var(--text-secondary)]">
                        {new Date(order.fechaDevolucionAcordada).toLocaleDateString('es-ES', { day: '2-digit', month: 'short', year: 'numeric' })}
                      </td>
                      <td className="p-3.5 text-right font-serif-editorial text-sm italic font-medium text-[var(--text-primary)]">
                        S/ {order.subtotal.toFixed(2)}
                      </td>
                      <td className="p-3.5 text-right text-[var(--accent-sage)]">
                        S/ {order.montoGarantiaTotal.toFixed(2)}
                      </td>
                      <td className="p-3.5 text-center">
                        <span className={`px-2 py-0.5 text-[9px] uppercase tracking-wider font-mono border ${
                          order.estado === 'CONFIRMADA' ? 'bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border-[var(--accent-gold)]/40' :
                          order.estado === 'EN_ALQUILER' || order.estado === 'EN_ALQUILAR' ? 'bg-[#5C1E26]/20 text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/40' :
                          order.estado === 'DEVUELTO_PENDIENTE_TINTORERIA' ? 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/40' :
                          'bg-transparent text-[var(--text-secondary)] border-[var(--border-subtle)]'
                        }`}>
                          {order.estado === 'CONFIRMADA' ? 'Confirmada' : order.estado === 'EN_ALQUILAR' ? 'En Velada' : order.estado}
                        </span>
                      </td>
                      <td className="p-3.5 text-center">
                        <div className="flex items-center justify-center gap-2">
                          <button
                            onClick={() => onDownloadPdf?.(order)}
                            className="p-1.5 border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] text-[var(--text-secondary)] hover:text-[var(--accent-gold)] transition-all"
                            title="Descargar Contrato Notarial & Recibo de Custodia"
                          >
                            <FileText className="w-3.5 h-3.5" />
                          </button>
                          <button
                            onClick={() => onSendWhatsApp?.(order)}
                            className="p-1.5 border border-[var(--border-subtle)] hover:border-[var(--accent-sage)] text-[var(--text-secondary)] hover:text-[var(--accent-sage)] transition-all"
                            title="Enviar Notificación de Concierge por WhatsApp"
                          >
                            <Send className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* New Order Modal with Gala Calendar and Bespoke Sastrería */}
      <AnimatePresence>
        {isOpenNewModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm">
            <motion.div
              initial={{ scale: 0.96, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.96, opacity: 0 }}
              transition={{ duration: 0.3, ease: [0.25, 1, 0.5, 1] }}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] w-full max-w-2xl p-6 relative max-h-[92vh] overflow-y-auto space-y-6 shadow-2xl"
            >
              {/* Modal Header */}
              <div className="flex items-center justify-between border-b border-[var(--border-subtle)] pb-4">
                <div>
                  <p className="text-[10px] uppercase tracking-[0.24em] font-mono text-[var(--accent-gold)]">
                    Atelier I.G.A.V. · Protocolo Notarial
                  </p>
                  <h3 className="font-serif-editorial text-2xl italic text-[var(--text-primary)]">
                    Formalizar Contrato de Alquiler de Gala
                  </h3>
                </div>
                <button
                  onClick={() => setIsOpenNewModal(false)}
                  className="p-1 text-[var(--text-secondary)] hover:text-[var(--text-primary)]"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <form onSubmit={handleCreateOrder} className="space-y-6 text-xs font-mono">
                {/* Cliente Selector */}
                <div>
                  <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                    Titular de la Custodia (Directorio Maestro) *
                  </label>
                  <select
                    value={selectedCustomerId}
                    onChange={(e) => handleCustomerChange(Number(e.target.value))}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-[var(--text-primary)] font-mono text-xs focus:outline-none focus:border-[var(--accent-gold)]"
                  >
                    {customers.map((c: any) => {
                      const docNumStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null
                        ? (c.documentoIdentidad.numero || '')
                        : (typeof c.numeroDocumento === 'string' ? c.numeroDocumento : '');
                      const nameStr = c.nombreCompleto || `${c.nombres || ''} ${c.apellidos || ''}`.trim() || 'Cliente';
                      return (
                        <option key={c.id} value={c.id}>
                          {nameStr} (Doc: {docNumStr})
                        </option>
                      );
                    })}
                  </select>
                </div>

                {/* Prenda Selector */}
                <div>
                  <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                    Pieza de Alta Costura Seleccionada *
                  </label>
                  <select
                    value={selectedGarmentId}
                    onChange={(e) => setSelectedGarmentId(Number(e.target.value))}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-[var(--text-primary)] font-mono text-xs focus:outline-none focus:border-[var(--accent-gold)]"
                  >
                    {garments.map((g) => (
                      <option key={g.id} value={g.id}>
                        {g.codigoUnico} · {g.nombre} (Talla: {g.talla}) · S/ {g.precioAlquiler}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Gala Calendar Section */}
                <div className="border border-[var(--border-subtle)] p-4 bg-[var(--surface-elevated)] space-y-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Calendar className="w-4 h-4 text-[var(--accent-gold)]" />
                      <span className="text-[10px] uppercase tracking-[0.2em] font-mono text-[var(--text-primary)] font-medium">
                        Agenda de Fechas Ceremoniales
                      </span>
                    </div>
                    <span className="text-[10px] font-mono text-[var(--accent-sage)]">
                      Regeneración: 48h post-evento
                    </span>
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-[10px] uppercase tracking-[0.14em] text-[var(--text-secondary)] mb-1">
                        Entrega para Prueba & Entalle *
                      </label>
                      <input
                        type="date"
                        required
                        value={fechaEntrega}
                        onChange={(e) => setFechaEntrega(e.target.value)}
                        className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] font-mono focus:outline-none focus:border-[var(--accent-gold)]"
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] uppercase tracking-[0.14em] text-[var(--text-secondary)] mb-1">
                        Devolución Tras la Velada *
                      </label>
                      <input
                        type="date"
                        required
                        value={fechaDevolucion}
                        onChange={(e) => setFechaDevolucion(e.target.value)}
                        className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] font-mono focus:outline-none focus:border-[var(--accent-gold)]"
                      />
                    </div>
                  </div>

                  {/* Collision Notice */}
                  {collisionResult.isCollision && (
                    <div className="p-3 border border-[var(--accent-burgundy)] bg-[var(--accent-burgundy-light)] text-[var(--text-primary)] flex items-start gap-2 text-[11px] leading-relaxed">
                      <AlertOctagon className="w-4 h-4 text-[var(--accent-burgundy)] shrink-0 mt-0.5" />
                      <span>{collisionResult.reason}</span>
                    </div>
                  )}
                </div>

                {/* Bespoke Fit / Sastrería Fina Section */}
                <div className="border border-[var(--border-subtle)] p-4 bg-[var(--surface-elevated)] space-y-3">
                  <div className="flex items-center gap-2">
                    <Scissors className="w-4 h-4 text-[var(--accent-gold)]" />
                    <span className="text-[10px] uppercase tracking-[0.2em] font-mono text-[var(--text-primary)] font-medium">
                      Ajustes de Taller · Medidas de Atelier (Bespoke)
                    </span>
                  </div>

                  <p className="text-[11px] text-[var(--text-secondary)] font-light leading-relaxed">
                    Nuestra maestra costurera aplicará un hilván temporal invisible en el forro para adaptar la caída a su fisonomía sin alterar el corte original.
                  </p>

                  <div className="grid grid-cols-4 gap-2 pt-1 text-[11px]">
                    <div>
                      <label className="block text-[9px] uppercase tracking-wider text-[var(--text-secondary)] mb-0.5">Busto (cm)</label>
                      <input
                        type="number"
                        value={bustoCm}
                        onChange={(e) => setBustoCm(Number(e.target.value))}
                        className="w-full px-2 py-1.5 bg-transparent border border-[var(--border-subtle)] text-center font-mono focus:border-[var(--accent-gold)]"
                      />
                    </div>
                    <div>
                      <label className="block text-[9px] uppercase tracking-wider text-[var(--text-secondary)] mb-0.5">Cintura (cm)</label>
                      <input
                        type="number"
                        value={cinturaCm}
                        onChange={(e) => setCinturaCm(Number(e.target.value))}
                        className="w-full px-2 py-1.5 bg-transparent border border-[var(--border-subtle)] text-center font-mono focus:border-[var(--accent-gold)]"
                      />
                    </div>
                    <div>
                      <label className="block text-[9px] uppercase tracking-wider text-[var(--text-secondary)] mb-0.5">Cadera (cm)</label>
                      <input
                        type="number"
                        value={caderaCm}
                        onChange={(e) => setCaderaCm(Number(e.target.value))}
                        className="w-full px-2 py-1.5 bg-transparent border border-[var(--border-subtle)] text-center font-mono focus:border-[var(--accent-gold)]"
                      />
                    </div>
                    <div>
                      <label className="block text-[9px] uppercase tracking-wider text-[var(--text-secondary)] mb-0.5">Alt.+Tacón (cm)</label>
                      <input
                        type="number"
                        value={alturaConTaconCm}
                        onChange={(e) => setAlturaConTaconCm(Number(e.target.value))}
                        className="w-full px-2 py-1.5 bg-transparent border border-[var(--border-subtle)] text-center font-mono focus:border-[var(--accent-gold)]"
                      />
                    </div>
                  </div>

                  <label className="flex items-center gap-2 pt-2 cursor-pointer text-[11px] text-[var(--text-secondary)] hover:text-[var(--text-primary)]">
                    <input
                      type="checkbox"
                      checked={solicitarConserjeSastre}
                      onChange={(e) => setSolicitarConserjeSastre(e.target.checked)}
                      className="accent-[#C4A47C]"
                    />
                    <span>Deseo asistencia privada de sastre en suite 24 horas antes para el vaporizado final.</span>
                  </label>
                </div>

                {/* Financial Breakdown */}
                <div className="border-t border-[var(--border-subtle)] pt-3 flex items-center justify-between text-xs font-mono">
                  <div>
                    <span className="text-[10px] uppercase text-[var(--text-secondary)] block">Tasa de Custodia:</span>
                    <span className="font-serif-editorial text-xl italic font-semibold text-[var(--accent-gold)]">
                      S/ {selectedGarment ? Number(selectedGarment.precioAlquiler).toFixed(2) : '280.00'}
                    </span>
                  </div>
                  <div className="text-right">
                    <span className="text-[10px] uppercase text-[var(--text-secondary)] block">Fianza Retornable:</span>
                    <span className="font-mono text-xs text-[var(--accent-sage)]">
                      S/ {selectedGarment ? Number(selectedGarment.depositoGarantia).toFixed(2) : '100.00'}
                    </span>
                  </div>
                </div>

                {/* Submit Actions */}
                <div className="flex justify-end gap-3 pt-2 border-t border-[var(--border-subtle)]">
                  <button
                    type="button"
                    onClick={() => setIsOpenNewModal(false)}
                    className="px-4 py-2 border border-[var(--border-subtle)] text-[var(--text-secondary)] hover:text-[var(--text-primary)] text-xs font-mono"
                  >
                    Cerrar
                  </button>
                  <button
                    type="submit"
                    disabled={collisionResult.isCollision}
                    className={`px-5 py-2 text-xs font-mono font-medium tracking-wider uppercase transition-all ${
                      collisionResult.isCollision
                        ? 'bg-[var(--surface-elevated)] text-[var(--text-tertiary)] border border-[var(--border-subtle)] cursor-not-allowed'
                        : 'bg-[var(--accent-gold)] text-[#151413] hover:bg-[#B39167]'
                    }`}
                  >
                    Confirmar Custodia en MySQL
                  </button>
                </div>
              </form>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
};