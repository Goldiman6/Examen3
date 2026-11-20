package co.edu.poli.Examen2.domain.model;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase base abstracta que representa un examen médico dentro del laboratorio clínico.
 * Encapsula los atributos comunes a cualquier tipo de examen y define el contrato de validación
 * y cálculo de costos según las reglas de negocio.
 * 
 * Implementa {@link Serializable} para soportar persistencia mediante flujos de bytes.
 * Pertenece al Núcleo de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public abstract class Examen implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Código identificador único del examen (ej. EXM-001) */
    protected String codigo;

    /** Nombre completo del paciente evaluado */
    protected String nombrePaciente;

    /** Costo monetario base del examen sin recargos adicionales */
    protected double costoBase;

    /**
     * Constructor por defecto protegido para subclases o frameworks de serialización.
     */
    protected Examen() {
    }

    /**
     * Constructor con los atributos comunes de un examen.
     * 
     * @param codigo Identificador alfanumérico único.
     * @param nombrePaciente Nombre del paciente.
     * @param costoBase Costo base del análisis (debe ser mayor a cero).
     */
    public Examen(String codigo, String nombrePaciente, double costoBase) {
        this.codigo = codigo != null ? codigo.trim() : null;
        this.nombrePaciente = nombrePaciente != null ? nombrePaciente.trim() : null;
        this.costoBase = costoBase;
    }

    /**
     * Valida que las reglas de negocio e invariantes del examen se cumplan.
     * 
     * @throws DomainValidationException Si algún campo obligatorio está ausente o no cumple con las restricciones.
     */
    public void validar() throws DomainValidationException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DomainValidationException("El código del examen es obligatorio y no puede estar vacío.");
        }
        if (nombrePaciente == null || nombrePaciente.trim().isEmpty()) {
            throw new DomainValidationException("El nombre del paciente es obligatorio.");
        }
        if (costoBase <= 0.0) {
            throw new DomainValidationException("El costo base debe ser un valor mayor a cero.");
        }
    }

    /**
     * Calcula el costo total del examen según las reglas de negocio aplicables.
     * 
     * @return Valor numérico del costo total.
     */
    public abstract double calcularCostoTotal();

    // ================= Getters y Setters =================

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

    /**
     * Retorna el detalle clínico específico del tipo de examen (ej. tipo de sangre y factor Rh).
     * 
     * @return Cadena con el detalle clínico.
     */
    public String getDetalleClinico() {
        return "";
    }

    /**
     * Retorna la descripción de los servicios o decoradores aplicados al examen.
     * 
     * @return Cadena descriptiva de los servicios añadidos.
     */
    public String getServiciosAplicados() {
        return "Estándar";
    }

    @Override
    public String toString() {
        String detalle = getDetalleClinico();
        String separador = (detalle != null && !detalle.isEmpty()) ? " | " + detalle : "";
        return String.format("[%s] Paciente: %-16s%s | Base: $%.2f | TOTAL: $%.2f | Servicios: %s",
                codigo,
                nombrePaciente,
                separador,
                costoBase,
                calcularCostoTotal(),
                getServiciosAplicados());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Examen)) return false;
        Examen examen = (Examen) o;
        return Objects.equals(codigo, examen.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
