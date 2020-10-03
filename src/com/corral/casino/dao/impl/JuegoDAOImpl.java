package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.JuegoDAO;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Juego;
import com.corral.casino.models.criteria.JuegoCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JuegoDAOImpl implements JuegoDAO {

    private final String select = " SELECT ID,JUEGO,RQ_POINTS ";
    private final Logger logger = LogManager.getLogger(JuegoDAOImpl.class.getName());

    @Override
    public Juego create(Connection connection, Juego juego) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de juego: {}", juego);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO JUEGO (JUEGO,RQ_POINTS) ")
                    .append(" values (?,?)");
            if (juego != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                preparedStatement.setString(i++, juego.getJuego());
                preparedStatement.setInt(i++, juego.getRqPoints());
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    juego.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                juego = null;
            }
            return juego;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Juego> findBy(Connection connection, JuegoCriteria juegoCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda juego criteria: {}", juegoCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM JUEGO ");
            boolean first = true;
            List<Juego> juegoList = new ArrayList<>();
            if (juegoCriteria != null) {
                if (juegoCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (juegoCriteria.getJuego() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(juego) like upper(?) ");
                    first = false;
                }
                if (juegoCriteria.getRqPoints() != null) {
                    DAOUtils.addClause(stringBuilder, first, " RQ_POINTS = ? ");
                    first = false;
                }


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (juegoCriteria.getId() != null) {
                    preparedStatement.setInt(i++, juegoCriteria.getId());
                }
                if (juegoCriteria.getJuego() != null) {
                    preparedStatement.setString(i++, juegoCriteria.getJuego());
                }
                if (juegoCriteria.getRqPoints() != null) {
                    preparedStatement.setInt(i++, juegoCriteria.getRqPoints());
                }


                resultSet = preparedStatement.executeQuery();
                Juego juego = null;
                while (resultSet.next()) {
                    juego = loadNext(resultSet);
                    juegoList.add(juego);
                }
                if (juegoList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return juegoList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Juego update(Connection connection, Juego juego) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda juego criteria: {}", juego);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE JUEGO ");
            boolean first = true;
            if (juego != null) {
                if (juego.getJuego() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " juego = ? ");
                    first = false;
                }
                if (juego.getRqPoints() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " RQ_POINTS = ? ");
                    first = false;
                }
                stringBuilder.append(" where id = ? ");


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (juego.getJuego() != null) {
                    preparedStatement.setString(i++, juego.getJuego());
                }
                if (juego.getRqPoints() != null) {
                    preparedStatement.setInt(i++, juego.getRqPoints());
                }
                preparedStatement.setInt(i++, juego.getId());


                resultSet = preparedStatement.executeQuery();

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    juego = null;
                }

            }
            return juego;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Juego loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Juego juego = new Juego();
        juego.setId(resultSet.getInt(i++));
        juego.setJuego(resultSet.getString(i++));
        juego.setRqPoints(resultSet.getInt(i++));

        return juego;
    }
}
