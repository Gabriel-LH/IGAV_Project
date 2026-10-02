'use client';

import React, { useState } from 'react';
import { Scissors, Ruler, CheckCircle2, Clock, Plus, User, FileText, X, Sparkles, ArrowRight } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { TailoringRecord, TailoringStatus, Order } from '../lib/types';
import { toast } from 'sonner';

interface TailoringManagementProps {
  records: TailoringRecord[];
  orders: Order[];
  onAddRecord: (record: Omit<TailoringRecord, 'id'>) => void;
  onUpdateStatus: (recordId: number, status: TailoringStatus) => void;
}

export const TailoringManagement: React.FC<TailoringManagementProps> = ({
  records,
  orders,
  onAddRecord,
  onUpdateStatus
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedOrderCodigo, setSelectedOrderCodigo] = useState(orders[0]?.codigoContrato || 'ORD-2026-9812');
  const [sastreAsignado, setSastreAsignado] = useState('Maestro Sastre Don Carlos');
  const [bastaPantalonCm, setBastaPantalonCm] = useState(76);
  const [cinturaCm, setCinturaCm] = useState(84);
  const [talleSacoCm, setTalleSacoCm] = useState(72);
  const [largoMangaCm, setLargoMangaCm] = useState(64);
  const [hombroCm, setHombroCm] = useState(44);
  const [instruccionesSastre, setInstruccionesSastre] = useState('Ajuste de basta invisible y entalle sutil en pinzas de cintura.');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const order = orders.find(o => o.codigoContrato === selectedOrderCodigo) || orders[0];

    onAddRecord({
      orderCodigo: selectedOrderCodigo,
      clienteNombre: order?.clienteNombreCompleto || 'Huésped Distinguido',
      prendaNombre: order?.items?.[0]?.garmentName || 'Prenda de Gala',
      prendaSku: order?.items?.[0]?.garmentSku || 'SKU-GAL-001',
      sastreAsignado,
      bastaPantalonCm: Number(bastaPantalonCm),
      cinturaCm: Number(cinturaCm),
      talleSacoCm: Number(talleSacoCm),
      largoMangaCm: Number(largoMangaCm),
      hombroCm: Number(hombroCm),
      instruccionesSastre,
      fechaPrueba: new Date().toISOString(),
      estado: 'EN_TALLER_COSTURA'
    });

    setIsModalOpen(false);
    toast.success(`Ficha de Sastrería registrada para el contrato ${selectedOrderCodigo}.`);
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-gold)] font-medium">
              Atelier de Couture & Bespoke
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Taller & Entalle Fino
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Ajustes milimétricos de bastas, silueta, talle y mangas bajo la supervisión de los maestros sastres del atelier.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsModalOpen(true)}
          className="px-5 py-2.5 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-all shadow-sm flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Nueva Ficha de Entallado</span>
        </motion.button>
      </div>

      {/* Tailoring Fiche Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {records.map((rec) => (
          <div
            key={rec.id}
            className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-6 hover:border-[var(--accent-gold)] transition-all duration-300 space-y-5"
          >
            <div className="flex items-start justify-between">
              <div>
                <span className="text-[10px] font-mono tracking-wider uppercase text-[var(--accent-gold)] px-2 py-0.5 border border-[var(--accent-gold)]/40 bg-[var(--accent-gold-light)] inline-block">
                  {rec.orderCodigo}
                </span>
                <h3 className="font-serif-editorial text-xl text-[var(--text-primary)] font-medium mt-2">
                  {rec.clienteNombre}
                </h3>
                <p className="text-xs font-sans-editorial text-[var(--text-secondary)] mt-0.5">
                  {rec.prendaNombre} <span className="font-mono text-[10px] text-[var(--text-tertiary)]">({rec.prendaSku})</span>
                </p>
              </div>

              <span className={`text-[9px] font-mono tracking-widest uppercase px-2.5 py-1 border ${
                rec.estado === 'PENDIENTE_MEDICION'
                  ? 'bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border-[var(--accent-gold)]/40'
                  : rec.estado === 'EN_TALLER_COSTURA'
                  ? 'bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/40'
                  : 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/40'
              }`}>
                {rec.estado === 'PENDIENTE_MEDICION' ? 'Por Medir' : rec.estado === 'EN_TALLER_COSTURA' ? 'En Costura' : 'Entalle Listo'}
              </span>
            </div>

            {/* Measurement Grid */}
            <div className="p-4 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] space-y-3">
              <div className="flex items-center gap-2 text-xs font-mono tracking-wider uppercase text-[var(--text-tertiary)] border-b border-[var(--border-subtle)] pb-2">
                <Ruler className="w-3.5 h-3.5 text-[var(--accent-gold)]" />
                <span>Especificaciones de Medida (cm)</span>
              </div>

              <div className="grid grid-cols-5 gap-2 text-center">
                <div className="p-2 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                  <span className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block">Basta</span>
                  <span className="font-mono text-xs font-semibold text-[var(--text-primary)]">{rec.bastaPantalonCm || '—'}</span>
                </div>
                <div className="p-2 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                  <span className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block">Cintura</span>
                  <span className="font-mono text-xs font-semibold text-[var(--text-primary)]">{rec.cinturaCm || '—'}</span>
                </div>
                <div className="p-2 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                  <span className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block">Talle</span>
                  <span className="font-mono text-xs font-semibold text-[var(--text-primary)]">{rec.talleSacoCm || '—'}</span>
                </div>
                <div className="p-2 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                  <span className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block">Manga</span>
                  <span className="font-mono text-xs font-semibold text-[var(--text-primary)]">{rec.largoMangaCm || '—'}</span>
                </div>
                <div className="p-2 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                  <span className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block">Hombro</span>
                  <span className="font-mono text-xs font-semibold text-[var(--text-primary)]">{rec.hombroCm || '—'}</span>
                </div>
              </div>
            </div>

            {/* Master Tailor Notes */}
            <div className="space-y-1.5">
              <span className="text-[9px] font-mono tracking-wider uppercase text-[var(--text-tertiary)]">
                Instrucciones del Maestro Sastre:
              </span>
              <p className="font-serif-editorial italic text-xs text-[var(--text-secondary)] leading-relaxed p-3 border border-[var(--border-subtle)] bg-[var(--surface-card)]">
                «{rec.instruccionesSastre}»
              </p>
            </div>

            {/* Action Progression */}
            <div className="pt-3 border-t border-[var(--border-subtle)] flex items-center justify-between text-xs">
              <span className="text-[11px] font-sans-editorial text-[var(--text-tertiary)]">
                Responsable: <span className="text-[var(--text-primary)] font-medium">{rec.sastreAsignado}</span>
              </span>

              {rec.estado !== 'ENTALLADO_LISTO_ENTREGA' && (
                <motion.button
                  whileTap={{ scale: 0.96 }}
                  onClick={() => {
                    const next = rec.estado === 'PENDIENTE_MEDICION' ? 'EN_TALLER_COSTURA' : 'ENTALLADO_LISTO_ENTREGA';
                    onUpdateStatus(rec.id, next);
                    toast.success(`Estado actualizado a: ${next === 'EN_TALLER_COSTURA' ? 'En Costura' : 'Entalle Listo para Velada'}`);
                  }}
                  className="px-3 py-1.5 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] text-[var(--accent-gold)] text-xs font-mono uppercase tracking-wider flex items-center gap-1.5 transition-colors"
                >
                  <span>{rec.estado === 'PENDIENTE_MEDICION' ? 'Enviar a Costura' : 'Finalizar Entalle'}</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </motion.button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* New Tailoring Ticket Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
            <motion.div
              initial={{ opacity: 0, scale: 0.96 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.96 }}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] w-full max-w-lg p-6 sm:p-8 space-y-6 shadow-2xl relative"
            >
              <div className="flex items-center justify-between pb-4 border-b border-[var(--border-subtle)]">
                <div className="space-y-1">
                  <span className="text-[9px] font-mono tracking-[0.2em] uppercase text-[var(--accent-gold)]">
                    Ficha Técnica de Sastrería
                  </span>
                  <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
                    Nueva Ficha de Entallado Bespoke
                  </h3>
                </div>
                <button
                  onClick={() => setIsModalOpen(false)}
                  className="p-1 text-[var(--text-tertiary)] hover:text-[var(--text-primary)] transition-colors"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Contrato de Velada Asociado
                  </label>
                  <select
                    value={selectedOrderCodigo}
                    onChange={(e) => setSelectedOrderCodigo(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  >
                    {orders.map((o) => (
                      <option key={o.id} value={o.codigoContrato}>
                        {o.codigoContrato} — {o.clienteNombreCompleto} ({o.items?.[0]?.garmentName || 'Prenda'})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Maestro Sastre Asignado
                  </label>
                  <input
                    type="text"
                    required
                    value={sastreAsignado}
                    onChange={(e) => setSastreAsignado(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="grid grid-cols-5 gap-2">
                  <div className="space-y-1">
                    <label className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block text-center">Basta (cm)</label>
                    <input
                      type="number"
                      value={bastaPantalonCm}
                      onChange={(e) => setBastaPantalonCm(Number(e.target.value))}
                      className="w-full px-2 py-1.5 text-center bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)]"
                    />
                  </div>
                  <div className="space-y-1">
                    <label className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block text-center">Cintura</label>
                    <input
                      type="number"
                      value={cinturaCm}
                      onChange={(e) => setCinturaCm(Number(e.target.value))}
                      className="w-full px-2 py-1.5 text-center bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)]"
                    />
                  </div>
                  <div className="space-y-1">
                    <label className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block text-center">Talle</label>
                    <input
                      type="number"
                      value={talleSacoCm}
                      onChange={(e) => setTalleSacoCm(Number(e.target.value))}
                      className="w-full px-2 py-1.5 text-center bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)]"
                    />
                  </div>
                  <div className="space-y-1">
                    <label className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block text-center">Manga</label>
                    <input
                      type="number"
                      value={largoMangaCm}
                      onChange={(e) => setLargoMangaCm(Number(e.target.value))}
                      className="w-full px-2 py-1.5 text-center bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)]"
                    />
                  </div>
                  <div className="space-y-1">
                    <label className="text-[9px] font-mono uppercase text-[var(--text-tertiary)] block text-center">Hombro</label>
                    <input
                      type="number"
                      value={hombroCm}
                      onChange={(e) => setHombroCm(Number(e.target.value))}
                      className="w-full px-2 py-1.5 text-center bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)]"
                    />
                  </div>
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Instrucciones Especiales de Entalle
                  </label>
                  <textarea
                    rows={3}
                    value={instruccionesSastre}
                    onChange={(e) => setInstruccionesSastre(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="pt-4 border-t border-[var(--border-subtle)] flex items-center justify-end gap-3">
                  <button
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 border border-[var(--border-subtle)] text-xs font-mono uppercase tracking-wider text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-colors"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] text-xs font-sans-editorial font-semibold uppercase tracking-wider transition-colors"
                  >
                    Emitir Ficha de Entallado
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