'use client';

import React from 'react';
import { AlertTriangle, RotateCcw, ShieldAlert, CheckCircle2, Shirt } from 'lucide-react';
import { motion } from 'framer-motion';
import { Garment } from '../lib/types';
import { toast } from 'sonner';

interface RotationAlertsViewProps {
  garments: Garment[];
  onResetWear: (garmentId: number) => void;
  onRetireGarment: (garmentId: number) => void;
}

export const RotationAlertsView: React.FC<RotationAlertsViewProps> = ({
  garments,
  onResetWear,
  onRetireGarment
}) => {
  const alertGarments = garments.filter(g => g.usosAcumulados >= g.maxUsosRecomendados);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
          <AlertTriangle className="w-5 h-5 text-amber-700 dark:text-amber-400" />
          Alertas de Desgaste y Control de Rotación (RF-12)
        </h2>
        <p className="text-xs text-[var(--text-secondary)]">
          Supervisión de prendas que han alcanzado el número máximo de alquileres permitidos. Evita la entrega de vestidos o ternos deteriorados.
        </p>
      </div>

      {alertGarments.length === 0 ? (
        <div className="glass-panel rounded-2xl p-12 text-center border border-[var(--glass-border)] space-y-3">
          <div className="w-12 h-12 rounded-full bg-emerald-500/10 text-emerald-700 dark:text-emerald-400 mx-auto flex items-center justify-center">
            <CheckCircle2 className="w-6 h-6" />
          </div>
          <h3 className="font-bold text-[var(--text-primary)]">Todas las prendas están en excelente estado</h3>
          <p className="text-xs text-[var(--text-secondary)] max-w-md mx-auto">
            Ninguna prenda de la sede actual supera el límite de alquileres recomendados.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {alertGarments.map((garment) => (
            <div key={garment.id} className="glass-card rounded-2xl border border-amber-500/40 p-5 space-y-4">
              <div className="flex items-center gap-3">
                <img
                  src={garment.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=400'}
                  alt={garment.nombre}
                  className="w-16 h-20 object-cover rounded-xl border border-[var(--glass-border)]"
                />
                <div>
                  <span className="px-2 py-0.5 rounded text-[10px] font-mono font-bold bg-amber-500/20 text-amber-600 dark:text-amber-300 border border-amber-500/30">
                    {garment.codigoUnico}
                  </span>
                  <h3 className="font-bold text-[var(--text-primary)] text-sm mt-1">{garment.nombre}</h3>
                  <p className="text-xs text-[var(--text-secondary)]">Talla: {garment.talla}</p>
                </div>
              </div>

              <div className="p-3 rounded-xl bg-amber-500/10 border border-amber-500/20 text-xs text-amber-700 dark:text-amber-200 space-y-1">
                <div className="flex justify-between items-center font-bold">
                  <span>Usos Realizados:</span>
                  <span className="text-amber-600 dark:text-amber-300 font-mono text-sm">{garment.usosAcumulados} / {garment.maxUsosRecomendados}</span>
                </div>
                <p className="text-[11px] text-amber-900 dark:text-amber-200/90 font-medium">
                  La prenda ha completado su ciclo inicial de alquiler. Se recomienda mantenimiento mayor o baja.
                </p>
              </div>

              <div className="flex items-center gap-2 pt-2">
                <motion.button
                  whileTap={{ scale: 0.95 }}
                  onClick={() => {
                    onResetWear(garment.id);
                    toast.success(`Contador de usos de "${garment.nombre}" reiniciado tras restauración.`);
                  }}
                  className="flex-1 py-2 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-xs transition-all shadow-md flex items-center justify-center gap-1.5"
                >
                  <RotateCcw className="w-3.5 h-3.5" />
                  Reiniciar Usos
                </motion.button>

                <motion.button
                  whileTap={{ scale: 0.95 }}
                  onClick={() => {
                    onRetireGarment(garment.id);
                    toast.info(`Prenda "${garment.nombre}" dada de baja.`);
                  }}
                  className="py-2 px-3 rounded-xl bg-rose-500/20 hover:bg-rose-500/30 text-rose-800 dark:text-rose-300 border border-rose-500/30 font-semibold text-xs transition-all"
                >
                  Dar de Baja
                </motion.button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
