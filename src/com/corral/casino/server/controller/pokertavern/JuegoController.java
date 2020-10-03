package com.corral.casino.server.controller.pokertavern;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Juego;
import com.corral.casino.models.Mesa;
import com.corral.casino.models.criteria.JuegoCriteria;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.JuegoServiceImpl;
import com.corral.casino.service.spi.JuegoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class JuegoController implements Controller {
    public static Logger logger = LogManager.getLogger(JuegoController.class);
    private static CasinoException casinoException;
    private static JuegoService juegoService;

    public JuegoController() {
        juegoService = new JuegoServiceImpl();
    }

    private static JsonObject create(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        String nombreJuego = entrada.has(Constants.JUEGO) ? entrada.get(Constants.JUEGO).getAsString() : null;
        Integer rqPoints = entrada.has(Constants.RQ_POINTS) ? entrada.get(Constants.RQ_POINTS).getAsInt() : null;
        if (nombreJuego != null) {
            try {
                Juego juego = new Juego();
                juego.setJuego(nombreJuego);
                if (rqPoints != null) {
                    juego.setRqPoints(rqPoints);
                }
                juego = juegoService.create(juego);
                salida.add(Constants.JUEGO, new Gson().toJsonTree(juego, new TypeToken<Juego>() {
                }.getType()).getAsJsonObject());
                return salida;
            } catch (CasinoException e) {
                casinoException = e;
                throw casinoException;
            }
        } else {
            casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
            throw casinoException;
        }
    }

    private static JsonObject find(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        String nombreJuego = entrada.has(Constants.JUEGO) ? entrada.get(Constants.JUEGO).getAsString() : null;
        Integer rqPoints = entrada.has(Constants.RQ_POINTS) ? entrada.get(Constants.RQ_POINTS).getAsInt() : null;
        List<Juego> juegoList;
        try {
            JuegoCriteria juegoCriteria = new JuegoCriteria();
            juegoCriteria.setId(id);
            juegoCriteria.setJuego(nombreJuego);
            juegoCriteria.setRqPoints(rqPoints);
            juegoList = juegoService.findBy(juegoCriteria);
            salida.add(Constants.MESAS, new Gson().toJsonTree(juegoList, new TypeToken<List<Juego>>() {
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
        String nombreJuego = entrada.has(Constants.JUEGO) ? entrada.get(Constants.JUEGO).getAsString() : null;
        Integer rqPoints = entrada.has(Constants.RQ_POINTS) ? entrada.get(Constants.RQ_POINTS).getAsInt() : null;
        if (id != null) {
            try {
                Juego juego = new Juego();
                juego.setId(id);
                juego.setJuego(nombreJuego);
                juego.setRqPoints(rqPoints);
                juego = juegoService.update(juego);
                salida.add(Constants.MESA, new Gson().toJsonTree(juego, new TypeToken<Juego>() {
                }.getType()).getAsJsonObject());
                return salida;
            } catch (CasinoException e) {
                casinoException = e;
                throw casinoException;
            }
        } else {
            casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
            throw casinoException;
        }
    }

    @Override
    public JsonObject init(JsonObject requestJson) {
        JsonObject salida;
        try {
            salida = (JsonObject) JuegoController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(JuegoController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }
}
