package com.corral.casino.service.spi;

import com.corral.casino.models.Mesa;
import com.corral.casino.models.criteria.MesaCriteria;

import java.util.List;

public interface MesaService {
    Mesa create(Mesa mesa);

    List<Mesa> findBy(MesaCriteria mesaCriteria);

    Mesa update(Mesa mesa);

    void delete(Mesa mesa);

}
