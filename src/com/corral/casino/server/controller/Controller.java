package com.corral.casino.server.controller;

import com.google.gson.JsonObject;

public interface Controller {
    JsonObject init(JsonObject requestJson);
}
