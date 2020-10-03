package com.corral.casino.dao.spi;

import com.corral.casino.models.Mesa;
import com.corral.casino.models.criteria.MesaCriteria;

import java.sql.Connection;
import java.util.List;

public interface MesaDAO {
    Mesa create(Connection connection, Mesa mesa);

    List<Mesa> findBy(Connection connection, MesaCriteria mesaCriteria);

    Mesa update(Connection connection, Mesa mesa);

    void delete(Connection connection, Mesa mesa);

}
