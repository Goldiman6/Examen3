package co.edu.poli.Examen2.domain.decorator;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;
import java.util.Objects;

/**
 * Decorador Base Abstracto perteneciente al Patrón de Diseño Estructural Decorator (GoF).
 * 
 * Extiende la clase abstracta {@link Examen} (manteniendo conformidad de tipo)
 * y contiene una referencia hacia la instancia decorada (relación de composición).
 * Permite añadir dinámicamente servicios y recargos adicionales a cualquier examen médico
 * sin recurrir a una explosión combinatoria de subclases por herencia.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public abstract class ExamenDecorator extends Examen {

    private static final long serialVersionUID = 1L;

    /** Referencia al componente examen envuelto / decorado */
    protected final Examen examenDecorado;

    /**
     * Construye un decorador envolviendo un examen existente.
     * 
     * @param examenDecorado Instancia de {@link Examen} que se desea extender (no nula).
     */
    public ExamenDecorator(Examen examenDecorado) {
        super();
        this.examenDecorado = Objects.requireNonNull(examenDecorado, "El examen a decorar no puede ser nulo.");
        this.codigo = examenDecorado.getCodigo();
        this.nombrePaciente = examenDecorado.getNombrePaciente();
        this.costoBase = examenDecorado.getCostoBase();
    }

    /**
     * Obtiene el examen interno envuelto por este decorador.
     * 
     * @return Instancia decorada.
     */
    public Examen getExamenDecorado() {
        return examenDecorado;
    }

    @Override
    public String getCodigo() {
        return examenDecorado.getCodigo();
    }

    @Override
    public String getNombrePaciente() {
        return examenDecorado.getNombrePaciente();
    }

    @Override
    public double getCostoBase() {
        return examenDecorado.getCostoBase();
    }

    @Override
    public void validar() throws DomainValidationException {
        examenDecorado.validar();
    }

    /**
     * Por defecto delega el cálculo al componente envuelto.
     * Las subclases decoradoras sobrescriben este método para agregar su recargo específico.
     * 
     * @return Costo calculado por el componente subyacente.
     */
    @Override
    public double calcularCostoTotal() {
        return examenDecorado.calcularCostoTotal();
    }

    @Override
    public String getDetalleClinico() {
        return examenDecorado.getDetalleClinico();
    }

    @Override
    public String getServiciosAplicados() {
        return examenDecorado.getServiciosAplicados();
    }
}
