package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.MovimientoDAO;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Movimiento;
import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.MovimientoCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAOImpl implements MovimientoDAO {

    private final String select = " SELECT ID,DINERO,PUNTOS,ID_USUARIO,ID_JUEGO,FECHA ";
    private final Logger logger = LogManager.getLogger(MovimientoDAOImpl.class.getName());

    @Override
    public Movimiento create(Connection connection, Movimiento movimiento) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de movimiento: {}", movimiento);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO MOVIMIENTO (DINERO,PUNTOS,ID_USUARIO,ID_JUEGO) ")
                    .append(" values (?,?,?,?)");
            if (movimiento != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                preparedStatement.setDouble(i++, movimiento.getDinero());
                preparedStatement.setInt(i++, movimiento.getPuntos());
                preparedStatement.setInt(i++, movimiento.getIdUsuario());
                preparedStatement.setInt(i++, movimiento.getIdjuego());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    movimiento.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                movimiento = null;
            }
            return movimiento;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Movimiento> findBy(Connection connection, MovimientoCriteria movimientoCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda movimiento criteria: {}", movimientoCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM MOVIMIENTO ");
            boolean first = true;
            List<Movimiento> movimientoList = new ArrayList<>();
            if (movimientoCriteria != null) {
                if (movimientoCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (movimientoCriteria.getDinero() != null) {
                    DAOUtils.addClause(stringBuilder, first, " dinero = ? ");
                    first = false;
                }
                if (movimientoCriteria.getPuntos() != null) {
                    DAOUtils.addClause(stringBuilder, first, " puntos = ? ");
                    first = false;
                }
                if (movimientoCriteria.getIdUsuario() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_usuario = ? ");
                    first = false;
                }
                if (movimientoCriteria.getIdjuego() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_juego = ? ");
                    first = false;
                }
                if (movimientoCriteria.getFecha() != null) {
                    DAOUtils.addClause(stringBuilder, first, " fecha < ? ");
                    first = false;
                }

                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (movimientoCriteria.getId() != null) {
                    preparedStatement.setInt(i++, movimientoCriteria.getId());
                }
                if (movimientoCriteria.getDinero() != null) {
                    preparedStatement.setDouble(i++, movimientoCriteria.getDinero());
                }
                if (movimientoCriteria.getPuntos() != null) {
                    preparedStatement.setInt(i++, movimientoCriteria.getPuntos());
                }
                if (movimientoCriteria.getIdUsuario() != null) {
                    preparedStatement.setInt(i++, movimientoCriteria.getIdUsuario());
                }
                if (movimientoCriteria.getIdjuego() != null) {
                    preparedStatement.setInt(i++, movimientoCriteria.getIdjuego());
                }
                if (movimientoCriteria.getFecha() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(movimientoCriteria.getFecha().getTime()));
                }

                resultSet = preparedStatement.executeQuery();
                Movimiento movimiento = null;
                while (resultSet.next()) {
                    movimiento = loadNext(resultSet);
                    movimientoList.add(movimiento);
                }
                if (movimientoList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return movimientoList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Movimiento update(Connection connection, Movimiento movimiento) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda movimiento criteria: {}", movimiento);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE MOVIMIENTO ");
            boolean first = true;
            if (movimiento != null) {
                if (movimiento.getId() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (movimiento.getDinero() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " dinero = ? ");
                    first = false;
                }
                if (movimiento.getPuntos() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " puntos = ? ");
                    first = false;
                }
                if (movimiento.getIdUsuario() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id_usuario = ? ");
                    first = false;
                }
                if (movimiento.getIdjuego() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id_juego = ? ");
                    first = false;
                }
                if (movimiento.getFecha() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " fecha = ? ");
                    first = false;
                }

                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (movimiento.getId() != null) {
                    preparedStatement.setInt(i++, movimiento.getId());
                }
                if (movimiento.getDinero() != null) {
                    preparedStatement.setDouble(i++, movimiento.getDinero());
                }
                if (movimiento.getPuntos() != null) {
                    preparedStatement.setInt(i++, movimiento.getPuntos());
                }
                if (movimiento.getIdUsuario() != null) {
                    preparedStatement.setInt(i++, movimiento.getIdUsuario());
                }
                if (movimiento.getIdjuego() != null) {
                    preparedStatement.setInt(i++, movimiento.getIdjuego());
                }
                if (movimiento.getFecha() != null) {
                    preparedStatement.setTimestamp(i++, new Timestamp(movimiento.getFecha().getTime()));
                }

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    movimiento = null;
                }


            }
            return movimiento;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Movimiento loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Movimiento movimiento = new Movimiento();
        movimiento.setId(resultSet.getInt(i++));
        movimiento.setDinero(resultSet.getDouble(i++));
        movimiento.setPuntos(resultSet.getInt(i++));
        movimiento.setIdUsuario(resultSet.getInt(i++));
        movimiento.setIdjuego(resultSet.getInt(i++));
        movimiento.setFecha(resultSet.getDate(i++));

        return movimiento;
    }
}
