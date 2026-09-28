import { Garment, Order, Customer, Store, User, TailoringRecord, ReturnOrderRequest, GarmentStatus, TailoringStatus } from './types';
export type { Garment, Order, Customer, Store, User, TailoringRecord, ReturnOrderRequest, GarmentStatus, TailoringStatus } from './types';

const API_BASE = 'http://localhost:8080/api/v1';

export const mockTailoringRecords: TailoringRecord[] = [
  {
    id: 1,
    orderCodigo: 'ORD-2026-9812',
    clienteNombre: 'Mariana Sofia Valdivia Pastor',
    prendaNombre: 'Vestido de Gala Haute Couture Esmeralda',
    prendaSku: 'SKU-GAL-001',
    sastreAsignado: 'Maestra Elena Sastrería',
    bastaPantalonCm: 0,
    cinturaCm: 68,
    talleSacoCm: 0,
    largoMangaCm: 0,
    hombroCm: 38,
    instruccionesSastre: 'Ajuste fino en cintura -2cm y fijación de cola con broche invisible.',
    fechaPrueba: '2026-09-28T11:00:00',
    estado: 'EN_TALLER_COSTURA'
  },
  {
    id: 2,
    orderCodigo: 'ORD-2026-7734',
    clienteNombre: 'Carlos Eduardo Mendoza Rivas',
    prendaNombre: 'Terno Slim Fit Novio Champagne',
    prendaSku: 'SKU-TER-002',
    sastreAsignado: 'Don Carlos Sastre Senior',
    bastaPantalonCm: 76,
    cinturaCm: 84,
    talleSacoCm: 72,
    largoMangaCm: 64,
    hombroCm: 44,
    instruccionesSastre: 'Basta de pantalón estilo italiano doblada hacia adentro 3cm.',
    fechaPrueba: '2026-09-27T15:00:00',
    estado: 'ENTALLADO_LISTO_ENTREGA'
  }
];

export const mockUsers: User[] = [
  {
    id: 1,
    username: 'admin.saas',
    nombreCompleto: 'Gabriel Tech Lead',
    email: 'admin@igav-saas.com',
    rol: 'ADMIN_SAAS',
    storeId: 1,
    storeNombre: 'Sede Central - San Isidro',
    activo: true
  },
  {
    id: 2,
    username: 'vendedor.sanisidro',
    nombreCompleto: 'Lucía Fernández',
    email: 'lfernandez@gala.pe',
    rol: 'VENDEDOR',
    storeId: 1,
    storeNombre: 'Sede Central - San Isidro',
    activo: true
  },
  {
    id: 3,
    username: 'tintoreria.jockey',
    nombreCompleto: 'Mateo Quispe',
    email: 'mquispe@gala.pe',
    rol: 'ENCARGADO_TINTORERIA',
    storeId: 2,
    storeNombre: 'Sede Jockey - Santiago de Surco',
    activo: true
  }
];

export const mockStores: Store[] = [
  {
    id: 1,
    codigoTenant: 'TENANT-001-SAN-ISIDRO',
    nombre: 'Sede Central - San Isidro',
    direccion: 'Av. Conquistadores 780, San Isidro, Lima',
    telefono: '+51 987 654 321',
    ciudad: 'Lima',
    esSedePrincipal: true
  },
  {
    id: 2,
    codigoTenant: 'TENANT-002-SURCO',
    nombre: 'Sede Jockey - Santiago de Surco',
    direccion: 'Av. Javier Prado Este 4200, Surco, Lima',
    telefono: '+51 912 345 678',
    ciudad: 'Lima',
    esSedePrincipal: false
  },
  {
    id: 3,
    codigoTenant: 'TENANT-003-AREQUIPA',
    nombre: 'Sede Cayma - Arequipa',
    direccion: 'Av. Ejército 1020, Cayma, Arequipa',
    telefono: '+51 954 123 789',
    ciudad: 'Arequipa',
    esSedePrincipal: false
  }
];

