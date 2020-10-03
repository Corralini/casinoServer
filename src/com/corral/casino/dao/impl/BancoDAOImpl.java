package com.corral.casino.dao.impl;

import com.corral.casino.dao.spi.BancoDAO;
import com.corral.casino.dao.utils.DAOUtils;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDAOImpl implements BancoDAO {

    private final String select = " SELECT ID,DINERO,PUNTOS,TARJETA ";
    private final Logger logger = LogManager.getLogger(JuegoDAOImpl.class.getName());

    @Override
    public Banco create(Connection connection, Banco banco) {
        if (logger.isDebugEnabled()) logger.debug("Creacion de banco: {}", banco);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO BANCO values ()");
            if (banco != null) {
                preparedStatement = connection.prepareStatement(sb.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    banco.setId(resultSet.getInt(1));
                } else {
                    throw new CasinoException(Codes.NO_AUTOKEY);
                }
            } else {
                banco = null;
            }
            return banco;
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Banco> findBy(Connection connection, BancoCriteria bancoCriteria) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda juego criteria: {}", bancoCriteria);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(select)
                    .append(" FROM BANCO ");
            boolean first = true;
            List<Banco> bancoList = new ArrayList<>();
            if (bancoCriteria != null) {
                if (bancoCriteria.getId() != null) {
                    DAOUtils.addClause(stringBuilder, first, " id = ? ");
                    first = false;
                }
                if (bancoCriteria.getDinero() != null) {
                    DAOUtils.addClause(stringBuilder, first, " dinero = ? ");
                    first = false;
                }
                if (bancoCriteria.getPuntos() != null) {
                    DAOUtils.addClause(stringBuilder, first, " PUNTOS = ? ");
                    first = false;
                }
                if (bancoCriteria.getTarjeta() != null) {
                    DAOUtils.addClause(stringBuilder, first, " upper(tarjeta) like upper(?) ");
                    first = false;
                }


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (bancoCriteria.getId() != null) {
                    preparedStatement.setInt(i++, bancoCriteria.getId());
                }
                if (bancoCriteria.getDinero() != null) {
                    preparedStatement.setDouble(i++, bancoCriteria.getDinero());
                }
                if (bancoCriteria.getPuntos() != null) {
                    preparedStatement.setInt(i++, bancoCriteria.getPuntos());
                }
                if (bancoCriteria.getTarjeta() != null) {
                    preparedStatement.setString(i++, bancoCriteria.getTarjeta());
                }


                resultSet = preparedStatement.executeQuery();
                Banco banco = null;
                while (resultSet.next()) {
                    banco = loadNext(resultSet);
                    bancoList.add(banco);
                }
                if (bancoList.isEmpty()) {
                    if (logger.isDebugEnabled()) logger.debug("No se han encontrado resultados");
                }

            }
            return bancoList;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public Banco update(Connection connection, Banco banco) {
        if (logger.isDebugEnabled()) logger.debug("Busqueda juego criteria: {}", banco);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(" UPDATE BANCO ");
            boolean first = true;
            if (banco != null && banco.getId() != null) {
                if (banco.getDinero() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " DINERO = ? ");
                    first = false;
                }
                if (banco.getPuntos() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " PUNTOS = ? ");
                    first = false;
                }
                if (banco.getTarjeta() != null) {
                    DAOUtils.addUpdate(stringBuilder, first, " tarjeta = ? ");
                    first = false;
                }
                stringBuilder.append(" where id = ? ");


                preparedStatement = connection.prepareStatement(stringBuilder.toString());
                int i = 1;

                if (banco.getDinero() != null) {
                    preparedStatement.setDouble(i++, banco.getDinero());
                }
                if (banco.getPuntos() != null) {
                    preparedStatement.setInt(i++, banco.getPuntos());
                }
                if (banco.getTarjeta() != null) {
                    preparedStatement.setString(i++, banco.getTarjeta());
                }
                preparedStatement.setInt(i++, banco.getId());

                int updatedRows = preparedStatement.executeUpdate();

                if (updatedRows != 0) {
                    banco = null;
                }

            }
            return banco;
        } catch (SQLException sqlException) {
            throw new CasinoException(Codes.ERR_DB, sqlException);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    private Banco loadNext(ResultSet resultSet) throws SQLException {
        int i = 1;
        Banco banco = new Banco();
        banco.setId(resultSet.getInt(i++));
        banco.setDinero(resultSet.getDouble(i++));
        banco.setPuntos(resultSet.getInt(i++));
        banco.setTarjeta(resultSet.getString(i++));

        return banco;
    }
}
