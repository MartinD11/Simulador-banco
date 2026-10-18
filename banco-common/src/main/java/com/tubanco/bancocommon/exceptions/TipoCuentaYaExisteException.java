package com.tubanco.bancocommon.exceptions;

public class TipoCuentaYaExisteException extends RuntimeException {
    public TipoCuentaYaExisteException(String message) {
        super(message);
    }
}