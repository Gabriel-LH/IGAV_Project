'use client';

import React, { useEffect, useState } from 'react';
import { Navbar } from '@/components/Navbar';
import { Sidebar, TabType } from '@/components/Sidebar';
import { DashboardOverview } from '@/components/DashboardOverview';
import { GarmentCatalog } from '@/components/GarmentCatalog';
import { CustomerManagement } from '@/components/CustomerManagement';
import { OrderManagement } from '@/components/OrderManagement';
import { TailoringManagement } from '@/components/TailoringManagement';
import { LaundryManagement } from '@/components/LaundryManagement';
import { RotationAlertsView } from '@/components/RotationAlertsView';
import { StoreManagement } from '@/components/StoreManagement';
import { UserManagement } from '@/components/UserManagement';
import { ReturnInspectionModal } from '@/components/ReturnInspectionModal';
import { RentalCartDrawer } from '@/components/RentalCartDrawer';
import { ContractPdfModal } from '@/components/ContractPdfModal';
import { WhatsAppNotificationModal } from '@/components/WhatsAppNotificationModal';
import { LoginModal, AuthSession } from '@/components/LoginModal';
import { Garment, Customer, Order, Store, User, TailoringRecord, TailoringStatus, fetchGarmentsByStore, fetchCustomers, fetchStores, fetchUsers } from '@/lib/api';
import { Toaster, toast } from 'sonner';
import { useTheme } from 'next-themes';

const initialGarmentsMock: Garment[] = [
  {
    id: 101,
    codigoUnico: 'VEST-GAL-001',
    nombre: 'Vestido de Gala Haute Couture Marfil',
    talla: 'S',
    color: 'Marfil / Champagne',
    precioAlquiler: 380.0,
    precioVenta: 2400.0,
    depositoGarantia: 200.0,
    estado: 'DISPONIBLE',
    categoryName: 'Vestidos de Gala',
    usosAcumulados: 3,
    maxUsosRecomendados: 10,
    horasTintoreriaBloqueo: 24,
    imageUrl: 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=600',
    descripcion: 'Vestido de seda natural con pedreria cosida a mano y corte sirena.'
  },
  {
    id: 102,
    codigoUnico: 'SLIP-NOV-002',
    nombre: 'Esmoquin Black Tie Italiano Royale',
    talla: 'M',
    color: 'Negro Obsidian',
    precioAlquiler: 320.0,
    precioVenta: 1950.0,
    depositoGarantia: 180.0,
    estado: 'ALQUILADO',
    categoryName: 'Ternos de Gala',
    usosAcumulados: 7,
    maxUsosRecomendados: 12,
    horasTintoreriaBloqueo: 48,
    imageUrl: 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?q=80&w=600',
    descripcion: 'Lana super 120s con solapa en raso de seda negra.'
  },
  {
    id: 103,
    codigoUnico: 'VEST-NOCH-003',
    nombre: 'Vestido Corte Princesa Azul Noche',
    talla: 'M',
    color: 'Azul Noche',
    precioAlquiler: 450.0,
    precioVenta: 3100.0,
    depositoGarantia: 250.0,
    estado: 'EN_TINTORERIA',
    categoryName: 'Vestidos de Gala',
    usosAcumulados: 10,
    maxUsosRecomendados: 10,
    horasTintoreriaBloqueo: 48,
    imageUrl: 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?q=80&w=600',
    descripcion: 'Bordado en hilo de plata con escote de corazon y cola imperial.'
  },
  {
    id: 104,
    codigoUnico: 'SMOK-LUX-004',
    nombre: 'Saco Blazer Terciopelo Burdeos',
    talla: 'L',
    color: 'Vino Burdeos',
    precioAlquiler: 290.0,
    precioVenta: 1600.0,
    depositoGarantia: 150.0,
    estado: 'DISPONIBLE',
    categoryName: 'Sacos y Blazers',
    usosAcumulados: 2,
    maxUsosRecomendados: 15,
    horasTintoreriaBloqueo: 24,
    imageUrl: 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=600',
    descripcion: 'Terciopelo de algodon premium con botones de gala forrados.'
  }
];

