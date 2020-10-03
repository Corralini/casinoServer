package com.corral.casino.service.impl;

import com.corral.casino.dao.impl.MovimientoDAOImpl;
import com.corral.casino.dao.impl.PrestamoDAOImpl;
import com.corral.casino.dao.spi.MovimientoDAO;
import com.corral.casino.dao.spi.PrestamoDAO;
import com.corral.casino.dao.utils.ConnectionManager;
import com.corral.casino.dao.utils.JDBCUtils;
import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Prestamo;
import com.corral.casino.models.criteria.PrestamoCriteria;
import com.corral.casino.service.spi.PrestamoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {

    private final Logger logger = LogManager.getLogger(PrestamoServiceImpl.class.getName());
    private final PrestamoDAO prestamoDAO;

    public PrestamoServiceImpl() {
        prestamoDAO = new PrestamoDAOImpl();
    }

    @Override
    public Prestamo create(Prestamo prestamo) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            if (prestamo != null && prestamo.getIdUsuario() != null && prestamo.getCantidad() != null) {
                prestamo = prestamoDAO.create(connection, prestamo);
            }
            return prestamo;
        } catch (SQLException throwables) {
            throw new CasinoException(Codes.ERR_DB, throwables);
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Override
    public List<Prestamo> findBy(PrestamoCriteria prestamoCriteria) {
        return null;
    }

    @Override
    public Prestamo update(Prestamo prestamo) {
        return null;
    }
}
