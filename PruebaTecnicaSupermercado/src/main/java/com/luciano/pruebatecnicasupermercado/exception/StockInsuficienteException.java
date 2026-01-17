package com.luciano.pruebatecnicasupermercado.exception;

public class StockInsuficienteException extends RuntimeException{

    public StockInsuficienteException(String message) {
        super(message);
    }
}
