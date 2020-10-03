package com.corral.casino.service.spi;

import com.corral.casino.models.Prestamo;
import com.corral.casino.models.criteria.PrestamoCriteria;

import java.util.List;

public interface PrestamoService {
    Prestamo create(Prestamo prestamo);

    List<Prestamo> findBy(PrestamoCriteria prestamoCriteria);

    Prestamo update(Prestamo prestamo);
}
