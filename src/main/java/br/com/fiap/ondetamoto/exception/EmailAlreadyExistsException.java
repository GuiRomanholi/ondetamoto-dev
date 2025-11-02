package br.com.fiap.ondetamoto.exception;

// Define uma exceção customizada para ser lançada quando um email já existe no sistema.
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}