const initialCustomersMock: Customer[] = [
  {
    id: 1,
    tipoDocumento: 'DNI',
    numeroDocumento: '47892301',
    nombres: 'Valentina',
    apellidos: 'De La Torre',
    nombreCompleto: 'Valentina De La Torre',
    email: 'valentina.delatorre@gala.com',
    telefono: '+51 987 654 321',
    direccion: 'Av. Primavera 1230, San Isidro',
    categoriaCliente: 'VIP',
    totalAlquileres: 8,
    calificacion: 5.0,
    fechaRegistro: '2025-11-12'
  },
  {
    id: 2,
    tipoDocumento: 'DNI',
    numeroDocumento: '10982345',
    nombres: 'Carlos Hugo',
    apellidos: 'Mendoza Paz',
    nombreCompleto: 'Carlos Hugo Mendoza Paz',
    email: 'carlos.mendoza@empresa.pe',
    telefono: '+51 991 234 567',
    direccion: 'Calle Los Naranjos 450, Miraflores',
    categoriaCliente: 'REGULAR',
    totalAlquileres: 2,
    calificacion: 4.8,
    fechaRegistro: '2026-01-20'
  }
];

const initialOrdersMock: Order[] = [
  {
    id: 501,
    codigoContrato: 'CTR-2026-0089',
    customerId: 1,
    clienteDocumento: '47892301',
    clienteNombreCompleto: 'Valentina De La Torre',
    clienteTelefono: '+51 987 654 321',
    storeId: 1,
    tipo: 'ALQUILAR',
    fechaEntregaAcordada: '2026-09-28',
    fechaDevolucionAcordada: '2026-10-01',
    subtotal: 380.0,
    montoGarantiaTotal: 200.0,
    descuentoGarantia: 0,
    montoPenalizacion: 0,
    montoTotal: 580.0,
    garantiaDevueltaNeta: 200.0,
    estado: 'EN_ALQUILAR',
    items: [
      {
        garmentId: 101,
        garmentName: 'Vestido de Gala Haute Couture Marfil',
        garmentSku: 'VEST-GAL-001',
        tipoItem: 'ALQUILAR',
        precioAplicado: 380.0,
        garantiaAplicada: 200.0
      }
    ]
  }
];

const initialStoresMock: Store[] = [
  {
    id: 1,
    codigoTenant: 'TENANT-LIMA-CENTRAL',
    nombre: 'Sede Principal San Isidro',
    direccion: 'Av. Conquistadores 890, San Isidro',
    telefono: '+51 1 421-9988',
    ciudad: 'Lima',
    esSedePrincipal: true
  },
  {
    id: 2,
    codigoTenant: 'TENANT-MIRAFLORES',
    nombre: 'Boutique Novias Miraflores',
    direccion: 'Av. Larco 1120, Miraflores',
    telefono: '+51 1 445-3322',
    ciudad: 'Lima',
    esSedePrincipal: false
  }
];

const initialUsersMock: User[] = [
  {
    id: 1,
    username: 'admin.gala',
    nombreCompleto: 'Tech Lead Administrador',
    email: 'admin@igav.pe',
    rol: 'ADMIN_SAAS',
    activo: true,
    storeId: 1,
    storeNombre: 'Sede Principal San Isidro'
  },
  {
    id: 2,
    username: 'vendedor.novias',
    nombreCompleto: 'Sofia Loren (Asesora)',
    email: 'sofia@igav.pe',
    rol: 'VENDEDOR',
    activo: true,
    storeId: 2,
    storeNombre: 'Boutique Novias Miraflores'
  }
];

