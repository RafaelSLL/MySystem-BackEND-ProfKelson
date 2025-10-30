package br.com.meusistema.api.service;

import br.com.meusistema.api.dtos.LoginRequestDTO;
import br.com.meusistema.api.dtos.LoginResponseDTO;
import br.com.meusistema.api.dtos.RegisterRequestDTO;
import br.com.meusistema.api.dtos.UsuarioResponseDTO;
import br.com.meusistema.api.enums.Roles;
import br.com.meusistema.api.model.Usuario;
import br.com.meusistema.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceMockTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setup(){
        usuario = Usuario.builder()
                .id(1L)
                .username("victor")
                .email("victor@gmail.com")
                .password("123456")
                .role(Roles.ADMIN)
                .build();

        registerRequestDTO = new RegisterRequestDTO(
                "victor",
                "123456",
                "victor@gmail.com",
                Roles.ADMIN
        );

        loginRequestDTO = new LoginRequestDTO(
                "victor",
                "123456"
        );

    }

    @Test
    void deveRegistrarUsuarioComSucesso(){
        when(passwordEncoder.encode("123456")).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        LoginResponseDTO response = authService.register(registerRequestDTO);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void deveLogarUsuarioComSucesso() {
        when(usuarioRepository.findByUsername("victor"))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        LoginResponseDTO response = authService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoNoLogin(){
        LoginRequestDTO request = new LoginRequestDTO(
                "invalido", "senha");

        when(usuarioRepository.findByUsername("invalido"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> authService.login(request));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void deveRetornarUsuarioLogado() {
        when(usuarioRepository.findByUsername("victor"))
                .thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = authService.getUsuarioLogado("victor");

        assertNotNull(response);
        assertEquals("victor", response.username());
        assertEquals(Roles.ADMIN, response.role());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoAoBuscarLogado(){
        when(usuarioRepository.findByUsername("invalido"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> authService.getUsuarioLogado("invalido"));
    }
}
