package com.corral.casino.exceptions;

public class CasinoException extends RuntimeException {

    public final Codes cod;

    public CasinoException(Codes code, Throwable cause) {
        super(code.logMessage, cause);
        this.cod = code;
    }

    public CasinoException(Codes code) {
        super(code.logMessage);
        this.cod = code;
    }

}
