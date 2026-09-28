'use client';

import React, { useState } from 'react';
import { 
  RotateCcw, 
  X, 
  AlertTriangle, 
  Sparkles, 
  DollarSign, 
  Clock, 
  CheckCircle2, 
  Droplet,
  Scissors
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Order, ReturnOrderRequest } from '../lib/types';
import { processReturn } from '../lib/api';
import { toast } from 'sonner';

interface ReturnInspectionModalProps {
  isOpen: boolean;
  onClose: () => void;
  orders: Order[];
  selectedOrderId?: number;
  onReturnProcessed: (updatedOrderId: number, deductedDeposit: number, horasBloqueo: number) => void;
}

export const ReturnInspectionModal: React.FC<ReturnInspectionModalProps> = ({
  isOpen,
  onClose,
  orders,
  selectedOrderId,
  onReturnProcessed
}) => {
  const activeOrders = orders.filter(o => o.estado === 'EN_ALQUILAR' || o.estado === 'BORRADOR');
  
  const [orderId, setOrderId] = useState<number>(
    selectedOrderId || activeOrders[0]?.id || orders[0]?.id || 101
  );

  // Incidents state
  const [hasStain, setHasStain] = useState(false);
  const [stainDeduction, setStainDeduction] = useState(30);
  
  const [hasDamage, setHasDamage] = useState(false);
  const [damageDeduction, setDamageDeduction] = useState(50);

  const [hasDelay, setHasDelay] = useState(false);
  const [delayPenalty, setDelayPenalty] = useState(40);

  const [notes, setNotes] = useState('');

  const currentOrder = orders.find(o => o.id === Number(orderId)) || orders[0];
  const initialDeposit = currentOrder?.montoGarantiaTotal || 150;

  // Calculate net deposit refund
  const totalDeductions = (hasStain ? stainDeduction : 0) + 
                         (hasDamage ? damageDeduction : 0) + 
                         (hasDelay ? delayPenalty : 0);

  const netRefund = Math.max(0, initialDeposit - totalDeductions);

  const handleSubmitReturn = async (e: React.FormEvent) => {
    e.preventDefault();

    const incidencias = [];
    if (hasStain) {
      incidencias.push({
        garmentId: currentOrder?.items?.[0]?.garmentId || 1,
        tipoIncidencia: 'MANCHA' as const,
        montoDescuentoGarantia: stainDeduction,
        descripcion: 'Mancha detectada en inspección de recepción.'
      });
    }
    if (hasDamage) {
      incidencias.push({
        garmentId: currentOrder?.items?.[0]?.garmentId || 1,
        tipoIncidencia: 'ROTURA_DANIO' as const,
        montoDescuentoGarantia: damageDeduction,
        descripcion: 'Daño o desperfecto en tela/costura.'
      });
    }

    const payload: ReturnOrderRequest = {
      orderId: currentOrder.id,
      montoPenalizacionMora: hasDelay ? delayPenalty : 0,
      incidencias,
      updatedBy: 'Recepción Sede Central'
    };

    const res = await processReturn(payload);
    
    // Auto-laundry block 24h-48h
    const laundryHours = (hasStain || hasDamage) ? 48 : 24;

    onReturnProcessed(currentOrder.id, totalDeductions, laundryHours);

    toast.success(`Devolución de ${currentOrder.codigoContrato} procesada. Garantía devuelta: S/ ${netRefund.toFixed(2)}. Prenda enviada a Tintorería (${laundryHours}h).`);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
      <motion.div
        initial={{ scale: 0.9, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0.9, opacity: 0 }}
        className="glass-panel border border-[var(--border-color)] rounded-2xl w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-5"
      >
        <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
          <div className="flex items-center gap-2">
            <RotateCcw className="w-5 h-5 text-purple-700 dark:text-purple-400" />
            <h3 className="text-lg font-bold text-[var(--text-primary)]">Recepción & Inspección de Devolución</h3>
          </div>
          <motion.button whileTap={{ scale: 0.96 }} onClick={onClose}
            className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
          >
            <X className="w-5 h-5" />
          </motion.button>
        </div>

        <form onSubmit={handleSubmitReturn} className="space-y-4 text-xs">
          <div>
            <label className="block font-semibold text-[var(--text-secondary)] mb-1">Seleccionar Contrato en Alquiler</label>
            <select
              value={orderId}
              onChange={(e) => setOrderId(Number(e.target.value))}
              className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] font-medium"
            >
              {orders.map((o) => (
                <option key={o.id} value={o.id}>
                  {o.codigoContrato} - {o.clienteNombreCompleto} (Garantía: S/ {o.montoGarantiaTotal})
                </option>
              ))}
            </select>
          </div>

          {currentOrder && (
            <div className="p-3 rounded-xl bg-[var(--glass-bg)] border border-[var(--glass-border)] flex justify-between items-center text-xs">
              <div>
                <span className="text-[var(--text-secondary)] block">Prenda Retornada:</span>
                <span className="font-bold text-amber-800 dark:text-amber-300">{currentOrder.items?.[0]?.garmentName || 'Prenda Gala'}</span>
              </div>
              <div className="text-right">
                <span className="text-[var(--text-secondary)] block">Depósito Inicial:</span>
                <span className="font-bold text-emerald-700 dark:text-emerald-400 text-sm">S/ {initialDeposit.toFixed(2)}</span>
              </div>
            </div>
          )}

          {/* Incidents checklist */}
          <div className="space-y-3 pt-2">
            <p className="font-semibold text-[var(--text-secondary)] uppercase text-[11px] tracking-wider">
              Evaluación de Estado e Incidencias (RF-08)
            </p>

            {/* Stain Incident */}
            <div className={`p-3 rounded-xl border transition-all ${
              hasStain ? 'bg-amber-500/10 border-amber-500/30' : 'bg-[var(--glass-bg)] border-[var(--border-color)]'
            }`}>
              <div className="flex items-center justify-between">
                <label className="flex items-center gap-2 font-semibold text-[var(--text-primary)] cursor-pointer">
                  <input
                    type="checkbox"
                    checked={hasStain}
                    onChange={(e) => setHasStain(e.target.checked)}
                    className="rounded text-amber-500 focus:ring-0 bg-[var(--card-bg)] border-[var(--border-color)]"
                  />
                  <Droplet className="w-4 h-4 text-amber-700 dark:text-amber-400" />
                  <span>Manchas o Suciedad Profunda (Requiere lavado especial)</span>
                </label>
                {hasStain && (
                  <div className="flex items-center gap-1 text-amber-800 dark:text-amber-300 font-bold">
                    <span>- S/</span>
                    <input
                      type="number"
                      value={stainDeduction}
                      onChange={(e) => setStainDeduction(Number(e.target.value))}
                      className="w-16 px-2 py-0.5 rounded bg-[var(--card-bg)] border border-[var(--border-color)] text-right text-xs"
                    />
                  </div>
                )}
              </div>
            </div>

            {/* Damage Incident */}
            <div className={`p-3 rounded-xl border transition-all ${
              hasDamage ? 'bg-rose-500/10 border-rose-500/30' : 'bg-[var(--glass-bg)] border-[var(--border-color)]'
            }`}>
              <div className="flex items-center justify-between">
                <label className="flex items-center gap-2 font-semibold text-[var(--text-primary)] cursor-pointer">
                  <input
                    type="checkbox"
                    checked={hasDamage}
                    onChange={(e) => setHasDamage(e.target.checked)}
                    className="rounded text-rose-500 focus:ring-0 bg-[var(--card-bg)] border-[var(--border-color)]"
                  />
                  <Scissors className="w-4 h-4 text-rose-700 dark:text-rose-400" />
                  <span>Desgarro, Rotura o Daño en Costura</span>
                </label>
                {hasDamage && (
                  <div className="flex items-center gap-1 text-rose-800 dark:text-rose-300 font-bold">
                    <span>- S/</span>
                    <input
                      type="number"
                      value={damageDeduction}
                      onChange={(e) => setDamageDeduction(Number(e.target.value))}
                      className="w-16 px-2 py-0.5 rounded bg-[var(--card-bg)] border border-[var(--border-color)] text-right text-xs"
                    />
                  </div>
                )}
              </div>
            </div>

            {/* Delay Penalty */}
            <div className={`p-3 rounded-xl border transition-all ${
              hasDelay ? 'bg-purple-500/10 border-purple-500/30' : 'bg-[var(--glass-bg)] border-[var(--border-color)]'
            }`}>
              <div className="flex items-center justify-between">
                <label className="flex items-center gap-2 font-semibold text-[var(--text-primary)] cursor-pointer">
                  <input
                    type="checkbox"
                    checked={hasDelay}
                    onChange={(e) => setHasDelay(e.target.checked)}
                    className="rounded text-purple-500 focus:ring-0 bg-[var(--card-bg)] border-[var(--border-color)]"
                  />
                  <Clock className="w-4 h-4 text-purple-700 dark:text-purple-400" />
                  <span>Retraso en Fecha de Devolución (Penalización Mora)</span>
                </label>
                {hasDelay && (
                  <div className="flex items-center gap-1 text-purple-800 dark:text-purple-300 font-bold">
                    <span>- S/</span>
                    <input
                      type="number"
                      value={delayPenalty}
                      onChange={(e) => setDelayPenalty(Number(e.target.value))}
                      className="w-16 px-2 py-0.5 rounded bg-[var(--card-bg)] border border-[var(--border-color)] text-right text-xs"
                    />
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Refund Calculation Summary */}
          <div className="p-4 rounded-xl glass-card border border-white/15 space-y-2">
            <div className="flex justify-between text-[var(--text-secondary)]">
              <span>Garantía en Custodia:</span>
              <span>S/ {initialDeposit.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-rose-700 dark:text-rose-400">
              <span>Total Retenciones / Incidencias:</span>
              <span>- S/ {totalDeductions.toFixed(2)}</span>
            </div>
            <div className="flex justify-between pt-2 border-t border-[var(--glass-border)] text-base font-extrabold text-emerald-700 dark:text-emerald-400">
              <span>Garantía Neta a Reembolsar al Cliente:</span>
              <span>S/ {netRefund.toFixed(2)}</span>
            </div>
          </div>

          {/* Auto Laundry Trigger Info (RF-05) */}
          <div className="p-3 rounded-xl bg-purple-500/10 border border-purple-500/20 text-[11px] text-purple-800 dark:text-purple-300 flex items-center gap-2">
            <Clock className="w-4 h-4 text-purple-700 dark:text-purple-400 shrink-0" />
            <span>
              Al procesar la devolución, la prenda pasará automáticamente a estado <strong>EN_TINTORERIA</strong> por <strong>{hasStain || hasDamage ? '48 horas' : '24 horas'}</strong>.
            </span>
          </div>

          <div className="pt-3 flex justify-end gap-3 border-t border-[var(--glass-border)]">
            <motion.button whileTap={{ scale: 0.96 }} type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-lg text-[var(--text-secondary)] hover:bg-[var(--glass-bg)]"
            >
              Cancelar
            </motion.button>
            <motion.button whileTap={{ scale: 0.96 }} type="submit"
              className="px-5 py-2 rounded-lg bg-purple-600 hover:bg-purple-500 text-[var(--text-primary)] font-bold shadow-lg shadow-purple-500/20"
            >
              Confirmar Devolución & Retención
            </motion.button>
          </div>
        </form>
      </motion.div>
    </div>
  );
};
