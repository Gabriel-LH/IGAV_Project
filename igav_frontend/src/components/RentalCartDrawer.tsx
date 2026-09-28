'use client';

import React, { useState, useEffect } from 'react';
import { 
  ShoppingBag, 
  X, 
  Trash2, 
  User, 
  UserPlus, 
  Calendar, 
  CreditCard, 
  QrCode, 
  Banknote, 
  Building2, 
  ShieldCheck, 
  CheckCircle2, 
  Tag,
  Sparkles,
  ArrowRight
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { Garment, Customer, Order, OrderItem } from '../lib/types';
import { toast } from 'sonner';

interface RentalCartDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  cart: Garment[];
  onRemoveFromCart: (garmentId: number) => void;
  onClearCart: () => void;
  customers: Customer[];
  onCheckoutComplete: (newOrder: Order, newCustomer?: Customer) => void;
}

export type PaymentMethodType = 'TARJETA_CREDITO_DEBITO' | 'BILLETERA_DIGITAL_YAPE_PLIN' | 'EFECTIVO' | 'TRANSFERENCIA';

export const RentalCartDrawer: React.FC<RentalCartDrawerProps> = ({
  isOpen,
  onClose,
  cart,
  onRemoveFromCart,
  onClearCart,
  customers,
  onCheckoutComplete
}) => {
  // Customer mode: 'EXISTING' | 'NEW'
  const [customerMode, setCustomerMode] = useState<'EXISTING' | 'NEW'>('EXISTING');
  
  // Existing customer selection
  const [selectedCustomerId, setSelectedCustomerId] = useState<number>(customers[0]?.id || 0);

  useEffect(() => {
    if ((!selectedCustomerId || selectedCustomerId === 0) && customers.length > 0) {
      setSelectedCustomerId(customers[0].id);
    }
  }, [customers, selectedCustomerId]);
  
  // New customer inputs
  const [newNombre, setNewNombre] = useState('');
  const [newTipoDoc, setNewTipoDoc] = useState<'DNI' | 'RUC' | 'PASAPORTE'>('DNI');
  const [newNumDoc, setNewNumDoc] = useState('');
  const [newTelefono, setNewTelefono] = useState('');
  const [newEmail, setNewEmail] = useState('');

  // Dates
  const todayStr = new Date().toISOString().split('T')[0];
  const threeDaysLater = new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];
  const [fechaEntrega, setFechaEntrega] = useState(todayStr);
  const [fechaDevolucion, setFechaDevolucion] = useState(threeDaysLater);

  // Payment Method
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethodType>('BILLETERA_DIGITAL_YAPE_PLIN');

  // Summary totals
  const subtotalAlquiler = cart.reduce((acc, item) => acc + item.precioAlquiler, 0);
  const totalGarantia = cart.reduce((acc, item) => acc + item.depositoGarantia, 0);
  const montoTotal = subtotalAlquiler + totalGarantia;

  const handleCompleteOrder = (e: React.FormEvent) => {
    e.preventDefault();
    if (cart.length === 0) {
      toast.error('El carrito de alquileres está vacío');
      return;
    }

    let customerObj: Partial<Customer> = {};
    let createdCustomer: Customer | undefined = undefined;

    if (customerMode === 'EXISTING') {
      const existing = customers.find(c => c.id === Number(selectedCustomerId)) || customers[0];
      if (!existing) {
        toast.error('Por favor seleccione un cliente de la lista');
        return;
      }
      const existingTel = typeof existing.telefono === 'object' && existing.telefono !== null
        ? ((existing.telefono as any).value || '')
        : (typeof existing.telefono === 'string' ? existing.telefono : '');
      const existingDoc = typeof (existing as any).documentoIdentidad === 'object' && (existing as any).documentoIdentidad !== null
        ? ((existing as any).documentoIdentidad.numero || '')
        : (typeof existing.numeroDocumento === 'string' ? existing.numeroDocumento : '');
      const existingNombre = existing.nombreCompleto || `${existing.nombres || ''} ${existing.apellidos || ''}`.trim() || 'Cliente Gala';

      customerObj = {
        id: existing.id,
        nombreCompleto: existingNombre,
        numeroDocumento: existingDoc,
        telefono: existingTel
      };
    } else {
      if (!newNombre || !newNumDoc) {
        toast.error('Ingrese el nombre y documento del nuevo cliente');
        return;
      }
      createdCustomer = {
        id: Date.now(),
        tipoDocumento: newTipoDoc,
        numeroDocumento: newNumDoc,
        nombres: newNombre.split(' ')[0] || newNombre,
        apellidos: newNombre.split(' ').slice(1).join(' ') || '',
        nombreCompleto: newNombre,
        email: newEmail || 'cliente@gala.com',
        telefono: newTelefono || '+51 900 000 000',
        direccion: 'Sede Gala Presencial',
        categoriaCliente: 'REGULAR',
        totalAlquileres: 1,
        calificacion: 5.0,
        fechaRegistro: todayStr
      };
      customerObj = {
        id: createdCustomer.id,
        nombreCompleto: createdCustomer.nombreCompleto,
        numeroDocumento: createdCustomer.numeroDocumento,
        telefono: createdCustomer.telefono
      };
    }

    // Build order items
    const orderItems: OrderItem[] = cart.map(g => ({
      garmentId: g.id,
      garmentName: g.nombre,
      garmentSku: g.codigoUnico,
      tipoItem: 'ALQUILAR',
      precioAplicado: g.precioAlquiler,
      garantiaAplicada: g.depositoGarantia
    }));

    const contractCode = "CTR-2026-" + Math.floor(1000 + Math.random() * 9000);

    const newOrder: Order = {
      id: Date.now(),
      codigoContrato: contractCode,
      customerId: customerObj.id,
      clienteDocumento: customerObj.numeroDocumento,
      clienteNombreCompleto: customerObj.nombreCompleto || 'Cliente Gala',
      clienteTelefono: customerObj.telefono,
      storeId: 1,
      tipo: 'ALQUILAR',
      fechaEntregaAcordada: fechaEntrega,
      fechaDevolucionAcordada: fechaDevolucion,
      subtotal: subtotalAlquiler,
      montoGarantiaTotal: totalGarantia,
      descuentoGarantia: 0,
      montoPenalizacion: 0,
      montoTotal: montoTotal,
      garantiaDevueltaNeta: totalGarantia,
      estado: 'EN_ALQUILAR',
      metodoPago: paymentMethod,
      items: orderItems
    };

    onCheckoutComplete(newOrder, createdCustomer);
    toast.success("Contrato " + contractCode + " creado con éxito para " + customerObj.nombreCompleto + "!", {
      description: "Total cobrado: S/ " + montoTotal.toFixed(2) + " (Incluye depósito garantía S/ " + totalGarantia.toFixed(2) + ")"
    });
    onClearCart();
    onClose();
  };

  return (
    <AnimatePresence>
      {isOpen && (
        <div className="fixed inset-0 z-50 overflow-hidden flex justify-end">
          {/* Backdrop */}
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
            className="fixed inset-0 bg-black/60 backdrop-blur-sm transition-opacity"
          />

          {/* Drawer Slide Panel */}
          <motion.div
            initial={{ x: '100%' }}
            animate={{ x: 0 }}
            exit={{ x: '100%' }}
            transition={{ type: 'spring', damping: 25, stiffness: 200 }}
            className="relative w-full max-w-lg bg-[var(--bg-main)] border-l border-[var(--glass-border)] shadow-2xl flex flex-col h-full z-10 text-[var(--text-primary)]"
          >
            {/* Drawer Header */}
            <div className="p-4 sm:p-5 border-b border-[var(--glass-border)] flex items-center justify-between bg-black/5 dark:bg-white/5">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-amber-500/20 text-amber-500 border border-amber-500/30 flex items-center justify-center font-bold">
                  <ShoppingBag className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="font-extrabold text-base flex items-center gap-2 text-[var(--text-primary)]">
                    Carrito de Alquileres & Gala
                    <span className="px-2 py-0.5 text-xs rounded-full bg-amber-500 text-black font-extrabold">
                      {cart.length} {cart.length === 1 ? 'prenda' : 'prendas'}
                    </span>
                  </h3>
                  <p className="text-xs text-[var(--text-secondary)]">Selección de prendas y contrato express</p>
                </div>
              </div>

              <motion.button
                whileTap={{ scale: 0.9 }}
                onClick={onClose}
                className="p-2 rounded-xl text-[var(--text-secondary)] hover:text-[var(--text-primary)] hover:bg-black/5 dark:hover:bg-white/10"
              >
                <X className="w-5 h-5" />
              </motion.button>
            </div>

            {/* Scrollable Form Body */}
            <form onSubmit={handleCompleteOrder} className="flex-1 overflow-y-auto p-4 sm:p-5 space-y-6">
              
              {/* STEP 1: Garment List */}
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <h4 className="text-xs font-bold uppercase tracking-wider text-[var(--text-secondary)] flex items-center gap-1.5">
                    <Sparkles className="w-3.5 h-3.5 text-amber-500" />
                    1. Prendas Seleccionadas
                  </h4>
                  {cart.length > 0 && (
                    <button
                      type="button"
                      onClick={onClearCart}
                      className="text-xs text-rose-500 hover:text-rose-400 font-semibold flex items-center gap-1"
                    >
                      <Trash2 className="w-3 h-3" />
                      Vaciar
                    </button>
                  )}
                </div>

                {cart.length === 0 ? (
                  <div className="p-8 text-center rounded-2xl border border-dashed border-[var(--glass-border)] bg-black/5 dark:bg-white/5 space-y-2">
                    <ShoppingBag className="w-10 h-10 text-slate-400 mx-auto opacity-50" />
                    <p className="text-sm font-semibold text-[var(--text-secondary)]">El carrito está vacío</p>
                    <p className="text-xs text-[var(--text-secondary)] opacity-75">
                      Explora el catálogo y presiona "Reservar Alquiler" o "Agregar al Carrito".
                    </p>
                  </div>
                ) : (
                  <div className="space-y-2.5 max-h-56 overflow-y-auto pr-1">
                    {cart.map((item) => (
                      <div
                        key={item.id}
                        className="p-3 rounded-xl bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] flex items-center gap-3 group transition-all"
                      >
                        <img
                          src={item.imageUrl || 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800'}
                          alt={item.nombre}
                          className="w-12 h-14 object-cover rounded-lg shrink-0 bg-slate-800"
                        />
                        <div className="flex-1 min-w-0">
                          <div className="flex items-center gap-2">
                            <span className="text-[10px] font-mono font-bold text-amber-500 bg-amber-500/10 px-1.5 py-0.5 rounded">
                              {item.codigoUnico}
                            </span>
                            <span className="text-[10px] font-semibold text-[var(--text-secondary)]">
                              Talla {item.talla}
                            </span>
                          </div>
                          <h5 className="font-bold text-xs truncate text-[var(--text-primary)] mt-0.5">
                            {item.nombre}
                          </h5>
                          <div className="flex items-center gap-3 text-[11px] mt-1">
                            <span className="text-amber-500 font-bold">
                              Alquiler: S/ {item.precioAlquiler.toFixed(2)}
                            </span>
                            <span className="text-emerald-500 font-semibold text-[10px]">
                              Depósitos: S/ {item.depositoGarantia.toFixed(2)}
                            </span>
                          </div>
                        </div>

                        <motion.button
                          whileTap={{ scale: 0.9 }}
                          type="button"
                          onClick={() => onRemoveFromCart(item.id)}
                          className="p-1.5 rounded-lg text-slate-400 hover:text-rose-500 hover:bg-rose-500/10 transition-colors"
                          title="Eliminar del carrito"
                        >
                          <Trash2 className="w-4 h-4" />
                        </motion.button>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* STEP 2: Customer Selection */}
              <div className="space-y-3 pt-2 border-t border-[var(--glass-border)]">
                <h4 className="text-xs font-bold uppercase tracking-wider text-[var(--text-secondary)] flex items-center gap-1.5">
                  <User className="w-3.5 h-3.5 text-amber-500" />
                  2. Datos del Cliente
                </h4>

                <div className="grid grid-cols-2 p-1 bg-black/10 dark:bg-white/5 rounded-xl text-xs font-semibold">
                  <button
                    type="button"
                    onClick={() => setCustomerMode('EXISTING')}
                    className={'py-2 rounded-lg transition-all flex items-center justify-center gap-1.5 ' + (
                      customerMode === 'EXISTING'
                        ? 'bg-amber-500 text-black font-bold shadow-md'
                        : 'text-[var(--text-secondary)] hover:text-current'
                    )}
                  >
                    <User className="w-3.5 h-3.5" />
                    Cliente Existente
                  </button>

                  <button
                    type="button"
                    onClick={() => setCustomerMode('NEW')}
                    className={'py-2 rounded-lg transition-all flex items-center justify-center gap-1.5 ' + (
                      customerMode === 'NEW'
                        ? 'bg-amber-500 text-black font-bold shadow-md'
                        : 'text-[var(--text-secondary)] hover:text-current'
                    )}
                  >
                    <UserPlus className="w-3.5 h-3.5" />
                    Nuevo Express
                  </button>
                </div>

                {customerMode === 'EXISTING' ? (
                  <div>
                    <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                      Seleccionar Cliente Maestro *
                    </label>
                    <select
                      value={selectedCustomerId}
                      onChange={(e) => setSelectedCustomerId(Number(e.target.value))}
                      className="w-full px-3 py-2.5 rounded-xl bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs font-medium text-[var(--text-primary)]"
                    >
                      {customers.map((c: any) => {
                        const telStr = typeof c.telefono === 'object' && c.telefono !== null
                          ? (c.telefono.value || c.telefono.numeroFormateado || '')
                          : (typeof c.telefono === 'string' ? c.telefono : '');
                        const docTipoStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null
                          ? (c.documentoIdentidad.tipo || 'DNI')
                          : (typeof c.tipoDocumento === 'string' ? c.tipoDocumento : 'DNI');
                        const docNumStr = typeof c.documentoIdentidad === 'object' && c.documentoIdentidad !== null
                          ? (c.documentoIdentidad.numero || '')
                          : (typeof c.numeroDocumento === 'string' ? c.numeroDocumento : '');
                        const nameStr = c.nombreCompleto || `${c.nombres || ''} ${c.apellidos || ''}`.trim() || 'Cliente';

                        return (
                          <option key={c.id} value={c.id}>
                            {nameStr} ({docTipoStr}: {docNumStr}) {telStr ? `• ${telStr}` : ''}
                          </option>
                        );
                      })}
                    </select>
                  </div>
                ) : (
                  <div className="space-y-3 text-xs bg-black/5 dark:bg-white/5 p-3.5 rounded-xl border border-[var(--glass-border)]">
                    <div>
                      <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                        Nombre Completo *
                      </label>
                      <input
                        type="text"
                        placeholder="Ej. Lucía Alarcón Medina"
                        value={newNombre}
                        onChange={(e) => setNewNombre(e.target.value)}
                        className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs"
                      />
                    </div>
                    <div className="grid grid-cols-2 gap-2">
                      <div>
                        <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                          Documento *
                        </label>
                        <div className="flex gap-1">
                          <select
                            value={newTipoDoc}
                            onChange={(e: any) => setNewTipoDoc(e.target.value)}
                            className="px-2 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs"
                          >
                            <option value="DNI">DNI</option>
                            <option value="RUC">RUC</option>
                            <option value="PASAPORTE">PAS</option>
                          </select>
                          <input
                            type="text"
                            placeholder="71234567"
                            value={newNumDoc}
                            onChange={(e) => setNewNumDoc(e.target.value)}
                            className="w-full px-2 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] font-mono text-xs"
                          />
                        </div>
                      </div>
                      <div>
                        <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                          Teléfono
                        </label>
                        <input
                          type="text"
                          placeholder="+51 987654321"
                          value={newTelefono}
                          onChange={(e) => setNewTelefono(e.target.value)}
                          className="w-full px-3 py-2 rounded-lg bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-xs"
                        />
                      </div>
                    </div>
                  </div>
                )}
              </div>

              {/* STEP 3: Dates */}
              <div className="space-y-3 pt-2 border-t border-[var(--glass-border)]">
                <h4 className="text-xs font-bold uppercase tracking-wider text-[var(--text-secondary)] flex items-center gap-1.5">
                  <Calendar className="w-3.5 h-3.5 text-amber-500" />
                  3. Fechas del Alquiler
                </h4>
                <div className="grid grid-cols-2 gap-3 text-xs">
                  <div>
                    <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                      Fecha Entrega
                    </label>
                    <input
                      type="date"
                      value={fechaEntrega}
                      onChange={(e) => setFechaEntrega(e.target.value)}
                      className="w-full px-3 py-2 rounded-xl bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] font-semibold text-[var(--text-secondary)] mb-1">
                      Fecha Devolución
                    </label>
                    <input
                      type="date"
                      value={fechaDevolucion}
                      onChange={(e) => setFechaDevolucion(e.target.value)}
                      className="w-full px-3 py-2 rounded-xl bg-black/5 dark:bg-[var(--glass-bg)] border border-[var(--glass-border)] text-[var(--text-primary)]"
                    />
                  </div>
                </div>
              </div>

              {/* STEP 4: Payment Method Selection */}
              <div className="space-y-3 pt-2 border-t border-[var(--glass-border)]">
                <h4 className="text-xs font-bold uppercase tracking-wider text-[var(--text-secondary)] flex items-center gap-1.5">
                  <CreditCard className="w-3.5 h-3.5 text-amber-500" />
                  4. Método de Pago (RF-Payment)
                </h4>

                <div className="grid grid-cols-2 gap-2 text-xs">
                  {/* Option 1: Yape / Plin */}
                  <div
                    onClick={() => setPaymentMethod('BILLETERA_DIGITAL_YAPE_PLIN')}
                    className={'p-3 rounded-xl border cursor-pointer transition-all flex items-center gap-2.5 ' + (
                      paymentMethod === 'BILLETERA_DIGITAL_YAPE_PLIN'
                        ? 'bg-amber-500/15 border-amber-500 text-amber-500 font-bold shadow-md'
                        : 'bg-black/5 dark:bg-white/5 border-[var(--glass-border)] text-[var(--text-secondary)] hover:border-amber-500/50'
                    )}
                  >
                    <QrCode className="w-5 h-5 shrink-0 text-purple-400" />
                    <div>
                      <div className="font-bold text-xs text-[var(--text-primary)]">Yape / Plin</div>
                      <div className="text-[10px] text-[var(--text-secondary)]">Billetera Digital QR</div>
                    </div>
                  </div>

                  {/* Option 2: Tarjeta */}
                  <div
                    onClick={() => setPaymentMethod('TARJETA_CREDITO_DEBITO')}
                    className={'p-3 rounded-xl border cursor-pointer transition-all flex items-center gap-2.5 ' + (
                      paymentMethod === 'TARJETA_CREDITO_DEBITO'
                        ? 'bg-amber-500/15 border-amber-500 text-amber-500 font-bold shadow-md'
                        : 'bg-black/5 dark:bg-white/5 border-[var(--glass-border)] text-[var(--text-secondary)] hover:border-amber-500/50'
                    )}
                  >
                    <CreditCard className="w-5 h-5 shrink-0 text-blue-400" />
                    <div>
                      <div className="font-bold text-xs text-[var(--text-primary)]">Tarjeta Créd/Déb</div>
                      <div className="text-[10px] text-[var(--text-secondary)]">Visa / Mastercard</div>
                    </div>
                  </div>

                  {/* Option 3: Efectivo */}
                  <div
                    onClick={() => setPaymentMethod('EFECTIVO')}
                    className={'p-3 rounded-xl border cursor-pointer transition-all flex items-center gap-2.5 ' + (
                      paymentMethod === 'EFECTIVO'
                        ? 'bg-amber-500/15 border-amber-500 text-amber-500 font-bold shadow-md'
                        : 'bg-black/5 dark:bg-white/5 border-[var(--glass-border)] text-[var(--text-secondary)] hover:border-amber-500/50'
                    )}
                  >
                    <Banknote className="w-5 h-5 shrink-0 text-emerald-400" />
                    <div>
                      <div className="font-bold text-xs text-[var(--text-primary)]">Efectivo</div>
                      <div className="text-[10px] text-[var(--text-secondary)]">Pago Presencial</div>
                    </div>
                  </div>

                  {/* Option 4: Transferencia */}
                  <div
                    onClick={() => setPaymentMethod('TRANSFERENCIA')}
                    className={'p-3 rounded-xl border cursor-pointer transition-all flex items-center gap-2.5 ' + (
                      paymentMethod === 'TRANSFERENCIA'
                        ? 'bg-amber-500/15 border-amber-500 text-amber-500 font-bold shadow-md'
                        : 'bg-black/5 dark:bg-white/5 border-[var(--glass-border)] text-[var(--text-secondary)] hover:border-amber-500/50'
                    )}
                  >
                    <Building2 className="w-5 h-5 shrink-0 text-cyan-400" />
                    <div>
                      <div className="font-bold text-xs text-[var(--text-primary)]">Transferencia</div>
                      <div className="text-[10px] text-[var(--text-secondary)]">BCP / BBVA / Interbank</div>
                    </div>
                  </div>
                </div>
              </div>

              {/* STEP 5: Financial Breakdown */}
              <div className="p-4 rounded-2xl bg-black/10 dark:bg-white/5 border border-[var(--glass-border)] space-y-2 text-xs">
                <div className="flex justify-between text-[var(--text-secondary)]">
                  <span>Subtotal Alquiler ({cart.length} items):</span>
                  <span className="font-bold text-[var(--text-primary)]">S/ {subtotalAlquiler.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-[var(--text-secondary)]">
                  <span className="flex items-center gap-1">
                    <ShieldCheck className="w-3.5 h-3.5 text-emerald-500" />
                    Depósito Garantía Custodia (Reembolsable):
                  </span>
                  <span className="font-bold text-emerald-500">S/ {totalGarantia.toFixed(2)}</span>
                </div>
                <div className="pt-2 border-t border-[var(--glass-border)] flex justify-between items-center text-sm">
                  <span className="font-extrabold text-[var(--text-primary)]">Total a Cobrar Inicial:</span>
                  <span className="text-lg font-black gold-gradient-text">
                    S/ {montoTotal.toFixed(2)}
                  </span>
                </div>
              </div>

              {/* Submit Checkout Button */}
              <div className="pt-2">
                <motion.button
                  whileTap={{ scale: 0.96 }}
                  type="submit"
                  disabled={cart.length === 0}
                  className={'w-full py-3.5 rounded-xl font-extrabold text-sm flex items-center justify-center gap-2 shadow-xl transition-all ' + (
                    cart.length > 0
                      ? 'bg-amber-500 hover:bg-amber-400 text-black shadow-amber-500/25 cursor-pointer'
                      : 'bg-slate-300 dark:bg-gray-800 text-slate-500 dark:text-slate-400 cursor-not-allowed'
                  )}
                >
                  <CheckCircle2 className="w-5 h-5" />
                  Culminar Alquiler y Emitir Contrato
                  <ArrowRight className="w-4 h-4 ml-1" />
                </motion.button>
              </div>

            </form>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};
