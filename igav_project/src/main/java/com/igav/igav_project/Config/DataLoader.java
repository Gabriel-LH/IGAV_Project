package com.igav.igav_project.Config;

import com.igav.igav_project.Model.Entity.Inventory.*;
import com.igav.igav_project.Model.Entity.Order.*;
import com.igav.igav_project.Model.Entity.Store.*;
import com.igav.igav_project.Model.Entity.User.*;
import com.igav.igav_project.Model.Shared.ValueObjects.*;
import com.igav.igav_project.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final GarmentRepository garmentRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public DataLoader(
            StoreRepository storeRepository,
            CategoryRepository categoryRepository,
            GarmentRepository garmentRepository,
            CustomerRepository customerRepository,
            UserRepository userRepository
    ) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.garmentRepository = garmentRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (storeRepository.count() > 0) {
            System.out.println(">>> Base de datos ya cuenta con datos cargados. Saltando DataLoader.");
            return;
        }

        System.out.println(">>> Inicializando datos de prueba en la base de datos MySQL (igav_db)...");

        // 1. Tiendas (Sedes)
        Store store1 = Store.create(
                "Sede Central - San Isidro",
                "IGAV Gala Luxury S.A.C.",
                new DocumentoIdentidad(TipoDocumento.RUC, "20601234567"),
                new Email("sanisidro@igav-gala.pe"),
                new Telefono("+5114219000"),
                new Address("Av. Conquistadores 780", "San Isidro", "Lima", "Perú", "15073", "Frente al Olivar", "15073"),
                "SYSTEM"
        );

        Store store2 = Store.create(
                "Sede Miraflores Luxury",
                "IGAV Gala Luxury S.A.C.",
                new DocumentoIdentidad(TipoDocumento.RUC, "20601234568"),
                new Email("miraflores@igav-gala.pe"),
                new Telefono("+5114458800"),
                new Address("Av. Santa Cruz 920", "Miraflores", "Lima", "Perú", "15074", "A 2 cuadras del Parque Kennedy", "15074"),
                "SYSTEM"
        );

        Store store3 = Store.create(
                "Sede Jockey Plaza - Surco",
                "IGAV Gala Luxury S.A.C.",
                new DocumentoIdentidad(TipoDocumento.RUC, "20601234569"),
                new Email("jockey@igav-gala.pe"),
                new Telefono("+5117123000"),
                new Address("CC. Jockey Plaza Nivel 2", "Surco", "Lima", "Perú", "15023", "Al lado de tiendas de lujo", "15023"),
                "SYSTEM"
        );

        store1 = storeRepository.save(store1);
        store2 = storeRepository.save(store2);
        store3 = storeRepository.save(store3);

        // 2. Categorías
        Category catVestidos = Category.create("Vestidos de Gala", "Vestidos largos de noche, seda natural y pedrería finísima", store1, "SYSTEM");
        Category catTernos = Category.create("Ternos de Gala", "Ternos de 3 piezas, esmóquines Black Tie y lana 120s italiana", store1, "SYSTEM");
        Category catSacos = Category.create("Sacos & Blazers", "Blazers premium de terciopelo y sacos de fiesta", store1, "SYSTEM");
        Category catAccesorios = Category.create("Accesorios de Gala", "Tiaras de cristal Swarovski, corbatas de moño y gemelos", store1, "SYSTEM");

        catVestidos = categoryRepository.save(catVestidos);
        catTernos = categoryRepository.save(catTernos);
        catSacos = categoryRepository.save(catSacos);
        catAccesorios = categoryRepository.save(catAccesorios);

        // 3. Prendas
        Garment g1 = Garment.create(
                "SKU-GAL-001",
                "Vestido de Gala Haute Couture Esmeralda",
                "Vestido largo de noche en seda esmeralda con bordados dorados en pedrería fina.",
                "Verde Esmeralda / Oro",
                GarmentSize.M,
                catVestidos,
                store1,
                new BigDecimal("280.00"),
                new BigDecimal("1800.00"),
                new BigDecimal("150.00"),
                12,
                24,
                "SYSTEM"
        );

        Garment g2 = Garment.create(
                "SKU-TER-002",
                "Terno Slim Fit Novio Champagne",
                "Terno de gala de 3 piezas en lana italiana super 120s con solapa en raso.",
                "Champagne / Negro",
                GarmentSize.L,
                catTernos,
                store1,
                new BigDecimal("250.00"),
                new BigDecimal("1600.00"),
                new BigDecimal("120.00"),
                10,
                24,
                "SYSTEM"
        );

        Garment g3 = Garment.create(
                "SKU-VES-003",
                "Vestido Sirena Azul Noche Royal",
                "Corte sirena con escote corazón y cola desmontable de tul satinado.",
                "Azul Noche Royal",
                GarmentSize.S,
                catVestidos,
                store1,
                new BigDecimal("320.00"),
                new BigDecimal("2200.00"),
                new BigDecimal("200.00"),
                10,
                48,
                "SYSTEM"
        );

        Garment g4 = Garment.create(
                "SKU-SAG-004",
                "Saco Smoking Velvet Burdeos",
                "Saco de terciopelo premium burdeos con solapa de chal en seda negra.",
                "Burdeos / Negro",
                GarmentSize.M,
                catSacos,
                store1,
                new BigDecimal("190.00"),
                new BigDecimal("1200.00"),
                new BigDecimal("100.00"),
                15,
                24,
                "SYSTEM"
        );

        Garment g5 = Garment.create(
                "SKU-ACC-005",
                "Juego de Tiaras & Joyería Cristal Swarovski",
                "Set de tiara y pendientes de gala bañados en rodio con cristales Swarovski.",
                "Plata / Cristal",
                GarmentSize.MEDIDA_CUSTOM,
                catAccesorios,
                store1,
                new BigDecimal("90.00"),
                new BigDecimal("450.00"),
                new BigDecimal("50.00"),
                25,
                12,
                "SYSTEM"
        );

        Garment g6 = Garment.create(
                "SKU-NOV-006",
                "Vestido Princesa Blanco Nupcial",
                "Vestido de corte princesa en raso de seda con corset bordado a mano.",
                "Blanco Marfil",
                GarmentSize.M,
                catVestidos,
                store2,
                new BigDecimal("450.00"),
                new BigDecimal("2900.00"),
                new BigDecimal("250.00"),
                8,
                48,
                "SYSTEM"
        );

        Garment g7 = Garment.create(
                "SKU-SMK-007",
                "Esmoquin Black Tie Italiano Royale",
                "Esmoquin clásico de etiqueta negra en lana super 140s con fajín y gemelos de azabache.",
                "Negro Obsidian",
                GarmentSize.L,
                catTernos,
                store2,
                new BigDecimal("380.00"),
                new BigDecimal("2400.00"),
                new BigDecimal("200.00"),
                12,
                24,
                "SYSTEM"
        );

        g1.setImageUrl("https://images.unsplash.com/photo-1566174053879-31528523f8ae?q=80&w=800");
        g2.setImageUrl("https://images.unsplash.com/photo-1594938298603-c8148c4dae35?q=80&w=800");
        g3.setImageUrl("https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?q=80&w=800");
        g4.setImageUrl("https://images.unsplash.com/photo-1507679799987-c73779587ccf?q=80&w=800");
        g5.setImageUrl("https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?q=80&w=800");
        g6.setImageUrl("https://images.unsplash.com/photo-1546804784-896d0dca3800?q=80&w=800");
        g7.setImageUrl("https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?q=80&w=800");

        garmentRepository.save(g1);
        garmentRepository.save(g2);
        garmentRepository.save(g3);
        garmentRepository.save(g4);
        garmentRepository.save(g5);
        garmentRepository.save(g6);
        garmentRepository.save(g7);

        // 4. Clientes
        Customer c1 = Customer.create(
                "Mariana Sofia",
                "Valdivia Pastor",
                new DocumentoIdentidad(TipoDocumento.DNI, "72839104"),
                new Email("marianavaldivia@gala.pe"),
                new Telefono("+51981234567"),
                new Address("Calle Los Cedros 340, Depto 401", "San Isidro", "Lima", "Perú", "15073", "Cerca al Parque Dammert", "15073"),
                store1,
                "SYSTEM"
        );

        Customer c2 = Customer.create(
                "Carlos Eduardo",
                "Mendoza Rivas",
                new DocumentoIdentidad(TipoDocumento.DNI, "45910283"),
                new Email("carlos.mendoza@corp.pe"),
                new Telefono("+51998765432"),
                new Address("Av. El Sol 512", "Miraflores", "Lima", "Perú", "15074", "A 1 cuadra de Larco", "15074"),
                store1,
                "SYSTEM"
        );

        Customer c3 = Customer.create(
                "Jean-Luc",
                "Dupont",
                new DocumentoIdentidad(TipoDocumento.PASAPORTE, "P8920192"),
                new Email("jdupont@paris-luxury.fr"),
                new Telefono("+33612345678"),
                new Address("Hotel Country Club, Suite 302", "San Isidro", "Lima", "Perú", "15073", "Hospedaje empresarial", "15073"),
                store1,
                "SYSTEM"
        );

        Customer c4 = Customer.create(
                "Fiorella Maria",
                "Bolognesi Vega",
                new DocumentoIdentidad(TipoDocumento.DNI, "71029384"),
                new Email("fbolognesi@gmail.com"),
                new Telefono("+51977112233"),
                new Address("Jr. Batalla de Junín 145", "Barranco", "Lima", "Perú", "15063", "Frente a la bajada de baños", "15063"),
                store1,
                "SYSTEM"
        );

        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
        customerRepository.save(c4);

        // 5. Usuarios
        User u1 = User.create(
                "Admin",
                "SaaS Root",
                new Email("admin.saas@igav.pe"),
                new DocumentoIdentidad(TipoDocumento.DNI, "00000001"),
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200",
                new Telefono("+51999000111"),
                GlobalRole.SUPER_ADMIN,
                new Address("Oficina Central IGAV", "San Isidro", "Lima", "Perú", "15073", "Sede Central", "15073"),
                store1,
                "SYSTEM"
        );

        User u2 = User.create(
                "Gabriel",
                "Vendedor Senior",
                new Email("gabriel.vendedor@igav.pe"),
                new DocumentoIdentidad(TipoDocumento.DNI, "44112233"),
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=200",
                new Telefono("+51988223344"),
                GlobalRole.VENDEDOR,
                new Address("Av. Conquistadores 780", "San Isidro", "Lima", "Perú", "15073", "Tienda San Isidro", "15073"),
                store1,
                "SYSTEM"
        );

        userRepository.save(u1);
        userRepository.save(u2);

        System.out.println(">>> ¡Carga de datos de muestra finalizada exitosamente en MySQL (igav_db)!");
    }
}
