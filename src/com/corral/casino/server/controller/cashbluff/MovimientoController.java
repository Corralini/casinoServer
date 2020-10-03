package com.corral.casino.server.controller.cashbluff;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Banco;
import com.corral.casino.models.Movimiento;
import com.corral.casino.models.criteria.MovimientoCriteria;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.DateUtils;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.MovimientoServiceImpl;
import com.corral.casino.service.spi.MovimientoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.List;

public class MovimientoController implements Controller {
    public static Logger logger = LogManager.getLogger(MovimientoController.class);
    private static CasinoException casinoException;
    private static MovimientoService movimientoService;

    public MovimientoController() {
        movimientoService = new MovimientoServiceImpl();
    }

    private static JsonObject create(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer idUsuario = entrada.has(Constants.ID_USUARIO) ? requestJson.getAsJsonObject(Constants.ID_USUARIO).getAsInt() : null;
        Integer idjuego = entrada.has(Constants.ID_JUEGO) ? requestJson.getAsJsonObject(Constants.ID_JUEGO).getAsInt() : null;
        Double dinero = entrada.has(Constants.DINERO) ? requestJson.getAsJsonObject(Constants.DINERO).getAsDouble() : null;
        Integer puntos = entrada.has(Constants.PUNTOS) ? requestJson.getAsJsonObject(Constants.PUNTOS).getAsInt() : null;
        try {
            if (idUsuario != null && idjuego != null && (dinero != null || puntos != null)) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdUsuario(idUsuario);
                movimiento.setIdjuego(idjuego);
                movimiento.setDinero(dinero);
                movimiento.setPuntos(puntos);
                movimiento = movimientoService.create(movimiento);
                salida.add(Constants.MOVIMIENTO, new Gson().toJsonTree(movimiento, new TypeToken<Movimiento>() {
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

    private static JsonObject find(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        Integer idUsuario = entrada.has(Constants.ID_USUARIO) ? requestJson.getAsJsonObject(Constants.ID_USUARIO).getAsInt() : null;
        Integer idjuego = entrada.has(Constants.ID_JUEGO) ? requestJson.getAsJsonObject(Constants.ID_JUEGO).getAsInt() : null;
        Double dinero = entrada.has(Constants.DINERO) ? requestJson.getAsJsonObject(Constants.DINERO).getAsDouble() : null;
        Integer puntos = entrada.has(Constants.PUNTOS) ? requestJson.getAsJsonObject(Constants.PUNTOS).getAsInt() : null;
        String fecha = entrada.has(Constants.FECHA) ? requestJson.getAsJsonObject(Constants.FECHA).getAsString() : null;
        List<Movimiento> movimientoList;
        try {
            MovimientoCriteria movimientoCriteria = new MovimientoCriteria();
            movimientoCriteria.setId(id);
            movimientoCriteria.setIdUsuario(idUsuario);
            movimientoCriteria.setIdjuego(idjuego);
            movimientoCriteria.setDinero(dinero);
            movimientoCriteria.setPuntos(puntos);
            movimientoCriteria.setFecha(DateUtils.stringToDate(fecha));
            movimientoList = movimientoService.findBy(movimientoCriteria);
            salida.add(Constants.MOVIMIENTOS, new Gson().toJsonTree(movimientoList, new TypeToken<List<Movimiento>>() {
            }.getType()).getAsJsonArray());
            return salida;
        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        } catch (ParseException pe) {
            casinoException = new CasinoException(Codes.MALFORMED_DATE);
            throw casinoException;
        }
    }

    private static JsonObject update(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        Integer idUsuario = entrada.has(Constants.ID_USUARIO) ? requestJson.getAsJsonObject(Constants.ID_USUARIO).getAsInt() : null;
        Integer idjuego = entrada.has(Constants.ID_JUEGO) ? requestJson.getAsJsonObject(Constants.ID_JUEGO).getAsInt() : null;
        Double dinero = entrada.has(Constants.DINERO) ? requestJson.getAsJsonObject(Constants.DINERO).getAsDouble() : null;
        Integer puntos = entrada.has(Constants.PUNTOS) ? requestJson.getAsJsonObject(Constants.PUNTOS).getAsInt() : null;
        String fecha = entrada.has(Constants.FECHA) ? requestJson.getAsJsonObject(Constants.FECHA).getAsString() : null;
        try {
            if (id != null) {
                Movimiento movimiento = new Movimiento();
                movimiento.setId(id);
                movimiento.setIdUsuario(idUsuario);
                movimiento.setIdjuego(idjuego);
                movimiento.setDinero(dinero);
                movimiento.setPuntos(puntos);
                movimiento.setFecha(DateUtils.stringToDate(fecha));
                movimiento = movimientoService.update(movimiento);
                salida.add(Constants.MOVIMIENTO, new Gson().toJsonTree(movimiento, new TypeToken<Movimiento>() {
                }.getType()).getAsJsonObject());
                return salida;
            } else {
                casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
                throw casinoException;
            }

        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        } catch (ParseException pe) {
            casinoException = new CasinoException(Codes.MALFORMED_DATE);
            throw casinoException;
        }
    }

    @Override
    public JsonObject init(JsonObject requestJson) {
        JsonObject salida;
        try {
            salida = (JsonObject) MovimientoController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(MovimientoController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }
}
