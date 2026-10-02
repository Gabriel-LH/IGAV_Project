'use client';

import React, { useState } from 'react';
import { Store as StoreIcon, Building2, MapPin, Phone, Shield, CheckCircle2, Plus, X, Sparkles } from 'lucide-react';
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
      toast.error('Ingrese el nombre y dirección de la nueva boutique.');
      return;
    }

    const tenantCode = `TENANT-00${stores.length + 1}-${nombre.toUpperCase().replace(/\s+/g, '-').slice(0, 10)}`;

    onAddStore({
      codigoTenant: tenantCode,
      nombre,
      direccion,
      telefono: telefono || '+51 1 420 0000',
      ciudad,
      esSedePrincipal: false
    });

    setIsModalOpen(false);
    toast.success(`Boutique "${nombre}" habilitada con código: ${tenantCode}.`);
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-gold)] font-medium">
              Red de Salones & Sedes
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Ateliers, Boutiques & Sedes Privadas
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Aislamiento estricto de inventarios y contratos por boutique ceremonial. Gestión de sedes en San Isidro, Miraflores y futuras aperturas.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsModalOpen(true)}
          className="px-5 py-2.5 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-all shadow-sm flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Inaugurar Nueva Boutique</span>
        </motion.button>
      </div>

      {/* Stores Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {stores.map((store) => {
          const isActive = store.id === activeStoreId;

          return (
            <div
              key={store.id}
              className={`bg-[var(--surface-card)] border p-6 transition-all duration-300 space-y-4 flex flex-col justify-between ${
                isActive
                  ? 'border-[var(--accent-gold)] shadow-md'
                  : 'border-[var(--border-subtle)] hover:border-[var(--accent-gold)]/60'
              }`}
            >
              <div className="space-y-3">
                <div className="flex items-start justify-between">
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-tertiary)]">
                        {store.codigoTenant}
                      </span>
                      {store.esSedePrincipal && (
                        <span className="text-[9px] font-mono uppercase tracking-widest px-2 py-0.5 border border-[var(--accent-gold)]/40 bg-[var(--accent-gold-light)] text-[var(--accent-gold)]">
                          Sede Insignia
                        </span>
                      )}
                    </div>
                    <h3 className="font-serif-editorial text-xl text-[var(--text-primary)] font-medium mt-1">
                      {store.nombre}
                    </h3>
                  </div>

                  {isActive ? (
                    <span className="text-[10px] font-mono tracking-wider uppercase px-2.5 py-1 bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border border-[var(--accent-sage)]/40 flex items-center gap-1">
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      <span>Salón Activo</span>
                    </span>
                  ) : (
                    <motion.button
                      whileTap={{ scale: 0.96 }}
                      onClick={() => {
                        onSelectActiveStore(store.id);
                        toast.info(`Cambiado a contexto de sede: ${store.nombre}`);
                      }}
                      className="text-xs font-mono tracking-wider uppercase px-3 py-1 border border-[var(--border-subtle)] text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:border-[var(--accent-gold)] transition-colors"
                    >
                      Ingresar
                    </motion.button>
                  )}
                </div>

                <div className="space-y-1.5 pt-2 border-t border-[var(--border-subtle)] text-xs font-sans-editorial text-[var(--text-secondary)]">
                  <div className="flex items-center gap-2">
                    <MapPin className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span>{store.direccion}, {store.ciudad}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Phone className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span>{store.telefono}</span>
                  </div>
                </div>
              </div>

              <div className="pt-3 border-t border-[var(--border-subtle)] flex items-center justify-between text-[11px] font-mono text-[var(--text-tertiary)]">
                <span>Catálogo y contratos aislados</span>
                <span>{store.ciudad}</span>
              </div>
            </div>
          );
        })}
      </div>

      {/* New Store Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
            <motion.div
              initial={{ opacity: 0, scale: 0.96 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.96 }}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] w-full max-w-lg p-6 sm:p-8 space-y-6 shadow-2xl relative"
            >
              <div className="flex items-center justify-between pb-4 border-b border-[var(--border-subtle)]">
                <div className="space-y-1">
                  <span className="text-[9px] font-mono tracking-[0.2em] uppercase text-[var(--accent-gold)]">
                    Expansión de Red
                  </span>
                  <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
                    Inauguración de Nueva Boutique
                  </h3>
                </div>
                <button
                  onClick={() => setIsModalOpen(false)}
                  className="p-1 text-[var(--text-tertiary)] hover:text-[var(--text-primary)] transition-colors"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Nombre de la Boutique
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Boutique Nupcial Chacarilla"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Dirección
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Calle Monterrey 320, Surco"
                    value={direccion}
                    onChange={(e) => setDireccion(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Teléfono
                    </label>
                    <input
                      type="text"
                      placeholder="+51 1 450 9900"
                      value={telefono}
                      onChange={(e) => setTelefono(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Ciudad
                    </label>
                    <input
                      type="text"
                      value={ciudad}
                      onChange={(e) => setCiudad(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                </div>

                <div className="pt-4 border-t border-[var(--border-subtle)] flex items-center justify-end gap-3">
                  <button
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-4 py-2 border border-[var(--border-subtle)] text-xs font-mono uppercase tracking-wider text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-colors"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] text-xs font-sans-editorial font-semibold uppercase tracking-wider transition-colors"
                  >
                    Registrar Boutique
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