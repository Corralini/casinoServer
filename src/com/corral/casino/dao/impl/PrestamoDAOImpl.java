package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.PrestamoDAO;
import com.corral.casino.dao.utils.BooleanUtils;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.Juego;
import com.corral.casino.models.Mesa;
import com.corral.casino.models.Prestamo;
import com.corral.casino.models.criteria.JuegoCriteria;
import com.corral.casino.models.criteria.PrestamoCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    private final String select = " SELECT ID,ID_USUARIO,CANTIDAD,DEVUELTO, FECHA_CREACION, FECHA_DEVUELTO ";
    private final Logger logger = LogManager.getLogger(PrestamoDAOImpl.class.getName());

    @Override
    public Prestamo create(Connection connection, Prestamo prestamo) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de prestamo: {}", prestamo);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO PRESTAMO (id_usuario,cantidad) ")
                    .append(" values (?,?)");
            if (prestamo != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                preparedStatement.setInt(i++, prestamo.getIdUsuario());
                preparedStatement.setDouble(i++, prestamo.getCantidad());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    prestamo.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                prestamo = null;
            }
            return prestamo;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Prestamo> findBy(Connection connection, PrestamoCriteria prestamoCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda prestamo criteria: {}", prestamoCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM PRESTAMO ");
            boolean first = true;
            List<Prestamo> prestamoList = new ArrayList<>();
            if (prestamoCriteria != null) {
                if (prestamoCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (prestamoCriteria.getIdUsuario() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_usuario = ? ");
                    first = false;
                }
                if (prestamoCriteria.getCantidad() != null) {
                    DAOUtils.addClause(stringBuilder, first, " CANTIDAD = ? ");
                    first = false;
                }
                if (prestamoCriteria.getDevuelto() != null) {
                    DAOUtils.addClause(stringBuilder, first, " DEVUELTO = ? ");
                    first = false;
                }
                if (prestamoCriteria.getFechaCreacion() != null) {
                    DAOUtils.addClause(stringBuilder, first, " FECHA_CREACION = ? ");
                    first = false;
                }
                if (prestamoCriteria.getFechaDevuelto() != null) {
                    DAOUtils.addClause(stringBuilder, first, " FECHA_DEVUELTO = ? ");
                    first = false;
                }


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (prestamoCriteria.getId() != null) {
                    preparedStatement.setInt(i++, prestamoCriteria.getId());
                }
                if (prestamoCriteria.getIdUsuario() != null) {
                    preparedStatement.setInt(i++, prestamoCriteria.getIdUsuario());
                }
                if (prestamoCriteria.getCantidad() != null) {
                    preparedStatement.setDouble(i++, prestamoCriteria.getCantidad());
                }
                if (prestamoCriteria.getDevuelto() != null) {
                    preparedStatement.setInt(i++, BooleanUtils.booleanToInt(prestamoCriteria.getDevuelto()));
                }
                if (prestamoCriteria.getFechaCreacion() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(prestamoCriteria.getFechaCreacion().getTime()));
                }
                if (prestamoCriteria.getFechaDevuelto() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(prestamoCriteria.getFechaDevuelto().getTime()));
                }


                resultSet = preparedStatement.executeQuery();
                Prestamo prestamo = null;
                while (resultSet.next()) {
                    prestamo = loadNext(resultSet);
                    prestamoList.add(prestamo);
                }
                if (prestamoList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return prestamoList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Prestamo update(Connection connection, Prestamo prestamo) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda prestamo criteria: {}", prestamo);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE PRESTAMO ");
            boolean first = true;
            if (prestamo != null) {
                if (prestamo.getCantidad() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " cantidad = ? ");
                    first = false;
                }
                if (prestamo.getDevuelto() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " devuelto = ? ");
                    first = false;
                }
                if (prestamo.getFechaCreacion() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " fecha_creacion = ? ");
                    first = false;
                }
                if (prestamo.getFechaDevuelto() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " fecha_devuelto = ? ");
                    first = false;
                }
                stringBuilder.append(" where id = ? ");


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (prestamo.getCantidad() != null) {
                    preparedStatement.setDouble(i++, prestamo.getCantidad());
                }
                if (prestamo.getDevuelto() != null) {
                    preparedStatement.setInt(i++, BooleanUtils.booleanToInt(prestamo.getDevuelto()));
                }

                if (prestamo.getFechaCreacion() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(prestamo.getFechaCreacion().getTime()));
                }
                if (prestamo.getFechaDevuelto() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(prestamo.getFechaDevuelto().getTime()));
                }
                preparedStatement.setInt(i++, prestamo.getId());


                resultSet = preparedStatement.executeQuery();

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    prestamo = null;
                }

            }
            return prestamo;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Prestamo loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Prestamo prestamo = new Prestamo();
        prestamo.setId(resultSet.getInt(i++));
        prestamo.setIdUsuario(resultSet.getInt(i++));
        prestamo.setCantidad(resultSet.getDouble(i++));
        prestamo.setDevuelto(BooleanUtils.intToBool(resultSet.getInt(i++)));
        prestamo.setFechaCreacion(resultSet.getDate(i++));
        prestamo.setFechaDevuelto(resultSet.getDate(i++));

        return prestamo;
    }
}
