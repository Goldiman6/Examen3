package co.edu.poli.Examen2.domain.model;

/**
 * Enumeración que representa el factor Rhesus (Rh) de un individuo:
 * Positivo (Rh+) o Negativo (Rh-).
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public enum FactorRH {
    POSITIVO("+", "Rh+"),
    NEGATIVO("-", "Rh-");

    private final String signo;
    private final String descripcion;

    /**
     * Constructor del enum FactorRH.
     * 
     * @param signo Símbolo matemático asociado (+ o -).
     * @param descripcion Notación clínica formal (Rh+ o Rh-).
     */
    FactorRH(String signo, String descripcion) {
        this.signo = signo;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el signo representativo (+ o -).
     * 
     * @return Cadena con el signo.
     */
    public String getSigno() {
        return signo;
    }

    /**
     * Obtiene la descripción clínica del factor Rh.
     * 
     * @return Cadena descriptiva (ej. "Rh+").
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Convierte un valor booleano o texto al enum FactorRH.
     * 
     * @param esPositivo Verdadero si es positivo, falso si es negativo.
     * @return Instancia de {@link FactorRH}.
     */
    public static FactorRH fromBoolean(boolean esPositivo) {
        return esPositivo ? POSITIVO : NEGATIVO;
    }
}
