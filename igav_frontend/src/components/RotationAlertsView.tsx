'use client';

import React from 'react';
import { AlertTriangle, RotateCcw, ShieldAlert, CheckCircle2, Shirt, Sparkles } from 'lucide-react';
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
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-burgundy)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-burgundy)] font-medium">
              Conservación de Archivo (RF-12)
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Inspección de Fibras & Longevidad Textil
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Supervisión minuciosa de piezas que han alcanzado el límite recomendado de veladas. Previene imperfecciones en eventos de alta sociedad mediante restauración artesanal.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="px-3.5 py-1.5 bg-[var(--surface-card)] border border-[var(--border-subtle)] text-[11px] font-mono text-[var(--text-secondary)]">
            <span className="text-[var(--accent-burgundy)] font-bold">{alertGarments.length}</span> piezas bajo observación
          </div>
        </div>
      </div>

      {alertGarments.length === 0 ? (
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-12 text-center space-y-3 max-w-xl mx-auto">
          <div className="w-12 h-12 border border-[var(--accent-sage)]/40 bg-[var(--accent-sage-light)] text-[var(--accent-sage)] mx-auto flex items-center justify-center">
            <CheckCircle2 className="w-6 h-6 stroke-[1.5]" />
          </div>
          <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
            El archivo de vestuario se encuentra en estado prístino
          </h3>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial leading-relaxed">
            Ninguna pieza de la sede actual ha superado el ciclo de usos recomendado. Todas las sedas, bordados y forros mantienen su integridad original.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {alertGarments.map((garment) => {
            const usagePercent = Math.min(100, Math.round((garment.usosAcumulados / garment.maxUsosRecomendados) * 100));

            return (
              <div
                key={garment.id}
                className="bg-[var(--surface-card)] border border-[var(--border-subtle)] hover:border-[var(--accent-burgundy)] transition-all duration-300 p-5 space-y-4 flex flex-col justify-between"
              >
                <div className="space-y-4">
                  <div className="flex items-start gap-4">
                    <div className="w-20 aspect-[3/4] bg-[var(--surface-elevated)] border border-[var(--border-subtle)] overflow-hidden shrink-0">
                      <img
                        src={garment.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=400'}
                        alt={garment.nombre}
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <div className="space-y-1 min-w-0">
                      <span className="text-[9px] font-mono tracking-widest uppercase px-2 py-0.5 border border-[var(--accent-burgundy)]/40 bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] inline-block">
                        {garment.codigoUnico}
                      </span>
                      <h4 className="font-serif-editorial text-base text-[var(--text-primary)] font-medium truncate">
                        {garment.nombre}
                      </h4>
                      <p className="text-xs font-sans-editorial text-[var(--text-secondary)]">
                        Talla {garment.talla} • {garment.color}
                      </p>
                    </div>
                  </div>

                  <div className="p-3.5 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] space-y-2 text-xs">
                    <div className="flex justify-between items-center font-mono text-[11px]">
                      <span className="text-[var(--text-tertiary)] uppercase tracking-wider">Ciclo Cumplido:</span>
                      <span className="text-[var(--accent-burgundy)] font-bold">
                        {garment.usosAcumulados} / {garment.maxUsosRecomendados} veladas
                      </span>
                    </div>

                    <div className="w-full bg-[var(--border-subtle)] h-1 rounded-full overflow-hidden">
                      <div
                        className="bg-[var(--accent-burgundy)] h-full transition-all duration-500"
                        style={{ width: `${usagePercent}%` }}
                      />
                    </div>

                    <p className="font-sans-editorial text-[11px] text-[var(--text-secondary)] leading-relaxed pt-1">
                      La pieza ha completado el ciclo de desgaste óptimo. Se requiere evaluación de forros y planchado artesanal antes de volver al catálogo.
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2 pt-2 border-t border-[var(--border-subtle)]">
                  <motion.button
                    whileTap={{ scale: 0.98 }}
                    onClick={() => {
                      onResetWear(garment.id);
                      toast.success(`Contador de usos de "${garment.nombre}" restablecido tras restauración.`);
                    }}
                    className="flex-1 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] text-[var(--accent-gold)] font-sans-editorial text-xs font-semibold uppercase tracking-wider transition-colors flex items-center justify-center gap-1.5"
                  >
                    <RotateCcw className="w-3.5 h-3.5" />
                    <span>Certificar Restauración</span>
                  </motion.button>

                  <motion.button
                    whileTap={{ scale: 0.98 }}
                    onClick={() => {
                      onRetireGarment(garment.id);
                      toast.info(`Pieza "${garment.nombre}" archivada permanentemente.`);
                    }}
                    className="px-3 py-2 border border-[var(--accent-burgundy)]/40 text-[var(--accent-burgundy)] hover:bg-[var(--accent-burgundy-light)] font-mono text-xs uppercase tracking-wider transition-colors"
                  >
                    Archivar
                  </motion.button>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};