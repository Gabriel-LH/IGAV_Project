'use client';

import React, { useState } from 'react';
import { Store as StoreIcon, Building2, MapPin, Phone, Shield, CheckCircle2, Plus, X } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Store } from '../lib/types';
import { toast } from 'sonner';

interface StoreManagementProps {
  stores: Store[];
  activeStoreId: number;
  onSelectActiveStore: (storeId: number) => void;
  onAddStore: (store: Omit<Store, 'id'>) => void;
}

export const StoreManagement: React.FC<StoreManagementProps> = ({
  stores,
  activeStoreId,
  onSelectActiveStore,
  onAddStore
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [nombre, setNombre] = useState('');
  const [direccion, setDireccion] = useState('');
  const [telefono, setTelefono] = useState('');
  const [ciudad, setCiudad] = useState('Lima');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!nombre || !direccion) {
      toast.error('Ingrese el nombre y dirección de la nueva sede');
      return;
    }

    const tenantCode = `TENANT-00${stores.length + 1}-${nombre.toUpperCase().replace(/\s+/g, '-').slice(0, 10)}`;

    onAddStore({
      codigoTenant: tenantCode,
      nombre,
      direccion,
      telefono: telefono || '+51 900 000 000',
      ciudad,
      esSedePrincipal: false
    });

    setIsModalOpen(false);
    toast.success(`Sede "${nombre}" configurada con Tenant ID: ${tenantCode} (Aislamiento Multi-tenant RF-01).`);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <Building2 className="w-5 h-5 text-amber-700 dark:text-amber-400" />
            Maestro de Sedes & Aislamiento Multi-Tenant (RF-01)
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Control de inventarios y contratos aislados por sucursal / tenant id en la arquitectura SaaS.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Registrar Nueva Sede / Tenant
        </motion.button>
      </div>

      {/* Store Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
        {stores.map((store) => {
          const isActive = store.id === activeStoreId;
          return (
            <div
              key={store.id}
              className={`glass-card rounded-2xl p-5 border space-y-4 transition-all ${
                isActive
                  ? 'border-amber-500/50 bg-amber-500/10 shadow-lg shadow-amber-500/10'
                  : 'border-[var(--glass-border)] hover:border-[var(--border-color)]'
              }`}
            >
              <div className="flex items-start justify-between">
                <div>
                  <span className="px-2 py-0.5 rounded text-[10px] font-mono font-bold bg-[var(--glass-bg)] border border-[var(--glass-border)] text-amber-800 dark:text-amber-300">
                    {store.codigoTenant}
                  </span>
                  <h3 className="font-bold text-[var(--text-primary)] text-base mt-2">{store.nombre}</h3>
                  <span className="text-xs text-[var(--text-secondary)] font-medium">{store.ciudad}</span>
                </div>
                {store.esSedePrincipal && (
                  <span className="px-2.5 py-1 rounded-md text-[10px] font-bold bg-emerald-500/20 text-emerald-800 dark:text-emerald-300 border border-emerald-500/40">
                    Sede Central
                  </span>
                )}
              </div>

              <div className="space-y-2 text-xs text-[var(--text-secondary)] pt-2 border-t border-[var(--border-color)]">
                <div className="flex items-center gap-2">
                  <MapPin className="w-4 h-4 text-amber-700 dark:text-amber-400 shrink-0" />
                  <span>{store.direccion}</span>
                </div>
                <div className="flex items-center gap-2">
                  <Phone className="w-4 h-4 text-emerald-700 dark:text-emerald-400 shrink-0" />
                  <span>{store.telefono}</span>
                </div>
              </div>

              <div className="pt-3 border-t border-[var(--border-color)]">
                {isActive ? (
                  <div className="w-full py-2 rounded-xl bg-amber-500/20 border border-amber-500/40 text-amber-800 dark:text-amber-300 font-bold text-xs flex items-center justify-center gap-2">
                    <CheckCircle2 className="w-4 h-4 text-amber-700 dark:text-amber-400" />
                    Sede Activa Seleccionada
                  </div>
                ) : (
                  <motion.button whileTap={{ scale: 0.96 }} onClick={() => {
                      onSelectActiveStore(store.id);
                      toast.success(`Cambiado a contexto de sede: ${store.nombre}`);
                    }}
                    className="w-full py-2 rounded-xl bg-[var(--glass-bg)] hover:bg-[var(--card-bg-hover)] border border-[var(--glass-border)] text-[var(--text-secondary)] hover:text-[var(--text-primary)] font-semibold text-xs transition-colors"
                  >
                    Cambiar a esta Sede →
                  </motion.button>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* New Store Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="glass-panel border border-[var(--border-color)] rounded-2xl w-full max-w-md p-6 relative space-y-5"
            >
              <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
                <div className="flex items-center gap-2">
                  <Building2 className="w-5 h-5 text-amber-700 dark:text-amber-400" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Registrar Nueva Sede</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsModalOpen(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4 text-xs">
                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Nombre de la Sede *</label>
                  <input
                    type="text"
                    required
                    placeholder="Ej. Sede La Molina"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Dirección Completa *</label>
                  <input
                    type="text"
                    required
                    placeholder="Av. Raúl Ferrero 1200, La Molina"
                    value={direccion}
                    onChange={(e) => setDireccion(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Ciudad</label>
                    <input
                      type="text"
                      value={ciudad}
                      onChange={(e) => setCiudad(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Teléfono Sede</label>
                    <input
                      type="text"
                      placeholder="+51 987 654 321"
                      value={telefono}
                      onChange={(e) => setTelefono(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                </div>

                <div className="pt-3 flex justify-end gap-3 border-t border-[var(--glass-border)]">
                  <motion.button whileTap={{ scale: 0.96 }} type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 rounded-lg text-[var(--text-secondary)] hover:bg-[var(--glass-bg)]"
                  >
                    Cancelar
                  </motion.button>
                  <motion.button whileTap={{ scale: 0.96 }} type="submit"
                    className="px-5 py-2 rounded-lg bg-amber-500 hover:bg-amber-400 text-black font-bold shadow-lg shadow-amber-500/20"
                  >
                    Crear Sede Multi-tenant
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
