package com.corral.casino.server.controller.pokertavern;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;
import com.corral.casino.models.Usuario;
import com.corral.casino.models.criteria.UsuarioCriteria;
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

import java.util.ArrayList;
import java.util.List;

public class UsuarioController implements Controller {

    public static Logger logger = LogManager.getLogger(UsuarioController.class);
    private static UsuarioService usuarioService;
    private static CasinoException casinoException;

    public UsuarioController() {
        usuarioService = new UsuarioServiceImpl();
    }

    private static JsonObject login(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        String usuario = entrada.has(Constants.USUARIO) ? entrada.get(Constants.USUARIO).getAsString() : null;
        String psswd = entrada.has(Constants.PSSWD) ? entrada.get(Constants.PSSWD).getAsString() : null;
        if (usuario != null && psswd != null) {
            try {
                Usuario login = usuarioService.login(usuario, psswd);
                salida.add(Constants.USUARIO, new Gson().toJsonTree(login, new TypeToken<Usuario>() {
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

    private static JsonObject signup(JsonObject requestJson) {
        JsonObject salida = new JsonObject();
        JsonObject entrada = requestJson.getAsJsonObject(Constants.ENTRADA);
        String email = entrada.has(Constants.EMAIL) ? entrada.get(Constants.EMAIL).getAsString() : null;
        String nombre = entrada.has(Constants.NOMBRE) ? entrada.get(Constants.NOMBRE).getAsString() : null;
        String apellido1 = entrada.has(Constants.APELLIDO1) ? entrada.get(Constants.APELLIDO1).getAsString() : null;
        String apellido2 = entrada.has(Constants.APELLIDO2) ? entrada.get(Constants.APELLIDO2).getAsString() : null;
        String user = entrada.has(Constants.USUARIO) ? entrada.get(Constants.USUARIO).getAsString() : null;
        String psswd = entrada.has(Constants.PSSWD) ? entrada.get(Constants.PSSWD).getAsString() : null;
        if (user != null && psswd != null && email != null && nombre != null && apellido1 != null && apellido2 != null) {
            try {
                Usuario usuario = new Usuario();
                usuario.setUsuario(user);
                usuario.setPsswd(psswd);
                usuario.setEmail(email);
                usuario.setNombre(nombre);
                usuario.setApellido1(apellido1);
                usuario.setApellido2(apellido2);
                usuario = usuarioService.create(usuario);
                salida.add(Constants.USUARIO, new Gson().toJsonTree(usuario, new TypeToken<Usuario>() {
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
        Integer id = entrada.has(Constants.ID) ? entrada.get(Constants.ID).getAsInt() : null;
        String email = entrada.has(Constants.EMAIL) ? entrada.get(Constants.EMAIL).getAsString() : null;
        String nombre = entrada.has(Constants.NOMBRE) ? entrada.get(Constants.NOMBRE).getAsString() : null;
        String apellido1 = entrada.has(Constants.APELLIDO1) ? entrada.get(Constants.APELLIDO1).getAsString() : null;
        String apellido2 = entrada.has(Constants.APELLIDO2) ? entrada.get(Constants.APELLIDO2).getAsString() : null;
        String usuario = entrada.has(Constants.USUARIO) ? entrada.get(Constants.USUARIO).getAsString() : null;
        String psswd = entrada.has(Constants.PSSWD) ? entrada.get(Constants.PSSWD).getAsString() : null;
        String fechaSub = entrada.has(Constants.FECHA_SUB) ? entrada.get(Constants.FECHA_SUB).getAsString() : null;
        Integer idMesa = entrada.has(Constants.ID_MESA) ? entrada.get(Constants.ID_MESA).getAsInt() : null;
        Integer idBanco = entrada.has(Constants.ID_BANCO) ? entrada.get(Constants.ID_BANCO).getAsInt() : null;
        List<Usuario> usuarioList;
        try {
            UsuarioCriteria usuarioCriteria = new UsuarioCriteria();
            usuarioCriteria.setId(id);
            usuarioCriteria.setUsuario(usuario);
            usuarioCriteria.setPsswd(psswd);
            usuarioCriteria.setEmail(email);
            usuarioCriteria.setNombre(nombre);
            usuarioCriteria.setApellido1(apellido1);
            usuarioCriteria.setApellido2(apellido2);
            usuarioCriteria.setFechaSub(fechaSub);
            usuarioCriteria.setIdMesa(idMesa);
            usuarioCriteria.setIdBanco(idBanco);
            usuarioList = usuarioService.findBy(usuarioCriteria);
            salida.add(Constants.USUARIOS, new Gson().toJsonTree(usuarioList, new TypeToken<List<Usuario>>() {
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
        Integer id = entrada.has(Constants.ID) ? entrada.get(Constants.ID).getAsInt() : null;
        String email = entrada.has(Constants.EMAIL) ? entrada.get(Constants.EMAIL).getAsString() : null;
        String nombre = entrada.has(Constants.NOMBRE) ? entrada.get(Constants.NOMBRE).getAsString() : null;
        String apellido1 = entrada.has(Constants.APELLIDO1) ? entrada.get(Constants.APELLIDO1).getAsString() : null;
        String apellido2 = entrada.has(Constants.APELLIDO2) ? entrada.get(Constants.APELLIDO2).getAsString() : null;
        ;
        String usuario = entrada.has(Constants.USUARIO) ? entrada.get(Constants.USUARIO).getAsString() : null;
        String psswd = entrada.has(Constants.PSSWD) ? entrada.get(Constants.PSSWD).getAsString() : null;
        String fechaSub = entrada.has(Constants.FECHA_SUB) ? entrada.get(Constants.FECHA_SUB).getAsString() : null;
        Integer idMesa = entrada.has(Constants.ID_MESA) ? entrada.get(Constants.ID_MESA).getAsInt() : null;
        Integer idBanco = entrada.has(Constants.ID_BANCO) ? entrada.get(Constants.ID_BANCO).getAsInt() : null;
        try {
            if (id != null) {
                Usuario user = new Usuario();
                user.setId(id);
                user.setUsuario(usuario);
                user.setPsswd(psswd);
                user.setEmail(email);
                user.setNombre(nombre);
                user.setApellido1(apellido1);
                user.setApellido2(apellido2);
                user.setFechaSub(fechaSub);
                user.setIdMesa(idMesa);
                user.setIdBanco(idBanco);

                user = usuarioService.update(user);
                salida.add(Constants.USUARIO, new Gson().toJsonTree(user, new TypeToken<Usuario>() {
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
            salida = (JsonObject) UsuarioController.class.getDeclaredMethod(requestJson.get(Constants.METODO).getAsString(), JsonObject.class)
                    .invoke(UsuarioController.class, requestJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            salida = ErrorUtils.gestError(e);
        }
        return salida;
    }

}