const initialTailoringRecordsMock: TailoringRecord[] = [
  {
    id: 1,
    orderCodigo: 'CTR-2026-0089',
    clienteNombre: 'Valentina De La Torre',
    prendaNombre: 'Vestido de Gala Haute Couture Marfil',
    prendaSku: 'VEST-GAL-001',
    sastreAsignado: 'Maestra Elena Sastrería',
    bastaPantalonCm: 0,
    cinturaCm: -2,
    talleSacoCm: 0,
    largoMangaCm: 0,
    hombroCm: 0,
    instruccionesSastre: 'Entallar cintura 2cm para ajuste de gala perfecto.',
    fechaPrueba: '2026-09-27',
    estado: 'EN_TALLER_COSTURA'
  }
];

export default function AppHome() {
  const { theme, setTheme } = useTheme();

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const urlParams = new URLSearchParams(window.location.search);
      const themeParam = urlParams.get('theme');
      if (themeParam === 'light' || themeParam === 'dark') {
        setTheme(themeParam);
      }
    }
  }, []);
  const [activeTab, setActiveTabState] = useState<TabType>('dashboard');

  const setActiveTab = (tab: TabType) => {
    setActiveTabState(tab);
    if (typeof window !== 'undefined') {
      window.location.hash = tab;
    }
  };

  useEffect(() => {
    const handleHash = () => {
      if (typeof window !== 'undefined') {
        const hash = window.location.hash.replace('#', '') as TabType;
        const validTabs: TabType[] = ['dashboard', 'garments', 'customers', 'orders', 'tailoring', 'laundry', 'alerts', 'stores', 'users'];
        if (validTabs.includes(hash)) {
          setActiveTabState(hash);
        }
      }
    };
    handleHash();
    window.addEventListener('hashchange', handleHash);
    return () => window.removeEventListener('hashchange', handleHash);
  }, []);
  const [garments, setGarments] = useState<Garment[]>(initialGarmentsMock);
  const [customers, setCustomers] = useState<Customer[]>(initialCustomersMock);
  const [orders, setOrders] = useState<Order[]>(initialOrdersMock);
  const [stores, setStores] = useState<Store[]>(initialStoresMock);
  const [users, setUsers] = useState<User[]>(initialUsersMock);
  const [tailoringRecords, setTailoringRecords] = useState<TailoringRecord[]>(initialTailoringRecordsMock);

  const [activeStoreId, setActiveStoreId] = useState<number>(1);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isOpenNewOrderModal, setIsOpenNewOrderModal] = useState<boolean>(false);
  const [isOpenReturnModal, setIsOpenReturnModal] = useState<boolean>(false);
  const [selectedReturnOrderId, setSelectedReturnOrderId] = useState<number | undefined>(undefined);
  const [cart, setCart] = useState<Garment[]>([]);
  const [isCartOpen, setIsCartOpen] = useState<boolean>(false);

  // Estados para las nuevas funcionalidades: JWT, PDF y WhatsApp (wsp-js)
  const [userSession, setUserSession] = useState<AuthSession | null>(null);
  const [isLoginModalOpen, setIsLoginModalOpen] = useState<boolean>(false);
  const [selectedOrderForPdf, setSelectedOrderForPdf] = useState<Order | null>(null);
  const [selectedOrderForWhatsApp, setSelectedOrderForWhatsApp] = useState<Order | null>(null);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const savedUser = localStorage.getItem('igav_user');
      if (savedUser) {
        try {
          setUserSession(JSON.parse(savedUser));
        } catch (e) {
          console.error('Error parseando sesión guardada', e);
        }
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('igav_token');
    localStorage.removeItem('igav_user');
    setUserSession(null);
    toast.info('Sesión cerrada correctamente');
  };

  const currentStore = stores.find((s) => s.id === activeStoreId) || stores[0];

  useEffect(() => {
    loadBackendData();
  }, [activeStoreId]);

  const loadBackendData = async () => {
    setIsLoading(true);
    try {
      const gData = await fetchGarmentsByStore(activeStoreId);
      const cData = await fetchCustomers();
      const sData = await fetchStores();
      const uData = await fetchUsers();

      if (Array.isArray(gData) && gData.length > 0) setGarments(gData);
      if (Array.isArray(cData) && cData.length > 0) setCustomers(cData);
      if (Array.isArray(sData) && sData.length > 0) setStores(sData);
      if (Array.isArray(uData) && uData.length > 0) setUsers(uData);

      toast.success('Sincronizacion completa con Spring Boot API');
    } catch (err) {
      console.log('Modo offline / datos locales cargados');
    } finally {
      setIsLoading(false);
    }
  };

  const handleAddGarment = (newG: Omit<Garment, 'id' | 'usosAcumulados' | 'estado'>) => {
    const created: Garment = {
      ...newG,
      id: Date.now(),
      usosAcumulados: 0,
      estado: 'DISPONIBLE'
    };
    setGarments((prev) => [created, ...prev]);
  };

  const handleAddCustomer = (newC: Omit<Customer, 'id' | 'totalAlquileres' | 'calificacion' | 'fechaRegistro'>) => {
    const created: Customer = {
      ...newC,
      id: Date.now(),
      totalAlquileres: 0,
      calificacion: 5.0,
      fechaRegistro: new Date().toISOString().split('T')[0]
    };
    setCustomers((prev) => [created, ...prev]);
  };

  const handleAddStore = (newS: Omit<Store, 'id'>) => {
    const created: Store = { ...newS, id: Date.now() };
    setStores((prev) => [...prev, created]);
  };

  const handleAddUser = (newU: Omit<User, 'id'>) => {
    const created: User = {
      ...newU,
      id: Date.now()
    };
    setUsers((prev) => [...prev, created]);
  };

  const handleAddTailoringRecord = (newT: Omit<TailoringRecord, 'id'>) => {
    const created: TailoringRecord = { ...newT, id: Date.now() };
    setTailoringRecords((prev) => [created, ...prev]);
  };

  const handleUpdateTailoringStatus = (recordId: number, status: TailoringStatus) => {
    setTailoringRecords((prev) =>
      prev.map((r) => (r.id === recordId ? { ...r, estado: status } : r))
    );
  };

  const handleToggleUserStatus = (userId: number) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === userId ? { ...u, activo: !u.activo } : u))
    );
  };

  const handleAddOrder = (newOrder: Order) => {
    setOrders((prev) => [newOrder, ...prev]);
    const garmentIds = newOrder.items?.map(i => i.garmentId) || [];
    if (garmentIds.length > 0) {
      setGarments((prev) =>
        prev.map((g) =>
          garmentIds.includes(g.id)
            ? { ...g, estado: 'ALQUILADO', usosAcumulados: g.usosAcumulados + 1 }
            : g
        )
      );
    }
    if (newOrder.customerId) {
      setCustomers((prev) =>
        prev.map((c) =>
          c.id === newOrder.customerId
            ? { ...c, totalAlquileres: c.totalAlquileres + 1 }
            : c
        )
      );
    }
  };

  const handleReturnProcessed = (
    updatedOrderId: number,
    deductedDeposit: number,
    laundryHours: number
  ) => {
    setOrders((prev) =>
      prev.map((o) => {
        if (o.id === updatedOrderId) {
          return {
            ...o,
            descuentoGarantia: deductedDeposit,
            garantiaDevueltaNeta: Math.max(0, o.montoGarantiaTotal - deductedDeposit),
            estado: 'DEVUELTO_PENDIENTE_TINTORERIA'
          };
        }
        return o;
      })
    );
    const orderObj = orders.find((o) => o.id === updatedOrderId);
    const garmentId = orderObj?.items?.[0]?.garmentId;
    if (garmentId) {
      setGarments((prev) =>
        prev.map((g) =>
          g.id === garmentId
            ? {
                ...g,
                estado: 'EN_TINTORERIA',
                horasTintoreriaBloqueo: laundryHours
              }
            : g
        )
      );
    }
  };

  const handleReleaseGarment = (garmentId: number) => {
    setGarments((prev) =>
      prev.map((g) => (g.id === garmentId ? { ...g, estado: 'DISPONIBLE' } : g))
    );
  };

  const handleResetWear = (garmentId: number) => {
    setGarments((prev) =>
      prev.map((g) => (g.id === garmentId ? { ...g, usosAcumulados: 0 } : g))
    );
  };

  const handleRetireGarment = (garmentId: number) => {
    setGarments((prev) =>
      prev.map((g) => (g.id === garmentId ? { ...g, estado: 'DADO_DE_BAJA' } : g))
    );
  };

  
  const handleAddToCart = (garment: Garment) => {
    if (garment.estado !== 'DISPONIBLE') {
      toast.warning(`La prenda "${garment.nombre}" no está disponible actualmente.`);
      return;
    }
    if (cart.some(g => g.id === garment.id)) {
      toast.info(`La prenda "${garment.nombre}" ya se encuentra en tu carrito.`);
      return;
    }
    setCart(prev => [...prev, garment]);
    toast.success(`"${garment.nombre}" agregada al carrito de alquileres.`, {
      description: `Talla ${garment.talla} • Alquiler S/ ${garment.precioAlquiler.toFixed(2)}`
    });
  };

  const handleRemoveFromCart = (garmentId: number) => {
    setCart(prev => prev.filter(g => g.id !== garmentId));
    toast.info('Prenda removida del carrito.');
  };

  const handleClearCart = () => {
    setCart([]);
  };

  const handleCheckoutComplete = (newOrder: Order, newCustomer?: Customer) => {
    if (newCustomer) {
      setCustomers(prev => [newCustomer, ...prev]);
    }
    handleAddOrder(newOrder);
  };

  const openReturnForOrder = (orderId?: number) => {
    setSelectedReturnOrderId(orderId);
    setIsOpenReturnModal(true);
  };

  const handleSelectCustomerForOrder = (customer: Customer) => {
    setActiveTab('orders');
    setIsOpenNewOrderModal(true);
  };

  const rotationAlertCount = garments.filter(
    (g) => g.usosAcumulados >= g.maxUsosRecomendados
  ).length;

  const laundryCount = garments.filter((g) => g.estado === 'EN_TINTORERIA').length;

  return (
    <div className="min-h-screen bg-[var(--bg-main)] text-[var(--text-primary)] flex flex-col transition-colors duration-300">
      <Toaster position="bottom-right" theme="dark" richColors />

      <Navbar
        currentStore={currentStore.nombre}
        onRefresh={loadBackendData}
        isLoading={isLoading}
        cartCount={cart.length}
        onOpenCart={() => setIsCartOpen(true)}
        userSession={userSession}
        onOpenLogin={() => setIsLoginModalOpen(true)}
        onLogout={handleLogout}
      />

      <div className="flex-1 flex w-full">
        <Sidebar
          activeTab={activeTab}
          setActiveTab={setActiveTab}
          rotationAlertCount={rotationAlertCount}
          laundryCount={laundryCount}
          customerCount={customers.length}
          userCount={users.length}
          tailoringCount={tailoringRecords.filter(t => t.estado !== 'ENTALLADO_LISTO_ENTREGA').length}
        />

        <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto min-w-0 w-full">
          {activeTab === 'dashboard' && (
            <DashboardOverview
              garments={garments}
              orders={orders}
              onNavigate={(tab) => setActiveTab(tab)}
              onOpenNewOrder={() => setIsOpenNewOrderModal(true)}
              onOpenReturnModal={openReturnForOrder}
            />
          )}

          {activeTab === 'garments' && (
            <GarmentCatalog
              garments={garments}
              onAddGarment={handleAddGarment}
              onBookGarment={(garment) => {
                handleAddToCart(garment);
                setIsCartOpen(true);
              }}
              onAddToCart={handleAddToCart}
              cartGarmentIds={cart.map(g => g.id)}
            />
          )}

          {activeTab === 'customers' && (
            <CustomerManagement
              customers={customers}
              onAddCustomer={handleAddCustomer}
              onSelectCustomerForOrder={(customer) => {
                setIsOpenNewOrderModal(true);
                toast.info(`Iniciando nuevo contrato para cliente ${customer.nombreCompleto}.`);
              }}
            />
          )}

          {activeTab === 'orders' && (
            <OrderManagement
              orders={orders}
              garments={garments}
              customers={customers}
              isOpenNewModal={isOpenNewOrderModal}
              setIsOpenNewModal={setIsOpenNewOrderModal}
              onAddOrder={handleAddOrder}
              onDownloadPdf={(order) => setSelectedOrderForPdf(order)}
              onSendWhatsApp={(order) => setSelectedOrderForWhatsApp(order)}
            />
          )}

          {activeTab === 'tailoring' && (
            <TailoringManagement
              records={tailoringRecords}
              orders={orders}
              onAddRecord={handleAddTailoringRecord}
              onUpdateStatus={handleUpdateTailoringStatus}
            />
          )}

          {activeTab === 'laundry' && (
            <LaundryManagement
              laundryGarments={garments.filter((g) => g.estado === 'EN_TINTORERIA')}
              onReleaseGarment={handleReleaseGarment}
            />
          )}

          {activeTab === 'alerts' && (
            <RotationAlertsView
              garments={garments}
              onResetWear={handleResetWear}
              onRetireGarment={handleRetireGarment}
            />
          )}

          {activeTab === 'stores' && (
            <StoreManagement
              stores={stores}
              activeStoreId={activeStoreId}
              onSelectActiveStore={setActiveStoreId}
              onAddStore={handleAddStore}
            />
          )}

          {activeTab === 'users' && (
            <UserManagement
              users={users}
              stores={stores}
              onAddUser={handleAddUser}
              onToggleUserStatus={handleToggleUserStatus}
            />
          )}
        </main>
      </div>

      <RentalCartDrawer
        isOpen={isCartOpen}
        onClose={() => setIsCartOpen(false)}
        cart={cart}
        onRemoveFromCart={handleRemoveFromCart}
        onClearCart={handleClearCart}
        customers={customers}
        onCheckoutComplete={handleCheckoutComplete}
      />

      <ReturnInspectionModal
        isOpen={isOpenReturnModal}
        onClose={() => setIsOpenReturnModal(false)}
        orders={orders}
        selectedOrderId={selectedReturnOrderId}
        onReturnProcessed={handleReturnProcessed}
      />

      {/* Modales de Valor Agregado: PDF de Contratos, Notificaciones WhatsApp y Login JWT */}
      <ContractPdfModal
        isOpen={!!selectedOrderForPdf}
        onClose={() => setSelectedOrderForPdf(null)}
        order={selectedOrderForPdf}
        customer={customers.find(c => c.id === selectedOrderForPdf?.customerId)}
        store={stores.find(s => s.id === selectedOrderForPdf?.storeId) || currentStore}
        onSendWhatsApp={(order) => {
          setSelectedOrderForPdf(null);
          setSelectedOrderForWhatsApp(order);
        }}
      />

      <WhatsAppNotificationModal
        isOpen={!!selectedOrderForWhatsApp}
        onClose={() => setSelectedOrderForWhatsApp(null)}
        order={selectedOrderForWhatsApp}
        customer={customers.find(c => c.id === selectedOrderForWhatsApp?.customerId)}
        storeName={stores.find(s => s.id === selectedOrderForWhatsApp?.storeId)?.nombre || currentStore.nombre}
      />

      <LoginModal
        isOpen={isLoginModalOpen}
        onClose={() => setIsLoginModalOpen(false)}
        onLoginSuccess={(session) => setUserSession(session)}
      />
    </div>
  );
}
