'use client';

import React, { useState } from 'react';
import { 
  Lock, 
  User as UserIcon, 
  KeyRound, 
  Sparkles, 
  LogIn, 
  Eye, 
  EyeOff, 
  Crown,
  Feather,
  Scissors,
  ArrowRight
} from 'lucide-react';
import { motion } from 'framer-motion';
import { toast } from 'sonner';

export interface AuthSession {
  token: string;
  email: string;
  nombreCompleto: string;
  rol: string;
  storeId: number;
  storeNombre: string;
}

interface LoginScreenProps {
  onLoginSuccess: (session: AuthSession) => void;
}

export const LoginScreen: React.FC<LoginScreenProps> = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('admin.saas@igav.pe');
  const [password, setPassword] = useState('admin123');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleQuickFill = (user: string, pass: string, roleName: string) => {
    setUsername(user);
    setPassword(pass);
    toast.info(`Credenciales cargadas: ${roleName}`);
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password) {
      toast.error('Ingrese su usuario o correo y contraseña.');
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
          storeNombre: data.storeNombre || 'Sede Central San Isidro'
        };

        localStorage.setItem('igav_token', session.token);
        localStorage.setItem('igav_user', JSON.stringify(session));
        onLoginSuccess(session);
        toast.success(`Bienvenido/a al Salón Privado, ${session.nombreCompleto}`, {
          description: `Acceso concedido — ${session.rol}`
        });
        return;
      } else {
        const errData = await response.json().catch(() => ({}));
        toast.error(errData.message || 'Credenciales no reconocidas en el archivo.');
      }
    } catch (err) {
      console.warn('API error en login. Aplicando fallback de sesión demostrativa.');
      // Local fallback for smooth testing if backend is temporarily unreachable
      const fallbackSession: AuthSession = {
        token: 'demo-token-luxury',
        email: username,
        nombreCompleto: username.includes('admin') ? 'Directora de Atelier' : 'Asesora de Gala',
        rol: username.includes('admin') ? 'SUPER_ADMIN' : 'VENDEDOR',
        storeId: 1,
        storeNombre: 'Sede Principal San Isidro'
      };
      localStorage.setItem('igav_token', fallbackSession.token);
      localStorage.setItem('igav_user', JSON.stringify(fallbackSession));
      onLoginSuccess(fallbackSession);
      toast.success(`Acceso al Salón Privado concedido`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-[#151413] text-[#EDE9E1] relative overflow-hidden px-4 py-12">
      {/* Subtle Warm Atmospheric Glow */}
      <div className="absolute top-1/4 left-1/3 w-96 h-96 bg-[var(--accent-gold)]/5 rounded-full blur-[140px] pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/3 w-80 h-80 bg-[var(--accent-burgundy)]/5 rounded-full blur-[120px] pointer-events-none" />

      <motion.div
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: 'easeOut' }}
        className="w-full max-w-md relative z-10 space-y-8"
      >
        {/* Brand Emblem */}
        <div className="text-center space-y-3">
          <div className="inline-flex items-center justify-center w-14 h-14 border border-[#C4A47C]/40 bg-[#1E1C1A] text-[#C4A47C] font-serif-editorial text-2xl font-semibold tracking-widest shadow-lg">
            IG
          </div>
          
          <div className="space-y-1">
            <span className="text-[9px] font-mono tracking-[0.28em] uppercase text-[#C4A47C] block">
              Maison I.G.A.V. Paris & Lima
            </span>
            <h1 className="font-serif-editorial text-3xl sm:text-4xl text-[#EDE9E1] font-light tracking-tight">
              Salón Privado & Atelier
            </h1>
            <p className="text-xs text-[#968F85] font-sans-editorial">
              Plataforma de alta costura, custodia textil y gestión de veladas
            </p>
          </div>
        </div>

        {/* Login Box */}
        <div className="bg-[#1A1817] border border-[#2A2724] p-6 sm:p-8 space-y-6 shadow-2xl">
          <form onSubmit={handleLogin} className="space-y-4">
            <div className="space-y-1.5">
              <label className="text-[10px] font-mono tracking-wider uppercase text-[#968F85]">
                Identificador / Correo de Atelier
              </label>
              <div className="relative">
                <UserIcon className="w-4 h-4 text-[#676159] absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  required
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="usuario@igav.pe"
                  className="w-full pl-9 pr-4 py-2.5 bg-[#151413] border border-[#2A2724] text-xs text-[#EDE9E1] placeholder-[#676159] focus:outline-none focus:border-[#C4A47C] transition-colors font-mono"
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="text-[10px] font-mono tracking-wider uppercase text-[#968F85]">
                Clave de Seguridad
              </label>
              <div className="relative">
                <KeyRound className="w-4 h-4 text-[#676159] absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-9 pr-10 py-2.5 bg-[#151413] border border-[#2A2724] text-xs text-[#EDE9E1] placeholder-[#676159] focus:outline-none focus:border-[#C4A47C] transition-colors font-mono"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-[#676159] hover:text-[#EDE9E1] transition-colors"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <motion.button
              whileTap={{ scale: 0.98 }}
              disabled={isLoading}
              type="submit"
              className="w-full py-3 bg-[#C4A47C] hover:bg-[#B39167] text-[#1A1817] font-sans-editorial text-xs font-semibold tracking-wider uppercase transition-colors flex items-center justify-center gap-2 mt-2 shadow-sm disabled:opacity-50"
            >
              <LogIn className="w-4 h-4" />
              <span>{isLoading ? 'Verificando...' : 'Ingresar al Salón Privado'}</span>
            </motion.button>
          </form>

          {/* Quick Demo Access Bar */}
          <div className="pt-4 border-t border-[#2A2724] space-y-2">
            <span className="text-[9px] font-mono tracking-wider uppercase text-[#676159] block text-center">
              Acceso Rápido de Demostración
            </span>
            <div className="grid grid-cols-3 gap-2">
              <button
                type="button"
                onClick={() => handleQuickFill('admin.saas@igav.pe', 'admin123', 'Directora')}
                className="p-2 border border-[#2A2724] hover:border-[#C4A47C] bg-[#151413] text-[10px] font-mono text-[#968F85] hover:text-[#EDE9E1] transition-colors text-center"
              >
                Directora
              </button>
              <button
                type="button"
                onClick={() => handleQuickFill('vendedora@igav.pe', 'admin123', 'Asesora')}
                className="p-2 border border-[#2A2724] hover:border-[#C4A47C] bg-[#151413] text-[10px] font-mono text-[#968F85] hover:text-[#EDE9E1] transition-colors text-center"
              >
                Asesora
              </button>
              <button
                type="button"
                onClick={() => handleQuickFill('tintoreria@igav.pe', 'admin123', 'Tintorería')}
                className="p-2 border border-[#2A2724] hover:border-[#C4A47C] bg-[#151413] text-[10px] font-mono text-[#968F85] hover:text-[#EDE9E1] transition-colors text-center"
              >
                Cuidado
              </button>
            </div>
          </div>
        </div>

        {/* Footer */}
        <p className="text-center text-[10px] font-mono tracking-[0.2em] uppercase text-[#676159]">
          Confidencialidad & Custodia Textil — Maison I.G.A.V.
        </p>
      </motion.div>
    </div>
  );
};