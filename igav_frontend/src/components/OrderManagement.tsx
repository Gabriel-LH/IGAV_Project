'use client';

import React, { useState } from 'react';
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
  User
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
  const [fechaEntrega, setFechaEntrega] = useState('2026-10-05');
  const [fechaDevolucion, setFechaDevolucion] = useState('2026-10-08');

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
            reason: `Conflicto de Fechas (RF-04 / RF-05): La prenda ya posee una reserva asignada (${order.codigoContrato}) o está bloqueada por tintorería hasta el ${new Date(orderEndWithLaundry).toLocaleDateString('es-ES')}.`
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
    if (!clienteNombre) {
      toast.error('Ingrese el nombre del cliente');
      return;
    }

    const newOrder: Order = {
      id: Date.now(),
      codigoContrato: `ORD-2026-${Math.floor(1000 + Math.random() * 9000)}`,
      customerId: selectedCustomerId,
      clienteDocumento: clienteDocumento,
      clienteNombreCompleto: clienteNombre,
      clienteTelefono: clienteTelefono,
      storeId: 1,
      tipo: 'ALQUILAR',
      fechaEntregaAcordada: `${fechaEntrega}T10:00:00`,
      fechaDevolucionAcordada: `${fechaDevolucion}T18:00:00`,
      subtotal: selectedGarment ? selectedGarment.precioAlquiler : 250,
      montoGarantiaTotal: selectedGarment ? selectedGarment.depositoGarantia : 100,
      descuentoGarantia: 0,
      montoPenalizacion: 0,
      montoTotal: selectedGarment ? selectedGarment.precioAlquiler : 250,
      garantiaDevueltaNeta: selectedGarment ? selectedGarment.depositoGarantia : 100,
      estado: 'EN_ALQUILAR',
      items: selectedGarment ? [
        {
          garmentId: selectedGarment.id,
          garmentName: selectedGarment.nombre,
          garmentSku: selectedGarment.codigoUnico,
          tipoItem: 'ALQUILAR',
          precioAplicado: selectedGarment.precioAlquiler,
          garantiaAplicada: selectedGarment.depositoGarantia
        }
      ] : []
    };

    onAddOrder(newOrder);
    setIsOpenNewModal(false);
    toast.success(`Reserva ${newOrder.codigoContrato} creada exitosamente. Fechas validadas sin solapamiento.`);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <CalendarCheck className="w-5 h-5 text-amber-700 dark:text-amber-400" />
            Contratos de Alquiler y Reservas de Gala
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Validación de traslape de fechas en tiempo real (RF-04) y depósito en garantía (RF-06).
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsOpenNewModal(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Crear Nuevo Contrato / Reserva
        </motion.button>
      </div>

      {/* Orders List Table */}
      <div className="glass-panel rounded-2xl border border-[var(--glass-border)] overflow-hidden">
        <div className="p-4 border-b border-[var(--glass-border)] flex items-center justify-between">
          <h3 className="font-bold text-[var(--text-primary)] text-sm">Registros de Alquiler Activos y Pasados</h3>
          <span className="text-xs text-amber-700 dark:text-amber-400 font-mono font-bold">{orders.length} Contratos Registrados</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-[var(--glass-bg)] border-b border-[var(--glass-border)] text-[var(--text-secondary)] uppercase font-semibold text-[10px] tracking-wider">
                <th className="p-3.5">Código Contrato</th>
                <th className="p-3.5">Cliente & DNI</th>
                <th className="p-3.5">Prenda Alquilada</th>
                <th className="p-3.5">Fecha Entrega</th>
                <th className="p-3.5">Fecha Devolución</th>
                <th className="p-3.5 text-right">Alquiler (S/)</th>
                <th className="p-3.5 text-right">Garantía (S/)</th>
                <th className="p-3.5 text-center">Estado</th>
                <th className="p-3.5 text-center">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {orders.map((order) => {
                const item = order.items?.[0];
                return (
                  <tr key={order.id} className="hover:bg-[var(--glass-bg)] transition-colors">
                    <td className="p-3.5 font-mono font-bold text-amber-800 dark:text-amber-300">{order.codigoContrato}</td>
                    <td className="p-3.5 font-medium text-[var(--text-primary)]">
                      <div>{order.clienteNombreCompleto}</div>
                      {order.clienteDocumento && (
                        <div className="text-[10px] text-[var(--text-secondary)] font-mono">Doc: {order.clienteDocumento}</div>
                      )}
                    </td>
                    <td className="p-3.5 text-[var(--text-secondary)] max-w-xs truncate">{item?.garmentName || 'Prenda de Gala'}</td>
                    <td className="p-3.5 text-[var(--text-secondary)]">
                      {new Date(order.fechaEntregaAcordada).toLocaleDateString('es-ES')}
                    </td>
                    <td className="p-3.5 text-[var(--text-secondary)]">
                      {new Date(order.fechaDevolucionAcordada).toLocaleDateString('es-ES')}
                    </td>
                    <td className="p-3.5 text-right font-bold text-amber-700 dark:text-amber-400">S/ {order.subtotal.toFixed(2)}</td>
                    <td className="p-3.5 text-right font-bold text-emerald-700 dark:text-emerald-400">S/ {order.montoGarantiaTotal.toFixed(2)}</td>
                    <td className="p-3.5 text-center">
                      <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold border ${
                        order.estado === 'EN_ALQUILAR' ? 'bg-amber-500/10 text-amber-800 dark:text-amber-300 border-amber-500/30' :
                        order.estado === 'DEVUELTO_PENDIENTE_TINTORERIA' ? 'bg-purple-500/10 text-purple-800 dark:text-purple-300 border-purple-500/30' :
                        'bg-emerald-500/10 text-emerald-800 dark:text-emerald-300 border-emerald-500/30'
                      }`}>
                        {order.estado === 'EN_ALQUILAR' ? 'EN USO' : order.estado}
                      </span>
                    </td>
                    <td className="p-3.5 text-center">
                      <div className="flex items-center justify-center gap-1.5">
                        <button
                          onClick={() => onDownloadPdf?.(order)}
                          className="p-1.5 bg-stone-800/60 hover:bg-amber-500/20 text-stone-300 hover:text-amber-400 border border-stone-700/50 rounded-lg transition-all"
                          title="Descargar Contrato PDF & Recibo Garantía"
                        >
                          <FileText className="w-3.5 h-3.5" />
                        </button>
                        <button
                          onClick={() => onSendWhatsApp?.(order)}
                          className="p-1.5 bg-emerald-600/20 hover:bg-emerald-600/30 text-emerald-400 border border-emerald-500/30 rounded-lg transition-all"
                          title="Enviar Alerta WhatsApp (wsp-js)"
                        >
                          <MessageSquare className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>

      {/* New Order / Collision Modal */}
      <AnimatePresence>
        {isOpenNewModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="glass-panel border border-[var(--border-color)] rounded-2xl w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-5"
            >
              <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
                <div className="flex items-center gap-2">
                  <CalendarCheck className="w-5 h-5 text-amber-700 dark:text-amber-400" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Nuevo Contrato de Alquiler</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsOpenNewModal(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleCreateOrder} className="space-y-4 text-xs">
                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Seleccionar Cliente Registrado (Maestro) *</label>
                  <select
                    value={selectedCustomerId}
                    onChange={(e) => handleCustomerChange(Number(e.target.value))}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] font-medium"
                  >
                    {customers.map((c: any) => {
                      const docTipoStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null
                        ? (c.documentoIdentidad.tipo || 'DNI')
                        : (typeof c.tipoDocumento === 'string' ? c.tipoDocumento : 'DNI');
                      const docNumStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null
                        ? (c.documentoIdentidad.numero || '')
                        : (typeof c.numeroDocumento === 'string' ? c.numeroDocumento : '');
                      const nameStr = c.nombreCompleto || `${c.nombres || ''} ${c.apellidos || ''}`.trim() || 'Cliente';
                      const catStr = typeof c.categoriaCliente === 'string' ? c.categoriaCliente : 'REGULAR';

                      return (
                        <option key={c.id} value={c.id}>
                          {nameStr} ({docTipoStr}: {docNumStr}) - [{catStr}]
                        </option>
                      );
                    })}
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Documento Identidad</label>
                    <input
                      type="text"
                      readOnly
                      value={clienteDocumento}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-secondary)] font-mono"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Teléfono Cliente</label>
                    <input
                      type="text"
                      readOnly
                      value={clienteTelefono}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-secondary)]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Seleccionar Prenda de Gala *</label>
                  <select
                    value={selectedGarmentId}
                    onChange={(e) => setSelectedGarmentId(Number(e.target.value))}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  >
                    {garments.map((g) => (
                      <option key={g.id} value={g.id}>
                        {g.codigoUnico} - {g.nombre} (Talla {g.talla}) - S/ {g.precioAlquiler}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Fecha de Entrega (Evento) *</label>
                    <input
                      type="date"
                      required
                      value={fechaEntrega}
                      onChange={(e) => setFechaEntrega(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Fecha de Devolución *</label>
                    <input
                      type="date"
                      required
                      value={fechaDevolucion}
                      onChange={(e) => setFechaDevolucion(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                </div>

                {/* Real-time Collision Check Indicator (RF-04 / RF-05) */}
                <div className={`p-3.5 rounded-xl border flex items-start gap-3 ${
                  collisionResult.isCollision 
                    ? 'bg-rose-500/10 border-rose-500/30 text-rose-800 dark:text-rose-300' 
                    : 'bg-emerald-500/10 border-emerald-500/30 text-emerald-800 dark:text-emerald-300'
                }`}>
                  {collisionResult.isCollision ? (
                    <AlertOctagon className="w-5 h-5 text-rose-700 dark:text-rose-400 shrink-0 mt-0.5" />
                  ) : (
                    <ShieldCheck className="w-5 h-5 text-emerald-700 dark:text-emerald-400 shrink-0 mt-0.5" />
                  )}
                  <div>
                    <h4 className="font-bold text-xs text-[var(--text-primary)]">
                      {collisionResult.isCollision ? 'Conflicto de Disponibilidad (RF-04)' : 'Fechas Validadas Exitosamente'}
                    </h4>
                    <p className="text-[11px] text-[var(--text-secondary)] mt-0.5">
                      {collisionResult.isCollision 
                        ? collisionResult.reason 
                        : `Prenda libre de reservas y con margen automático de tintorería (${selectedGarment?.horasTintoreriaBloqueo || 24}h post-evento).`}
                    </p>
                  </div>
                </div>

                {/* Price Breakdown */}
                {selectedGarment && (
                  <div className="p-3.5 rounded-xl bg-[var(--glass-bg)] border border-[var(--glass-border)] space-y-2">
                    <div className="flex justify-between">
                      <span className="text-[var(--text-secondary)]">Precio Alquiler:</span>
                      <span className="font-bold text-amber-700 dark:text-amber-400">S/ {selectedGarment.precioAlquiler.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-[var(--text-secondary)]">Depósito Garantía (Reembolsable):</span>
                      <span className="font-bold text-emerald-700 dark:text-emerald-400">S/ {selectedGarment.depositoGarantia.toFixed(2)}</span>
                    </div>
                    <div className="flex justify-between pt-2 border-t border-[var(--glass-border)] text-sm font-extrabold">
                      <span className="text-[var(--text-primary)]">Total a Cobrar Inicial:</span>
                      <span className="text-[var(--text-primary)]">S/ {(selectedGarment.precioAlquiler + selectedGarment.depositoGarantia).toFixed(2)}</span>
                    </div>
                  </div>
                )}

                <div className="pt-3 flex justify-end gap-3 border-t border-[var(--glass-border)]">
                  <motion.button whileTap={{ scale: 0.96 }} type="button"
                    onClick={() => setIsOpenNewModal(false)}
                    className="px-4 py-2 rounded-lg text-[var(--text-secondary)] hover:bg-[var(--glass-bg)]"
                  >
                    Cancelar
                  </motion.button>
                  <motion.button whileTap={{ scale: 0.96 }} type="submit"
                    disabled={collisionResult.isCollision}
                    className={`px-5 py-2 rounded-lg font-bold transition-all ${
                      collisionResult.isCollision 
                        ? 'bg-gray-700 text-[var(--text-secondary)] cursor-not-allowed'
                        : 'bg-amber-500 hover:bg-amber-400 text-black shadow-lg shadow-amber-500/20'
                    }`}
                  >
                    Confirmar Contrato
                  </motion.button>
                </div>
              </form>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
};
