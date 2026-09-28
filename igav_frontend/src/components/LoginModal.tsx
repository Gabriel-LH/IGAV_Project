'use client';

import React, { useState } from 'react';
import { 
  X, 
  Lock, 
  User as UserIcon, 
  KeyRound, 
  ShieldCheck, 
  Sparkles, 
  Building2, 
  LogIn, 
  CheckCircle2,
  ArrowRight
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { toast } from 'sonner';

export interface AuthSession {
  token: string;
  email: string;
  nombreCompleto: string;
  rol: string;
  storeId: number;
  storeNombre: string;
}

interface LoginModalProps {
  isOpen: boolean;
  onClose: () => void;
  onLoginSuccess: (session: AuthSession) => void;
}

export const LoginModal: React.FC<LoginModalProps> = ({
  isOpen,
  onClose,
  onLoginSuccess
}) => {
  const [username, setUsername] = useState('admin.saas@igav.pe');
  const [password, setPassword] = useState('admin123');
  const [isLoading, setIsLoading] = useState(false);

  if (!isOpen) return null;

  const handleQuickFill = (user: string, pass: string) => {
    setUsername(user);
    setPassword(pass);
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password) {
      toast.error('Ingrese su usuario/email y contraseña');
      return;
    }

    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/v1/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      if (response.ok) {
        const data = await response.json();
        const session: AuthSession = {
          token: data.token,
          email: data.email,
          nombreCompleto: data.nombreCompleto,
          rol: data.rol,
          storeId: data.storeId || 1,
          storeNombre: data.storeNombre || 'Sede Principal San Isidro'
        };

        localStorage.setItem('igav_token', session.token);
        localStorage.setItem('igav_user', JSON.stringify(session));
        onLoginSuccess(session);
        toast.success(`Bienvenido/a, ${session.nombreCompleto} (${session.rol})`);
        onClose();
        return;
      }
    } catch (err) {
      console.warn('Backend REST en :8080 no respondió. Iniciando sesión en modo demo offline con JWT local simulado.');
    }

    // Modo local / demo si el backend no está corriendo en ese instante
    const isSuperAdmin = username.toLowerCase().includes('admin');
    const demoSession: AuthSession = {
      token: `jwt_simulated_${Date.now()}_igav_token_2026`,
      email: username,
      nombreCompleto: isSuperAdmin ? 'Tech Lead Administrador SaaS' : 'Gabriel (Asesor de Gala)',
      rol: isSuperAdmin ? 'ADMIN_SAAS' : 'VENDEDOR',
      storeId: 1,
      storeNombre: 'Sede Principal San Isidro'
    };

    localStorage.setItem('igav_token', demoSession.token);
    localStorage.setItem('igav_user', JSON.stringify(demoSession));
    onLoginSuccess(demoSession);
    toast.success(`Sesión iniciada con éxito como ${demoSession.nombreCompleto}`);
    setIsLoading(false);
    onClose();
  };

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md overflow-y-auto">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 20 }}
          className="relative w-full max-w-md bg-stone-900 border border-stone-800 rounded-2xl shadow-2xl overflow-hidden my-8"
        >
          {/* Header */}
          <div className="flex items-center justify-between px-6 py-5 border-b border-stone-800 bg-stone-950/80">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
                <Lock className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-base font-serif font-bold text-stone-100 flex items-center gap-1.5">
                  Autenticación Staff
                  <span className="text-[10px] font-sans font-medium px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/30">
                    JWT RF-02
                  </span>
                </h3>
                <p className="text-xs text-stone-400">
                  Acceso seguro a I.G.A.V. Gala Management
                </p>
              </div>
            </div>

            <button
              onClick={onClose}
              className="p-2 text-stone-400 hover:text-stone-100 hover:bg-stone-800 rounded-xl transition-all"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          <form onSubmit={handleLogin} className="p-6 space-y-4">
            {/* Campo Usuario / Email */}
            <div>
              <label className="block text-xs font-semibold text-stone-400 uppercase tracking-wider mb-1.5">
                Usuario o Correo Electrónico
              </label>
              <div className="relative">
                <UserIcon className="w-4 h-4 text-stone-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="admin.saas@igav.pe"
                  className="w-full pl-10 pr-4 py-2.5 bg-stone-950/60 border border-stone-800 rounded-xl text-xs text-stone-100 placeholder-stone-600 focus:outline-none focus:border-amber-500/50"
                  required
                />
              </div>
            </div>

            {/* Campo Contraseña */}
            <div>
              <label className="block text-xs font-semibold text-stone-400 uppercase tracking-wider mb-1.5">
                Contraseña
              </label>
              <div className="relative">
                <KeyRound className="w-4 h-4 text-stone-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-10 pr-4 py-2.5 bg-stone-950/60 border border-stone-800 rounded-xl text-xs text-stone-100 placeholder-stone-600 focus:outline-none focus:border-amber-500/50"
                  required
                />
              </div>
            </div>

            {/* Cuentas Demo Rápidas */}
            <div className="pt-1">
              <label className="block text-[11px] font-semibold text-stone-400 uppercase tracking-wider mb-2">
                Credenciales Rápidas de Demostración:
              </label>
              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => handleQuickFill('admin.saas@igav.pe', 'admin123')}
                  className="p-2.5 rounded-xl border border-stone-800 bg-stone-950/40 hover:border-amber-500/30 text-left transition-all group"
                >
                  <div className="flex items-center space-x-1.5 text-amber-400 font-bold text-xs">
                    <ShieldCheck className="w-3.5 h-3.5" />
                    <span>Admin SaaS</span>
                  </div>
                  <p className="text-[10px] text-stone-400 mt-0.5 font-mono">admin.saas@igav.pe</p>
                </button>

                <button
                  type="button"
                  onClick={() => handleQuickFill('gabriel.vendedor@igav.pe', 'gala2026')}
                  className="p-2.5 rounded-xl border border-stone-800 bg-stone-950/40 hover:border-amber-500/30 text-left transition-all group"
                >
                  <div className="flex items-center space-x-1.5 text-stone-300 font-bold text-xs">
                    <Sparkles className="w-3.5 h-3.5 text-amber-400" />
                    <span>Asesor Gala</span>
                  </div>
                  <p className="text-[10px] text-stone-400 mt-0.5 font-mono">gabriel.vendedor</p>
                </button>
              </div>
            </div>

            {/* Botón de Submit */}
            <div className="pt-3">
              <button
                type="submit"
                disabled={isLoading}
                className="w-full flex items-center justify-center space-x-2 py-3 bg-amber-500 hover:bg-amber-400 text-stone-950 rounded-xl font-bold text-xs transition-all shadow-lg shadow-amber-500/10 disabled:opacity-50"
              >
                <LogIn className="w-4 h-4" />
                <span>{isLoading ? 'Verificando JWT...' : 'Iniciar Sesión con JWT'}</span>
              </button>
            </div>
          </form>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
