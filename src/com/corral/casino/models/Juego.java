package com.corral.casino.models;

public class Juego extends AbstractValueObject {
    private Integer id;
    private String juego;
    private Integer rqPoints;

    public Juego() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public Integer getRqPoints() {
        return rqPoints;
    }

    public void setRqPoints(Integer rqPoints) {
        this.rqPoints = rqPoints;
    }
}
