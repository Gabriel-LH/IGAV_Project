'use client';

import React from 'react';
import { Calendar, Tag, ShieldCheck, ShoppingBag, Check } from 'lucide-react';
import { motion } from 'framer-motion';
import { Garment } from '../../lib/types';

interface DressRentalCardProps {
  item: Garment;
  onBookNow?: (item: Garment) => void;
  onAddToCart?: (item: Garment) => void;
  isInCart?: boolean;
}

export const DressRentalCard: React.FC<DressRentalCardProps> = ({ item, onBookNow, onAddToCart, isInCart }) => {
  const isAvailable = item.estado === 'DISPONIBLE';

  return (
    <motion.div
      whileHover={{ y: -4 }}
      className="backdrop-blur-md bg-[var(--glass-bg)] border border-[var(--glass-border)] rounded-2xl p-4 shadow-xl flex flex-col justify-between transition-all duration-300 group"
    >
      {/* Top Banner Image with Availability Badge */}
      <div className="relative h-52 w-full rounded-xl overflow-hidden mb-3 bg-slate-900">
        <img
          src={item.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800'}
          alt={item.nombre}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent"></div>

        {/* SKU Badge */}
        <div className="absolute top-3 left-3">
          <span className="px-2.5 py-1 rounded-md bg-black/60 backdrop-blur-md text-[10px] font-mono font-bold text-amber-400 border border-[var(--border-color)]">
            {item.codigoUnico}
          </span>
        </div>

        {/* Status Badge */}
        <div className="absolute top-3 right-3">
          <span className={'px-2.5 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider backdrop-blur-md border ' + (
            isAvailable 
              ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40' 
              : item.estado === 'EN_TINTORERIA'
              ? 'bg-purple-500/20 text-purple-300 border-purple-500/40'
              : 'bg-amber-500/20 text-amber-300 border-amber-500/40'
          )}>
            {item.estado}
          </span>
        </div>

        {/* Bottom Specs */}
        <div className="absolute bottom-3 left-3 right-3 flex justify-between items-center text-xs text-white">
          <span className="font-semibold flex items-center gap-1 bg-black/50 px-2 py-0.5 rounded-md backdrop-blur-sm">
            <Tag className="w-3 h-3 text-amber-400" />
            {item.categoryName || 'Gala'} • Talla: {item.talla}
          </span>
        </div>
      </div>

      {/* Card Content */}
      <div className="space-y-3">
        <div>
          <h3 className="font-bold text-sm line-clamp-1 text-[var(--text-primary)]">{item.nombre}</h3>
          <p className="text-xs text-[var(--text-secondary)] line-clamp-2 mt-0.5">{item.descripcion}</p>
        </div>

        {/* Pricing Breakdown */}
        <div className="flex items-center justify-between pt-2 border-t border-[var(--glass-border)] text-xs">
          <div>
            <span className="text-[10px] text-[var(--text-secondary)] block uppercase font-semibold">Alquiler por Día</span>
            <span className="font-extrabold gold-gradient-text text-base">S/ {item.precioAlquiler.toFixed(2)}</span>
          </div>

          <div className="text-right">
            <span className="text-[10px] text-[var(--text-secondary)] block uppercase font-semibold">Garantía Custodia</span>
            <span className="font-bold text-emerald-700 dark:text-emerald-400 text-xs">S/ {item.depositoGarantia.toFixed(2)}</span>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="grid grid-cols-5 gap-2 pt-1">
          <motion.button
            whileTap={{ scale: 0.96 }}
            onClick={() => onBookNow?.(item)}
            disabled={!isAvailable}
            className={'col-span-3 py-2.5 rounded-xl font-bold text-xs transition-all shadow-md flex items-center justify-center gap-1.5 ' + (
              isAvailable
                ? 'bg-amber-500 hover:bg-amber-400 text-black shadow-amber-500/20'
                : 'bg-slate-200 dark:bg-gray-700/50 text-slate-500 dark:text-[var(--text-secondary)] cursor-not-allowed border border-slate-300 dark:border-white/5'
            )}
          >
            <Calendar className="w-3.5 h-3.5" />
            {isAvailable ? 'Alquilar Ahora' : item.estado}
          </motion.button>

          <motion.button
            whileTap={{ scale: 0.96 }}
            onClick={() => onAddToCart?.(item)}
            disabled={!isAvailable}
            className={'col-span-2 py-2.5 rounded-xl font-bold text-xs transition-all shadow-md flex items-center justify-center gap-1 border ' + (
              !isAvailable
                ? 'bg-slate-100 dark:bg-gray-800 text-slate-400 dark:text-slate-600 border-slate-200 dark:border-gray-700 cursor-not-allowed'
                : isInCart
                ? 'bg-emerald-500/10 text-emerald-500 border-emerald-500/30 hover:bg-emerald-500/20'
                : 'bg-black/5 dark:bg-white/5 text-[var(--text-primary)] border-[var(--glass-border)] hover:bg-amber-500/10 hover:border-amber-500/30'
            )}
            title={isInCart ? 'Prenda agregada al carrito' : 'Agregar a carrito multi-reserva'}
          >
            {isInCart ? (
              <>
                <Check className="w-3.5 h-3.5 text-emerald-500" />
                <span>En Carrito</span>
              </>
            ) : (
              <>
                <ShoppingBag className="w-3.5 h-3.5 text-amber-500" />
                <span>+ Carrito</span>
              </>
            )}
          </motion.button>
        </div>
      </div>
    </motion.div>
  );
};
