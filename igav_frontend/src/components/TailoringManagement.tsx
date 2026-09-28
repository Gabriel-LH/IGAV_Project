'use client';

import React, { useState } from 'react';
import { Scissors, Ruler, CheckCircle2, Clock, Plus, User, FileText, X, Sparkles } from 'lucide-react';
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
  const [sastreAsignado, setSastreAsignado] = useState('Don Carlos Sastre Senior');
  const [bastaPantalonCm, setBastaPantalonCm] = useState(76);
  const [cinturaCm, setCinturaCm] = useState(84);
  const [talleSacoCm, setTalleSacoCm] = useState(72);
  const [largoMangaCm, setLargoMangaCm] = useState(64);
  const [hombroCm, setHombroCm] = useState(44);
  const [instruccionesSastre, setInstruccionesSastre] = useState('Ajuste de basta doblada hacia adentro y entallado ligero de cintura.');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const order = orders.find(o => o.codigoContrato === selectedOrderCodigo) || orders[0];

    onAddRecord({
      orderCodigo: selectedOrderCodigo,
      clienteNombre: order?.clienteNombreCompleto || 'Cliente de Gala',
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
    toast.success(`Ficha de Sastrería registrada para el contrato ${selectedOrderCodigo}. Enviada al taller.`);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <Scissors className="w-5 h-5 text-amber-700 dark:text-amber-400" />
            Taller de Sastrería, Entallado & Medidas de Gala
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Control de bastas, cintura, talle, mangas y hombros asignados al sastre previo a la entrega del evento.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Nueva Ficha de Entallado
        </motion.button>
      </div>

      {/* Tailoring Grid Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
        {records.map((rec) => (
          <div key={rec.id} className="glass-card rounded-2xl p-5 border border-[var(--glass-border)] space-y-4">
            <div className="flex items-start justify-between">
              <div>
                <span className="px-2.5 py-0.5 rounded text-[10px] font-mono font-bold bg-amber-500/20 text-amber-800 dark:text-amber-300 border border-amber-500/40">
                  {rec.orderCodigo}
                </span>
                <h3 className="font-bold text-[var(--text-primary)] text-base mt-2">{rec.clienteNombre}</h3>
                <p className="text-xs text-amber-700 dark:text-amber-400 font-medium">{rec.prendaNombre} ({rec.prendaSku})</p>
              </div>

              <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold border ${
                rec.estado === 'PENDIENTE_MEDICION' ? 'bg-amber-500/20 text-amber-800 dark:text-amber-300 border-amber-500/40' :
                rec.estado === 'EN_TALLER_COSTURA' ? 'bg-purple-500/20 text-purple-800 dark:text-purple-300 border-purple-500/40' :
                'bg-emerald-500/20 text-emerald-800 dark:text-emerald-300 border-emerald-500/40'
              }`}>
                {rec.estado}
              </span>
            </div>

            {/* Measurements Specification Table */}
            <div className="p-3.5 rounded-xl bg-[var(--glass-bg)] border border-[var(--glass-border)] space-y-2">
              <div className="flex items-center gap-1 text-xs font-bold text-[var(--text-primary)] border-b border-[var(--glass-border)] pb-1.5">
                <Ruler className="w-4 h-4 text-amber-700 dark:text-amber-400" />
                <span>Especificaciones de Medida (cm)</span>
              </div>
              <div className="grid grid-cols-3 sm:grid-cols-5 gap-2 text-center text-[11px] pt-1">
                <div className="bg-slate-100 dark:bg-black/30 p-1.5 rounded border border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)] block text-[9px]">Basta</span>
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{rec.bastaPantalonCm || '-'} cm</span>
                </div>
                <div className="bg-slate-100 dark:bg-black/30 p-1.5 rounded border border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)] block text-[9px]">Cintura</span>
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{rec.cinturaCm || '-'} cm</span>
                </div>
                <div className="bg-slate-100 dark:bg-black/30 p-1.5 rounded border border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)] block text-[9px]">Talle</span>
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{rec.talleSacoCm || '-'} cm</span>
                </div>
                <div className="bg-slate-100 dark:bg-black/30 p-1.5 rounded border border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)] block text-[9px]">Manga</span>
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{rec.largoMangaCm || '-'} cm</span>
                </div>
                <div className="bg-slate-100 dark:bg-black/30 p-1.5 rounded border border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)] block text-[9px]">Hombro</span>
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{rec.hombroCm || '-'} cm</span>
                </div>
              </div>
            </div>

            <div className="text-xs text-[var(--text-secondary)] space-y-1">
              <span className="text-[10px] uppercase font-bold text-[var(--text-secondary)]">Instrucciones Especiales del Sastre:</span>
              <p className="p-2.5 rounded-lg bg-slate-100 dark:bg-black/40 border border-[var(--border-color)] text-[11px] text-[var(--text-primary)] italic">
                "{rec.instruccionesSastre}"
              </p>
            </div>

            <div className="flex items-center justify-between pt-2 border-t border-[var(--border-color)] text-xs">
              <span className="text-[var(--text-secondary)] font-medium flex items-center gap-1">
                <User className="w-3.5 h-3.5 text-amber-700 dark:text-amber-400" />
                {rec.sastreAsignado}
              </span>

              {rec.estado !== 'ENTALLADO_LISTO_ENTREGA' && (
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => {
                    onUpdateStatus(rec.id, 'ENTALLADO_LISTO_ENTREGA');
                    toast.success(`Entallado de ${rec.prendaSku} completado y listo para entrega.`);
                  }}
                  className="px-3 py-1.5 rounded-lg bg-emerald-500 hover:bg-emerald-400 text-black font-bold text-xs transition-colors flex items-center gap-1"
                >
                  <CheckCircle2 className="w-3.5 h-3.5" />
                  Marcar Listo para Entrega
                </motion.button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* New Tailoring Record Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="glass-panel border border-[var(--border-color)] rounded-2xl w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-5"
            >
              <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
                <div className="flex items-center gap-2">
                  <Scissors className="w-5 h-5 text-amber-700 dark:text-amber-400" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Ficha de Entallado para Taller</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsModalOpen(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4 text-xs">
                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Seleccionar Contrato de Alquiler *</label>
                  <select
                    value={selectedOrderCodigo}
                    onChange={(e) => setSelectedOrderCodigo(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] font-medium"
                  >
                    {orders.map((o) => (
                      <option key={o.id} value={o.codigoContrato}>
                        {o.codigoContrato} - {o.clienteNombreCompleto} ({o.items?.[0]?.garmentName || 'Prenda Gala'})
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Sastre Asignado *</label>
                  <select
                    value={sastreAsignado}
                    onChange={(e) => setSastreAsignado(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  >
                    <option value="Don Carlos Sastre Senior">Don Carlos Sastre Senior (Sacos & Ternos)</option>
                    <option value="Maestra Elena Sastrería">Maestra Elena Sastrería (Vestidos Haute Couture)</option>
                    <option value="Taller Central San Isidro">Taller Central San Isidro</option>
                  </select>
                </div>

                {/* Measurements Grid */}
                <div className="p-3.5 rounded-xl bg-[var(--glass-bg)] border border-[var(--glass-border)] space-y-3">
                  <span className="font-bold text-amber-700 dark:text-amber-400 block text-xs">Medidas del Cliente (en Centímetros):</span>
                  <div className="grid grid-cols-3 sm:grid-cols-5 gap-3">
                    <div>
                      <label className="block text-[10px] text-[var(--text-secondary)] mb-1">Basta (cm)</label>
                      <input
                        type="number"
                        value={bastaPantalonCm}
                        onChange={(e) => setBastaPantalonCm(Number(e.target.value))}
                        className="w-full px-2.5 py-1.5 rounded bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] text-center font-mono"
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] text-[var(--text-secondary)] mb-1">Cintura (cm)</label>
                      <input
                        type="number"
                        value={cinturaCm}
                        onChange={(e) => setCinturaCm(Number(e.target.value))}
                        className="w-full px-2.5 py-1.5 rounded bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] text-center font-mono"
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] text-[var(--text-secondary)] mb-1">Talle (cm)</label>
                      <input
                        type="number"
                        value={talleSacoCm}
                        onChange={(e) => setTalleSacoCm(Number(e.target.value))}
                        className="w-full px-2.5 py-1.5 rounded bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] text-center font-mono"
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] text-[var(--text-secondary)] mb-1">Manga (cm)</label>
                      <input
                        type="number"
                        value={largoMangaCm}
                        onChange={(e) => setLargoMangaCm(Number(e.target.value))}
                        className="w-full px-2.5 py-1.5 rounded bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] text-center font-mono"
                      />
                    </div>
                    <div>
                      <label className="block text-[10px] text-[var(--text-secondary)] mb-1">Hombro (cm)</label>
                      <input
                        type="number"
                        value={hombroCm}
                        onChange={(e) => setHombroCm(Number(e.target.value))}
                        className="w-full px-2.5 py-1.5 rounded bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] text-center font-mono"
                      />
                    </div>
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Instrucciones Especiales de Entallado</label>
                  <textarea
                    rows={2}
                    value={instruccionesSastre}
                    onChange={(e) => setInstruccionesSastre(e.target.value)}
                    placeholder="Detalles de pinzas, dobladillos, silueta..."
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div className="pt-3 flex justify-end gap-3 border-t border-[var(--glass-border)]">
                  <motion.button whileTap={{ scale: 0.96 }} type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 rounded-lg text-[var(--text-secondary)] hover:bg-[var(--glass-bg)]"
                  >
                    Cancelar
                  </motion.button>
                  <motion.button whileTap={{ scale: 0.96 }} type="submit"
                    className="px-5 py-2 rounded-lg bg-amber-500 hover:bg-amber-400 text-black font-bold shadow-lg shadow-amber-500/20"
                  >
                    Enviar a Taller
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
