package com.corral.casino.server.controller.cashbluff;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Prestamo;
import com.corral.casino.models.criteria.PrestamoCriteria;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.DateUtils;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.PrestamoServiceImpl;
import com.corral.casino.service.spi.PrestamoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.List;

public class PrestamoController implements Controller {
    public static Logger logger = LogManager.getLogger(PrestamoController.class);
    private static CasinoException casinoException;
    private static PrestamoService prestamoService;

    public PrestamoController() {
        prestamoService = new PrestamoServiceImpl();
    }

    private static JsonObject find(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        Integer idUsuario = entrada.has(Constants.ID_USUARIO) ? requestJson.getAsJsonObject(Constants.ID_USUARIO).getAsInt() : null;
        Double cantidad = entrada.has(Constants.CANTIDAD) ? requestJson.getAsJsonObject(Constants.CANTIDAD).getAsDouble() : null;
        Boolean devuelto = entrada.has(Constants.DEVUELTO) ? requestJson.getAsJsonObject(Constants.DEVUELTO).getAsBoolean() : null;
        String fechaCreacion = entrada.has(Constants.FECHA_CREACION) ? requestJson.getAsJsonObject(Constants.FECHA_CREACION).getAsString() : null;
        String fechaDevuelto = entrada.has(Constants.FECHA_DEVUELTO) ? requestJson.getAsJsonObject(Constants.FECHA_DEVUELTO).getAsString() : null;
        List<Prestamo> prestamoList;
        try {
            PrestamoCriteria prestamoCriteria = new PrestamoCriteria();
            prestamoCriteria.setId(id);
            prestamoCriteria.setIdUsuario(idUsuario);
            prestamoCriteria.setCantidad(cantidad);
            prestamoCriteria.setDevuelto(devuelto);
            prestamoCriteria.setFechaCreacion(DateUtils.stringToDate(fechaCreacion));
            prestamoCriteria.setFechaDevuelto(DateUtils.stringToDate(fechaDevuelto));
            prestamoList = prestamoService.findBy(prestamoCriteria);
            salida.add(Constants.PRESTAMOS, new Gson().toJsonTree(prestamoList, new TypeToken<List<Prestamo>>() {
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
        Double cantidad = entrada.has(Constants.CANTIDAD) ? requestJson.getAsJsonObject(Constants.CANTIDAD).getAsDouble() : null;
        Boolean devuelto = entrada.has(Constants.DEVUELTO) ? requestJson.getAsJsonObject(Constants.DEVUELTO).getAsBoolean() : null;
        String fechaCreacion = entrada.has(Constants.FECHA_CREACION) ? requestJson.getAsJsonObject(Constants.FECHA_CREACION).getAsString() : null;
        String fechaDevuelto = entrada.has(Constants.FECHA_DEVUELTO) ? requestJson.getAsJsonObject(Constants.FECHA_DEVUELTO).getAsString() : null;
        try {
            if (id != null) {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(id);
                prestamo.setIdUsuario(idUsuario);
                prestamo.setCantidad(cantidad);
                prestamo.setDevuelto(devuelto);
                prestamo.setFechaCreacion(DateUtils.stringToDate(fechaCreacion));
                prestamo.setFechaDevuelto(DateUtils.stringToDate(fechaDevuelto));
                prestamo = prestamoService.update(prestamo);
                salida.add(Constants.PRESTAMO, new Gson().toJsonTree(prestamo, new TypeToken<Prestamo>() {
                }.getType()).getAsJsonObject());
                return salida;
            } else {
                casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
                throw casinoException;
            }

        } catch (CasinoException e) {
            casinoException = e;
            throw casinoException;
        } catch (ParseException e) {
            casinoException = new CasinoException(Codes.MALFORMED_DATE);
            throw casinoException;
        }
    }

    private static JsonObject create(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer idUsuario = entrada.has(Constants.ID_USUARIO) ? requestJson.getAsJsonObject(Constants.ID_USUARIO).getAsInt() : null;
        Double cantidad = entrada.has(Constants.CANTIDAD) ? requestJson.getAsJsonObject(Constants.CANTIDAD).getAsDouble() : null;
        try {
            if (idUsuario != null && cantidad != null) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdUsuario(idUsuario);
                prestamo.setCantidad(cantidad);
                prestamo = prestamoService.create(prestamo);
                salida.add(Constants.PRESTAMO, new Gson().toJsonTree(prestamo, new TypeToken<Prestamo>() {
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

    @Override
    public JsonObject init(JsonObject requestJson) {
        JsonObject salida;
        try {
            salida = (JsonObject) PrestamoController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(PrestamoController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }
}
