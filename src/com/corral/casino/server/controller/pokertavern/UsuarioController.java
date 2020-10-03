package com.corral.casino.server.controller.pokertavern;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Usuario;
import com.corral.casino.server.controller.Controller;
import com.corral.casino.server.utils.Constants;
import com.corral.casino.server.utils.ErrorUtils;
import com.corral.casino.service.impl.UsuarioServiceImpl;
import com.corral.casino.service.spi.UsuarioService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsuarioController implements Controller {

    public static Logger logger = LogManager.getLogger(UsuarioController.class);
    private static UsuarioService usuarioService;
    private static CasinoException casinoException;

    public UsuarioController() {
        usuarioService = new UsuarioServiceImpl();
    }

    @Override
    public JsonObject init(JsonObject requestJson) {
        JsonObject salida;
        try {
            salida = (JsonObject) UsuarioController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(UsuarioController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }

    private static JsonObject login(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        String usuario = entrada.get(Constants.USER).getAsString();
        String psswd = entrada.get(Constants.PSSWD).getAsString();
        if (usuario != null && psswd != null) {
            try{
                Usuario login = usuarioService.login(usuario, psswd);
                salida.add(Constants.USUARIO, new Gson().toJsonTree(login, new TypeToken<Usuario>(){}.getType()).getAsJsonObject());
                return salida;
            } catch (CasinoException e){
                casinoException = e;
                throw casinoException;
            }
        } else {
            casinoException = new CasinoException(Codes.MANDATORY_PARAMS);
            throw casinoException;
        }
    }
}
