package com.corral.casino.server;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.server.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MainServlet extends HttpServlet {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_KO = "KO";
    private static final String SALIDA = "Salida";
    private static Logger logger = LogManager.getLogger(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject respuestaJson = new JsonObject();
        try {
            JsonObject requestJSON = process(request);
            if (requestJSON != null && requestJSON.get(Constants.CONTROLLER).getAsString() != null && requestJSON.get(Constants.APPLICATION).getAsString() != null) {
//                checkLogin(requestJSON);
                StringBuilder controllerName = new StringBuilder("com.corral.casino.server.controller.")
                        .append(requestJSON.get(Constants.APPLICATION).getAsString()).append(".")
                        .append(requestJSON.get(Constants.CONTROLLER).getAsString()).append("Controller");
                Class<?> cls = Class.forName(controllerName.toString());
                Object object = cls.newInstance();

                JsonObject salidaJSON = (JsonObject) cls.getDeclaredMethod("init", JsonObject.class).invoke(object, requestJSON);
                processResponse(respuestaJson, salidaJSON, false);
                respuestaJson.add(SALIDA, salidaJSON);
            }
        } catch (Exception e) {
            JsonObject salidaJSON = new JsonObject();
            salidaJSON.addProperty(Constants.STATUS, STATUS_KO);
            processResponse(respuestaJson, salidaJSON, true);
            respuestaJson.add(SALIDA, salidaJSON);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        out.println(respuestaJson.toString());

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    private JsonObject process(HttpServletRequest request) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            throw new CasinoException(Codes.MALFORMED_JSON, e);
        }

        return new Gson().fromJson(jb.toString(), JsonObject.class);

    }

    private void processResponse(JsonObject respuestaJson, JsonObject salidaJSON, boolean isKO) {
        if (salidaJSON.has(Constants.STATUS)) {
            respuestaJson.addProperty(Constants.STATUS, salidaJSON.get(Constants.STATUS).getAsString());
            salidaJSON.remove(Constants.STATUS);
        } else {
            if (isKO) {
                respuestaJson.addProperty(Constants.STATUS, MainServlet.STATUS_KO);
            } else {
                respuestaJson.addProperty(Constants.STATUS, MainServlet.STATUS_OK);

            }
        }
    }
}
