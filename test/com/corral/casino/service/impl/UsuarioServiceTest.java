package com.corral.casino.service.impl;

import com.corral.casino.models.Usuario;
import com.corral.casino.service.spi.UsuarioService;

public class UsuarioServiceTest {

    private static UsuarioService usuarioService = new UsuarioServiceImpl();

    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        usuario.setPsswd("Test123.");
        usuario.setUsuario("test");
        usuario.setApellido1("Test");
        usuario.setApellido2("Test");
        usuario.setNombre("Test");
        usuario.setEmail("test@pokertavern.com");
        usuario = usuarioService.create(usuario);
        System.out.println(usuarioService.login("test", "Test123."));
    }
}
