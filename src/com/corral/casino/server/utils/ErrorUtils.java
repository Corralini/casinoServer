package com.corral.casino.server.utils;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.server.MainServlet;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorUtils {

    private static Logger logger = LogManager.getLogger(ErrorUtils.class);

    private ErrorUtils() {
    }

    public static JsonObject gestError(Exception e) {
        Codes error;
        if (e instanceof CasinoException) {
            logger.warn("Exception: " + ((CasinoException) e).cod.logMessage);
            error = ((CasinoException) e).cod;
        } else {
            logger.warn("Exception: " + e.getMessage(), e);
            error = Codes.ERROR_GENERAL;
        }
        JsonObject errores = new JsonObject();
        errores.addProperty(Constants.STATUS, MainServlet.STATUS_KO);
        errores.addProperty("Codigo", error.cod);
        return errores;
    }

}
