package co.edu.poli.Examen2.domain.decorator;

import co.edu.poli.Examen2.domain.model.Examen;

/**
 * Decorador Concreto (ConcreteDecorator) del Patrón GoF Decorator.
 * 
 * Agrega el servicio clínico de "Procesamiento Prioritario / Urgencia" al examen médico.
 * Incorpora un recargo porcentual sobre el costo base del análisis para reflejar
 * la prioridad operativa del laboratorio clínico.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class UrgenciaDecorator extends ExamenDecorator {

    private static final long serialVersionUID = 1L;

    /** Porcentaje de recargo por urgencia (por defecto 25% = 0.25) */
    private final double porcentajeRecargo;

    /**
     * Construye un decorador de urgencia con el recargo estándar del 25%.
     * 
     * @param examenDecorado Examen al cual se le añade la urgencia.
     */
    public UrgenciaDecorator(Examen examenDecorado) {
        this(examenDecorado, 0.25);
    }

    /**
     * Construye un decorador de urgencia especificando el porcentaje de recargo.
     * 
     * @param examenDecorado Examen al cual se le añade la urgencia.
     * @param porcentajeRecargo Fracción decimal de recargo (ej. 0.25 para 25%).
     */
    public UrgenciaDecorator(Examen examenDecorado, double porcentajeRecargo) {
        super(examenDecorado);
        this.porcentajeRecargo = porcentajeRecargo;
    }

    /**
     * Calcula el costo total sumando al costo acumulado del examen el recargo por urgencia.
     * 
     * @return Costo total con recargo de urgencia incluido.
     */
    @Override
    public double calcularCostoTotal() {
        double costoPrevio = examenDecorado.calcularCostoTotal();
        double recargoUrgencia = examenDecorado.getCostoBase() * porcentajeRecargo;
        return costoPrevio + recargoUrgencia;
    }

    /**
     * Obtiene el porcentaje de recargo aplicado.
     * 
     * @return Valor del porcentaje de recargo.
     */
    public double getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    @Override
    public String getServiciosAplicados() {
        String previo = examenDecorado.getServiciosAplicados();
        String servicio = String.format("Urgencia (+%.0f%%)", porcentajeRecargo * 100);
        return "Estándar".equals(previo) ? servicio : previo + " + " + servicio;
    }
}
