'use client';

import React from 'react';
import { 
  Shirt, 
  CalendarCheck, 
  Clock, 
  AlertTriangle, 
  PlusCircle, 
  RotateCcw, 
  Sparkles,
  ArrowRight,
  ShieldCheck,
  Feather,
  Coins
} from 'lucide-react';
import { motion } from 'framer-motion';
import { Garment, Order } from '../lib/types';

interface DashboardOverviewProps {
  garments: Garment[];
  orders: Order[];
  onNavigate: (tab: any) => void;
  onOpenNewOrder: () => void;
  onOpenReturnModal: (orderId?: number) => void;
}

export const DashboardOverview: React.FC<DashboardOverviewProps> = ({
  garments,
  orders,
  onNavigate,
  onOpenNewOrder,
  onOpenReturnModal
}) => {
  const totalGarments = garments.length;
  const activeRentals = garments.filter(g => g.estado === 'ALQUILADO').length;
  const inLaundry = garments.filter(g => g.estado === 'EN_TINTORERIA').length;
  const rotationAlerts = garments.filter(g => g.usosAcumulados >= g.maxUsosRecomendados).length;
  const totalDepositHeld = orders
    .filter(o => o.estado === 'EN_ALQUILAR')
    .reduce((acc, curr) => acc + (curr.montoGarantiaTotal || 0), 0);

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Editorial Welcome Header */}
      <section className="relative overflow-hidden bg-[var(--surface-elevated)] border border-[var(--border-subtle)] p-6 sm:p-8 transition-colors duration-300">
        <div className="absolute top-0 right-0 w-80 h-80 bg-[var(--accent-gold)]/5 rounded-full blur-3xl pointer-events-none" />
        
        <div className="relative z-10 flex flex-col lg:flex-row lg:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]" />
              <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-gold)] font-medium">
                Cuaderno de Dirección & Salón Principal
              </span>
            </div>
            
            <h1 className="font-serif-editorial text-2xl sm:text-3xl lg:text-4xl text-[var(--text-primary)] font-light tracking-tight">
              Maison I.G.A.V. <span className="italic font-serif-editorial text-[var(--accent-gold)]">Haute Couture</span>
            </h1>
            
            <p className="text-xs sm:text-sm text-[var(--text-secondary)] font-sans-editorial max-w-2xl leading-relaxed">
              Curaduría y supervisión del archivo textil, veladas ceremoniales programadas sin solapamiento de fechas y custodia preventiva de piezas de gala.
            </p>
          </div>

          {/* Luxury Action Triggers */}
          <div className="flex flex-wrap items-center gap-3">
            <motion.button
              whileTap={{ scale: 0.98 }}
              onClick={onOpenNewOrder}
              className="px-5 py-2.5 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-all duration-200 shadow-sm flex items-center gap-2"
            >
              <PlusCircle className="w-4 h-4" />
              <span>Nueva Reserva de Gala</span>
            </motion.button>

            <motion.button
              whileTap={{ scale: 0.98 }}
              onClick={() => onOpenReturnModal()}
              className="px-5 py-2.5 bg-transparent hover:bg-[var(--surface-card)] text-[var(--text-primary)] border border-[var(--border-subtle)] font-sans-editorial text-xs font-medium tracking-wider uppercase transition-all duration-200 flex items-center gap-2"
            >
              <RotateCcw className="w-4 h-4 text-[var(--accent-gold)]" />
              <span>Recepción & Fianza</span>
            </motion.button>
          </div>
        </div>
      </section>

      {/* Haute Couture Metrics Bar */}
      <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Garments */}
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 relative overflow-hidden transition-all duration-300 hover:border-[var(--accent-gold)] group">
          <div className="flex justify-between items-start">
            <div className="space-y-1">
              <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
                Archivo de Vestuario
              </span>
              <h3 className="font-serif-editorial text-3xl text-[var(--text-primary)] font-light">
                {totalGarments}
              </h3>
              <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial flex items-center gap-1.5 pt-1">
                <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-sage)] inline-block" />
                <span>Piezas registradas y curadas</span>
              </p>
            </div>
            <div className="w-10 h-10 border border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--text-secondary)] group-hover:text-[var(--accent-gold)] group-hover:border-[var(--accent-gold)] transition-colors">
              <Shirt className="w-4 h-4 stroke-[1.5]" />
            </div>
          </div>
        </div>

        {/* Active Rentals */}
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 relative overflow-hidden transition-all duration-300 hover:border-[var(--accent-gold)] group">
          <div className="flex justify-between items-start">
            <div className="space-y-1">
              <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
                Piezas en Velada Activa
              </span>
              <h3 className="font-serif-editorial text-3xl text-[var(--accent-gold)] font-light">
                {activeRentals}
              </h3>
              <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial flex items-center gap-1.5 pt-1">
                <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)] inline-block" />
                <span>En custodia de huéspedes</span>
              </p>
            </div>
            <div className="w-10 h-10 border border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--text-secondary)] group-hover:text-[var(--accent-gold)] group-hover:border-[var(--accent-gold)] transition-colors">
              <CalendarCheck className="w-4 h-4 stroke-[1.5]" />
            </div>
          </div>
        </div>

        {/* Regenerating in Laundry */}
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 relative overflow-hidden transition-all duration-300 hover:border-[var(--accent-burgundy)] group">
          <div className="flex justify-between items-start">
            <div className="space-y-1">
              <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
                Regeneración de Fibras
              </span>
              <h3 className="font-serif-editorial text-3xl text-[var(--text-primary)] font-light">
                {inLaundry}
              </h3>
              <p className="text-[11px] text-[var(--accent-burgundy)] font-sans-editorial flex items-center gap-1.5 pt-1">
                <Clock className="w-3 h-3 stroke-[1.5]" />
                <span>Bloqueo 24-48h de cuidado</span>
              </p>
            </div>
            <div className="w-10 h-10 border border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--text-secondary)] group-hover:text-[var(--accent-burgundy)] group-hover:border-[var(--accent-burgundy)] transition-colors">
              <Feather className="w-4 h-4 stroke-[1.5]" />
            </div>
          </div>
        </div>

        {/* Custody Guarantee Held */}
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 relative overflow-hidden transition-all duration-300 hover:border-[var(--accent-sage)] group">
          <div className="flex justify-between items-start">
            <div className="space-y-1">
              <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
                Fianzas en Custodia
              </span>
              <h3 className="font-serif-editorial text-3xl text-[var(--accent-sage)] font-light">
                S/ {totalDepositHeld.toFixed(2)}
              </h3>
              <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial flex items-center gap-1.5 pt-1">
                <ShieldCheck className="w-3 h-3 stroke-[1.5] text-[var(--accent-sage)]" />
                <span>Depósitos en resguardo oficial</span>
              </p>
            </div>
            <div className="w-10 h-10 border border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--text-secondary)] group-hover:text-[var(--accent-sage)] group-hover:border-[var(--accent-sage)] transition-colors">
              <Coins className="w-4 h-4 stroke-[1.5]" />
            </div>
          </div>
        </div>
      </section>

      {/* Rotation & Inspection Alert if threshold met */}
      {rotationAlerts > 0 && (
        <section className="bg-[var(--surface-card)] border border-[var(--accent-burgundy)]/40 p-4 sm:p-5 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-start gap-3.5">
            <div className="w-9 h-9 border border-[var(--accent-burgundy)]/30 bg-[var(--accent-burgundy-light)] flex items-center justify-center text-[var(--accent-burgundy)] shrink-0 mt-0.5">
              <AlertTriangle className="w-4 h-4 stroke-[1.5]" />
            </div>
            <div className="space-y-1">
              <h4 className="font-serif-editorial text-base text-[var(--text-primary)] font-medium">
                Protocolo de Rotación e Inspección Textil (RF-12)
              </h4>
              <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-2xl leading-relaxed">
                {rotationAlerts} pieza(s) han alcanzado el umbral recomendado de alquileres. Se sugiere evaluación minuciosa de forros, pedrería o planchado de alta temperatura antes de su siguiente asignación.
              </p>
            </div>
          </div>
          <motion.button
            whileTap={{ scale: 0.98 }}
            onClick={() => onNavigate('alerts')}
            className="px-4 py-2 border border-[var(--accent-burgundy)] text-[var(--accent-burgundy)] hover:bg-[var(--accent-burgundy)] hover:text-white text-xs font-mono tracking-wider uppercase transition-colors shrink-0 self-start sm:self-auto"
          >
            Examinar Piezas
          </motion.button>
        </section>
      )}

      {/* Main Dual Editorial Panel: Featured Garments & Recent Contracts */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left Column: Featured Garments Curatorial View */}
        <section className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[var(--border-subtle)]">
            <div className="flex items-center gap-2.5">
              <Sparkles className="w-4 h-4 text-[var(--accent-gold)] stroke-[1.5]" />
              <h3 className="font-serif-editorial text-lg text-[var(--text-primary)]">
                Selección de Alta Costura en Salón
              </h3>
            </div>
            
            <button
              onClick={() => onNavigate('garments')}
              className="text-xs font-mono text-[var(--accent-gold)] hover:text-[var(--accent-gold-hover)] tracking-wider uppercase flex items-center gap-1.5 transition-colors"
            >
              <span>Ver Archivo Completo ({garments.length})</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-4">
            {garments.slice(0, 6).map((garment) => (
              <div 
                key={garment.id}
                className="bg-[var(--surface-card)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] transition-all duration-300 flex flex-col group overflow-hidden"
              >
                {/* 3:4 Vertical Image Frame */}
                <div className="relative aspect-[3/4] bg-[var(--surface-elevated)] overflow-hidden">
                  <img
                    src={garment.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=600'}
                    alt={garment.nombre}
                    className="w-full h-full object-cover transition-transform duration-700 ease-out group-hover:scale-105"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                  
                  {/* Status Badge */}
                  <div className="absolute top-2.5 left-2.5">
                    <span className={`text-[9px] font-mono tracking-widest uppercase px-2 py-0.5 border ${
                      garment.estado === 'DISPONIBLE'
                        ? 'bg-[var(--surface-card)]/90 text-[var(--accent-sage)] border-[var(--accent-sage)]/40'
                        : garment.estado === 'ALQUILADO'
                        ? 'bg-[var(--surface-card)]/90 text-[var(--accent-gold)] border-[var(--accent-gold)]/40'
                        : 'bg-[var(--surface-card)]/90 text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/40'
                    }`}>
                      {garment.estado === 'DISPONIBLE' ? 'Disponible' : garment.estado === 'ALQUILADO' ? 'En Velada' : 'En Cuidado'}
                    </span>
                  </div>

                  {/* Size & Code Overlay */}
                  <div className="absolute bottom-2.5 left-2.5 right-2.5 flex items-center justify-between text-[10px] font-mono text-white/90">
                    <span>Talla {garment.talla}</span>
                    <span>{garment.codigoUnico}</span>
                  </div>
                </div>

                {/* Details */}
                <div className="p-3.5 flex-1 flex flex-col justify-between space-y-2">
                  <div>
                    <span className="text-[9px] font-mono tracking-widest uppercase text-[var(--text-tertiary)] block">
                      {garment.categoryName || 'Alta Costura'}
                    </span>
                    <h4 className="font-serif-editorial text-sm text-[var(--text-primary)] line-clamp-1 group-hover:text-[var(--accent-gold)] transition-colors">
                      {garment.nombre}
                    </h4>
                  </div>

                  <div className="pt-2 border-t border-[var(--border-subtle)] flex items-center justify-between text-xs">
                    <span className="font-serif-editorial text-sm font-medium text-[var(--text-primary)]">
                      S/ {garment.precioAlquiler.toFixed(2)}
                    </span>
                    <span className="text-[10px] font-mono text-[var(--text-tertiary)]">
                      {garment.usosAcumulados}/{garment.maxUsosRecomendados} usos
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Right Column: Upcoming Gala Contracts & Returns */}
        <section className="space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[var(--border-subtle)]">
            <div className="flex items-center gap-2">
              <CalendarCheck className="w-4 h-4 text-[var(--accent-gold)] stroke-[1.5]" />
              <h3 className="font-serif-editorial text-lg text-[var(--text-primary)]">
                Contratos de Salón
              </h3>
            </div>

            <button
              onClick={() => onNavigate('orders')}
              className="text-xs font-mono text-[var(--accent-gold)] hover:text-[var(--accent-gold-hover)] tracking-wider uppercase flex items-center gap-1.5 transition-colors"
            >
              <span>Ver Todos</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="space-y-3">
            {orders.length === 0 ? (
              <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-8 text-center space-y-2">
                <CalendarCheck className="w-6 h-6 text-[var(--text-tertiary)] mx-auto stroke-[1.5]" />
                <p className="font-serif-editorial text-base text-[var(--text-primary)]">Sin contratos activos</p>
                <p className="text-xs text-[var(--text-secondary)] font-sans-editorial">Inicie una nueva reserva de atelier para agendar una velada.</p>
              </div>
            ) : (
              orders.slice(0, 5).map((order) => (
                <div 
                  key={order.id}
                  className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-4 space-y-2.5 hover:border-[var(--accent-gold)] transition-colors"
                >
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-mono text-[11px] font-medium text-[var(--accent-gold)] tracking-wider">
                      {order.codigoContrato}
                    </span>
                    <span className={`text-[9px] font-mono tracking-widest uppercase px-2 py-0.5 border ${
                      order.estado === 'EN_ALQUILAR'
                        ? 'bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border-[var(--accent-gold)]/40'
                        : order.estado === 'DEVUELTO_PENDIENTE_TINTORERIA'
                        ? 'bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/40'
                        : 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/40'
                    }`}>
                      {order.estado === 'EN_ALQUILAR' ? 'En Uso' : order.estado === 'DEVUELTO_PENDIENTE_TINTORERIA' ? 'En Lavandería' : 'Completado'}
                    </span>
                  </div>

                  <div>
                    <h5 className="font-serif-editorial text-sm font-medium text-[var(--text-primary)]">
                      {order.clienteNombreCompleto}
                    </h5>
                    <p className="text-[11px] font-sans-editorial text-[var(--text-secondary)] flex items-center gap-1.5 mt-0.5">
                      <Clock className="w-3 h-3 text-[var(--text-tertiary)] stroke-[1.5]" />
                      <span>Retorno acordado: {order.fechaDevolucionAcordada ? new Date(order.fechaDevolucionAcordada).toLocaleDateString('es-ES', { day: 'numeric', month: 'short' }) : 'Por definir'}</span>
                    </p>
                  </div>

                  <div className="pt-2 border-t border-[var(--border-subtle)] flex items-center justify-between text-xs">
                    <span className="font-mono text-[11px] text-[var(--text-secondary)]">
                      Fianza: S/ {order.montoGarantiaTotal?.toFixed(2)}
                    </span>
                    
                    {order.estado === 'EN_ALQUILAR' && (
                      <motion.button
                        whileTap={{ scale: 0.96 }}
                        onClick={() => onOpenReturnModal(order.id)}
                        className="text-[11px] font-mono uppercase tracking-wider text-[var(--accent-gold)] hover:text-[var(--accent-gold-hover)] flex items-center gap-1"
                      >
                        <span>Recepción</span>
                        <ArrowRight className="w-3 h-3" />
                      </motion.button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        </section>
      </div>

      {/* Curatorial Atelier Footer Quote */}
      <footer className="pt-8 border-t border-[var(--border-subtle)] text-center space-y-1">
        <p className="font-serif-editorial italic text-sm text-[var(--text-secondary)] max-w-lg mx-auto">
          «La alta costura no es ostentación; es el respeto por la proporción, el silencio del buen tejido y la memoria de una velada inolvidable.»
        </p>
        <p className="text-[10px] font-mono uppercase tracking-[0.2em] text-[var(--text-tertiary)]">
          Maison I.G.A.V. — París & Lima
        </p>
      </footer>
    </div>
  );
};