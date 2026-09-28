'use client';

import React from 'react';
import { Clock, CheckCircle2, Sparkles, AlertCircle, Shirt } from 'lucide-react';
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
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
          <Clock className="w-5 h-5 text-purple-700 dark:text-purple-400" />
          Recepción, Limpieza y Bloqueo de Tintorería (RF-05)
        </h2>
        <p className="text-xs text-[var(--text-secondary)]">
          Bloqueo preventivo automático de 24h a 48h post-evento para garantizado de sanitizado y planchado de prendas de gala.
        </p>
      </div>

      {laundryGarments.length === 0 ? (
        <div className="glass-panel rounded-2xl p-12 text-center border border-[var(--glass-border)] space-y-3">
          <div className="w-12 h-12 rounded-full bg-purple-500/10 text-purple-700 dark:text-purple-400 mx-auto flex items-center justify-center">
            <CheckCircle2 className="w-6 h-6" />
          </div>
          <h3 className="font-bold text-[var(--text-primary)]">No hay prendas en tintorería actualmente</h3>
          <p className="text-xs text-[var(--text-secondary)] max-w-md mx-auto">
            Todas las prendas están disponibles en showroom o alquiladas. Al procesar una devolución de contrato, las prendas ingresarán automáticamente a este ciclo.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {laundryGarments.map((garment) => (
            <div key={garment.id} className="glass-card rounded-2xl border border-purple-500/30 p-5 space-y-4">
              <div className="flex items-center gap-3">
                <img
                  src={garment.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=400'}
                  alt={garment.nombre}
                  className="w-16 h-20 object-cover rounded-xl border border-[var(--glass-border)]"
                />
                <div>
                  <span className="px-2 py-0.5 rounded text-[10px] font-mono font-bold bg-purple-500/20 text-purple-800 dark:text-purple-300 border border-purple-500/30">
                    {garment.codigoUnico}
                  </span>
                  <h3 className="font-bold text-[var(--text-primary)] text-sm mt-1">{garment.nombre}</h3>
                  <p className="text-xs text-[var(--text-secondary)]">Talla: {garment.talla} • {garment.color}</p>
                </div>
              </div>

              <div className="p-3 rounded-xl bg-purple-500/10 border border-purple-500/20 text-xs text-purple-200 space-y-1">
                <div className="flex justify-between items-center font-semibold">
                  <span className="flex items-center gap-1">
                    <Clock className="w-3.5 h-3.5 text-purple-700 dark:text-purple-400" />
                    Bloqueo Programado:
                  </span>
                  <span className="text-purple-800 dark:text-purple-300 font-mono font-bold">{garment.horasTintoreriaBloqueo} Horas</span>
                </div>
                <p className="text-[11px] text-purple-800 dark:text-purple-300/80">
                  Desinfección ultrasónica, vaporizado y control de acabado de gala en proceso.
                </p>
              </div>

              <motion.button
                whileTap={{ scale: 0.95 }}
                onClick={() => {
                  onReleaseGarment(garment.id);
                  toast.success(`Prenda "${garment.nombre}" liberada de Tintorería. Ahora está DISPONIBLE en catálogo.`);
                }}
                className="w-full py-2.5 rounded-xl bg-emerald-500 hover:bg-emerald-400 text-black font-bold text-xs transition-all shadow-lg shadow-emerald-500/20 flex items-center justify-center gap-2"
              >
                <CheckCircle2 className="w-4 h-4" />
                Aprobar Sanitización y Dar de Alta
              </motion.button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