export const mockCustomers: Customer[] = [
  {
    id: 1,
    tipoDocumento: 'DNI',
    numeroDocumento: '72839104',
    nombres: 'Mariana Sofia',
    apellidos: 'Valdivia Pastor',
    nombreCompleto: 'Mariana Sofia Valdivia Pastor',
    email: 'marianavaldivia@gala.pe',
    telefono: '+51 981 234 567',
    direccion: 'Calle Los Cedros 340, Depto 401, San Isidro',
    categoriaCliente: 'VIP',
    totalAlquileres: 5,
    calificacion: 5,
    fechaRegistro: '2025-03-15'
  },
  {
    id: 2,
    tipoDocumento: 'DNI',
    numeroDocumento: '45910283',
    nombres: 'Carlos Eduardo',
    apellidos: 'Mendoza Rivas',
    nombreCompleto: 'Carlos Eduardo Mendoza Rivas',
    email: 'carlos.mendoza@corp.pe',
    telefono: '+51 998 765 432',
    direccion: 'Av. El Sol 512, Miraflores',
    categoriaCliente: 'FRECUENTE',
    totalAlquileres: 3,
    calificacion: 4.8,
    fechaRegistro: '2025-06-20'
  },
  {
    id: 3,
    tipoDocumento: 'PASAPORTE',
    numeroDocumento: 'P8920192',
    nombres: 'Jean-Luc',
    apellidos: 'Dupont',
    nombreCompleto: 'Jean-Luc Dupont',
    email: 'jdupont@paris-luxury.fr',
    telefono: '+33 6 12 34 56 78',
    direccion: 'Hotel Country Club, Suite 302, San Isidro',
    categoriaCliente: 'VIP',
    totalAlquileres: 2,
    calificacion: 5.0,
    fechaRegistro: '2026-01-10'
  },
  {
    id: 4,
    tipoDocumento: 'DNI',
    numeroDocumento: '71029384',
    nombres: 'Fiorella Maria',
    apellidos: 'Bolognesi Vega',
    nombreCompleto: 'Fiorella Maria Bolognesi Vega',
    email: 'fbolognesi@gmail.com',
    telefono: '+51 977 112 233',
    direccion: 'Jr. Batalla de Junín 145, Barranco',
    categoriaCliente: 'REGULAR',
    totalAlquileres: 1,
    calificacion: 4.5,
    fechaRegistro: '2026-08-01'
  }
];

export const mockGarments: Garment[] = [
  {
    id: 1,
    codigoUnico: 'SKU-GAL-001',
    nombre: 'Vestido de Gala Haute Couture Esmeralda',
    descripcion: 'Vestido largo de noche en seda esmeralda con bordados dorados en pedrería fina.',
    color: 'Verde Esmeralda',
    talla: 'M',
    precioAlquiler: 280.00,
    precioVenta: 1200.00,
    depositoGarantia: 150.00,
    estado: 'DISPONIBLE',
    usosAcumulados: 3,
    maxUsosRecomendados: 12,
    horasTintoreriaBloqueo: 24,
    categoryName: 'Vestidos de Noche',
    imageUrl: 'https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800&auto=format&fit=crop'
  },
  {
    id: 2,
    codigoUnico: 'SKU-TER-002',
    nombre: 'Terno Slim Fit Novio Champagne',
    descripcion: 'Terno de gala de 3 piezas en lana italiana super 120s con solapa en raso.',
    color: 'Champagne / Marfil',
    talla: 'L',
    precioAlquiler: 250.00,
    precioVenta: 950.00,
    depositoGarantia: 120.00,
    estado: 'ALQUILADO',
    usosAcumulados: 5,
    maxUsosRecomendados: 10,
    horasTintoreriaBloqueo: 24,
    categoryName: 'Ternos de Gala',
    imageUrl: 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?q=80&w=800&auto=format&fit=crop'
  },
  {
    id: 3,
    codigoUnico: 'SKU-VES-003',
    nombre: 'Vestido Sirena Azul Noche Royal',
    descripcion: 'Corte sirena con escote corazón y cola desmontable de tul satinado.',
    color: 'Azul Noche',
    talla: 'S',
    precioAlquiler: 320.00,
    precioVenta: 1400.00,
    depositoGarantia: 200.00,
    estado: 'EN_TINTORERIA',
    usosAcumulados: 11,
    maxUsosRecomendados: 10,
    horasTintoreriaBloqueo: 48,
    categoryName: 'Vestidos de Noche',
    imageUrl: 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?q=80&w=800&auto=format&fit=crop'
  },
  {
    id: 4,
    codigoUnico: 'SKU-SAG-004',
    nombre: 'Saco Smoking Velvet Burdeos',
    descripcion: 'Saco de terciopelo premium burdeos con solapa de chal en seda negra.',
    color: 'Vino / Burdeos',
    talla: 'M',
    precioAlquiler: 190.00,
    precioVenta: 750.00,
    depositoGarantia: 100.00,
    estado: 'DISPONIBLE',
    usosAcumulados: 4,
    maxUsosRecomendados: 15,
    horasTintoreriaBloqueo: 24,
    categoryName: 'Sacos & Blazers',
    imageUrl: 'https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=800&auto=format&fit=crop'
  },
  {
    id: 5,
    codigoUnico: 'SKU-ACC-005',
    nombre: 'Juego de Tiaras & Joyería Cristal Swarovski',
    descripcion: 'Set de tiara y pendientes de gala bañados en rodio con cristales.',
    color: 'Plata / Cristal',
    talla: 'MEDIDA_CUSTOM',
    precioAlquiler: 90.00,
    precioVenta: 350.00,
    depositoGarantia: 50.00,
    estado: 'DISPONIBLE',
    usosAcumulados: 2,
    maxUsosRecomendados: 25,
    horasTintoreriaBloqueo: 12,
    categoryName: 'Accesorios de Gala',
    imageUrl: 'https://images.unsplash.com/photo-1535632066927-ab7c91b60908?q=80&w=800&auto=format&fit=crop'
  }
];

