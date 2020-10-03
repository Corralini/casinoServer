package com.corral.casino.dao.spi;

import com.corral.casino.models.Prestamo;
import com.corral.casino.models.criteria.PrestamoCriteria;

import java.sql.Connection;
import java.util.List;

public interface PrestamoDAO {

    Prestamo create(Connection connection, Prestamo prestamo);

    List<Prestamo> findBy(Connection connection, PrestamoCriteria prestamoCriteria);

    Prestamo update(Connection connection, Prestamo prestamo);

}
