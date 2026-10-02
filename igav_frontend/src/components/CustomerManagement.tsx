'use client';

import React, { useState } from 'react';
import { 
  Users, 
  Search, 
  Plus, 
  Star, 
  Phone, 
  Mail, 
  MapPin, 
  X,
  Crown,
  Sparkles,
  ArrowRight,
  ShieldCheck,
  UserCheck
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Customer } from '../lib/types';
import { toast } from 'sonner';

interface CustomerManagementProps {
  customers: Customer[];
  onAddCustomer: (customer: Omit<Customer, 'id' | 'totalAlquileres' | 'calificacion' | 'fechaRegistro'>) => void;
  onSelectCustomerForOrder?: (customer: Customer) => void;
}

export const CustomerManagement: React.FC<CustomerManagementProps> = ({
  customers,
  onAddCustomer,
  onSelectCustomerForOrder
}) => {
  const [search, setSearch] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<string>('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Form State
  const [tipoDocumento, setTipoDocumento] = useState<'DNI' | 'RUC' | 'PASAPORTE' | 'CARNET_EXTRANJERIA'>('DNI');
  const [numeroDocumento, setNumeroDocumento] = useState('');
  const [nombres, setNombres] = useState('');
  const [apellidos, setApellidos] = useState('');
  const [email, setEmail] = useState('');
  const [telefono, setTelefono] = useState('');
  const [direccion, setDireccion] = useState('');
  const [categoriaCliente, setCategoriaCliente] = useState<'VIP' | 'FRECUENTE' | 'REGULAR'>('REGULAR');

  const filteredCustomers = customers.filter((c) => {
    const matchesSearch = c.nombreCompleto.toLowerCase().includes(search.toLowerCase()) || 
                          c.numeroDocumento.includes(search) || 
                          c.email.toLowerCase().includes(search.toLowerCase());
    const matchesCat = categoryFilter === 'ALL' || c.categoriaCliente === categoryFilter;
    return matchesSearch && matchesCat;
  });

  const handleSubmitNewCustomer = (e: React.FormEvent) => {
    e.preventDefault();
    if (!numeroDocumento || !nombres || !apellidos) {
      toast.error('Complete el número de documento y nombres del huésped.');
      return;
    }

    onAddCustomer({
      tipoDocumento,
      numeroDocumento,
      nombres,
      apellidos,
      nombreCompleto: `${nombres} ${apellidos}`,
      email: email || `${nombres.toLowerCase().replace(/\s+/g, '')}@maison.com`,
      telefono: telefono || '+51 900 000 000',
      direccion: direccion || 'Lima, Perú',
      categoriaCliente
    });

    setIsModalOpen(false);
    toast.success(`Huésped "${nombres} ${apellidos}" incorporado al Directorio.`);

    // Reset
    setNumeroDocumento('');
    setNombres('');
    setApellidos('');
    setEmail('');
    setTelefono('');
    setDireccion('');
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-gold)] font-medium">
              Directorio de Huéspedes
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Círculo de Gala & Clientes Distinguidos
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Registro confidencial de huéspedes, historial de veladas, categorización de cortesía y fichas de entallado.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsModalOpen(true)}
          className="px-5 py-2.5 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-all shadow-sm flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Registrar Nuevo Huésped</span>
        </motion.button>
      </div>

      {/* Metrics Bar */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 hover:border-[var(--accent-gold)] transition-colors">
          <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
            Huéspedes en Registro
          </span>
          <h3 className="font-serif-editorial text-3xl text-[var(--text-primary)] font-light mt-1">
            {customers.length}
          </h3>
          <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial mt-1">
            Directorio activo del atelier
          </p>
        </div>

        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 hover:border-[var(--accent-gold)] transition-colors">
          <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
            Círculo de Honor VIP
          </span>
          <h3 className="font-serif-editorial text-3xl text-[var(--accent-gold)] font-light mt-1">
            {customers.filter(c => c.categoriaCliente === 'VIP' || c.categoriaCliente === 'FRECUENTE').length}
          </h3>
          <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial mt-1">
            Atención prioritaria y entalle exclusivo
          </p>
        </div>

        <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 hover:border-[var(--accent-sage)] transition-colors">
          <span className="text-[10px] font-mono tracking-[0.18em] uppercase text-[var(--text-tertiary)] block">
            Con Veladas Realizadas
          </span>
          <h3 className="font-serif-editorial text-3xl text-[var(--accent-sage)] font-light mt-1">
            {customers.filter(c => c.totalAlquileres > 0).length}
          </h3>
          <p className="text-[11px] text-[var(--text-secondary)] font-sans-editorial mt-1">
            Historial de alquileres verificado
          </p>
        </div>
      </div>

      {/* Search and Category Filter */}
      <div className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <div className="relative w-full md:w-96">
          <Search className="w-4 h-4 text-[var(--text-tertiary)] absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Buscar por nombre, documento o correo..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] placeholder-[var(--text-tertiary)] focus:outline-none focus:border-[var(--accent-gold)] transition-colors"
          />
        </div>

        {/* Filter Pills */}
        <div className="flex items-center gap-1.5 overflow-x-auto w-full md:w-auto pb-1 md:pb-0">
          {[
            { id: 'ALL', label: 'Todos' },
            { id: 'VIP', label: 'Círculo VIP' },
            { id: 'FRECUENTE', label: 'Frecuentes' },
            { id: 'REGULAR', label: 'Regulares' }
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setCategoryFilter(tab.id)}
              className={`px-3 py-1.5 text-xs font-mono tracking-wider uppercase transition-colors shrink-0 ${
                categoryFilter === tab.id
                  ? 'bg-[var(--accent-gold)] text-[#1A1817] font-semibold'
                  : 'text-[var(--text-secondary)] hover:text-[var(--text-primary)] border border-transparent hover:border-[var(--border-subtle)]'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      {/* Customer Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filteredCustomers.map((customer) => {
          const initials = customer.nombreCompleto
            .split(' ')
            .map(n => n[0])
            .slice(0, 2)
            .join('');

          return (
            <div
              key={customer.id}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] p-5 hover:border-[var(--accent-gold)] transition-all duration-300 flex flex-col justify-between group space-y-4"
            >
              <div className="space-y-3">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-11 h-11 border border-[var(--accent-gold)]/40 bg-[var(--accent-gold-light)] flex items-center justify-center font-serif-editorial text-sm font-semibold text-[var(--accent-gold)] tracking-wider">
                      {initials}
                    </div>
                    <div>
                      <h4 className="font-serif-editorial text-base text-[var(--text-primary)] font-medium group-hover:text-[var(--accent-gold)] transition-colors">
                        {customer.nombreCompleto}
                      </h4>
                      <p className="text-[10px] font-mono text-[var(--text-tertiary)] uppercase tracking-wider">
                        {customer.tipoDocumento}: {customer.numeroDocumento}
                      </p>
                    </div>
                  </div>

                  <span className={`text-[9px] font-mono tracking-widest uppercase px-2 py-0.5 border ${
                    customer.categoriaCliente === 'VIP'
                      ? 'bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border-[var(--accent-gold)]/40'
                      : customer.categoriaCliente === 'FRECUENTE'
                      ? 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/40'
                      : 'bg-[var(--surface-elevated)] text-[var(--text-secondary)] border-[var(--border-subtle)]'
                  }`}>
                    {customer.categoriaCliente === 'VIP' ? 'Círculo VIP' : customer.categoriaCliente === 'FRECUENTE' ? 'Frecuente' : 'Huésped'}
                  </span>
                </div>

                <div className="space-y-1.5 pt-2 border-t border-[var(--border-subtle)] text-xs font-sans-editorial text-[var(--text-secondary)]">
                  <div className="flex items-center gap-2">
                    <Mail className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span className="truncate">{customer.email}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Phone className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span>{customer.telefono}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <MapPin className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span className="truncate">{customer.direccion || 'Lima, Perú'}</span>
                  </div>
                </div>
              </div>

              {/* Bottom Meta & Actions */}
              <div className="pt-3 border-t border-[var(--border-subtle)] flex items-center justify-between">
                <span className="text-[10px] font-mono text-[var(--text-tertiary)]">
                  {customer.totalAlquileres} veladas realizadas
                </span>

                {onSelectCustomerForOrder && (
                  <motion.button
                    whileTap={{ scale: 0.96 }}
                    onClick={() => onSelectCustomerForOrder(customer)}
                    className="text-xs font-mono uppercase tracking-wider text-[var(--accent-gold)] hover:text-[var(--accent-gold-hover)] flex items-center gap-1"
                  >
                    <span>Iniciar Contrato</span>
                    <ArrowRight className="w-3 h-3" />
                  </motion.button>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* New Customer Modal */}
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
                    Inscripción en Directorio
                  </span>
                  <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
                    Ficha de Nuevo Huésped
                  </h3>
                </div>
                <button
                  onClick={() => setIsModalOpen(false)}
                  className="p-1 text-[var(--text-tertiary)] hover:text-[var(--text-primary)] transition-colors"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <form onSubmit={handleSubmitNewCustomer} className="space-y-4">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Tipo Documento
                    </label>
                    <select
                      value={tipoDocumento}
                      onChange={(e: any) => setTipoDocumento(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    >
                      <option value="DNI">DNI (Perú)</option>
                      <option value="PASAPORTE">Pasaporte</option>
                      <option value="CARNET_EXTRANJERIA">Carnet de Extranjería</option>
                      <option value="RUC">RUC</option>
                    </select>
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Número Documento
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="78901234"
                      value={numeroDocumento}
                      onChange={(e) => setNumeroDocumento(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Nombres
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="Constanza"
                      value={nombres}
                      onChange={(e) => setNombres(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Apellidos
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="De Almenara"
                      value={apellidos}
                      onChange={(e) => setApellidos(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Teléfono Móvil
                    </label>
                    <input
                      type="text"
                      placeholder="+51 987 654 321"
                      value={telefono}
                      onChange={(e) => setTelefono(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Categoría de Cortesía
                    </label>
                    <select
                      value={categoriaCliente}
                      onChange={(e: any) => setCategoriaCliente(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    >
                      <option value="REGULAR">Huésped Estándar</option>
                      <option value="FRECUENTE">Huésped Frecuente</option>
                      <option value="VIP">Círculo de Honor VIP</option>
                    </select>
                  </div>
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Correo Electrónico
                  </label>
                  <input
                    type="email"
                    placeholder="constanza@maison.pe"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                    Dirección Residencial
                  </label>
                  <input
                    type="text"
                    placeholder="Av. Los Eucaliptos 450, San Isidro"
                    value={direccion}
                    onChange={(e) => setDireccion(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
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
                    Guardar Huésped
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