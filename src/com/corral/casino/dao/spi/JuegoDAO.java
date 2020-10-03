package com.corral.casino.dao.spi;

import com.corral.casino.models.Juego;
import com.corral.casino.models.criteria.JuegoCriteria;

import java.sql.Connection;
import java.util.List;

public interface JuegoDAO {
    Juego create(Connection connection, Juego juego);

    List<Juego> findBy(Connection connection, JuegoCriteria juegoCriteria);

    Juego update(Connection connection, Juego juego);

}
