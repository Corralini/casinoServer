package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.JuegoDAOImpl;
import com.corral.casino.dao.spi.JuegoDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Juego;
import com.corral.casino.models.criteria.JuegoCriteria;
import com.corral.casino.service.spi.JuegoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class JuegoServiceImpl implements JuegoService {

    private final Logger logger = LogManager.getLogger(JuegoServiceImpl.class.getName());
    private final JuegoDAO juegoDAO;

    public JuegoServiceImpl() {
        juegoDAO = new JuegoDAOImpl();
    }

    @Override
    public Juego create(Juego juego) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (juego != null && juego.getJuego() != null) {
                juego = juegoDAO.create(connection, juego);
            }
            return juego;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public List<Juego> findBy(JuegoCriteria juegoCriteria) {
        List<Juego> juegoList = null;
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (juegoCriteria != null) {
                juegoList = juegoDAO.findBy(connection, juegoCriteria);
            }
            return juegoList;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public Juego update(Juego juego) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (juego != null && juego.getId() != null) {
                juego = juegoDAO.update(connection, juego);
            }
            return juego;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }
}
