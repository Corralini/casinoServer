package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.BancoDAOImpl;
import com.corral.casino.dao.spi.BancoDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;
import com.corral.casino.service.spi.BancoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BancoServiceImpl implements BancoService {

    private final Logger logger = LogManager.getLogger(BancoServiceImpl.class.getName());
    private final BancoDAO bancoDAO;

    public BancoServiceImpl() {
        bancoDAO = new BancoDAOImpl();
    }

    @Override
    public Banco create(Banco banco) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (banco != null) {
                banco = bancoDAO.create(connection, banco);
            }
            return banco;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public List<Banco> findBy(BancoCriteria bancoCriteria) {
        List<Banco> bancoList = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (bancoCriteria != null) {
                bancoList = bancoDAO.findBy(connection, bancoCriteria);
            }
            return bancoList;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Banco update(Banco banco) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (banco != null && banco.getId() != null) {
                banco = bancoDAO.update(connection, banco);
            }
            return banco;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Banco asociarTarjeta(Banco banco, String tarjeta) {
        if (banco != null && banco.getId() != null) {
            banco.setTarjeta(tarjeta);
            return update(banco);
        }
        return null;
    }
}