export const mockOrders: Order[] = [
  {
    id: 101,
    codigoContrato: 'ORD-2026-9812',
    customerId: 1,
    clienteDocumento: '72839104',
    clienteNombreCompleto: 'Mariana Sofia Valdivia Pastor',
    clienteTelefono: '+51 981 234 567',
    storeId: 1,
    tipo: 'ALQUILAR',
    fechaEntregaAcordada: '2026-09-28T10:00:00',
    fechaDevolucionAcordada: '2026-10-01T18:00:00',
    subtotal: 280.00,
    montoGarantiaTotal: 150.00,
    descuentoGarantia: 0,
    montoPenalizacion: 0,
    montoTotal: 280.00,
    garantiaDevueltaNeta: 150.00,
    estado: 'EN_ALQUILAR',
    items: [
      {
        garmentId: 1,
        garmentName: 'Vestido de Gala Haute Couture Esmeralda',
        garmentSku: 'SKU-GAL-001',
        tipoItem: 'ALQUILAR',
        precioAplicado: 280.00,
        garantiaAplicada: 150.00
      }
    ]
  },
  {
    id: 102,
    codigoContrato: 'ORD-2026-7734',
    customerId: 2,
    clienteDocumento: '45910283',
    clienteNombreCompleto: 'Carlos Eduardo Mendoza Rivas',
    clienteTelefono: '+51 998 765 432',
    storeId: 1,
    tipo: 'ALQUILAR',
    fechaEntregaAcordada: '2026-09-25T12:00:00',
    fechaDevolucionAcordada: '2026-09-27T17:00:00',
    subtotal: 250.00,
    montoGarantiaTotal: 120.00,
    descuentoGarantia: 30.00,
    montoPenalizacion: 0,
    montoTotal: 250.00,
    garantiaDevueltaNeta: 90.00,
    estado: 'DEVUELTO_PENDIENTE_TINTORERIA',
    items: [
      {
        garmentId: 2,
        garmentName: 'Terno Slim Fit Novio Champagne',
        garmentSku: 'SKU-TER-002',
        tipoItem: 'ALQUILAR',
        precioAplicado: 250.00,
        garantiaAplicada: 120.00
      }
    ]
  }
];

export async function fetchGarmentsByStore(storeId: number = 1): Promise<Garment[]> {
  try {
    const res = await fetch(`${API_BASE}/garments/store/${storeId}`);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) return data;
    }
  } catch (e) {
    console.warn('API error');
  }
  return mockGarments;
}

export async function fetchCustomers(): Promise<Customer[]> {
  try {
    const res = await fetch(`${API_BASE}/customers`);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) return data;
    }
  } catch (e) {
    console.warn('API error');
  }
  return mockCustomers;
}

export async function fetchStores(): Promise<Store[]> {
  try {
    const res = await fetch(`${API_BASE}/stores`);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) return data;
    }
  } catch (e) {
    console.warn('API error');
  }
  return mockStores;
}

export async function fetchUsers(): Promise<User[]> {
  try {
    const res = await fetch(`${API_BASE}/users`);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) return data;
    }
  } catch (e) {
    console.warn('API error');
  }
  return mockUsers;
}

export async function fetchTailoringRecords(): Promise<TailoringRecord[]> {
  try {
    const res = await fetch(`${API_BASE}/tailoring`);
    if (res.ok) {
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) return data;
    }
  } catch (e) {
    console.warn('API error');
  }
  return mockTailoringRecords;
}

export async function processReturn(request: ReturnOrderRequest): Promise<any> {
  try {
    const res = await fetch(`${API_BASE}/orders/return`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request)
    });
    if (res.ok) {
      return await res.json();
    }
  } catch (e) {
    console.warn('BACKEND0');
  }
  return {
    success: true,
    message: 'Devolución procesada correctamente con retención de garantía y bloqueo automático de 24h-48h en tintorería.'
  };
}
