package com.igav.igav_project.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Proveedor y validador de JSON Web Tokens (JWT) para autenticación sin estado en la plataforma IGAV SaaS.
 * Emplea HMAC-SHA256 con soporte nativo estándar de Java sin dependencias externas inestables.
 *
 * @author IGAV Development Team
 * @version 1.0.0
 */
@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGO = "HmacSHA256";
    private final String secretKey;
    private final long expirationTimeMs;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtTokenProvider(
            @Value("${app.jwt.secret:IGAV_SAAS_SUPER_SECRET_KEY_FOR_JWT_SIGNING_2026_VERY_SECURE_TOKEN_XYZ}") String secretKey,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationTimeMs // 24 horas por defecto
    ) {
        this.secretKey = secretKey;
        this.expirationTimeMs = expirationTimeMs;
    }

    /**
     * Genera un nuevo token JWT firmado con los datos del usuario autenticado.
     *
     * @param email Email del usuario (Subject).
     * @param role Rol del usuario (ej: ADMIN_SAAS, VENDEDOR, etc.).
     * @param storeId Identificador de la sede o tenant asignado.
     * @param fullName Nombre completo del usuario.
     * @return Cadena JWT codificada en formato Header.Payload.Signature.
     */
    public String generateToken(String email, String role, Long storeId, String fullName) {
        try {
            long now = System.currentTimeMillis();
            long exp = now + expirationTimeMs;

            // Header
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("alg", "HS256");
            headerMap.put("typ", "JWT");
            String headerJson = objectMapper.writeValueAsString(headerMap);
            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

            // Payload
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("sub", email);
            payloadMap.put("role", role);
            payloadMap.put("storeId", storeId);
            payloadMap.put("name", fullName);
            payloadMap.put("iat", now / 1000);
            payloadMap.put("exp", exp / 1000);
            String payloadJson = objectMapper.writeValueAsString(payloadMap);
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            // Signature
            String dataToSign = encodedHeader + "." + encodedPayload;
            String signature = signHmacSha256(dataToSign, secretKey);

            return dataToSign + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el token JWT", e);
        }
    }

    /**
     * Valida la firma y tiempo de expiración del token JWT proporcionado.
     *
     * @param token Token JWT recibido en la cabecera Authorization.
     * @return true si es auténtico y está vigente; false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String dataToSign = parts[0] + "." + parts[1];
            String expectedSignature = signHmacSha256(dataToSign, secretKey);
            if (!expectedSignature.equals(parts[2])) {
                return false;
            }

            // Validar expiración
            Map<String, Object> claims = getClaims(token);
            if (claims.containsKey("exp")) {
                long exp = ((Number) claims.get("exp")).longValue() * 1000;
                if (new Date().getTime() > exp) {
                    return false; // Token expirado
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae los claims del cuerpo (payload) del token JWT.
     *
     * @param token Token JWT.
     * @return Mapa de claims decodificado.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Token JWT malformado");
            }
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return objectMapper.readValue(decoded, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer claims del JWT", e);
        }
    }

    /**
     * Obtiene el email (Subject) contenido en el token.
     *
     * @param token Token JWT.
     * @return Email del usuario.
     */
    public String getEmailFromToken(String token) {
        return (String) getClaims(token).get("sub");
    }

    /**
     * Obtiene el rol del usuario asignado en el token.
     *
     * @param token Token JWT.
     * @return Rol del usuario.
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * Obtiene el identificador de la tienda o tenant asignado al usuario.
     *
     * @param token Token JWT.
     * @return ID de la tienda o null.
     */
    public Long getStoreIdFromToken(String token) {
        Object storeIdObj = getClaims(token).get("storeId");
        if (storeIdObj instanceof Number) {
            return ((Number) storeIdObj).longValue();
        }
        return null;
    }

    private String signHmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        sha256Hmac.init(secretKeySpec);
        byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signedBytes);
    }
}
