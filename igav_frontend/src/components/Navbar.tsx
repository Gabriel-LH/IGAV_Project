'use client';

import React, { useEffect, useState } from 'react';
import { Store, ShieldCheck, RefreshCw, Sun, Moon, Sparkles, ShoppingBag } from 'lucide-react';
import { motion } from 'framer-motion';
import { useTheme } from 'next-themes';

export interface AuthSession {
  token: string;
  email: string;
  nombreCompleto: string;
  rol: string;
  storeId: number;
  storeNombre: string;
}

interface NavbarProps {
  currentStore: string;
  onRefresh: () => void;
  isLoading: boolean;
  cartCount: number;
  onOpenCart: () => void;
  userSession?: AuthSession | null;
  onOpenLogin?: () => void;
  onLogout?: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ 
  currentStore, 
  onRefresh, 
  isLoading, 
  cartCount, 
  onOpenCart,
  userSession,
  onOpenLogin,
  onLogout
}) => {
  const { theme, setTheme } = useTheme();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  const toggleTheme = () => {
    setTheme(theme === 'dark' ? 'light' : 'dark');
  };

  return (
    <header className="sticky top-0 z-40 w-full glass-panel border-b border-[var(--glass-border)] transition-colors duration-200">
      <div className="w-full px-4 sm:px-6 lg:px-8 py-2.5 flex items-center justify-between gap-4">
        {/* Left: Brand / Logo */}
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-amber-500 via-amber-400 to-yellow-200 flex items-center justify-center shadow-lg shadow-amber-500/20 text-black font-black text-lg tracking-wider shrink-0">
            IG
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-base font-extrabold tracking-tight text-[var(--text-primary)]">
                I.G.A.V. <span className="gold-gradient-text font-black">SaaS</span>
              </h1>
              <span className="hidden sm:inline-flex px-2 py-0.5 text-[10px] font-bold tracking-wider uppercase bg-amber-500/10 text-amber-500 border border-amber-500/20 rounded-full items-center gap-1">
                <Sparkles className="w-2.5 h-2.5" />
                Gala & Bridal
              </span>
            </div>
            <p className="text-[11px] text-[var(--text-secondary)] font-medium leading-tight">
              Gestión Integral de Alquileres, Entalle y Tintorería
            </p>
          </div>
        </div>

        {/* Right: Controls & Profile */}
        <div className="flex items-center gap-2 sm:gap-2.5">
          {/* Shopping Cart Drawer Trigger Button */}
          <motion.button
            whileTap={{ scale: 0.95 }}
            onClick={onOpenCart}
            className={'relative h-9 px-3.5 rounded-xl border transition-all flex items-center gap-2 text-xs font-bold shadow-md ' + (
              cartCount > 0
                ? 'bg-amber-500 text-black border-amber-400 shadow-amber-500/20'
                : 'bg-[var(--card-bg)] text-[var(--text-primary)] border-[var(--border-color)] hover:border-amber-500/40'
            )}
            title="Abrir Carrito de Alquileres"
          >
            <ShoppingBag className={'w-4 h-4 ' + (cartCount > 0 ? 'text-black' : 'text-amber-500')} />
            <span className="hidden sm:inline">Carrito</span>
            <span className={'px-1.5 py-0.2 rounded-full text-[11px] font-extrabold ' + (
              cartCount > 0 ? 'bg-black text-amber-400' : 'bg-amber-500/20 text-amber-500'
            )}>
              {cartCount}
            </span>
          </motion.button>

          {/* Active Store Indicator */}
          <div className="hidden md:flex items-center gap-2 h-9 px-3 rounded-lg bg-[var(--card-bg)] border border-[var(--border-color)] text-xs shadow-sm">
            <Store className="w-3.5 h-3.5 text-amber-500 shrink-0" />
            <div className="flex flex-col">
              <span className="text-[9px] text-[var(--text-secondary)] uppercase font-semibold leading-none">Sede</span>
              <span className="font-bold text-[var(--text-primary)] leading-tight max-w-[140px] truncate">{currentStore}</span>
            </div>
          </div>

          {/* Theme Switcher Button */}
          {mounted && (
            <motion.button
              whileTap={{ scale: 0.95 }}
              onClick={toggleTheme}
              className="h-9 px-3 rounded-lg bg-[var(--card-bg)] border border-[var(--border-color)] hover:border-amber-500/40 transition-all flex items-center gap-2 text-xs font-semibold shadow-sm text-[var(--text-primary)]"
              title={theme === 'dark' ? 'Cambiar a Modo Día (Novias / Alta Costura)' : 'Cambiar a Modo Noche (Black Tie / Gala)'}
            >
              {theme === 'dark' ? (
                <>
                  <Sun className="w-4 h-4 text-amber-700 dark:text-amber-400 shrink-0" />
                  <span className="hidden sm:inline text-amber-700 dark:text-amber-400 text-[11px]">Día / Novias</span>
                </>
              ) : (
                <>
                  <Moon className="w-4 h-4 text-amber-600 shrink-0" />
                  <span className="hidden sm:inline text-amber-700 text-[11px]">Gala Noche</span>
                </>
              )}
            </motion.button>
          )}

          {/* Refresh Button */}
          <motion.button
            whileTap={{ scale: 0.95 }}
            onClick={onRefresh}
            className="w-9 h-9 rounded-lg bg-[var(--card-bg)] border border-[var(--border-color)] hover:border-amber-500/40 text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-all flex items-center justify-center shadow-sm shrink-0"
            title="Sincronizar datos con Backend"
          >
            <RefreshCw className={'w-3.5 h-3.5 ' + (isLoading ? 'animate-spin text-amber-500' : '')} />
          </motion.button>

          {/* API Activa Status Badge */}
          <div className="hidden lg:flex items-center gap-1.5 h-9 px-2.5 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-[11px] text-emerald-500 font-semibold shadow-sm">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            <ShieldCheck className="w-3.5 h-3.5" />
            <span>API Online</span>
          </div>

          {/* User Profile / Login Button */}
          <div className="flex items-center gap-2.5 pl-2 border-l border-[var(--glass-border)]">
            {userSession ? (
              <div className="flex items-center gap-2">
                <div className="w-9 h-9 rounded-lg bg-gradient-to-br from-amber-500 to-amber-700 flex items-center justify-center font-extrabold text-xs text-black shadow-md shrink-0">
                  {userSession.nombreCompleto.substring(0, 2).toUpperCase()}
                </div>
                <div className="hidden xl:flex flex-col">
                  <span className="text-xs font-bold text-[var(--text-primary)] leading-tight">
                    {userSession.nombreCompleto}
                  </span>
                  <span className="text-[10px] text-amber-500 font-semibold leading-none">
                    {userSession.rol}
                  </span>
                </div>
                <button
                  onClick={onLogout}
                  className="px-2 py-1 bg-stone-800/60 hover:bg-rose-500/20 text-stone-400 hover:text-rose-400 border border-stone-700/50 rounded-lg text-[10px] font-semibold transition-all"
                  title="Cerrar sesión actual"
                >
                  Salir
                </button>
              </div>
            ) : (
              <button
                onClick={onOpenLogin}
                className="px-3 py-1.5 bg-amber-500 hover:bg-amber-400 text-stone-950 font-bold rounded-lg text-xs transition-all shadow-md shadow-amber-500/10 flex items-center gap-1.5"
              >
                <ShieldCheck className="w-3.5 h-3.5" />
                <span>Acceso Staff</span>
              </button>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};
