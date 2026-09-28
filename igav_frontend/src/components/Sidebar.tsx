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
  Scissors
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
}

export const Sidebar: React.FC<SidebarProps> = ({ 
  activeTab, 
  setActiveTab,
  rotationAlertCount,
  laundryCount,
  customerCount,
  userCount,
  tailoringCount
}) => {
  const menuItems = [
    {
      id: 'dashboard' as TabType,
      label: 'Panel General',
      icon: LayoutDashboard,
      badge: null
    },
    {
      id: 'garments' as TabType,
      label: 'Catálogo de Prendas',
      icon: Shirt,
      badge: null
    },
    {
      id: 'customers' as TabType,
      label: 'Maestro de Clientes',
      icon: Users,
      badge: customerCount > 0 ? customerCount : null,
      badgeColor: 'bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 border-emerald-500/30'
    },
    {
      id: 'orders' as TabType,
      label: 'Alquileres & Reservas',
      icon: CalendarCheck,
      badge: null
    },
    {
      id: 'tailoring' as TabType,
      label: 'Taller de Sastrería',
      icon: Scissors,
      badge: tailoringCount > 0 ? tailoringCount : null,
      badgeColor: 'bg-amber-500/15 text-amber-700 dark:text-amber-400 border-amber-500/30'
    },
    {
      id: 'laundry' as TabType,
      label: 'Recepción & Tintorería',
      icon: Clock,
      badge: laundryCount > 0 ? laundryCount : null,
      badgeColor: 'bg-purple-500/15 text-purple-700 dark:text-purple-400 border-purple-500/30'
    },
    {
      id: 'alerts' as TabType,
      label: 'Alertas de Desgaste',
      icon: AlertTriangle,
      badge: rotationAlertCount > 0 ? rotationAlertCount : null,
      badgeColor: 'bg-rose-500/15 text-rose-700 dark:text-rose-400 border-rose-500/30'
    },
    {
      id: 'stores' as TabType,
      label: 'Sedes & Multi-tenant',
      icon: Building2,
      badge: null
    },
    {
      id: 'users' as TabType,
      label: 'Staff & Roles (RF-02)',
      icon: Shield,
      badge: userCount > 0 ? userCount : null,
      badgeColor: 'bg-blue-500/15 text-blue-700 dark:text-blue-400 border-blue-500/30'
    }
  ];

  return (
    <aside className="w-64 shrink-0 glass-panel border-r border-[var(--glass-border)] p-4 flex flex-col justify-between hidden md:flex min-h-[calc(100vh-65px)] transition-colors duration-200">
      <div className="space-y-6">
        <div>
          <p className="px-3 text-[10px] font-bold text-[var(--text-secondary)] uppercase tracking-wider mb-2.5">
            Módulos Principales
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
                  className={'w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl font-medium text-xs transition-all duration-200 ' +
                    (isActive
                      ? 'bg-amber-500/15 text-amber-800 dark:text-amber-400 border border-amber-500/30 shadow-sm font-bold'
                      : 'text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-[var(--card-bg)] border border-transparent')
                  }
                >
                  <div className="flex items-center gap-2.5">
                    <Icon className={'w-4 h-4 ' + (isActive ? 'text-amber-500' : 'text-[var(--text-secondary)]')} />
                    <span>{item.label}</span>
                  </div>

                  {item.badge !== null && (
                    <span className={'px-2 py-0.5 text-[10px] font-extrabold rounded-full border ' + item.badgeColor}>
                      {item.badge}
                    </span>
                  )}
                </motion.button>
              );
            })}
          </nav>
        </div>

        <div className="pt-4 border-t border-[var(--glass-border)]">
          <p className="px-3 text-[10px] font-bold text-[var(--text-secondary)] uppercase tracking-wider mb-2.5">
            Reglas de Negocio
          </p>
          <div className="space-y-2 px-3 text-[11px] text-[var(--text-secondary)] font-medium">
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span>
              <span>RF-01 Multi-tenant Isolator</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-blue-500"></span>
              <span>RF-02 Role Security & BCrypt</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-amber-500"></span>
              <span>RF-04 Date Collision Guard</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-purple-500"></span>
              <span>RF-05 24h-48h Tintorería Block</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-1.5 h-1.5 rounded-full bg-rose-500"></span>
              <span>RF-12 Max Wear Counter</span>
            </div>
          </div>
        </div>
      </div>

      <div className="p-3 rounded-xl bg-amber-500/10 border border-amber-500/20 text-xs text-[var(--text-secondary)]">
        <div className="flex items-center gap-1.5 font-bold text-amber-800 dark:text-amber-400 mb-1">
          <Sparkles className="w-3.5 h-3.5" />
          <span>Gala Wear System</span>
        </div>
        <p className="text-[11px] leading-relaxed">
          Control total de prendas de etiqueta, liquidación de garantías y ciclos de tintorería.
        </p>
      </div>
    </aside>
  );
};
