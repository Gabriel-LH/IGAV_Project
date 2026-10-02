'use client';

import React, { useState } from 'react';
import { Calendar, ShieldCheck, ShoppingBag, Check, Sparkles } from 'lucide-react';
import { motion } from 'framer-motion';
import { Garment } from '../../lib/types';

interface DressRentalCardProps {
  item: Garment;
  onBookNow?: (item: Garment) => void;
  onAddToCart?: (item: Garment) => void;
  isInCart?: boolean;
}

export const DressRentalCard: React.FC<DressRentalCardProps> = ({ item, onBookNow, onAddToCart, isInCart }) => {
  const [isHovered, setIsHovered] = useState(false);
  const isAvailable = item.estado === 'DISPONIBLE';

  const designerName = item.categoryName?.includes('Terno') || item.nombre.includes('Terno') || item.nombre.includes('Esmoquin')
    ? 'BRIONI · ARCHIVIO SARTO'
    : item.nombre.includes('Esmeralda') || item.nombre.includes('Haute')
    ? 'VALENTINO · COUTURE ARCHIVE'
    : item.nombre.includes('Sirena')
    ? 'ELIE SAAB · SOIRÉE CRÉATION'
    : 'MAISON I.G.A.V. · ATELIER';

  return (
    <motion.div
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      className="group relative flex flex-col justify-between bg-[var(--surface-card)] border border-[var(--border-subtle)] transition-all duration-500 hover:border-[var(--accent-gold)] p-3.5"
    >
      {/* Editorial Image Stage with 3:4 aspect ratio */}
      <div className="relative aspect-[3/4] w-full overflow-hidden bg-[var(--surface-elevated)] mb-3.5">
        <motion.img
          src={item.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800'}
          alt={item.nombre}
          className="w-full h-full object-cover object-center transition-transform duration-700 ease-[cubic-bezier(0.25,1,0.5,1)] group-hover:scale-[1.03]"
        />

        {/* Ambient Vignette & Grain */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent pointer-events-none opacity-60"></div>

        {/* Top Minimalist Archive Tag */}
        <div className="absolute top-2.5 left-2.5 flex items-center gap-1.5">
          <span className="px-2 py-0.5 text-[9px] uppercase tracking-[0.2em] font-mono bg-[#151413]/80 backdrop-blur-sm text-[#F6F4EE] border border-white/10">
            {item.codigoUnico}
          </span>
        </div>

        {/* Status Indicator */}
        <div className="absolute top-2.5 right-2.5">
          <span className={`px-2 py-0.5 text-[9px] uppercase tracking-[0.18em] font-mono border backdrop-blur-sm ${
            isAvailable
              ? 'bg-[#435245]/85 text-[#F6F4EE] border-[#435245]'
              : item.estado === 'EN_TINTORERIA'
              ? 'bg-[#5C1E26]/85 text-[#F6F4EE] border-[#5C1E26]'
              : 'bg-[#1E1C1A]/85 text-[#C4A47C] border-[#C4A47C]/40'
          }`}>
            {isAvailable ? 'Disponible' : item.estado === 'EN_TINTORERIA' ? 'En Taller / Vaporizado' : 'Reservado'}
          </span>
        </div>

        {/* Specs on Bottom Left */}
        <div className="absolute bottom-2.5 left-2.5 right-2.5 flex justify-between items-center text-[10px] text-[#EDE9E1] font-mono tracking-wider">
          <span className="bg-black/60 backdrop-blur-sm px-2 py-0.5 border border-white/10">
            Talla: {item.talla} · {item.color || 'Couture'}
          </span>
          {item.horasTintoreriaBloqueo && (
            <span className="bg-black/60 backdrop-blur-sm px-1.5 py-0.5 border border-white/10 text-[9px] opacity-80">
              Vaporizado: {item.horasTintoreriaBloqueo}h
            </span>
          )}
        </div>
      </div>

      {/* Editorial Content */}
      <div className="space-y-2.5">
        <div>
          <p className="text-[10px] uppercase tracking-[0.22em] text-[var(--text-secondary)] font-mono">
            {designerName}
          </p>
          <h3 className="font-serif-editorial text-lg text-[var(--text-primary)] italic font-normal line-clamp-1 mt-0.5">
            {item.nombre}
          </h3>
          <p className="text-xs text-[var(--text-secondary)] line-clamp-2 mt-1 font-light leading-relaxed">
            {item.descripcion || 'Confección a medida con tejidos nobles para veladas de etiqueta rigurosa.'}
          </p>
        </div>

        {/* Pricing / Tasa de Custodia */}
        <div className="flex items-end justify-between pt-2.5 border-t border-[var(--border-subtle)] text-xs">
          <div>
            <span className="text-[9px] text-[var(--text-secondary)] block uppercase tracking-[0.16em] font-mono">
              Custodia por Gala
            </span>
            <span className="font-serif-editorial text-xl italic font-semibold text-[var(--accent-gold)]">
              S/ {item.precioAlquiler.toFixed(2)}
            </span>
          </div>

          <div className="text-right">
            <span className="text-[9px] text-[var(--text-secondary)] block uppercase tracking-[0.16em] font-mono">
              Fianza de Retorno
            </span>
            <span className="font-mono text-xs text-[var(--text-secondary)]">
              S/ {item.depositoGarantia.toFixed(2)}
            </span>
          </div>
        </div>

        {/* Editorial Action Buttons */}
        <div className="grid grid-cols-5 gap-2 pt-1.5">
          <motion.button
            whileTap={{ scale: 0.98 }}
            onClick={() => onBookNow?.(item)}
            disabled={!isAvailable}
            className={`col-span-3 py-2 px-3 text-xs tracking-wider uppercase font-mono font-medium transition-all flex items-center justify-center gap-1.5 border ${
              isAvailable
                ? 'bg-[var(--accent-gold)] text-[#151413] hover:bg-[#B39167] border-[var(--accent-gold)]'
                : 'bg-[var(--surface-elevated)] text-[var(--text-tertiary)] border-[var(--border-subtle)] cursor-not-allowed'
            }`}
          >
            <Calendar className="w-3.5 h-3.5" />
            <span>{isAvailable ? 'Agendar Gala' : 'En Compromiso'}</span>
          </motion.button>

          <motion.button
            whileTap={{ scale: 0.98 }}
            onClick={() => onAddToCart?.(item)}
            disabled={!isAvailable}
            className={`col-span-2 py-2 px-2 text-xs tracking-wider uppercase font-mono font-medium transition-all flex items-center justify-center gap-1 border ${
              !isAvailable
                ? 'bg-[var(--surface-elevated)] text-[var(--text-tertiary)] border-[var(--border-subtle)] cursor-not-allowed'
                : isInCart
                ? 'bg-[var(--accent-sage)] text-white border-[var(--accent-sage)]'
                : 'bg-transparent text-[var(--text-primary)] border-[var(--border-subtle)] hover:border-[var(--accent-gold)] hover:text-[var(--accent-gold)]'
            }`}
            title={isInCart ? 'Pieza en Salón Privado' : 'Añadir al Salón Privado'}
          >
            {isInCart ? (
              <>
                <Check className="w-3.5 h-3.5 text-white" />
                <span className="text-[10px]">Guardado</span>
              </>
            ) : (
              <>
                <ShoppingBag className="w-3.5 h-3.5" />
                <span className="text-[10px]">+ Salón</span>
              </>
            )}
          </motion.button>
        </div>
      </div>
    </motion.div>
  );
};