'use client';

import React, { useEffect, useState } from 'react';
import { Store, RefreshCw, Sun, Moon, ShoppingBag, LogOut } from 'lucide-react';
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
    <header className="sticky top-0 z-40 w-full bg-[var(--surface-overlay)] backdrop-blur-md border-b border-[var(--border-subtle)] transition-colors duration-300">
      <div className="w-full px-4 sm:px-8 py-3 flex items-center justify-between gap-4">
        {/* Left: Brand / Logo */}
        <div className="flex items-center gap-3.5">
          <div className="w-9 h-9 border border-[var(--accent-gold)] bg-[var(--surface-elevated)] flex items-center justify-center text-[var(--accent-gold)] font-cinzel text-base tracking-widest shrink-0 font-bold">
            IG
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="font-cinzel text-sm sm:text-base font-bold tracking-[0.16em] text-[var(--text-primary)]">
                I.G.A.V. <span className="font-serif-editorial italic font-normal text-[var(--accent-gold)] text-lg">Atelier</span>
              </h1>
              <span className="hidden sm:inline-flex px-2 py-0.5 text-[9px] uppercase tracking-[0.2em] font-mono bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border border-[var(--accent-gold)]/30">
                Haute Couture Archive
              </span>
            </div>
            <p className="text-[10px] text-[var(--text-secondary)] font-mono tracking-wider leading-tight">
              Salón Privado de Alquiler de Gala & Sastrería a Medida
            </p>
          </div>
        </div>

        {/* Right: Controls & Profile */}
        <div className="flex items-center gap-2 sm:gap-3">
          {/* Shopping Cart Drawer Trigger Button (Salón Privado) */}
          <motion.button
            whileTap={{ scale: 0.98 }}
            onClick={onOpenCart}
            className={`h-8 px-3 border transition-all flex items-center gap-2 text-[11px] font-mono tracking-wider uppercase ${
              cartCount > 0
                ? 'bg-[var(--accent-gold)] text-[#151413] border-[var(--accent-gold)] font-medium shadow-sm'
                : 'bg-transparent text-[var(--text-primary)] border-[var(--border-subtle)] hover:border-[var(--accent-gold)]'
            }`}
            title="Abrir Salón Privado de Selección"
          >
            <ShoppingBag className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Salón Privado</span>
            <span className={`px-1.5 py-0.2 text-[10px] font-mono font-bold ${
              cartCount > 0 ? 'bg-[#151413] text-[var(--accent-gold)]' : 'bg-[var(--surface-elevated)] text-[var(--text-secondary)]'
            }`}>
              {cartCount}
            </span>
          </motion.button>

          {/* Active Store Indicator */}
          <div className="hidden md:flex items-center gap-2 h-8 px-3 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] text-xs font-mono">
            <Store className="w-3.5 h-3.5 text-[var(--accent-gold)] shrink-0" />
            <div className="flex flex-col">
              <span className="text-[8px] text-[var(--text-secondary)] uppercase tracking-wider leading-none">Sede</span>
              <span className="text-[11px] text-[var(--text-primary)] leading-tight max-w-[150px] truncate font-medium">{currentStore}</span>
            </div>
          </div>

          {/* Theme Switcher Button */}
          {mounted && (
            <motion.button
              whileTap={{ scale: 0.98 }}
              onClick={toggleTheme}
              className="h-8 px-2.5 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] transition-all flex items-center gap-1.5 text-[11px] font-mono text-[var(--text-primary)]"
              title={theme === 'dark' ? 'Cambiar a Modo Diurno (Novias / Taller)' : 'Cambiar a Modo Nocturno (Soirée / Black Tie)'}
            >
              {theme === 'dark' ? (
                <>
                  <Sun className="w-3.5 h-3.5 text-[var(--accent-gold)] shrink-0" />
                  <span className="hidden sm:inline text-[10px]">Atelier Día</span>
                </>
              ) : (
                <>
                  <Moon className="w-3.5 h-3.5 text-[var(--accent-gold)] shrink-0" />
                  <span className="hidden sm:inline text-[10px]">Soirée Noche</span>
                </>
              )}
            </motion.button>
          )}

          {/* Refresh Button */}
          <motion.button
            whileTap={{ scale: 0.98 }}
            onClick={onRefresh}
            className="w-8 h-8 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] hover:border-[var(--accent-gold)] text-[var(--text-secondary)] hover:text-[var(--text-primary)] transition-all flex items-center justify-center shrink-0"
            title="Sincronizar con Archivo MySQL"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${isLoading ? 'animate-spin text-[var(--accent-gold)]' : ''}`} />
          </motion.button>

          {/* User Session Profile Button */}
          {userSession ? (
            <div className="flex items-center gap-2 pl-2 border-l border-[var(--border-subtle)]">
              <div className="hidden sm:flex flex-col text-right">
                <span className="text-xs font-serif-editorial italic font-medium text-[var(--text-primary)] leading-tight">{userSession.nombreCompleto}</span>
                <span className="text-[9px] font-mono text-[var(--accent-gold)] uppercase tracking-wider">{userSession.rol.replace('_', ' ')}</span>
              </div>
              <button
                onClick={onLogout}
                className="w-8 h-8 border border-[var(--border-subtle)] hover:border-[var(--accent-burgundy)] text-[var(--text-secondary)] hover:text-[var(--accent-burgundy)] flex items-center justify-center transition-all"
                title="Cerrar Sesión de Atelier"
              >
                <LogOut className="w-3.5 h-3.5" />
              </button>
            </div>
          ) : (
            <button
              onClick={onOpenLogin}
              className="h-8 px-3 border border-[var(--accent-gold)] bg-[var(--accent-gold-light)] text-[var(--accent-gold)] text-[11px] font-mono uppercase tracking-wider hover:bg-[var(--accent-gold)] hover:text-[#151413] transition-all"
            >
              Ingresar
            </button>
          )}
        </div>
      </div>
    </header>
  );
};