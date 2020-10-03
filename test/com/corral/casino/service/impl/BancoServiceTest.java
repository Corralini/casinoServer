package com.corral.casino.service.impl;

import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;
import com.corral.casino.service.spi.BancoService;

public class BancoServiceTest {

    private static BancoService bancoService = new BancoServiceImpl();

    private static void asociarTarjeta(Integer bancoId) {
        BancoCriteria bancoCriteria = new BancoCriteria();
        bancoCriteria.setId(bancoId);
        Banco banco = bancoService.findBy(bancoCriteria).get(0);
        if (banco != null) {
            bancoService.asociarTarjeta(banco, "0003454409");
        }
    }

    public static void main(String[] args) {
        asociarTarjeta(1);
    }
}
