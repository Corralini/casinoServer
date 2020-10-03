package com.corral.casino.dao.spi;

import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;

import java.sql.Connection;
import java.util.List;

public interface BancoDAO {
    Banco create(Connection connection, Banco banco);

    List<Banco> findBy(Connection connection, BancoCriteria bancoCriteria);

    Banco update(Connection connection, Banco banco);

}
