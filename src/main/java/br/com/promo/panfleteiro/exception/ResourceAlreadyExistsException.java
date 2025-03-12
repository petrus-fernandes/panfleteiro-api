package br.com.promo.panfleteiro.exception;



public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String className, String id) {
        super(className + " already exists with id: " + id);
    }
}

