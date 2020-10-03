package com.corral.casino.service.spi;

import com.corral.casino.models.Banco;
import com.corral.casino.models.Movimiento;
import com.corral.casino.models.criteria.BancoCriteria;
import com.corral.casino.models.criteria.MovimientoCriteria;

import java.util.List;

public interface MovimientoService {
    Movimiento create(Movimiento movimiento);

    List<Movimiento> findBy(MovimientoCriteria movimientoCriteria);

    Movimiento update(Movimiento movimiento);
}
