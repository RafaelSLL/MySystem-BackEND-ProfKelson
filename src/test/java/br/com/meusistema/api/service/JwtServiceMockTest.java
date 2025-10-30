package br.com.meusistema.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtServiceMockTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setup() throws Exception {
        jwtService = new JwtService();

        // Define o SECRET_KEY manualmente, já que @Value não é carregado fora do contexto Spring
        Field secretKeyField = JwtService.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, "12345678901234567890123456789012"); // precisa ter 32+ bytes para HS256

        userDetails = User.builder()
                .username("victor")
                .password("senha123")
                .authorities(Collections.emptyList())
                .build();

    }

    @Test
    void deveGerarTokenValido() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // formato JWT
    }

    @Test
    void deveExtrairUsernameCorretamente() {
        String token = jwtService.generateToken(userDetails);

        String usernameExtraido = jwtService.extractUsername(token);

        assertEquals("victor", usernameExtraido);
    }

    @Test
    void deveValidarTokenComSucesso() {
        String token = jwtService.generateToken(userDetails);

        boolean valido = jwtService.isTokenValid(token, userDetails);

        assertTrue(valido);
    }

    @Test
    void deveRetornarFalsoParaTokenDeOutroUsuario() {
        String token = jwtService.generateToken(userDetails);

        UserDetails outroUsuario = User.builder()
                .username("gabi")
                .password("senha456")
                .authorities(Collections.emptyList())
                .build();

        boolean valido = jwtService.isTokenValid(token, outroUsuario);

        assertFalse(valido);
    }
}
