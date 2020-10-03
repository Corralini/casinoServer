package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.MesaDAOImpl;
import com.corral.casino.dao.spi.MesaDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Mesa;
import com.corral.casino.models.criteria.MesaCriteria;
import com.corral.casino.service.spi.MesaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MesaServiceImpl implements MesaService {

    private final Logger logger = LogManager.getLogger(MovimientoServiceImpl.class.getName());
    private final MesaDAO mesaDAO;

    public MesaServiceImpl() {
        mesaDAO = new MesaDAOImpl();
    }

    @Override
    public Mesa create(Mesa mesa) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (mesa != null && mesa.getMesa() != null && mesa.getIdJuego() != null) {
                mesa = mesaDAO.create(connection, mesa);
            }
            return mesa;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public List<Mesa> findBy(MesaCriteria mesaCriteria) {
        List<Mesa> mesaList = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (mesaCriteria != null) {
                mesaList = mesaDAO.findBy(connection, mesaCriteria);
            }
            return mesaList;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Mesa update(Mesa mesa) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (mesa != null && mesa.getId() != null) {
                mesa = mesaDAO.update(connection, mesa);
            }
            return mesa;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public void delete(Mesa mesa) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (mesa != null && mesa.getId() != null) {
                mesaDAO.delete(connection, mesa);
            }
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }
}
