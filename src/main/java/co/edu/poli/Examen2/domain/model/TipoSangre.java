package co.edu.poli.Examen2.domain.model;

/**
 * Enumeración que representa los cuatro grupos sanguíneos principales
 * según el sistema ABO: A, B, AB y O.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public enum TipoSangre {
    A,
    B,
    AB,
    O;

    /**
     * Parsea un texto al tipo de sangre correspondiente de forma segura.
     * 
     * @param valor Texto a convertir (ej. "A", "AB").
     * @return Constante de {@link TipoSangre}.
     * @throws IllegalArgumentException Si el valor no corresponde a ningún grupo válido.
     */
    public static TipoSangre fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de sangre no puede ser nulo o vacío.");
        }
        return TipoSangre.valueOf(valor.trim().toUpperCase());
    }
}
