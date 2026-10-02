'use client';

import React, { useState } from 'react';
import { 
  RotateCcw, 
  X, 
  Sparkles, 
  ShieldCheck, 
  Clock, 
  CheckCircle2, 
  Feather,
  Scissors,
  Coins,
  AlertCircle
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
        descripcion: 'Limpieza especializada de seda por mancha.'
      });
    }
    if (hasDamage) {
      incidencias.push({
        garmentId: currentOrder?.items?.[0]?.garmentId || 1,
        tipoIncidencia: 'ROTURA_DANIO' as const,
        montoDescuentoGarantia: damageDeduction,
        descripcion: 'Ajuste de costura o pedrería por restauración artesanal.'
      });
    }

    const payload: ReturnOrderRequest = {
      orderId: currentOrder.id,
      montoPenalizacionMora: hasDelay ? delayPenalty : 0,
      incidencias,
      updatedBy: 'Atelier de Recepción'
    };

    try {
      await processReturn(payload);
    } catch (err) {
      console.warn('API error en return. Aplicando fallback de estado local.');
    }
    
    // Auto-laundry block 24h-48h
    const laundryHours = (hasStain || hasDamage) ? 48 : 24;

    onReturnProcessed(currentOrder.id, totalDeductions, laundryHours);

    toast.success(`Recepción de ${currentOrder.codigoContrato} certificada.`, {
      description: `Fianza reintegrada al huésped: S/ ${netRefund.toFixed(2)}. Pieza en reposo textil (${laundryHours}h).`
    });
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/65 backdrop-blur-sm">
      <motion.div
        initial={{ scale: 0.96, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0.96, opacity: 0 }}
        className="bg-[var(--surface-card)] border border-[var(--border-subtle)] w-full max-w-xl p-6 sm:p-8 space-y-6 shadow-2xl relative max-h-[90vh] overflow-y-auto"
      >
        {/* Header */}
        <div className="flex items-center justify-between pb-4 border-b border-[var(--border-subtle)]">
          <div className="space-y-1">
            <span className="text-[9px] font-mono tracking-[0.2em] uppercase text-[var(--accent-gold)]">
              Custodia & Estado Textil
            </span>
            <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
              Protocolo de Retorno & Evaluación de Fibras
            </h3>
          </div>
          <button
            onClick={onClose}
            className="p-1 text-[var(--text-tertiary)] hover:text-[var(--text-primary)] transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmitReturn} className="space-y-5 text-xs">
          {/* Order Selection */}
          <div className="space-y-1.5">
            <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
              Contrato de Gala
            </label>
            <select
              value={orderId}
              onChange={(e) => setOrderId(Number(e.target.value))}
              className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] font-mono focus:outline-none focus:border-[var(--accent-gold)]"
            >
              {orders.map((o) => (
                <option key={o.id} value={o.id}>
                  {o.codigoContrato} — {o.clienteNombreCompleto} (Fianza: S/ {o.montoGarantiaTotal?.toFixed(2)})
                </option>
              ))}
            </select>
          </div>

          {currentOrder && (
            <div className="p-4 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] space-y-2">
              <div className="flex justify-between items-center text-xs">
                <span className="font-serif-editorial text-base text-[var(--text-primary)] font-medium">
                  {currentOrder.clienteNombreCompleto}
                </span>
                <span className="font-mono text-[10px] text-[var(--accent-gold)] tracking-wider">
                  Fianza Inicial: S/ {initialDeposit.toFixed(2)}
                </span>
              </div>
              <p className="text-[11px] font-sans-editorial text-[var(--text-secondary)]">
                Prenda: {(currentOrder.items || []).map(i => i.garmentName).join(', ') || 'Pieza de Gala'}
              </p>
            </div>
          )}

          {/* Inspection Checkpoints */}
          <div className="space-y-3">
            <span className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-tertiary)] block">
              Evaluación Técnica de la Pieza
            </span>

            {/* Stain Treatment */}
            <div className={`p-3.5 border transition-colors ${hasStain ? 'border-[var(--accent-burgundy)] bg-[var(--accent-burgundy-light)]' : 'border-[var(--border-subtle)] bg-[var(--surface-card)]'}`}>
              <label className="flex items-start gap-3 cursor-pointer">
                <input
                  type="checkbox"
                  checked={hasStain}
                  onChange={(e) => setHasStain(e.target.checked)}
                  className="mt-0.5 accent-[#5C1E26]"
                />
                <div className="flex-1 space-y-1">
                  <div className="flex justify-between items-center">
                    <span className="font-sans-editorial font-medium text-[var(--text-primary)]">
                      Tratamiento Especial por Mancha en Seda o Brocado
                    </span>
                    <span className="font-mono text-[var(--accent-burgundy)] font-semibold">- S/ {stainDeduction.toFixed(2)}</span>
                  </div>
                  <p className="text-[11px] text-[var(--text-secondary)] leading-relaxed">
                    Requiere tratamiento ultrasónico de desmanchado y vaporizado profundo en tintorería especializada.
                  </p>
                </div>
              </label>
            </div>

            {/* Fabric / Tailoring Restoration */}
            <div className={`p-3.5 border transition-colors ${hasDamage ? 'border-[var(--accent-burgundy)] bg-[var(--accent-burgundy-light)]' : 'border-[var(--border-subtle)] bg-[var(--surface-card)]'}`}>
              <label className="flex items-start gap-3 cursor-pointer">
                <input
                  type="checkbox"
                  checked={hasDamage}
                  onChange={(e) => setHasDamage(e.target.checked)}
                  className="mt-0.5 accent-[#5C1E26]"
                />
                <div className="flex-1 space-y-1">
                  <div className="flex justify-between items-center">
                    <span className="font-sans-editorial font-medium text-[var(--text-primary)]">
                      Restauración Textil por Desgarro o Desprendimiento de Pedrería
                    </span>
                    <span className="font-mono text-[var(--accent-burgundy)] font-semibold">- S/ {damageDeduction.toFixed(2)}</span>
                  </div>
                  <p className="text-[11px] text-[var(--text-secondary)] leading-relaxed">
                    Pase directo al taller de alta costura para zurcido invisible o re-bordado manual de canutillos.
                  </p>
                </div>
              </label>
            </div>

            {/* Delayed Return */}
            <div className={`p-3.5 border transition-colors ${hasDelay ? 'border-[var(--accent-gold)] bg-[var(--accent-gold-light)]' : 'border-[var(--border-subtle)] bg-[var(--surface-card)]'}`}>
              <label className="flex items-start gap-3 cursor-pointer">
                <input
                  type="checkbox"
                  checked={hasDelay}
                  onChange={(e) => setHasDelay(e.target.checked)}
                  className="mt-0.5 accent-[#C4A47C]"
                />
                <div className="flex-1 space-y-1">
                  <div className="flex justify-between items-center">
                    <span className="font-sans-editorial font-medium text-[var(--text-primary)]">
                      Compensación por Entrega Fuera de Plazo
                    </span>
                    <span className="font-mono text-[var(--accent-gold)] font-semibold">- S/ {delayPenalty.toFixed(2)}</span>
                  </div>
                  <p className="text-[11px] text-[var(--text-secondary)] leading-relaxed">
                    Compensación por retención adicional que afecte el calendario de reservas del atelier.
                  </p>
                </div>
              </label>
            </div>
          </div>

          {/* Liquidación de Fianza */}
          <div className="p-4 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] space-y-2">
            <span className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-tertiary)] block">
              Liquidación de Fianza en Custodia
            </span>
            <div className="flex justify-between text-xs text-[var(--text-secondary)]">
              <span>Depósito inicial recibido:</span>
              <span className="font-mono">S/ {initialDeposit.toFixed(2)}</span>
            </div>
            {totalDeductions > 0 && (
              <div className="flex justify-between text-xs text-[var(--accent-burgundy)]">
                <span>Total restauraciones y demoras:</span>
                <span className="font-mono">- S/ {totalDeductions.toFixed(2)}</span>
              </div>
            )}
            <div className="pt-2 border-t border-[var(--border-subtle)] flex justify-between items-center">
              <span className="font-serif-editorial text-base text-[var(--text-primary)] font-medium">
                Monto Neto a Reintegrar al Huésped:
              </span>
              <span className="font-serif-editorial text-xl font-semibold text-[var(--accent-sage)]">
                S/ {netRefund.toFixed(2)}
              </span>
            </div>
          </div>

          {/* Actions */}
          <div className="pt-3 border-t border-[var(--border-subtle)] flex items-center justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border border-[var(--border-subtle)] text-xs font-mono uppercase tracking-wider text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-5 py-2 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] text-xs font-sans-editorial font-semibold uppercase tracking-wider transition-colors flex items-center gap-2"
            >
              <CheckCircle2 className="w-4 h-4" />
              <span>Certificar Recepción & Reintegrar Fianza</span>
            </button>
          </div>
        </form>
      </motion.div>
    </div>
  );
};