'use client';

import React from 'react';
import { Clock, CheckCircle2, Sparkles, Feather, ShieldCheck } from 'lucide-react';
import { motion } from 'framer-motion';
import { Garment } from '../lib/types';
import { toast } from 'sonner';

interface LaundryManagementProps {
  laundryGarments: Garment[];
  onReleaseGarment: (garmentId: number) => void;
}

export const LaundryManagement: React.FC<LaundryManagementProps> = ({
  laundryGarments,
  onReleaseGarment
}) => {
  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-burgundy)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-burgundy)] font-medium">
              Cuidado & Longevidad Textil (RF-05)
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Vaporizado, Sanitización & Reposo de Fibras
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Protocolo de reposo textil obligatorio de 24h a 48h post-velada. Purificación con vapor suave, control de pedrería y restauración de caídas naturales.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="px-3.5 py-1.5 bg-[var(--surface-card)] border border-[var(--border-subtle)] text-[11px] font-mono text-[var(--text-secondary)]">
            <span className="text-[var(--accent-burgundy)] font-bold">{laundryGarments.length}</span> piezas en regeneración
          </div>
        </div>
      </div>

      {laundryGarments.length === 0 ? (
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-12 text-center space-y-3 max-w-xl mx-auto">
          <div className="w-12 h-12 border border-[var(--accent-sage)]/40 bg-[var(--accent-sage-light)] text-[var(--accent-sage)] mx-auto flex items-center justify-center">
            <CheckCircle2 className="w-6 h-6 stroke-[1.5]" />
          </div>
          <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
            Todas las piezas se encuentran descansadas y listas
          </h3>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial leading-relaxed">
            No hay prendas en ciclo de tintorería en este momento. Al procesar la recepción de un contrato de gala, la pieza ingresará automáticamente a este protocolo.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {laundryGarments.map((garment) => (
            <div
              key={garment.id}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 hover:border-[var(--accent-burgundy)] transition-all duration-300 space-y-4 flex flex-col justify-between"
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
                    <span className="flex items-center gap-1.5 text-[var(--accent-burgundy)]">
                      <Clock className="w-3.5 h-3.5 stroke-[1.5]" />
                      <span>Bloqueo Preventivo:</span>
                    </span>
                    <span className="font-semibold text-[var(--text-primary)]">
                      {garment.horasTintoreriaBloqueo || 24} Horas
                    </span>
                  </div>
                  <p className="font-sans-editorial text-[11px] text-[var(--text-secondary)] leading-relaxed">
                    Vaporizado vertical suave, fijado de pedrería y control olfativo antes del retorno al salón de gala.
                  </p>
                </div>
              </div>

              <motion.button
                whileTap={{ scale: 0.98 }}
                onClick={() => {
                  onReleaseGarment(garment.id);
                  toast.success(`Prenda "${garment.nombre}" certificada. Disponible nuevamente en el salón.`);
                }}
                className="w-full py-2.5 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] hover:border-[var(--accent-sage)] text-[var(--accent-sage)] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-colors flex items-center justify-center gap-2"
              >
                <CheckCircle2 className="w-4 h-4 stroke-[1.5]" />
                <span>Certificar Estado & Retornar a Salón</span>
              </motion.button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};