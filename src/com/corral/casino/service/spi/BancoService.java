package com.corral.casino.service.spi;

import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;

import java.util.List;

public interface BancoService {

    Banco create(Banco banco);

    List<Banco> findBy(BancoCriteria bancoCriteria);

    Banco update(Banco banco);

    Banco asociarTarjeta(Banco banco, String tarjeta);

}
