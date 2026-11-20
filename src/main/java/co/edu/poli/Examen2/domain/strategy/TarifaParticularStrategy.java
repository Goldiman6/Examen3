package co.edu.poli.Examen2.domain.strategy;

/**
 * Estrategia Concreta (ConcreteStrategy) del Patrón GoF Strategy.
 * Representa la política de tarificación para pacientes particulares
 * que no tienen ningún tipo de convenio o descuento vigente.
 * 
 * El precio final es el costo acumulado tal cual, sin ninguna modificación.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class TarifaParticularStrategy implements TarifaStrategy {

    /**
     * Retorna el costo acumulado sin ningún ajuste.
     * El paciente particular paga el 100% del valor del examen incluyendo
     * todos los servicios adicionales aplicados (Decorators).
     * 
     * @param costoAcumulado Costo total acumulado con los servicios adicionales.
     * @return El mismo costo acumulado sin descuento.
     */
    @Override
    public double calcularPrecioFinal(double costoAcumulado) {
        return costoAcumulado;
    }

    @Override
    public String getNombreEstrategia() {
        return "Particular";
    }

    @Override
    public String getDescripcionAjuste() {
        return "Sin descuento (100%)";
    }
}
