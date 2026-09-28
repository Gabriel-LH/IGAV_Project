'use client';

import React, { useState } from 'react';
import { 
  Users, 
  Search, 
  Plus, 
  UserCheck, 
  Star, 
  Phone, 
  Mail, 
  MapPin, 
  FileText, 
  X,
  Sparkles,
  ShieldCheck
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
      toast.error('Complete el número de documento y nombres completos del cliente');
      return;
    }

    onAddCustomer({
      tipoDocumento,
      numeroDocumento,
      nombres,
      apellidos,
      nombreCompleto: `${nombres} ${apellidos}`,
      email: email || `${nombres.toLowerCase().replace(/\s+/g, '')}@gmail.com`,
      telefono: telefono || '+51 900 000 000',
      direccion: direccion || 'Av. Principal San Isidro',
      categoriaCliente
    });

    setIsModalOpen(false);
    toast.success(`Cliente "${nombres} ${apellidos}" registrado en el Maestro de Clientes.`);

    // Reset
    setNumeroDocumento('');
    setNombres('');
    setApellidos('');
    setEmail('');
    setTelefono('');
    setDireccion('');
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <Users className="w-5 h-5 text-amber-700 dark:text-amber-400" />
            Maestro de Clientes & Afiliados de Gala
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Registro unificado de clientes, documento de identidad, historial de alquileres y perfil de riesgo.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Registrar Nuevo Cliente
        </motion.button>
      </div>

      {/* Stats Bar */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="glass-card rounded-xl p-4 border border-[var(--glass-border)] flex items-center justify-between">
          <div>
            <p className="text-[10px] uppercase font-bold text-[var(--text-secondary)]">Clientes Totales</p>
            <h3 className="text-2xl font-extrabold text-[var(--text-primary)] mt-1">{customers.length}</h3>
          </div>
          <div className="w-10 h-10 rounded-lg bg-amber-500/10 text-amber-700 dark:text-amber-400 border border-amber-500/20 flex items-center justify-center">
            <Users className="w-5 h-5" />
          </div>
        </div>

        <div className="glass-card rounded-xl p-4 border border-[var(--glass-border)] flex items-center justify-between">
          <div>
            <p className="text-[10px] uppercase font-bold text-[var(--text-secondary)]">Clientes VIP & Frecuentes</p>
            <h3 className="text-2xl font-extrabold text-amber-800 dark:text-amber-300 mt-1">
              {customers.filter(c => c.categoriaCliente === 'VIP' || c.categoriaCliente === 'FRECUENTE').length}
            </h3>
          </div>
          <div className="w-10 h-10 rounded-lg bg-emerald-500/10 text-emerald-700 dark:text-emerald-400 border border-emerald-500/20 flex items-center justify-center">
            <Star className="w-5 h-5 fill-emerald-400/20" />
          </div>
        </div>

        <div className="glass-card rounded-xl p-4 border border-[var(--glass-border)] flex items-center justify-between">
          <div>
            <p className="text-[10px] uppercase font-bold text-[var(--text-secondary)]">Con Historial de Alquiler</p>
            <h3 className="text-2xl font-extrabold text-purple-800 dark:text-purple-300 mt-1">
              {customers.filter(c => c.totalAlquileres > 0).length}
            </h3>
          </div>
          <div className="w-10 h-10 rounded-lg bg-purple-500/10 text-purple-700 dark:text-purple-400 border border-purple-500/20 flex items-center justify-center">
            <ShieldCheck className="w-5 h-5" />
          </div>
        </div>
      </div>

      {/* Filter Bar */}
      <div className="glass-panel rounded-xl p-4 border border-[var(--glass-border)] flex flex-col md:flex-row items-center justify-between gap-4">
        {/* Search */}
        <div className="relative w-full md:w-80">
          <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[var(--text-secondary)]" />
          <input
            type="text"
            placeholder="Buscar por DNI/RUC, Nombre o Email..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs text-[var(--text-primary)] placeholder-[var(--text-secondary)] focus:outline-none focus:border-amber-500/50"
          />
        </div>

        {/* Category Filters */}
        <div className="flex flex-wrap items-center gap-1.5 w-full md:w-auto">
          {[
            { id: 'ALL', label: 'Todos' },
            { id: 'VIP', label: '⭐ VIP' },
            { id: 'FRECUENTE', label: 'Frecuentes' },
            { id: 'REGULAR', label: 'Regulares' }
          ].map((btn) => (
            <motion.button whileTap={{ scale: 0.96 }}
              key={btn.id}
              onClick={() => setCategoryFilter(btn.id)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                categoryFilter === btn.id
                  ? 'bg-amber-500 text-black'
                  : 'bg-[var(--glass-bg)] text-[var(--text-secondary)] hover:bg-[var(--card-bg-hover)] border border-[var(--border-color)]'
              }`}
            >
              {btn.label}
            </motion.button>
          ))}
        </div>
      </div>

      {/* Customers Table */}
      <div className="glass-panel rounded-2xl border border-[var(--glass-border)] overflow-hidden">
        <div className="p-4 border-b border-[var(--glass-border)] flex items-center justify-between">
          <h3 className="font-bold text-[var(--text-primary)] text-sm">Directorio de Clientes Registrados</h3>
          <span className="text-xs text-amber-700 dark:text-amber-400 font-mono font-bold">{filteredCustomers.length} Clientes</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-[var(--glass-bg)] border-b border-[var(--glass-border)] text-[var(--text-secondary)] uppercase font-semibold text-[10px] tracking-wider">
                <th className="p-3.5">Documento</th>
                <th className="p-3.5">Cliente</th>
                <th className="p-3.5">Contacto</th>
                <th className="p-3.5">Dirección</th>
                <th className="p-3.5 text-center">Categoría</th>
                <th className="p-3.5 text-center">Alquileres</th>
                <th className="p-3.5 text-center">Calificación</th>
                <th className="p-3.5 text-right">Acción</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {filteredCustomers.map((customer) => (
                <tr key={customer.id} className="hover:bg-[var(--glass-bg)] transition-colors">
                  <td className="p-3.5 font-mono font-bold text-amber-800 dark:text-amber-300">
                    <span className="text-[10px] text-[var(--text-secondary)] block">{customer.tipoDocumento}</span>
                    {customer.numeroDocumento}
                  </td>
                  <td className="p-3.5 font-semibold text-[var(--text-primary)]">
                    {customer.nombreCompleto}
                  </td>
                  <td className="p-3.5 text-[var(--text-secondary)]">
                    <div className="flex items-center gap-1.5 text-[var(--text-secondary)]">
                      <Mail className="w-3 h-3 text-amber-700 dark:text-amber-400" />
                      <span>{customer.email}</span>
                    </div>
                    <div className="flex items-center gap-1.5 text-[var(--text-secondary)] text-[11px] mt-0.5">
                      <Phone className="w-3 h-3 text-emerald-700 dark:text-emerald-400" />
                      <span>{customer.telefono}</span>
                    </div>
                  </td>
                  <td className="p-3.5 text-[var(--text-secondary)] max-w-xs truncate">
                    <div className="flex items-center gap-1">
                      <MapPin className="w-3 h-3 text-[var(--text-secondary)] shrink-0" />
                      <span>{customer.direccion}</span>
                    </div>
                  </td>
                  <td className="p-3.5 text-center">
                    <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold border ${
                      customer.categoriaCliente === 'VIP' ? 'bg-amber-500/20 text-amber-800 dark:text-amber-300 border-amber-500/40' :
                      customer.categoriaCliente === 'FRECUENTE' ? 'bg-emerald-500/20 text-emerald-800 dark:text-emerald-300 border-emerald-500/40' :
                      'bg-gray-500/20 text-[var(--text-secondary)] border-gray-500/40'
                    }`}>
                      {customer.categoriaCliente}
                    </span>
                  </td>
                  <td className="p-3.5 text-center font-bold text-[var(--text-primary)]">
                    {customer.totalAlquileres}
                  </td>
                  <td className="p-3.5 text-center">
                    <div className="flex items-center justify-center gap-1 text-amber-700 dark:text-amber-400 font-bold">
                      <Star className="w-3.5 h-3.5 fill-amber-400" />
                      <span>{customer.calificacion}</span>
                    </div>
                  </td>
                  <td className="p-3.5 text-right">
                    {onSelectCustomerForOrder && (
                      <motion.button whileTap={{ scale: 0.96 }} onClick={() => onSelectCustomerForOrder(customer)}
                        className="px-2.5 py-1 rounded-md bg-amber-500/10 hover:bg-amber-500/20 border border-amber-500/30 text-amber-800 dark:text-amber-300 font-semibold text-[11px]"
                      >
                        Crear Contrato →
                      </motion.button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* New Customer Modal */}
      <AnimatePresence>
        {isModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md">
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="glass-panel border border-[var(--border-color)] rounded-2xl w-full max-w-xl p-6 relative max-h-[90vh] overflow-y-auto space-y-5"
            >
              <div className="flex items-center justify-between border-b border-[var(--glass-border)] pb-4">
                <div className="flex items-center gap-2">
                  <UserCheck className="w-5 h-5 text-amber-700 dark:text-amber-400" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Registrar Cliente en Maestro</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsModalOpen(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleSubmitNewCustomer} className="space-y-4 text-xs">
                <div className="grid grid-cols-3 gap-3">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Tipo Documento</label>
                    <select
                      value={tipoDocumento}
                      onChange={(e: any) => setTipoDocumento(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    >
                      <option value="DNI">DNI</option>
                      <option value="RUC">RUC</option>
                      <option value="PASAPORTE">PASAPORTE</option>
                      <option value="CARNET_EXTRANJERIA">CARNET EXTRANJERÍA</option>
                    </select>
                  </div>
                  <div className="col-span-2">
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Número de Documento *</label>
                    <input
                      type="text"
                      required
                      placeholder="72839104"
                      value={numeroDocumento}
                      onChange={(e) => setNumeroDocumento(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] font-mono"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Nombres *</label>
                    <input
                      type="text"
                      required
                      placeholder="Mariana Sofia"
                      value={nombres}
                      onChange={(e) => setNombres(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Apellidos *</label>
                    <input
                      type="text"
                      required
                      placeholder="Valdivia Pastor"
                      value={apellidos}
                      onChange={(e) => setApellidos(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Email *</label>
                    <input
                      type="email"
                      required
                      placeholder="cliente@gala.pe"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                  <div>
                    <label className="block font-semibold text-[var(--text-secondary)] mb-1">Teléfono Móvil *</label>
                    <input
                      type="text"
                      required
                      placeholder="+51 987 654 321"
                      value={telefono}
                      onChange={(e) => setTelefono(e.target.value)}
                      className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Dirección de Domicilio / Residencia</label>
                  <input
                    type="text"
                    placeholder="Av. Los Conquistadores 450, San Isidro"
                    value={direccion}
                    onChange={(e) => setDireccion(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Categoría del Cliente</label>
                  <select
                    value={categoriaCliente}
                    onChange={(e: any) => setCategoriaCliente(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  >
                    <option value="REGULAR">REGULAR - Cliente nuevo / estándar</option>
                    <option value="FRECUENTE">FRECUENTE - Más de 3 alquileres exitosos</option>
                    <option value="VIP">⭐ VIP - Alta preferencia y depósitos preferenciales</option>
                  </select>
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
                    Guardar Cliente
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
