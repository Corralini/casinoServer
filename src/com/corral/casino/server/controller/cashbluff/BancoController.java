package com.corral.casino.server.controller.cashbluff;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.criteria.BancoCriteria;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.BancoServiceImpl;
import com.corral.casino.service.spi.BancoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BancoController implements Controller {
    public static Logger logger = LogManager.getLogger(BancoController.class);
    private static CasinoException casinoException;
    private static BancoService bancoService;

    public BancoController() {
        bancoService = new BancoServiceImpl();
    }

    private static JsonObject create(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        try {
            Banco banco = new Banco();
            banco = bancoService.create(banco);
            salida.add(Constants.BANCO, new Gson().toJsonTree(banco, new TypeToken<Banco>() {
            }.getType()).getAsJsonObject());
            return salida;
        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        }
    }

    private static JsonObject find(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        String tarjeta = entrada.has(Constants.TARJETA) ? entrada.get(Constants.TARJETA).getAsString() : null;
        Integer puntos = entrada.has(Constants.PUNTOS) ? entrada.get(Constants.PUNTOS).getAsInt() : null;
        Double dinero = entrada.has(Constants.DINERO) ? entrada.get(Constants.DINERO).getAsDouble() : null;
        List<Banco> bancoList;
        try {
            BancoCriteria bancoCriteria = new BancoCriteria();
            bancoCriteria.setId(id);
            bancoCriteria.setTarjeta(tarjeta);
            bancoCriteria.setPuntos(puntos);
            bancoCriteria.setDinero(dinero);
            bancoList = bancoService.findBy(bancoCriteria);
            salida.add(Constants.BANCOS, new Gson().toJsonTree(bancoList, new TypeToken<List<Banco>>() {
            }.getType()).getAsJsonArray());
            return salida;
        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        }
    }

    private static JsonObject update(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        String tarjeta = entrada.has(Constants.TARJETA) ? entrada.get(Constants.TARJETA).getAsString() : null;
        Integer puntos = entrada.has(Constants.PUNTOS) ? entrada.get(Constants.PUNTOS).getAsInt() : null;
        Double dinero = entrada.has(Constants.DINERO) ? entrada.get(Constants.DINERO).getAsDouble() : null;
        try {
            if (id != null) {
                Banco banco = new Banco();
                banco.setId(id);
                banco.setTarjeta(tarjeta);
                banco.setPuntos(puntos);
                banco.setDinero(dinero);
                banco = bancoService.update(banco);
                salida.add(Constants.BANCO, new Gson().toJsonTree(banco, new TypeToken<Banco>() {
                }.getType()).getAsJsonObject());
                return salida;
            } else {
                casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
                throw casinoException;
            }
        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        }
    }

    private static JsonObject asociarTarjeta(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        String tarjeta = entrada.has(Constants.TARJETA) ? entrada.get(Constants.TARJETA).getAsString() : null;
        try {
            if (id != null) {
                Banco banco = new Banco();
                banco.setId(id);
                banco = bancoService.asociarTarjeta(banco, tarjeta);
                salida.add(Constants.BANCO, new Gson().toJsonTree(banco, new TypeToken<Banco>() {
                }.getType()).getAsJsonObject());
                return salida;
            } else {
                throw new CasinoException(Codes.MANDATORY_PARAMS);
            }
        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        }
    }

    @Override
    public JsonObject init(JsonObject requestJson) {
        JsonObject salida;
        try {
            salida = (JsonObject) BancoController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(BancoController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }
}
