package co.edu.poli.Examen2.domain.exceptions;

/**
 * Excepción de dominio utilizada para notificar infracciones a las reglas
 * y restricciones del negocio (ej. código inválido, campos vacíos o valores monetarios no positivos).
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class DomainValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye una nueva excepción de validación de dominio con un mensaje descriptivo.
     * 
     * @param message Detalle del error o regla de negocio infringida.
     */
    public DomainValidationException(String message) {
        super(message);
    }

    /**
     * Construye una nueva excepción con mensaje y causa original.
     * 
     * @param message Detalle del error.
     * @param cause Causa de la excepción.
     */
    public DomainValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
