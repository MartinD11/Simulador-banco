package com.tubanco.bancocommon.exceptions;

public class CuentaInactivaException extends RuntimeException {
    public CuentaInactivaException(String message) {
        super(message);
    }
}