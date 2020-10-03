package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.UsuarioDAO;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.UsuarioCriteria;
import com.corral.casino.service.utils.EncryptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final String select = " SELECT ID,EMAIL,NOMBRE,APELLIDO1,APELLIDO2,USUARIO,PSSWD,FECHA_SUB,ID_MESA,ID_BANCO ";
    private final Logger logger = LogManager.getLogger(UsuarioDAOImpl.class.getName());

    @Override
    public Usuario create(Connection connection, Usuario usuario) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de usuario: {}", usuario);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO USUARIO (EMAIL,NOMBRE,APELLIDO1,APELLIDO2,USUARIO,PSSWD,ID_BANCO) ")
                    .append(" values (?,?,?,?,upper(?),?,?)");
            if (usuario != null && usuario.getEmail() != null && usuario.getNombre() != null && usuario.getApellido1() != null
                    && usuario.getUsuario() != null && usuario.getPsswd() != null
                    && usuario.getIdBanco() != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                preparedStatement.setString(i++, usuario.getEmail());
                preparedStatement.setString(i++, usuario.getNombre());
                preparedStatement.setString(i++, usuario.getApellido1());
                preparedStatement.setString(i++, usuario.getApellido2());
                preparedStatement.setString(i++, usuario.getUsuario());
                preparedStatement.setString(i++, EncryptionUtils.encryptPassword(usuario.getPsswd()));
                preparedStatement.setInt(i, usuario.getIdBanco());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    usuario.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                usuario = null;
            }
            return usuario;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Usuario> findBy(Connection connection, UsuarioCriteria usuarioCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda usuario criteria: {}", usuarioCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM USUARIO ");
            boolean first = true;
            List<Usuario> usuarioList = new ArrayList<>();
            if (usuarioCriteria != null) {
                if (usuarioCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (usuarioCriteria.getEmail() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(email) like upper(?) ");
                    first = false;
                }
                if (usuarioCriteria.getNombre() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(nombre) like upper(?) ");
                    first = false;
                }
                if (usuarioCriteria.getApellido1() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(apellido1) like upper(?) ");
                    first = false;
                }
                if (usuarioCriteria.getApellido2() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(apellido2) like upper(?) ");
                    first = false;
                }
                if (usuarioCriteria.getUsuario() != null) {
                    DAOUtils.addClause(stringBuilder, first, " usuario = ? ");
                    first = false;
                }
                if (usuarioCriteria.getPsswd() != null) {
                    DAOUtils.addClause(stringBuilder, first, " psswd = ? ");
                    first = false;
                }
                if (usuarioCriteria.getFechaSub() != null) {
                    DAOUtils.addClause(stringBuilder, first, " fecha_sub = ? ");
                    first = false;
                }
                if (usuarioCriteria.getIdMesa() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_mesa = ? ");
                    first = false;
                }
                if (usuarioCriteria.getIdBanco() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_banco = ? ");
                    first = false;
                }
                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (usuarioCriteria.getId() != null) {
                    preparedStatement.setInt(i++, usuarioCriteria.getId());
                }
                if (usuarioCriteria.getEmail() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getEmail());
                }
                if (usuarioCriteria.getNombre() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getNombre());
                }
                if (usuarioCriteria.getApellido1() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getApellido1());
                }
                if (usuarioCriteria.getApellido2() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getApellido2());
                }
                if (usuarioCriteria.getUsuario() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getUsuario());
                }
                if (usuarioCriteria.getPsswd() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getPsswd());
                }
                if (usuarioCriteria.getFechaSub() != null) {
                    preparedStatement.setString(i++, usuarioCriteria.getFechaSub());
                }
                if (usuarioCriteria.getIdMesa() != null) {
                    preparedStatement.setInt(i++, usuarioCriteria.getIdMesa());
                }
                if (usuarioCriteria.getIdBanco() != null) {
                    preparedStatement.setInt(i, usuarioCriteria.getIdBanco());
                }

                resultSet = preparedStatement.executeQuery();
                Usuario usuario = null;
                while (resultSet.next()) {
                    usuario = loadNext(resultSet);
                    usuarioList.add(usuario);
                }
                if (usuarioList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return usuarioList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Usuario update(Connection connection, Usuario usuario) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda usuario criteria: {}", usuario);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE USUARIO ");
            boolean first = true;
            if (usuario != null) {
                if (usuario.getEmail() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " email = upper(?) ");
                    first = false;
                }
                if (usuario.getNombre() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " nombre = ? ");
                    first = false;
                }
                if (usuario.getApellido1() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " apellido1 = ? ");
                    first = false;
                }
                if (usuario.getApellido2() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " apellido2 = ? ");
                    first = false;
                }
                if (usuario.getUsuario() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " usuario = ? ");
                    first = false;
                }
                if (usuario.getPsswd() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " psswd = ? ");
                    first = false;
                }
                if (usuario.getFechaSub() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " fecha_sub = ? ");
                    first = false;
                }
                if (usuario.getIdMesa() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id_mesa = ? ");
                    first = false;
                }
                if (usuario.getIdBanco() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id_banco = ? ");
                    first = false;
                }
                stringBuilder.append(" where id = ?");
                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (usuario.getEmail() != null) {
                    preparedStatement.setString(i++, usuario.getEmail());
                }
                if (usuario.getNombre() != null) {
                    preparedStatement.setString(i++, usuario.getNombre());
                }
                if (usuario.getApellido1() != null) {
                    preparedStatement.setString(i++, usuario.getApellido1());
                }
                if (usuario.getApellido2() != null) {
                    preparedStatement.setString(i++, usuario.getApellido2());
                }
                if (usuario.getUsuario() != null) {
                    preparedStatement.setString(i++, usuario.getUsuario());
                }
                if (usuario.getPsswd() != null) {
                    preparedStatement.setString(i++, usuario.getPsswd());
                }
                if (usuario.getFechaSub() != null) {
                    preparedStatement.setString(i++, usuario.getFechaSub());
                }
                if (usuario.getIdMesa() != null) {
                    preparedStatement.setInt(i++, usuario.getIdMesa());
                }
                if (usuario.getIdBanco() != null) {
                    preparedStatement.setInt(i++, usuario.getIdBanco());
                }
                preparedStatement.setInt(i, usuario.getId());

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    usuario = null;
                }

            }
            return usuario;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Usuario loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt(i++));
        usuario.setEmail(resultSet.getString(i++));
        usuario.setNombre(resultSet.getString(i++));
        usuario.setApellido1(resultSet.getString(i++));
        usuario.setApellido2(resultSet.getString(i++));
        usuario.setUsuario(resultSet.getString(i++));
        usuario.setPsswd(resultSet.getString(i++));
        usuario.setFechaSub(resultSet.getString(i++));
        usuario.setIdMesa(resultSet.getInt(i++));
        usuario.setIdBanco(resultSet.getInt(i));
        return usuario;
    }
}
