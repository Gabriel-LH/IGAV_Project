'use client';

import React, { useState } from 'react';
import {
  Shirt, 
  Search, 
  Plus, 
  Sparkles, 
  Clock, 
  Tag,
  SlidersHorizontal,
  X,
  Compass,
  Grid,
  Columns
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Garment } from '../lib/types';
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
  const [occasionFilter, setOccasionFilter] = useState<string>('TODAS');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [viewMode, setViewMode] = useState<'ASYMMETRIC' | 'GRID'>('ASYMMETRIC');

  // Form State
  const [nombre, setNombre] = useState('');
  const [codigoUnico, setCodigoUnico] = useState('');
  const [color, setColor] = useState('');
  const [talla, setTalla] = useState<any>('M');
  const [precioAlquiler, setPrecioAlquiler] = useState(280);
  const [precioVenta, setPrecioVenta] = useState(1800);
  const [depositoGarantia, setDepositoGarantia] = useState(120);
  const [maxUsosRecomendados, setMaxUsosRecomendados] = useState(10);
  const [horasTintoreriaBloqueo, setHorasTintoreriaBloqueo] = useState(24);
  const [categoryName, setCategoryName] = useState('Vestidos de Gala');
  const [descripcion, setDescripcion] = useState('');
  const [imageUrl, setImageUrl] = useState('');

  const occasions = [
    { id: 'TODAS', label: 'El Archivo Completo', note: 'Todas las siluetas curadas para la temporada.' },
    { id: 'OPERA', label: 'Noche en la Ópera', note: 'Terciopelos, drapeados nobles y colas ceremoniales.' },
    { id: 'VANGUARDIA', label: 'Gala de Vanguardia', note: 'Cortes asimétricos, deconstrucción y seda arquitectónica.' },
    { id: 'BLACK_TIE', label: 'Etiqueta Negra & Nupcial', note: 'Esmóquines en lana fría 130s y vestidos champagne.' },
    { id: 'COCKTAIL', label: 'Cóctel en la Embajada', note: 'Líneas puristas, midi estructurado y sobriedad atemporal.' },
  ];

  const filteredGarments = garments.filter((g) => {
    const matchesSearch = g.nombre.toLowerCase().includes(search.toLowerCase()) || 
                          g.codigoUnico.toLowerCase().includes(search.toLowerCase()) ||
                          (g.color && g.color.toLowerCase().includes(search.toLowerCase()));
    
    const matchesStatus = statusFilter === 'ALL' || g.estado === statusFilter;

    let matchesOccasion = true;
    if (occasionFilter === 'OPERA') {
      matchesOccasion = g.nombre.toLowerCase().includes('haute') || g.nombre.toLowerCase().includes('esmeralda') || g.nombre.toLowerCase().includes('velvet');
    } else if (occasionFilter === 'VANGUARDIA') {
      matchesOccasion = g.nombre.toLowerCase().includes('sirena') || g.nombre.toLowerCase().includes('joyería') || g.nombre.toLowerCase().includes('tiaras');
    } else if (occasionFilter === 'BLACK_TIE') {
      matchesOccasion = g.nombre.toLowerCase().includes('terno') || g.nombre.toLowerCase().includes('smoking') || g.nombre.toLowerCase().includes('esmoquin') || g.nombre.toLowerCase().includes('princesa');
    } else if (occasionFilter === 'COCKTAIL') {
      matchesOccasion = g.nombre.toLowerCase().includes('saco') || g.nombre.toLowerCase().includes('slim');
    }

    return matchesSearch && matchesStatus && matchesOccasion;
  });

  const handleSubmitNewGarment = (e: React.FormEvent) => {
    e.preventDefault();
    if (!nombre || !codigoUnico) {
      toast.error('Por favor complete la denominación y código SKU de la pieza');
      return;
    }

    onAddGarment({
      codigoUnico,
      nombre,
      descripcion: descripcion || 'Pieza de alta costura confeccionada en tejidos nobles con terminaciones de taller artesanal.',
      color: color || 'Negro Carbón',
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
    toast.success(`La creación "${nombre}" ha sido catalogada en el Archivo.`);
    
    // Reset
    setNombre('');
    setCodigoUnico('');
    setImageUrl('');
  };

  return (
    <div className="space-y-8">
      {/* Editorial Header / Manifiesto */}
      <div className="relative border-b border-[var(--border-subtle)] pb-6 pt-2">
        <div className="flex flex-col lg:flex-row lg:items-end justify-between gap-6">
          <div className="space-y-2 max-w-2xl">
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]"></span>
              <p className="text-[10px] uppercase tracking-[0.24em] font-mono text-[var(--accent-gold)]">
                Collection Archive · Soirée 2026/2027
              </p>
            </div>
            <h2 className="font-serif-editorial text-3xl sm:text-4xl text-[var(--text-primary)] italic font-normal tracking-wide">
              Salón Privado de Alta Costura & Archivo de Gala
            </h2>
            <p className="text-xs text-[var(--text-secondary)] font-light leading-relaxed">
              Cada creación es custodiada como un testimonio de sastrería viva. La disponibilidad incluye un ciclo 
              estricto de vaporizado vertical y regeneración de fibras de 24h a 48h entre veladas.
            </p>
          </div>

          <div className="flex items-center gap-3">
            {/* View Switcher */}
            <div className="hidden sm:flex items-center border border-[var(--border-subtle)] p-0.5 text-xs font-mono">
              <button
                onClick={() => setViewMode('ASYMMETRIC')}
                className={`px-3 py-1.5 flex items-center gap-1.5 transition-all ${
                  viewMode === 'ASYMMETRIC'
                    ? 'bg-[var(--accent-gold)] text-[#151413] font-medium'
                    : 'text-[var(--text-secondary)] hover:text-[var(--text-primary)]'
                }`}
                title="Composición Asimétrica Editorial"
              >
                <Columns className="w-3.5 h-3.5" />
                <span>Asimétrica</span>
              </button>
              <button
                onClick={() => setViewMode('GRID')}
                className={`px-3 py-1.5 flex items-center gap-1.5 transition-all ${
                  viewMode === 'GRID'
                    ? 'bg-[var(--accent-gold)] text-[#151413] font-medium'
                    : 'text-[var(--text-secondary)] hover:text-[var(--text-primary)]'
                }`}
                title="Cuadrícula Clásica"
              >
                <Grid className="w-3.5 h-3.5" />
                <span>Salón</span>
              </button>
            </div>

            <motion.button
              whileTap={{ scale: 0.98 }}
              onClick={() => setIsModalOpen(true)}
              className="px-4 py-2 text-xs tracking-wider uppercase font-mono font-medium bg-[#1A1817] dark:bg-[#EDE9E1] text-[#F6F4EE] dark:text-[#1A1817] hover:opacity-90 transition-all flex items-center gap-2 border border-transparent shadow-sm"
            >
              <Plus className="w-3.5 h-3.5 text-[var(--accent-gold)]" />
              <span>Catalogar Nueva Pieza</span>
            </motion.button>
          </div>
        </div>

        {/* Narrative Filters Bar */}
        <div className="mt-6 pt-4 border-t border-[var(--border-subtle)]">
          <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none">
            <span className="text-[10px] uppercase tracking-[0.2em] font-mono text-[var(--text-tertiary)] mr-2 flex items-center gap-1">
              <Compass className="w-3 h-3 text-[var(--accent-gold)]" /> Ocasión:
            </span>
            {occasions.map((occ) => (
              <button
                key={occ.id}
                onClick={() => setOccasionFilter(occ.id)}
                className={`px-3 py-1 text-xs whitespace-nowrap font-mono transition-all border ${
                  occasionFilter === occ.id
                    ? 'bg-[var(--surface-elevated)] border-[var(--accent-gold)] text-[var(--text-primary)] font-medium shadow-sm'
                    : 'bg-transparent border-transparent text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:border-[var(--border-subtle)]'
                }`}
              >
                {occ.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Filter and Search Subtle Strip */}
      <div className="flex flex-col sm:flex-row items-center justify-between gap-4 text-xs font-mono">
        <div className="relative w-full sm:w-72">
          <Search className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[var(--text-tertiary)]" />
          <input
            type="text"
            placeholder="Buscar por código SKU, diseñador o seda..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-3 py-1.5 bg-transparent border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] placeholder-[var(--text-tertiary)] focus:outline-none focus:border-[var(--accent-gold)] font-mono"
          />
        </div>

        <div className="flex items-center gap-1 w-full sm:w-auto overflow-x-auto">
          <span className="text-[10px] uppercase tracking-[0.16em] text-[var(--text-tertiary)] mr-1">Disponibilidad:</span>
          {[
            { id: 'ALL', label: 'Todas' },
            { id: 'DISPONIBLE', label: 'En Salón' },
            { id: 'ALQUILADO', label: 'En Compromiso' },
            { id: 'EN_TINTORERIA', label: 'En Vaporizado' }
          ].map((btn) => (
            <button
              key={btn.id}
              onClick={() => setStatusFilter(btn.id)}
              className={`px-2.5 py-1 text-[11px] font-mono transition-all border ${
                statusFilter === btn.id
                  ? 'border-[var(--text-primary)] bg-[var(--surface-elevated)] text-[var(--text-primary)] font-medium'
                  : 'border-transparent text-[var(--text-secondary)] hover:border-[var(--border-subtle)]'
              }`}
            >
              {btn.label}
            </button>
          ))}
        </div>
      </div>

      {/* Garments Display */}
      {viewMode === 'ASYMMETRIC' ? (
        /* Asymmetric Bespoke Layout */
        <div className="space-y-10">
          {filteredGarments.length === 0 ? (
            <div className="p-16 text-center border border-[var(--border-subtle)] space-y-2">
              <p className="font-serif-editorial text-xl italic text-[var(--text-secondary)]">
                No existen piezas en archivo que coincidan con la búsqueda.
              </p>
              <p className="text-xs font-mono text-[var(--text-tertiary)]">
                Intente seleccionar otra ocasión ceremonial o limpie los filtros.
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-12 gap-6 items-start">
              {/* Highlight Hero Card (Span 7 cols) */}
              {filteredGarments[0] && (
                <div className="lg:col-span-7">
                  <div className="text-[10px] uppercase tracking-[0.2em] font-mono text-[var(--accent-gold)] mb-2 flex items-center gap-1.5">
                    <Sparkles className="w-3 h-3" /> Pieza Insignia de Temporada
                  </div>
                  <DressRentalCard
                    item={filteredGarments[0]}
                    onBookNow={onBookGarment}
                    onAddToCart={onAddToCart}
                    isInCart={cartGarmentIds?.includes(filteredGarments[0].id)}
                  />
                </div>
              )}

              {/* Editorial Stylist Note & Companion Card (Span 5 cols) */}
              <div className="lg:col-span-5 space-y-6">
                <div className="border border-[var(--border-subtle)] p-6 bg-[var(--surface-elevated)] space-y-3">
                  <p className="text-[10px] uppercase tracking-[0.24em] font-mono text-[var(--accent-gold)]">
                    Nota de la Directora de Atelier
                  </p>
                  <blockquote className="font-serif-editorial text-lg italic text-[var(--text-primary)] font-light leading-relaxed">
                    &ldquo;El verdadero lujo no busca la estridencia, sino el peso exacto de la caída y la armonía 
                    entre el volumen de la basta y la postura de la velada.&rdquo;
                  </blockquote>
                  <p className="text-[10px] font-mono uppercase tracking-[0.16em] text-[var(--text-secondary)] pt-2 border-t border-[var(--border-subtle)]">
                    Atelier I.G.A.V. · Protocolo de Etiqueta Solemne
                  </p>
                </div>

                {filteredGarments[1] && (
                  <div>
                    <DressRentalCard
                      item={filteredGarments[1]}
                      onBookNow={onBookGarment}
                      onAddToCart={onAddToCart}
                      isInCart={cartGarmentIds?.includes(filteredGarments[1].id)}
                    />
                  </div>
                )}
              </div>

              {/* Remaining garments in balanced 3-column flow */}
              {filteredGarments.slice(2).map((garment) => (
                <div key={garment.id} className="lg:col-span-4">
                  <DressRentalCard
                    item={garment}
                    onBookNow={onBookGarment}
                    onAddToCart={onAddToCart}
                    isInCart={cartGarmentIds?.includes(garment.id)}
                  />
                </div>
              ))}
            </div>
          )}
        </div>
      ) : (
        /* Regular Symmetrical Grid */
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
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
      )}

      {/* New Garment Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
            <motion.div
              initial={{ scale: 0.96, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.96, opacity: 0 }}
              transition={{ duration: 0.3, ease: [0.25, 1, 0.5, 1] }}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-6 shadow-2xl"
            >
              <div className="flex items-center justify-between border-b border-[var(--border-subtle)] pb-4">
                <div>
                  <p className="text-[10px] uppercase tracking-[0.2em] font-mono text-[var(--accent-gold)]">
                    Atelier Entry Record
                  </p>
                  <h3 className="font-serif-editorial text-2xl italic text-[var(--text-primary)]">
                    Catalogar Pieza de Alta Costura
                  </h3>
                </div>
                <button
                  onClick={() => setIsModalOpen(false)}
                  className="p-1 text-[var(--text-secondary)] hover:text-[var(--text-primary)]"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <form onSubmit={handleSubmitNewGarment} className="space-y-4 text-xs font-mono">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Código SKU de Archivo *
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="SKU-GAL-008"
                      value={codigoUnico}
                      onChange={(e) => setCodigoUnico(e.target.value)}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)] font-mono"
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Línea de Confección
                    </label>
                    <select
                      value={categoryName}
                      onChange={(e) => setCategoryName(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] text-[var(--text-primary)] border border-[var(--border-subtle)] font-mono"
                    >
                      <option value="Vestidos de Gala">Vestidos de Gala</option>
                      <option value="Ternos de Gala">Ternos de Gala</option>
                      <option value="Sacos & Blazers">Sacos & Blazers</option>
                      <option value="Accesorios de Gala">Accesorios de Gala</option>
                    </select>
                  </div>
                </div>

                <div>
                  <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                    Denominación de la Creación *
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Vestido Capa en Georgette de Seda"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)] font-sans-editorial"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Pigmento / Tonalidad
                    </label>
                    <input
                      type="text"
                      placeholder="Esmeralda Imperial / Negro Carbón"
                      value={color}
                      onChange={(e) => setColor(e.target.value)}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Talla de Patrón
                    </label>
                    <select
                      value={talla}
                      onChange={(e) => setTalla(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] text-[var(--text-primary)] border border-[var(--border-subtle)]"
                    >
                      <option value="XS">XS (Extra Petite)</option>
                      <option value="S">S (Petite)</option>
                      <option value="M">M (Standard Couture)</option>
                      <option value="L">L (Comfort Fitting)</option>
                      <option value="XL">XL (Grand Couture)</option>
                      <option value="MEDIDA_CUSTOM">Medida a Medida (Bespoke)</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-3">
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Custodia Gala (S/)
                    </label>
                    <input
                      type="number"
                      value={precioAlquiler}
                      onChange={(e) => setPrecioAlquiler(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Fianza Retorno (S/)
                    </label>
                    <input
                      type="number"
                      value={depositoGarantia}
                      onChange={(e) => setDepositoGarantia(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Venta Archivo (S/)
                    </label>
                    <input
                      type="number"
                      value={precioVenta}
                      onChange={(e) => setPrecioVenta(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Bloqueo Regeneración (Horas)
                    </label>
                    <input
                      type="number"
                      value={horasTintoreriaBloqueo}
                      onChange={(e) => setHorasTintoreriaBloqueo(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                  <div>
                    <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                      Límite Usos de Alerta
                    </label>
                    <input
                      type="number"
                      value={maxUsosRecomendados}
                      onChange={(e) => setMaxUsosRecomendados(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                    Fotografía Editorial (URL)
                  </label>
                  <input
                    type="url"
                    placeholder="https://images.unsplash.com/..."
                    value={imageUrl}
                    onChange={(e) => setImageUrl(e.target.value)}
                    className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)] font-mono text-[11px]"
                  />
                </div>

                <div>
                  <label className="block text-[10px] uppercase tracking-[0.16em] text-[var(--text-secondary)] mb-1">
                    Memoria de Confección & Tejido
                  </label>
                  <textarea
                    rows={2}
                    placeholder="Seda salvaje con forro interno en satén de seda, micro-pedrería aplicada y caída al bies."
                    value={descripcion}
                    onChange={(e) => setDescripcion(e.target.value)}
                    className="w-full px-3 py-2 bg-transparent border border-[var(--border-subtle)] text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)] font-sans-editorial"
                  />
                </div>

                <div className="pt-2 flex justify-end gap-3 border-t border-[var(--border-subtle)]">
                  <button
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 border border-[var(--border-subtle)] text-[var(--text-secondary)] hover:text-[var(--text-primary)] text-xs font-mono"
                  >
                    Descartar
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 bg-[var(--accent-gold)] text-[#151413] hover:bg-[#B39167] text-xs font-mono font-medium tracking-wider uppercase transition-all"
                  >
                    Guardar en Archivo
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