package co.edu.poli.Examen2.domain.model;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;

/**
 * Entidad de dominio concreta que representa un Examen de Sangre.
 * Extiende la clase abstracta {@link Examen} incorporando atributos clínicos específicos:
 * el tipo de sangre (sistema ABO) y el factor Rhesus (Rh).
 * 
 * Pertenece al Núcleo de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class ExamenSangre extends Examen {

    private static final long serialVersionUID = 1L;

    /** Tipo de sangre del paciente (A, B, AB, O) */
    private TipoSangre tipoSangre;

    /** Factor Rhesus (POSITIVO / NEGATIVO) */
    private FactorRH factorRH;

    /**
     * Constructor por defecto para deserialización.
     */
    public ExamenSangre() {
        super();
    }

    /**
     * Constructor completo para inicializar un examen de sangre.
     * 
     * @param codigo Identificador del examen.
     * @param nombrePaciente Nombre completo del paciente.
     * @param costoBase Costo base del análisis.
     * @param tipoSangre Grupo sanguíneo del paciente.
     * @param factorRH Factor Rhesus del paciente.
     */
    public ExamenSangre(String codigo, String nombrePaciente, double costoBase, TipoSangre tipoSangre, FactorRH factorRH) {
        super(codigo, nombrePaciente, costoBase);
        this.tipoSangre = tipoSangre;
        this.factorRH = factorRH;
    }

    /**
     * Valida los atributos generales del examen más las invariantes específicas
     * del examen de sangre.
     * 
     * @throws DomainValidationException Si el tipo de sangre o factor RH no están definidos.
     */
    @Override
    public void validar() throws DomainValidationException {
        super.validar();
        if (tipoSangre == null) {
            throw new DomainValidationException("Debe seleccionar un tipo de sangre válido (A, B, AB u O).");
        }
        if (factorRH == null) {
            throw new DomainValidationException("Debe especificar el factor RH (Positivo o Negativo).");
        }
    }

    /**
     * Calcula el costo total del examen de sangre.
     * En esta fase de arquitectura limpia, retorna el costo base establecido.
     * 
     * @return Costo final del examen.
     */
    @Override
    public double calcularCostoTotal() {
        return this.costoBase;
    }

    // ================= Getters y Setters =================

    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(TipoSangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public FactorRH getFactorRH() {
        return factorRH;
    }

    public void setFactorRH(FactorRH factorRH) {
        this.factorRH = factorRH;
    }

    @Override
    public String getDetalleClinico() {
        return String.format("Tipo: %-2s %-3s",
                tipoSangre != null ? tipoSangre.name() : "N/D",
                factorRH != null ? factorRH.getSigno() : "");
    }
}
