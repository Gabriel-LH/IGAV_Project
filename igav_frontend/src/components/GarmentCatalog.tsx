'use client';

import React, { useState } from 'react';
import {
  Image, 
  Shirt, 
  Search, 
  Filter, 
  Plus, 
  Sparkles, 
  CheckCircle2, 
  Clock, 
  AlertTriangle,
  Tag,
  SlidersHorizontal,
  X
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Garment, GarmentStatus } from '../lib/types';
import { DressRentalCard } from './ui/DressRentalCard';
import { toast } from 'sonner';

interface GarmentCatalogProps {
  garments: Garment[];
  onAddGarment: (garment: Omit<Garment, 'id' | 'usosAcumulados'>) => void;
  onBookGarment?: (garment: Garment) => void;
  onAddToCart?: (garment: Garment) => void;
  cartGarmentIds?: number[];
}

export const GarmentCatalog: React.FC<GarmentCatalogProps> = ({ garments, onAddGarment, onBookGarment, onAddToCart, cartGarmentIds }) => {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Form State
  const [nombre, setNombre] = useState('');
  const [codigoUnico, setCodigoUnico] = useState('');
  const [color, setColor] = useState('');
  const [talla, setTalla] = useState<any>('M');
  const [precioAlquiler, setPrecioAlquiler] = useState(200);
  const [precioVenta, setPrecioVenta] = useState(800);
  const [depositoGarantia, setDepositoGarantia] = useState(100);
  const [maxUsosRecomendados, setMaxUsosRecomendados] = useState(12);
  const [horasTintoreriaBloqueo, setHorasTintoreriaBloqueo] = useState(24);
  const [categoryName, setCategoryName] = useState('Vestidos de Gala');
  const [descripcion, setDescripcion] = useState('');
  const [imageUrl, setImageUrl] = useState('');

  const filteredGarments = garments.filter((g) => {
    const matchesSearch = g.nombre.toLowerCase().includes(search.toLowerCase()) || 
                          g.codigoUnico.toLowerCase().includes(search.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || g.estado === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const handleSubmitNewGarment = (e: React.FormEvent) => {
    e.preventDefault();
    if (!nombre || !codigoUnico) {
      toast.error('Por favor complete el nombre y código SKU de la prenda');
      return;
    }

    onAddGarment({
      codigoUnico,
      nombre,
      descripcion: descripcion || 'Prenda exclusiva de alta costura para eventos y gala.',
      color: color || 'Negro / Noche',
      talla,
      precioAlquiler: Number(precioAlquiler),
      precioVenta: Number(precioVenta),
      depositoGarantia: Number(depositoGarantia),
      estado: 'DISPONIBLE',
      maxUsosRecomendados: Number(maxUsosRecomendados),
      horasTintoreriaBloqueo: Number(horasTintoreriaBloqueo),
      categoryName,
      imageUrl: imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800'
    });

    setIsModalOpen(false);
    toast.success(`Prenda "${nombre}" registrada exitosamente en inventario.`);
    
    // Reset
    setNombre('');
    setCodigoUnico('');
    setImageUrl('');
  };

  return (
    <div className="space-y-6">
      {/* Header & Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <Shirt className="w-5 h-5 text-amber-500" />
            Catálogo de Prendas y Alta Costura
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Control de inventario, código de barras/SKU, trazabilidad de usos y bloqueo de tintorería.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Registrar Nueva Prenda
        </motion.button>
      </div>

      {/* Filter Bar */}
      <div className="glass-panel rounded-xl p-4 border border-[var(--glass-border)] flex flex-col md:flex-row items-center justify-between gap-4">
        {/* Search */}
        <div className="relative w-full md:w-80">
          <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Buscar por SKU o Nombre..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs text-current placeholder-[var(--text-secondary)] focus:outline-none focus:border-amber-500/50"
          />
        </div>

        {/* Status Filters */}
        <div className="flex flex-wrap items-center gap-1.5 w-full md:w-auto">
          {[
            { id: 'ALL', label: 'Todas' },
            { id: 'DISPONIBLE', label: 'Disponibles' },
            { id: 'ALQUILADO', label: 'Alquiladas' },
            { id: 'EN_TINTORERIA', label: 'Tintorería' }
          ].map((btn) => (
            <motion.button whileTap={{ scale: 0.96 }}
              key={btn.id}
              onClick={() => setStatusFilter(btn.id)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                statusFilter === btn.id
                  ? 'bg-amber-500 text-black'
                  : 'bg-black/5 dark:bg-[var(--glass-bg)] text-[var(--text-secondary)] hover:text-current border border-[var(--glass-border)]'
              }`}
            >
              {btn.label}
            </motion.button>
          ))}
        </div>
      </div>

      {/* Garments Grid using DressRentalCard Component */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-5">
        {filteredGarments.map((garment) => (
          <DressRentalCard
            key={garment.id}
            item={garment}
            onBookNow={onBookGarment}
            onAddToCart={onAddToCart}
            isInCart={cartGarmentIds?.includes(garment.id)}
          />
        ))}
      </div>

      {/* New Garment Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="glass-panel border border-[var(--glass-border)] rounded-2xl w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-5"
            >
              <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
                <div className="flex items-center gap-2">
                  <Shirt className="w-5 h-5 text-amber-500" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Registrar Prenda de Alta Costura</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsModalOpen(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-current hover:bg-black/5 dark:hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleSubmitNewGarment} className="space-y-4 text-xs">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Código Único (SKU) *</label>
                    <input
                      type="text"
                      required
                      placeholder="SKU-GAL-009"
                      value={codigoUnico}
                      onChange={(e) => setCodigoUnico(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] font-mono"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Categoría</label>
                    <select
                      value={categoryName}
                      onChange={(e) => setCategoryName(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] text-[var(--text-primary)] border border-[var(--glass-border)]"
                    >
                      <option value="Vestidos de Gala">Vestidos de Gala</option>
                      <option value="Ternos de Gala">Ternos de Gala</option>
                      <option value="Sacos & Blazers">Sacos & Blazers</option>
                      <option value="Accesorios de Gala">Accesorios de Gala</option>
                    </select>
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Nombre de la Prenda *</label>
                  <input
                    type="text"
                    required
                    placeholder="Vestido Haute Couture Marfil"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                  />
                </div>

                <div className="grid grid-cols-3 gap-3">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Talla</label>
                    <select
                      value={talla}
                      onChange={(e) => setTalla(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] text-[var(--text-primary)] border border-[var(--glass-border)]"
                    >
                      <option value="XS">XS</option>
                      <option value="S">S</option>
                      <option value="M">M</option>
                      <option value="L">L</option>
                      <option value="XL">XL</option>
                      <option value="MEDIDA_CUSTOM">MEDIDA_CUSTOM</option>
                    </select>
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Color Principal</label>
                    <input
                      type="text"
                      placeholder="Champagne"
                      value={color}
                      onChange={(e) => setColor(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Horas Tintorería Block</label>
                    <input
                      type="number"
                      value={horasTintoreriaBloqueo}
                      onChange={(e) => setHorasTintoreriaBloqueo(Number(e.target.value))}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-3">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Precio Alquiler (S/)</label>
                    <input
                      type="number"
                      value={precioAlquiler}
                      onChange={(e) => setPrecioAlquiler(Number(e.target.value))}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Precio Venta (S/)</label>
                    <input
                      type="number"
                      value={precioVenta}
                      onChange={(e) => setPrecioVenta(Number(e.target.value))}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Garantía Exigida (S/)</label>
                    <input
                      type="number"
                      value={depositoGarantia}
                      onChange={(e) => setDepositoGarantia(Number(e.target.value))}
                      className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Máx Usos Recomendados (RF-12)</label>
                  <input
                    type="number"
                    value={maxUsosRecomendados}
                    onChange={(e) => setMaxUsosRecomendados(Number(e.target.value))}
                    className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                  />
                </div>

                
                {/* Image URL & Live Preview Section */}
                <div className="space-y-2 bg-black/5 dark:bg-white/5 p-3.5 rounded-xl border border-[var(--glass-border)]">
                  <label className="block font-semibold text-[var(--text-secondary)] flex items-center justify-between">
                    <span className="flex items-center gap-1.5">
                      <Image className="w-3.5 h-3.5 text-amber-500" />
                      Fotografía de la Prenda (URL Imagen HD)
                    </span>
                    {imageUrl && (
                      <span className="text-[10px] text-emerald-500 font-bold uppercase tracking-wider">
                        ✓ Vista previa lista
                      </span>
                    )}
                  </label>

                  <div className="flex gap-3 items-center">
                    <div className="flex-1">
                      <input
                        type="url"
                        placeholder="https://images.unsplash.com/photo-..."
                        value={imageUrl}
                        onChange={(e) => setImageUrl(e.target.value)}
                        className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] font-mono text-xs"
                      />
                    </div>
                    
                    {/* Live Preview Card */}
                    <div className="w-12 h-14 rounded-lg overflow-hidden border border-amber-500/40 bg-slate-900 shrink-0 relative shadow-md">
                      <img
                        src={imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800'}
                        alt="Preview"
                        className="w-full h-full object-cover"
                        onError={(e: any) => {
                          e.target.src = 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800';
                        }}
                      />
                    </div>
                  </div>

                  {/* Preset Gallery Pills */}
                  <div>
                    <span className="text-[10px] font-semibold text-[var(--text-secondary)] block mb-1">
                      Catálogo Rápido de Fotografía Gala:
                    </span>
                    <div className="flex flex-wrap gap-1.5">
                      {[
                        { label: '👗 Esmeralda Gala', url: 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800' },
                        { label: '🤵 Esmoquin Black Tie', url: 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?q=80&w=800' },
                        { label: '👗 Azul Noche', url: 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?q=80&w=800' },
                        { label: '🤵 Velvet Burdeos', url: 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=800' },
                        { label: '👰 Princesa Marfil', url: 'https://images.unsplash.com/photo-1546804784-896d0dca3800?q=80&w=800' }
                      ].map((preset, idx) => (
                        <button
                          key={idx}
                          type="button"
                          onClick={() => setImageUrl(preset.url)}
                          className="px-2 py-1 rounded-md text-[10px] font-bold bg-amber-500/10 hover:bg-amber-500/20 text-amber-500 border border-amber-500/30 transition-all"
                        >
                          {preset.label}
                        </button>
                      ))}
                    </div>
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Descripción</label>
                  <textarea
                    rows={2}
                    value={descripcion}
                    onChange={(e) => setDescripcion(e.target.value)}
                    placeholder="Detalles sobre pedrería, seda, confección..."
                    className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)]"
                  />
                </div>

                <div className="pt-3 flex justify-end gap-3 border-t border-[var(--glass-border)]">
                  <motion.button whileTap={{ scale: 0.96 }} type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 rounded-lg text-[var(--text-secondary)] hover:bg-black/5 dark:hover:bg-[var(--glass-bg)]"
                  >
                    Cancelar
                  </motion.button>
                  <motion.button whileTap={{ scale: 0.96 }} type="submit"
                    className="px-5 py-2 rounded-lg bg-amber-500 hover:bg-amber-400 text-black font-bold"
                  >
                    Guardar Prenda
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
