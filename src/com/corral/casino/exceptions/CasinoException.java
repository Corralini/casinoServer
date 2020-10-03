package com.corral.casino.exceptions;

public class CasinoException extends RuntimeException {

    public final String cod;

    public CasinoException(Codes code, Throwable cause) {
        super(code.logMessage, cause);
        this.cod = code.cod;
    }
    public CasinoException(Codes code) {
        super(code.logMessage);
        this.cod = code.cod;
    }

}
