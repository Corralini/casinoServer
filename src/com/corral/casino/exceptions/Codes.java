package com.corral.casino.exceptions;

public enum Codes {
    ERR_DB("E001", "Error en la base de datos"),
    COMMIT_DISABLED("E002", "Autocommit disabled. Commit or Rollback should be done explicitly."),
    NO_AUTOKEY("E003", "Unable to fetch autogenerated primary key"),
    MALFORMED_JSON("E004", "JSON mal estructurado"),
    ERROR_GENERAL("E005", "Error"),
    MANDATORY_PARAMS("E006", "Faltan parámetros obligatorios"),
    MALFORMED_DATE("E007", "Fecha malformada");

    public final String cod;
    public final String logMessage;

    Codes(String code, String logMessage) {
        this.cod = code;
        this.logMessage = logMessage;
    }
}
