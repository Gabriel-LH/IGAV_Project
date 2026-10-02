'use client';

import React from 'react';
import { 
  LayoutDashboard, 
  Shirt, 
  CalendarCheck, 
  Sparkles, 
  AlertTriangle, 
  Clock, 
  Users,
  Building2,
  Shield,
  Scissors,
  Crown
} from 'lucide-react';
import { motion } from 'framer-motion';

export type TabType = 'dashboard' | 'garments' | 'customers' | 'orders' | 'tailoring' | 'laundry' | 'alerts' | 'stores' | 'users';

interface SidebarProps {
  activeTab: TabType;
  setActiveTab: (tab: TabType) => void;
  rotationAlertCount: number;
  laundryCount: number;
  customerCount: number;
  userCount: number;
  tailoringCount: number;
  userRole?: string;
}

export const roleAccessMap: Record<string, TabType[]> = {
  SUPER_ADMIN: ['dashboard', 'garments', 'customers', 'orders', 'tailoring', 'laundry', 'alerts', 'stores', 'users'],
  ADMIN_SAAS: ['dashboard', 'garments', 'customers', 'orders', 'tailoring', 'laundry', 'alerts', 'stores', 'users'],
  ADMIN_TIENDA: ['dashboard', 'garments', 'customers', 'orders', 'tailoring', 'laundry', 'alerts', 'stores'],
  VENDEDOR: ['dashboard', 'garments', 'customers', 'orders', 'tailoring'],
  ENCARGADO_ALMACEN_TINTORERIA: ['dashboard', 'garments', 'laundry', 'alerts'],
  ENCARGADO_TINTORERIA: ['dashboard', 'garments', 'laundry', 'alerts']
};

export const Sidebar: React.FC<SidebarProps> = ({ 
  activeTab, 
  setActiveTab,
  rotationAlertCount,
  laundryCount,
  customerCount,
  userCount,
  tailoringCount,
  userRole = 'SUPER_ADMIN'
}) => {
  const allMenuItems = [
    {
      id: 'dashboard' as TabType,
      label: 'Salón Principal',
      icon: LayoutDashboard,
      badge: null
    },
    {
      id: 'garments' as TabType,
      label: 'Archivo de Vestuario',
      icon: Shirt,
      badge: null
    },
    {
      id: 'customers' as TabType,
      label: 'Directorio de Huéspedes',
      icon: Users,
      badge: customerCount > 0 ? customerCount : null,
      badgeColor: 'bg-[var(--accent-sage-light)] text-[var(--accent-sage)] border-[var(--accent-sage)]/30'
    },
    {
      id: 'orders' as TabType,
      label: 'Contratos de Custodia',
      icon: CalendarCheck,
      badge: null
    },
    {
      id: 'tailoring' as TabType,
      label: 'Taller & Entalle Fino',
      icon: Scissors,
      badge: tailoringCount > 0 ? tailoringCount : null,
      badgeColor: 'bg-[var(--accent-gold-light)] text-[var(--accent-gold)] border-[var(--accent-gold)]/30'
    },
    {
      id: 'laundry' as TabType,
      label: 'Vaporizado & Cuidados',
      icon: Clock,
      badge: laundryCount > 0 ? laundryCount : null,
      badgeColor: 'bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/30'
    },
    {
      id: 'alerts' as TabType,
      label: 'Inspección de Fibras',
      icon: AlertTriangle,
      badge: rotationAlertCount > 0 ? rotationAlertCount : null,
      badgeColor: 'bg-[var(--accent-burgundy-light)] text-[var(--accent-burgundy)] border-[var(--accent-burgundy)]/30'
    },
    {
      id: 'stores' as TabType,
      label: 'Ateliers & Sedes',
      icon: Building2,
      badge: null
    },
    {
      id: 'users' as TabType,
      label: 'Maestros & Conserjes',
      icon: Shield,
      badge: userCount > 0 ? userCount : null,
      badgeColor: 'bg-[var(--surface-elevated)] text-[var(--text-secondary)] border-[var(--border-subtle)]'
    }
  ];

  // RBAC Access Filter
  const normalizedRole = userRole.toUpperCase();
  const allowed = roleAccessMap[normalizedRole] || roleAccessMap.VENDEDOR;
  const menuItems = allMenuItems.filter(item => allowed.includes(item.id));

  return (
    <aside className="w-64 shrink-0 bg-[var(--surface-card)] border-r border-[var(--border-subtle)] p-4 flex flex-col justify-between hidden md:flex min-h-[calc(100vh-65px)] transition-colors duration-300">
      <div className="space-y-6">
        {/* Role Badge Indicator */}
        <div className="px-3 py-2 bg-[var(--surface-elevated)] border border-[var(--border-subtle)] flex items-center justify-between">
          <div className="flex items-center gap-2">
            {normalizedRole.includes('ADMIN') ? (
              <Crown className="w-3.5 h-3.5 text-[var(--accent-gold)]" />
            ) : normalizedRole.includes('TINTORERIA') ? (
              <Clock className="w-3.5 h-3.5 text-[var(--accent-burgundy)]" />
            ) : (
              <Sparkles className="w-3.5 h-3.5 text-[var(--accent-sage)]" />
            )}
            <span className="text-[10px] font-mono uppercase tracking-[0.16em] text-[var(--text-secondary)]">
              {normalizedRole.replace('_', ' ')}
            </span>
          </div>
          <span className="text-[9px] font-mono px-1.5 py-0.5 bg-[var(--surface-card)] border border-[var(--border-subtle)] text-[var(--text-secondary)]">
            Activo
          </span>
        </div>

        <div>
          <p className="px-3 text-[9px] font-mono font-medium text-[var(--text-tertiary)] uppercase tracking-[0.24em] mb-2.5">
            Salones de Gestión
          </p>
          <nav className="space-y-1">
            {menuItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;
              return (
                <motion.button
                  key={item.id}
                  whileTap={{ scale: 0.98 }}
                  onClick={() => setActiveTab(item.id)}
                  className={`w-full flex items-center justify-between px-3 py-2 text-xs font-mono transition-all duration-200 border ${
                    isActive
                      ? 'bg-[var(--surface-elevated)] text-[var(--text-primary)] border-[var(--border-subtle)] border-l-2 border-l-[var(--accent-gold)] font-medium shadow-sm'
                      : 'border-transparent text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:border-[var(--border-subtle)]'
                  }`}
                >
                  <div className="flex items-center gap-2.5">
                    <Icon className={`w-3.5 h-3.5 ${isActive ? 'text-[var(--accent-gold)]' : 'text-[var(--text-tertiary)]'}`} />
                    <span className="font-sans-editorial text-xs">{item.label}</span>
                  </div>

                  {item.badge !== null && (
                    <span className={`px-1.5 py-0.2 text-[9px] font-mono border ${item.badgeColor}`}>
                      {item.badge}
                    </span>
                  )}
                </motion.button>
              );
            })}
          </nav>
        </div>
      </div>

      {/* Footer Branding */}
      <div className="pt-4 border-t border-[var(--border-subtle)] space-y-1">
        <p className="text-[9px] uppercase tracking-[0.2em] font-mono text-[var(--text-tertiary)]">
          Maison I.G.A.V. — París & Lima
        </p>
        <p className="font-serif-editorial text-xs italic text-[var(--text-secondary)]">
          Custodia y Arquitectura Textil
        </p>
      </div>
    </aside>
  );
};