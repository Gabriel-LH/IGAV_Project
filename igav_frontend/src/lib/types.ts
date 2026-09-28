export type GarmentStatus = 
  | 'DISPONIBLE' 
  | 'RESERVADO' 
  | 'ALQUILADO' 
  | 'EN_TINTORERIA' 
  | 'EN_REPARACION' 
  | 'DADO_DE_BAJA';

export type GarmentSize = 'XS' | 'S' | 'M' | 'L' | 'XL' | 'XXL' | 'MEDIDA_CUSTOM';

export interface Garment {
  id: number;
  codigoUnico: string;
  nombre: string;
  descripcion: string;
  color: string;
  talla: GarmentSize;
  precioAlquiler: number;
  precioVenta?: number;
  depositoGarantia: number;
  estado: GarmentStatus;
  usosAcumulados: number;
  maxUsosRecomendados: number;
  horasTintoreriaBloqueo: number;
  categoryName?: string;
  imageUrl?: string;
}

export interface Customer {
  id: number;
  tipoDocumento: 'DNI' | 'RUC' | 'PASAPORTE' | 'CARNET_EXTRANJERIA';
  numeroDocumento: string;
  nombres: string;
  apellidos: string;
  nombreCompleto: string;
  email: string;
  telefono: string;
  direccion: string;
  categoriaCliente: 'VIP' | 'FRECUENTE' | 'REGULAR';
  totalAlquileres: number;
  calificacion: number;
  fechaRegistro: string;
}

export interface Store {
  id: number;
  codigoTenant: string;
  nombre: string;
  direccion: string;
  telefono: string;
  ciudad: string;
  esSedePrincipal: boolean;
}

export type UserRole = 'ADMIN_SAAS' | 'ADMIN_STORE' | 'VENDEDOR' | 'ENCARGADO_TINTORERIA';

export interface User {
  id: number;
  username: string;
  nombreCompleto: string;
  email: string;
  rol: UserRole;
  storeId: number;
  storeNombre?: string;
  activo: boolean;
}

export type TailoringStatus = 'PENDIENTE_MEDICION' | 'EN_TALLER_COSTURA' | 'ENTALLADO_LISTO_ENTREGA';

export interface TailoringRecord {
  id: number;
  orderCodigo: string;
  clienteNombre: string;
  prendaNombre: string;
  prendaSku: string;
  sastreAsignado: string;
  bastaPantalonCm: number;
  cinturaCm: number;
  talleSacoCm: number;
  largoMangaCm: number;
  hombroCm: number;
  instruccionesSastre: string;
  fechaPrueba: string;
  estado: TailoringStatus;
}

export type OrderStatus = 
  | 'BORRADOR' 
  | 'CONFIRMADA' 
  | 'EN_ALQUILAR' 
  | 'DEVUELTO_PENDIENTE_TINTORERIA' 
  | 'COMPLETADA' 
  | 'CANCELADA';

export type OrderType = 'ALQUILAR' | 'VENTA' | 'MIXTO';

export interface OrderItem {
  garmentId: number;
  garmentName: string;
  garmentSku: string;
  tipoItem: 'ALQUILAR' | 'VENTA';
  precioAplicado: number;
  garantiaAplicada: number;
}

export interface Order {
  id: number;
  codigoContrato: string;
  customerId?: number;
  clienteDocumento?: string;
  clienteNombreCompleto: string;
  clienteTelefono?: string;
  storeId: number;
  tipo: OrderType;
  fechaEntregaAcordada: string;
  fechaDevolucionAcordada: string;
  subtotal: number;
  montoGarantiaTotal: number;
  descuentoGarantia: number;
  montoPenalizacion: number;
  montoTotal: number;
  garantiaDevueltaNeta: number;
  estado: OrderStatus;
  metodoPago?: 'TARJETA_CREDITO_DEBITO' | 'BILLETERA_DIGITAL_YAPE_PLIN' | 'EFECTIVO' | 'TRANSFERENCIA';
  items?: OrderItem[];
}

export interface IncidentRequest {
  garmentId: number;
  tipoIncidencia: 'MANCHA' | 'ROTURA_DANIO' | 'RETRASO_DEVOLUCION' | 'PERDIDA_TOTAL';
  montoDescuentoGarantia: number;
  descripcion: string;
}

export interface ReturnOrderRequest {
  orderId: number;
  montoPenalizacionMora: number;
  incidencias: IncidentRequest[];
  updatedBy: string;
}
