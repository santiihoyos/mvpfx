package Exceptions;

/**
 * Excepcion personalizada para notificar errores de mal implementacion de la
 * biblioteca.
 *
 * @author Santiago Hoyos Zea
 *
 */
public class Mvp4FxException extends Exception {

    /**
     *
     * @param message
     */
    public Mvp4FxException(String message) {
        super(message);
    }

}
