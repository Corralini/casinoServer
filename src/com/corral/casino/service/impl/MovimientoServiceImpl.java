package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.MovimientoDAOImpl;
import com.corral.casino.dao.spi.MovimientoDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Movimiento;
import com.corral.casino.models.criteria.MovimientoCriteria;
import com.corral.casino.service.spi.MovimientoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MovimientoServiceImpl implements MovimientoService {

    private final Logger logger = LogManager.getLogger(MovimientoServiceImpl.class.getName());
    private final MovimientoDAO movimientoDAO;

    public MovimientoServiceImpl() {
        movimientoDAO = new MovimientoDAOImpl();
    }

    @Override
    public Movimiento create(Movimiento movimiento) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (movimiento != null && movimiento.getIdUsuario() != null && movimiento.getIdjuego() != null
                    && (movimiento.getDinero() != null || movimiento.getPuntos() != null)) {
                movimiento = movimientoDAO.create(connection, movimiento);
            }
            return movimiento;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public List<Movimiento> findBy(MovimientoCriteria movimientoCriteria) {
        List<Movimiento> movimientoList = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (movimientoCriteria != null) {
                movimientoList = movimientoDAO.findBy(connection, movimientoCriteria);
            }
            return movimientoList;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Movimiento update(Movimiento movimiento) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (movimiento != null && movimiento.getId() != null) {
                movimiento = movimientoDAO.update(connection, movimiento);
            }
            return movimiento;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }
}
