package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.MesaDAO;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Mesa;
import com.corral.casino.models.criteria.MesaCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAOImpl implements MesaDAO {

    private final String select = " SELECT ID,ID_JUEGO,MESA ";
    private final Logger logger = LogManager.getLogger(MesaDAOImpl.class.getName());

    @Override
    public Mesa create(Connection connection, Mesa mesa) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de mesa: {}", mesa);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO MESA (ID_JUEGO,MESA) ")
                    .append(" values (?,?)");
            if (mesa != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                preparedStatement.setInt(i++, mesa.getIdJuego());
                preparedStatement.setString(i++, mesa.getMesa());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    mesa.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                mesa = null;
            }
            return mesa;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Mesa> findBy(Connection connection, MesaCriteria mesaCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda mesa criteria: {}", mesaCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM MESA ");
            boolean first = true;
            List<Mesa> mesaList = new ArrayList<>();
            if (mesaCriteria != null) {
                if (mesaCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (mesaCriteria.getIdJuego() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id_juego = ? ");
                    first = false;
                }
                if (mesaCriteria.getMesa() != null) {
                    DAOUtils.addClause(stringBuilder, first, " mesa = ? ");
                    first = false;
                }


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (mesaCriteria.getId() != null) {
                    preparedStatement.setInt(i++, mesaCriteria.getId());
                }

                if (mesaCriteria.getIdJuego() != null) {
                    preparedStatement.setInt(i++, mesaCriteria.getIdJuego());
                }
                if (mesaCriteria.getMesa() != null) {
                    preparedStatement.setString(i++, mesaCriteria.getMesa());
                }


                resultSet = preparedStatement.executeQuery();
                Mesa mesa = null;
                while (resultSet.next()) {
                    mesa = loadNext(resultSet);
                    mesaList.add(mesa);
                }
                if (mesaList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return mesaList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Mesa update(Connection connection, Mesa mesa) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda mesa criteria: {}", mesa);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE MESA ");
            boolean first = true;
            if (mesa != null) {
                if (mesa.getIdJuego() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " id_juego = ? ");
                    first = false;
                }
                if (mesa.getMesa() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " mesa = ? ");
                    first = false;
                }
                stringBuilder.append(" where id = ? ");


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (mesa.getIdJuego() != null) {
                    preparedStatement.setInt(i++, mesa.getIdJuego());
                }
                if (mesa.getMesa() != null) {
                    preparedStatement.setString(i++, mesa.getMesa());
                }
                preparedStatement.setInt(i++, mesa.getId());


                resultSet = preparedStatement.executeQuery();

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    mesa = null;
                }

            }
            return mesa;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public void delete(Connection connection, Mesa mesa) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de mesa: {}", mesa);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("DELETE FROM MESA WHERE ID = ?");
            if (mesa != null) {
                preparedStatement = connection.prepareStatement(sb.toString());
                int i = 1;
                preparedStatement.setInt(i++, mesa.getId());

                resultSet = preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Mesa loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Mesa mesa = new Mesa();
        mesa.setId(resultSet.getInt(i++));
        mesa.setIdJuego(resultSet.getInt(i++));
        mesa.setMesa(resultSet.getString(i++));

        return mesa;
    }
}
