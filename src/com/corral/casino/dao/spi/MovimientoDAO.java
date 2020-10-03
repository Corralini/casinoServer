package com.corral.casino.dao.spi;

import com.corral.casino.models.Movimiento;
import com.corral.casino.models.criteria.MovimientoCriteria;

import java.sql.Connection;
import java.util.List;

public interface MovimientoDAO {
    Movimiento create(Connection connection, Movimiento movimiento);

    List<Movimiento> findBy(Connection connection, MovimientoCriteria movimientoCriteria);

    Movimiento update(Connection connection, Movimiento movimiento);

}
