package com.igav.igav_project.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración global de OpenAPI (Swagger) y generación automática de documentación técnica de la API REST.
 * Cumple con los estándares solicitados por el proveedor para el manual interactivo de endpoints.
 *
 * @author IGAV Development Team
 * @version 1.0.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Define los metadatos globales de la especificación OpenAPI v3 del proyecto IGAV.
     *
     * @return Instancia configurada de {@link OpenAPI}.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("I.G.A.V. - Plataforma SaaS de Alquiler de Gala y Alta Costura")
                        .version("1.0.0")
                        .description("API REST Enterprise para la gestión integral de alquileres de gala, control de colisión de fechas (RF-04), " +
                                "cuarentena en tintorería (RF-05), retención/liquidación de depósitos de garantía (RF-08, RF-13), " +
                                "medidas de sastrería y trazabilidad multi-tenant.")
                        .contact(new Contact()
                                .name("Soporte Técnico IGAV")
                                .email("soporte@igav-saas.com")
                                .url("https://igav-saas.com"))
                        .license(new License()
                                .name("Licencia Comercial Propietaria IGAV SaaS")
                                .url("https://igav-saas.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local de Desarrollo / Pruebas")
                ));
    }
}
