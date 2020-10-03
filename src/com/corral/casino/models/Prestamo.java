package com.corral.casino.models;

import java.util.Date;

public class Prestamo extends AbstractValueObject {
    private Integer id;
    private Integer idUsuario;
    private Double cantidad;
    private Boolean devuelto;
    private Date fechaCreacion;
    private Date fechaDevuelto;

    public Prestamo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaDevuelto() {
        return fechaDevuelto;
    }

    public void setFechaDevuelto(Date fechaDevuelto) {
        this.fechaDevuelto = fechaDevuelto;
    }
}
