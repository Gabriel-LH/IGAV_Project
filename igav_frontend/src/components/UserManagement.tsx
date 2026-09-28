'use client';

import React, { useState } from 'react';
import { Shield, UserPlus, Key, UserCheck, Mail, Building2, CheckCircle2, XCircle, Plus, X } from 'lucide-react';
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
      toast.error('Complete el usuario, nombre completo y correo electrónico');
      return;
    }

    const assignedStore = stores.find((s) => s.id === Number(storeId));

    onAddUser({
      username,
      nombreCompleto,
      email,
      rol,
      storeId: Number(storeId),
      storeNombre: assignedStore?.nombre || 'Sede Central',
      activo: true
    });

    setIsModalOpen(false);
    toast.success(`Usuario de Staff "${username}" creado con Rol ${rol} (Seguridad RF-02).`);

    // Reset
    setUsername('');
    setNombreCompleto('');
    setEmail('');
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-[var(--text-primary)] flex items-center gap-2">
            <Shield className="w-5 h-5 text-amber-700 dark:text-amber-400" />
            Maestro de Usuarios, Staff & Control de Roles (RF-02)
          </h2>
          <p className="text-xs text-[var(--text-secondary)]">
            Asignación de credenciales, encriptación BCrypt, roles por módulo y contexto por sede.
          </p>
        </div>

        <motion.button
          whileTap={{ scale: 0.95 }}
          onClick={() => setIsModalOpen(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-black font-bold text-sm transition-all shadow-lg shadow-amber-500/20 flex items-center gap-2 self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          Crear Usuario de Staff
        </motion.button>
      </div>

      {/* Users Table */}
      <div className="glass-panel rounded-2xl border border-[var(--glass-border)] overflow-hidden">
        <div className="p-4 border-b border-[var(--glass-border)] flex items-center justify-between">
          <h3 className="font-bold text-[var(--text-primary)] text-sm">Personal Registrado en la Plataforma</h3>
          <span className="text-xs text-amber-700 dark:text-amber-400 font-mono font-bold">{users.length} Usuarios Activos</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-[var(--glass-bg)] border-b border-[var(--glass-border)] text-[var(--text-secondary)] uppercase font-semibold text-[10px] tracking-wider">
                <th className="p-3.5">Usuario / Username</th>
                <th className="p-3.5">Nombre Completo</th>
                <th className="p-3.5">Email</th>
                <th className="p-3.5">Rol de Seguridad</th>
                <th className="p-3.5">Sede Asignada</th>
                <th className="p-3.5 text-center">Estado</th>
                <th className="p-3.5 text-right">Acción</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {users.map((user) => (
                <tr key={user.id} className="hover:bg-[var(--glass-bg)] transition-colors">
                  <td className="p-3.5 font-mono font-bold text-amber-800 dark:text-amber-300">
                    @{user.username}
                  </td>
                  <td className="p-3.5 font-semibold text-[var(--text-primary)]">
                    {user.nombreCompleto}
                  </td>
                  <td className="p-3.5 text-[var(--text-secondary)]">
                    <div className="flex items-center gap-1.5">
                      <Mail className="w-3 h-3 text-amber-700 dark:text-amber-400" />
                      <span>{user.email}</span>
                    </div>
                  </td>
                  <td className="p-3.5">
                    <span className={`px-2.5 py-1 rounded-full text-[10px] font-bold border ${
                      user.rol === 'ADMIN_SAAS' ? 'bg-purple-500/20 text-purple-800 dark:text-purple-300 border-purple-500/40' :
                      user.rol === 'ADMIN_STORE' ? 'bg-amber-500/20 text-amber-800 dark:text-amber-300 border-amber-500/40' :
                      user.rol === 'VENDEDOR' ? 'bg-emerald-500/20 text-emerald-800 dark:text-emerald-300 border-emerald-500/40' :
                      'bg-blue-500/20 text-blue-300 border-blue-500/40'
                    }`}>
                      {user.rol}
                    </span>
                  </td>
                  <td className="p-3.5 text-[var(--text-secondary)]">
                    <div className="flex items-center gap-1.5 text-[var(--text-secondary)]">
                      <Building2 className="w-3 h-3 text-amber-700 dark:text-amber-400" />
                      <span>{user.storeNombre || 'Sede Central'}</span>
                    </div>
                  </td>
                  <td className="p-3.5 text-center">
                    {user.activo ? (
                      <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-emerald-500/10 text-emerald-700 dark:text-emerald-400 border border-emerald-500/30">
                        <CheckCircle2 className="w-3 h-3 text-emerald-700 dark:text-emerald-400" />
                        Activo
                      </span>
                    ) : (
                      <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-rose-500/10 text-rose-700 dark:text-rose-400 border border-rose-500/30">
                        <XCircle className="w-3 h-3 text-rose-700 dark:text-rose-400" />
                        Inactivo
                      </span>
                    )}
                  </td>
                  <td className="p-3.5 text-right">
                    <motion.button whileTap={{ scale: 0.96 }} onClick={() => {
                        onToggleUserStatus(user.id);
                        toast.info(`Estado del usuario @${user.username} actualizado.`);
                      }}
                      className="px-2.5 py-1 rounded-md bg-[var(--glass-bg)] hover:bg-[var(--card-bg-hover)] border border-[var(--glass-border)] text-[var(--text-secondary)] text-[11px]"
                    >
                      {user.activo ? 'Desactivar' : 'Activar'}
                    </motion.button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* New User Modal */}
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
                  <UserPlus className="w-5 h-5 text-amber-700 dark:text-amber-400" />
                  <h3 className="text-lg font-bold text-[var(--text-primary)]">Nuevo Usuario de Staff</h3>
                </div>
                <motion.button whileTap={{ scale: 0.96 }} onClick={() => setIsModalOpen(false)}
                  className="p-1 rounded-lg text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg-hover)]"
                >
                  <X className="w-5 h-5" />
                </motion.button>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4 text-xs">
                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Nombre de Usuario (Username) *</label>
                  <input
                    type="text"
                    required
                    placeholder="ej. lucia.vendedora"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)] font-mono"
                  />
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Nombre Completo del Empleado *</label>
                  <input
                    type="text"
                    required
                    placeholder="Lucía Fernández Vega"
                    value={nombreCompleto}
                    onChange={(e) => setNombreCompleto(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Correo Electrónico Corporativo *</label>
                  <input
                    type="email"
                    required
                    placeholder="lfernandez@gala.pe"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  />
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Rol y Permisos de Seguridad (RF-02)</label>
                  <select
                    value={rol}
                    onChange={(e: any) => setRol(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  >
                    <option value="ADMIN_SAAS">ADMIN_SAAS - Acceso Total Multi-tenant</option>
                    <option value="ADMIN_STORE">ADMIN_STORE - Administrador de Sede Especifica</option>
                    <option value="VENDEDOR">VENDEDOR - Contratos de Alquiler y Registro Clientes</option>
                    <option value="ENCARGADO_TINTORERIA">ENCARGADO_TINTORERIA - Recepción e Inspección</option>
                  </select>
                </div>

                <div>
                  <label className="block font-semibold text-[var(--text-secondary)] mb-1">Sede Asignada</label>
                  <select
                    value={storeId}
                    onChange={(e) => setStoreId(Number(e.target.value))}
                    className="w-full px-3 py-2 rounded-lg bg-[var(--card-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                  >
                    {stores.map((s) => (
                      <option key={s.id} value={s.id}>
                        {s.nombre} ({s.ciudad})
                      </option>
                    ))}
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
                    Crear Usuario
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
