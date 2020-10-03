package com.corral.casino.service.spi;

import com.corral.casino.models.Juego;
import com.corral.casino.models.criteria.JuegoCriteria;

import java.util.List;

public interface JuegoService {

    Juego create(Juego juego);

    List<Juego> findBy(JuegoCriteria juegoCriteria);

    Juego update(Juego juego);


}
