package com.corral.casino.dao.spi;

import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.UsuarioCriteria;

import java.sql.Connection;
import java.util.List;

public interface UsuarioDAO {
    Usuario create(Connection connection, Usuario usuario);

    List<Usuario> findBy(Connection connection, UsuarioCriteria usuarioCriteria);

    Usuario update(Connection connection, Usuario usuarioCriteria);

}
