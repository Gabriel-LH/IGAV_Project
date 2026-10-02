'use client';

import React, { useState } from 'react';
import { Shield, UserPlus, Key, UserCheck, Mail, Building2, CheckCircle2, XCircle, Plus, X, Crown, Scissors, Clock } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { User, UserRole, Store } from '../lib/types';
import { toast } from 'sonner';

interface UserManagementProps {
  users: User[];
  stores: Store[];
  onAddUser: (user: Omit<User, 'id'>) => void;
  onToggleUserStatus: (userId: number) => void;
}

export const UserManagement: React.FC<UserManagementProps> = ({
  users,
  stores,
  onAddUser,
  onToggleUserStatus
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [username, setUsername] = useState('');
  const [nombreCompleto, setNombreCompleto] = useState('');
  const [email, setEmail] = useState('');
  const [rol, setRol] = useState<UserRole>('VENDEDOR');
  const [storeId, setStoreId] = useState<number>(stores[0]?.id || 1);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !nombreCompleto || !email) {
      toast.error('Complete el identificador, nombre completo y correo.');
      return;
    }

    const assignedStore = stores.find((s) => s.id === Number(storeId));

    onAddUser({
      username,
      nombreCompleto,
      email,
      rol,
      storeId: Number(storeId),
      storeNombre: assignedStore?.nombre || 'Sede Principal',
      activo: true
    });

    setIsModalOpen(false);
    toast.success(`Miembro de staff "${nombreCompleto}" asignado con rol ${rol}.`);

    // Reset
    setUsername('');
    setNombreCompleto('');
    setEmail('');
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 pb-6 border-b border-[var(--border-subtle)]">
        <div className="space-y-1">
          <div className="flex items-center gap-2">
            <span className="w-1.5 h-1.5 rounded-full bg-[var(--accent-gold)]" />
            <span className="text-[10px] font-mono tracking-[0.24em] uppercase text-[var(--accent-gold)] font-medium">
              Equipo de Atelier & Dirección
            </span>
          </div>
          <h2 className="font-serif-editorial text-2xl sm:text-3xl text-[var(--text-primary)] font-light">
            Maestros, Conserjes & Roles de Salón
          </h2>
          <p className="text-xs text-[var(--text-secondary)] font-sans-editorial max-w-xl">
            Control de accesos y credenciales confidenciales. Asignación de directoras, asesores de alta costura, maestros de sastrería y conservadores de fibras.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.98 }}
          onClick={() => setIsModalOpen(true)}
          className="px-5 py-2.5 bg-[var(--accent-gold)] hover:bg-[var(--accent-gold-hover)] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-all shadow-sm flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>Asignar Miembro de Staff</span>
        </motion.button>
      </div>

      {/* Staff Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {users.map((user) => {
          const isDirector = user.rol.includes('ADMIN');
          const isCraft = user.rol.includes('TINTORERIA');

          return (
            <div
              key={user.id}
              className="bg-[var(--surface-card)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] p-5 transition-all duration-300 space-y-4 flex flex-col justify-between"
            >
              <div className="space-y-3">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 border border-[var(--border-subtle)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--accent-gold)]">
                      {isDirector ? <Crown className="w-4 h-4" /> : isCraft ? <Clock className="w-4 h-4" /> : <Shield className="w-4 h-4" />}
                    </div>
                    <div>
                      <h4 className="font-serif-editorial text-base text-[var(--text-primary)] font-medium">
                        {user.nombreCompleto}
                      </h4>
                      <p className="text-[10px] font-mono text-[var(--text-tertiary)]">
                        @{user.username}
                      </p>
                    </div>
                  </div>

                  <span className={`text-[9px] font-mono tracking-widest uppercase px-2 py-0.5 border ${
                    user.activo
                      ? 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/40'
                      : 'bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/40'
                  }`}>
                    {user.activo ? 'Activo' : 'Pausado'}
                  </span>
                </div>

                <div className="space-y-1.5 pt-2 border-t border-[var(--border-subtle)] text-xs font-sans-editorial text-[var(--text-secondary)]">
                  <div className="flex items-center gap-2">
                    <Mail className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span className="truncate">{user.email}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Building2 className="w-3.5 h-3.5 text-[var(--text-tertiary)] shrink-0" />
                    <span className="truncate">{user.storeNombre || 'Sede Principal'}</span>
                  </div>
                </div>
              </div>

              <div className="pt-3 border-t border-[var(--border-subtle)] flex items-center justify-between">
                <span className="text-[10px] font-mono tracking-wider uppercase text-[var(--accent-gold)]">
                  {user.rol.replace('_', ' ')}
                </span>

                <motion.button
                  whileTap={{ scale: 0.96 }}
                  onClick={() => onToggleUserStatus(user.id)}
                  className="text-[11px] font-mono uppercase tracking-wider text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-colors"
                >
                  {user.activo ? 'Suspender' : 'Activar'}
                </motion.button>
              </div>
            </div>
          );
        })}
      </div>

      {/* New User Modal */}
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
                    Directorio de Staff
                  </span>
                  <h3 className="font-serif-editorial text-xl text-[var(--text-primary)]">
                    Alta de Miembro de Atelier
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
                    Nombre Completo
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Genevieve Laroche"
                    value={nombreCompleto}
                    onChange={(e) => setNombreCompleto(e.target.value)}
                    className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Identificador de Usuario
                    </label>
                    <input
                      type="text"
                      required
                      placeholder="genevieve.couture"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] font-mono focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Rol de Salón
                    </label>
                    <select
                      value={rol}
                      onChange={(e: any) => setRol(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    >
                      <option value="SUPER_ADMIN">Directora General (Super Admin)</option>
                      <option value="ADMIN_SAAS">Administración Central</option>
                      <option value="ADMIN_TIENDA">Directora de Boutique</option>
                      <option value="VENDEDOR">Asesora de Alta Costura</option>
                      <option value="ENCARGADO_TINTORERIA">Conservador de Fibras (Tintorería)</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Correo Electrónico
                    </label>
                    <input
                      type="email"
                      required
                      placeholder="genevieve@igav.pe"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    />
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-wider uppercase text-[var(--text-secondary)]">
                      Sede Asignada
                    </label>
                    <select
                      value={storeId}
                      onChange={(e) => setStoreId(Number(e.target.value))}
                      className="w-full px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs text-[var(--text-primary)] focus:outline-none focus:border-[var(--accent-gold)]"
                    >
                      {stores.map((s) => (
                        <option key={s.id} value={s.id}>{s.nombre}</option>
                      ))}
                    </select>
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
                    Asignar Credenciales
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