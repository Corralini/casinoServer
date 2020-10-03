package com.corral.casino.server.controller.pokertavern;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Mesa;
import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.MesaCriteria;
import com.corral.casino.models.criteria.UsuarioCriteria;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.MesaServiceImpl;
import com.corral.casino.service.impl.UsuarioServiceImpl;
import com.corral.casino.service.spi.MesaService;
import com.corral.casino.service.spi.UsuarioService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MesaController implements Controller {
    public static Logger logger = LogManager.getLogger(MesaController.class);
    private static CasinoException casinoException;
    private static MesaService mesaService;
    private static UsuarioService usuarioService;

    public MesaController() {
        mesaService = new MesaServiceImpl();
        usuarioService = new UsuarioServiceImpl();
    }

    private static JsonObject create(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer idJuego = entrada.has(Constants.ID_JUEGO) ? entrada.get(Constants.ID_JUEGO).getAsInt() : null;
        String nombreMesa = entrada.has(Constants.MESA) ? entrada.get(Constants.MESA).getAsString() : null;
        if (idJuego != null && nombreMesa != null) {
            try {
                Mesa mesa = new Mesa();
                mesa.setIdJuego(idJuego);
                mesa.setMesa(nombreMesa);
                mesa = mesaService.create(mesa);
                salida.add(Constants.MESA, new Gson().toJsonTree(mesa, new TypeToken<Mesa>() {
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
        Integer idJuego = entrada.has(Constants.ID_JUEGO) ? entrada.get(Constants.ID_JUEGO).getAsInt() : null;
        String nombreMesa = entrada.has(Constants.MESA) ? entrada.get(Constants.MESA).getAsString() : null;
        List<Mesa> mesaList;
        try {
            MesaCriteria mesaCriteria = new MesaCriteria();
            mesaCriteria.setId(id);
            mesaCriteria.setIdJuego(idJuego);
            mesaCriteria.setMesa(nombreMesa);
            mesaList = mesaService.findBy(mesaCriteria);
            salida.add(Constants.MESAS, new Gson().toJsonTree(mesaList, new TypeToken<List<Mesa>>() {
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
        Integer idJuego = entrada.has(Constants.ID_JUEGO) ? entrada.get(Constants.ID_JUEGO).getAsInt() : null;
        String nombreMesa = entrada.has(Constants.MESA) ? entrada.get(Constants.MESA).getAsString() : null;
        if (id != null) {
            try {
                Mesa mesa = new Mesa();
                mesa.setId(id);
                mesa.setIdJuego(idJuego);
                mesa.setMesa(nombreMesa);
                mesa = mesaService.update(mesa);
                salida.add(Constants.MESA, new Gson().toJsonTree(mesa, new TypeToken<Mesa>() {
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

    private static JsonObject delete(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        Integer id = entrada.has(Constants.ID) ? requestJson.getAsJsonObject(Constants.ID).getAsInt() : null;
        if (id != null) {
            try {
                UsuarioCriteria usuarioCriteria = new UsuarioCriteria();
                usuarioCriteria.setIdMesa(id);
                List<Usuario> usuarioList = usuarioService.findBy(usuarioCriteria);
                for (Usuario usuario : usuarioList) {
                    usuario.setIdMesa(null);
                    usuarioService.update(usuario);
                }
                Mesa mesa = new Mesa();
                mesa.setId(id);
                mesaService.delete(mesa);
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
            salida = (JsonObject) MesaController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(MesaController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }

}
