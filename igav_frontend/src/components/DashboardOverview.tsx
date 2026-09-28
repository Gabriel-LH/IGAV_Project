'use client';

import React from 'react';
import { 
  Shirt, 
  CalendarCheck, 
  DollarSign, 
  Clock, 
  AlertTriangle, 
  PlusCircle, 
  RotateCcw, 
  ArrowUpRight, 
  CheckCircle2,
  Sparkles
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
  // Calculations
  const totalGarments = garments.length;
  const activeRentals = garments.filter(g => g.estado === 'ALQUILADO').length;
  const inLaundry = garments.filter(g => g.estado === 'EN_TINTORERIA').length;
  const rotationAlerts = garments.filter(g => g.usosAcumulados >= g.maxUsosRecomendados).length;
  const totalDepositHeld = orders
    .filter(o => o.estado === 'EN_ALQUILAR')
    .reduce((acc, curr) => acc + (curr.montoGarantiaTotal || 0), 0);

  return (
    <div className="space-y-6">
      {/* Top Banner / Welcome */}
      <div className="relative overflow-hidden rounded-2xl glass-panel border border-amber-500/20 p-6 bg-gradient-to-r from-amber-500/10 via-purple-500/5 to-transparent">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 z-10 relative">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <Sparkles className="w-5 h-5 text-amber-700 dark:text-amber-400" />
              <h2 className="text-2xl font-extrabold text-[var(--text-primary)]">Panel de Control de Alquileres & Gala</h2>
            </div>
            <p className="text-sm text-[var(--text-secondary)] max-w-xl">
              Monitoreo de prendas exclusivas en tiempo real, control de reservas sin traslape de fechas y liquidación automática de depósitos en garantía.
            </p>
          </div>
          <div className="flex flex-wrap items-center gap-3">
            <motion.button
              whileTap={{ scale: 0.95 }}
              onClick={onOpenNewOrder}
              className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2"
            >
              <PlusCircle className="w-4 h-4" />
              Nueva Reserva / Alquiler
            </motion.button>

            <motion.button
              whileTap={{ scale: 0.95 }}
              onClick={() => onOpenReturnModal()}
              className="px-4 py-2.5 rounded-xl bg-purple-600/80 hover:bg-purple-500 text-[var(--text-primary)] font-medium text-sm border border-purple-500/30 transition-all flex items-center gap-2"
            >
              <RotateCcw className="w-4 h-4" />
              Recepción & Devolución
            </motion.button>
          </div>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Total Garments */}
        <div className="glass-card rounded-xl p-5 border border-[var(--glass-border)] relative overflow-hidden">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">Catálogo Total</p>
              <h3 className="text-3xl font-extrabold text-[var(--text-primary)] mt-1">{totalGarments}</h3>
              <p className="text-xs text-emerald-700 dark:text-emerald-400 mt-2 flex items-center gap-1">
                <ArrowUpRight className="w-3.5 h-3.5" />
                <span>Prendas registradas</span>
              </p>
            </div>
            <div className="w-12 h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-700 dark:text-emerald-400">
              <Shirt className="w-6 h-6" />
            </div>
          </div>
        </div>

        {/* Active Rentals */}
        <div className="glass-card rounded-xl p-5 border border-[var(--glass-border)] relative overflow-hidden">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">Alquileres Activos</p>
              <h3 className="text-3xl font-extrabold text-[var(--text-primary)] mt-1">{activeRentals}</h3>
              <p className="text-xs text-amber-700 dark:text-amber-400 mt-2 flex items-center gap-1">
                <span>En poder de clientes</span>
              </p>
            </div>
            <div className="w-12 h-12 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-700 dark:text-amber-400">
              <CalendarCheck className="w-6 h-6" />
            </div>
          </div>
        </div>

        {/* Laundry / Maintenance */}
        <div className="glass-card rounded-xl p-5 border border-[var(--glass-border)] relative overflow-hidden">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">En Tintorería (24-48h)</p>
              <h3 className="text-3xl font-extrabold text-[var(--text-primary)] mt-1">{inLaundry}</h3>
              <p className="text-xs text-purple-700 dark:text-purple-400 mt-2 flex items-center gap-1">
                <Clock className="w-3.5 h-3.5" />
                <span>Bloqueo preventivo de fecha</span>
              </p>
            </div>
            <div className="w-12 h-12 rounded-xl bg-purple-500/10 border border-purple-500/20 flex items-center justify-center text-purple-700 dark:text-purple-400">
              <Clock className="w-6 h-6" />
            </div>
          </div>
        </div>

        {/* Guarantee Held */}
        <div className="glass-card rounded-xl p-5 border border-[var(--glass-border)] relative overflow-hidden">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-xs font-semibold text-[var(--text-secondary)] uppercase tracking-wider">Garantías Retenidas</p>
              <h3 className="text-3xl font-extrabold text-emerald-700 dark:text-emerald-400 mt-1">S/ {totalDepositHeld.toFixed(2)}</h3>
              <p className="text-xs text-[var(--text-secondary)] mt-2">
                En depósito por alquileres
              </p>
            </div>
            <div className="w-12 h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-700 dark:text-emerald-400">
              <DollarSign className="w-6 h-6" />
            </div>
          </div>
        </div>
      </div>

      {/* Alert Banner if rotation threshold reached */}
      {rotationAlerts > 0 && (
        <div className="glass-card rounded-xl border border-amber-500/40 p-4 bg-amber-500/10 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-amber-500/20 border border-amber-500/40 flex items-center justify-center text-amber-700 dark:text-amber-400">
              <AlertTriangle className="w-5 h-5 animate-bounce" />
            </div>
            <div>
              <h4 className="font-bold text-amber-200 text-sm">Alerta de Rotación e Inspección (RF-12)</h4>
              <p className="text-xs text-amber-200/80">
                Hay {rotationAlerts} prenda(s) que han alcanzado o superado el límite recomendado de usos. Requieren inspección de calidad o pase a mantenimiento.
              </p>
            </div>
          </div>
          <motion.button whileTap={{ scale: 0.96 }} onClick={() => onNavigate('alerts')}
            className="px-3.5 py-1.5 rounded-lg bg-amber-500 text-black font-semibold text-xs hover:bg-amber-400 transition-colors"
          >
            Ver Alertas
          </motion.button>
        </div>
      )}

      {/* Main Grid Section: Active Garments Preview & Orders Summary */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Garments Summary */}
        <div className="lg:col-span-2 glass-panel rounded-2xl p-5 border border-[var(--glass-border)] space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="font-bold text-[var(--text-primary)] flex items-center gap-2">
              <Shirt className="w-5 h-5 text-amber-700 dark:text-amber-400" />
              Prendas Destacadas en Catálogo
            </h3>
            <motion.button whileTap={{ scale: 0.96 }} onClick={() => onNavigate('garments')}
              className="text-xs text-amber-700 dark:text-amber-400 hover:underline font-medium"
            >
              Ver todas ({garments.length}) →
            </motion.button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-3.5">
            {garments.slice(0, 6).map((garment) => (
              <div key={garment.id} className="glass-card rounded-xl p-3.5 border border-[var(--glass-border)] flex gap-3">
                <img
                  src={garment.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=400'}
                  alt={garment.nombre}
                  className="w-16 h-20 object-cover rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)]"
                />
                <div className="flex-1 flex flex-col justify-between">
                  <div>
                    <span className="text-[10px] uppercase font-bold text-[var(--text-secondary)] tracking-wider">
                      {garment.categoryName || 'Gala'} • Talla {garment.talla}
                    </span>
                    <h4 className="text-xs font-bold text-[var(--text-primary)] line-clamp-1">{garment.nombre}</h4>
                    <p className="text-[11px] text-amber-700 dark:text-amber-400 font-semibold mt-0.5">
                      S/ {garment.precioAlquiler.toFixed(2)} / alquiler
                    </p>
                  </div>
                  
                  <div className="flex items-center justify-between text-[10px]">
                    <span className={`px-2 py-0.5 rounded-full font-semibold border ${
                      garment.estado === 'DISPONIBLE' ? 'bg-emerald-500/10 text-emerald-700 dark:text-emerald-400 border-emerald-500/30' :
                      garment.estado === 'ALQUILADO' ? 'bg-amber-500/10 text-amber-700 dark:text-amber-400 border-amber-500/30' :
                      garment.estado === 'EN_TINTORERIA' ? 'bg-purple-500/10 text-purple-700 dark:text-purple-400 border-purple-500/30' :
                      'bg-gray-500/10 text-[var(--text-secondary)] border-gray-500/30'
                    }`}>
                      {garment.estado}
                    </span>
                    <span className="text-[var(--text-secondary)]">{garment.usosAcumulados}/{garment.maxUsosRecomendados} usos</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Orders Sidebar */}
        <div className="glass-panel rounded-2xl p-5 border border-[var(--glass-border)] space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="font-bold text-[var(--text-primary)] flex items-center gap-2">
              <CalendarCheck className="w-5 h-5 text-purple-700 dark:text-purple-400" />
              Últimos Contratos
            </h3>
            <motion.button whileTap={{ scale: 0.96 }} onClick={() => onNavigate('orders')}
              className="text-xs text-purple-700 dark:text-purple-400 hover:underline font-medium"
            >
              Ver Todos →
            </motion.button>
          </div>

          <div className="space-y-3">
            {orders.map((order) => (
              <div key={order.id} className="glass-card rounded-xl p-3.5 border border-[var(--glass-border)] space-y-2">
                <div className="flex items-center justify-between text-xs">
                  <span className="font-mono font-bold text-amber-800 dark:text-amber-300">{order.codigoContrato}</span>
                  <span className={`px-2 py-0.5 rounded-md text-[10px] font-bold ${
                    order.estado === 'EN_ALQUILAR' ? 'bg-amber-500/20 text-amber-800 dark:text-amber-300 border border-amber-500/30' :
                    order.estado === 'DEVUELTO_PENDIENTE_TINTORERIA' ? 'bg-purple-500/20 text-purple-800 dark:text-purple-300 border border-purple-500/30' :
                    'bg-emerald-500/20 text-emerald-800 dark:text-emerald-300 border border-emerald-500/30'
                  }`}>
                    {order.estado === 'EN_ALQUILAR' ? 'EN USO' : order.estado}
                  </span>
                </div>
                <div>
                  <p className="text-xs font-semibold text-[var(--text-primary)]">{order.clienteNombreCompleto}</p>
                  <p className="text-[11px] text-[var(--text-secondary)]">
                    Retorno: {new Date(order.fechaDevolucionAcordada).toLocaleDateString('es-ES')}
                  </p>
                </div>
                <div className="flex justify-between items-center text-xs pt-1 border-t border-[var(--border-color)]">
                  <span className="text-[var(--text-secondary)]">Depósito: S/ {order.montoGarantiaTotal}</span>
                  <motion.button whileTap={{ scale: 0.96 }} onClick={() => onOpenReturnModal(order.id)}
                    className="text-amber-700 dark:text-amber-400 hover:text-amber-800 dark:text-amber-300 text-[11px] font-semibold flex items-center gap-1"
                  >
                    Procesar Devolución →
                  </motion.button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
