package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.BancoDAOImpl;
import com.corral.casino.dao.impl.UsuarioDAOImpl;
import com.corral.casino.dao.spi.BancoDAO;
import com.corral.casino.dao.spi.UsuarioDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.UsuarioCriteria;
import com.corral.casino.service.spi.UsuarioService;
import com.corral.casino.service.utils.EncryptionUtils;
import com.corral.casino.service.utils.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    private final Logger logger = LogManager.getLogger(UsuarioDAOImpl.class.getName());
    private final UsuarioDAO usuarioDAO;
    private final BancoDAO bancoDAO;

    public UsuarioServiceImpl() {
        usuarioDAO = new UsuarioDAOImpl();
        bancoDAO = new BancoDAOImpl();
    }

    @Override
    public Usuario login(String user, String psswd) {
        logger.info("Login: user {}; psswd {}", user, psswd);
        Usuario usuario = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (user != null && psswd != null) {
                UsuarioCriteria usuarioCriteria = new UsuarioCriteria();
                usuarioCriteria.setUsuario(user);
                Usuario exist = usuarioDAO.findBy(connection, usuarioCriteria).get(0);
                if (exist != null && EncryptionUtils.checkPassword(psswd, exist.getPsswd())) {
                    usuario = exist;
                }
            }
            return usuario;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Usuario create(Usuario usuario) {
        Connection connection = null;
        boolean commit = false;
        try {
            connection = ConnectionManager.getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            if (usuario != null && usuario.getEmail() != null && usuario.getNombre() != null && usuario.getApellido1() != null
                    && usuario.getUsuario() != null && usuario.getPsswd() != null) {
                Banco banco = bancoDAO.create(connection, new Banco());
                banco.setPuntos(100);
                banco.setDinero((double) RandomUtils.generateInitialSalary());
                bancoDAO.update(connection, banco);
                usuario.setIdBanco(banco.getId());
                usuario = usuarioDAO.create(connection, usuario);
                if (usuario.getId() == null) {
                    usuario = null;
                } else {
                    commit = true;
                }
            }
            return usuario;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection, commit);
        }
    }

    @Override
    public List<Usuario> findBy(UsuarioCriteria usuarioCriteria) {
        List<Usuario> usuarioList = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (usuarioCriteria != null) {
                usuarioList = usuarioDAO.findBy(connection, usuarioCriteria);
            }
            return usuarioList;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Usuario update(Usuario usuario) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (usuario != null && usuario.getId() != null) {
                usuario = usuarioDAO.update(connection, usuario);
            }
            return usuario;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }
}
