package com.corral.casino.service.spi;

import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.UsuarioCriteria;

import java.util.List;

public interface UsuarioService {

    Usuario login(String user, String psswd);

    Usuario create(Usuario usuario);

    List<Usuario> findBy(UsuarioCriteria usuarioCriteria);

    Usuario update(Usuario usuario);
}
