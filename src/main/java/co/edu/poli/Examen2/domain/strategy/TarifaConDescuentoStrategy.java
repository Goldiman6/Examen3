package co.edu.poli.Examen2.domain.strategy;

/**
 * Estrategia abstracta base para políticas de tarificación basadas en porcentajes de descuento.
 * Encapsula y centraliza el algoritmo matemático de cálculo de descuentos sobre el costo acumulado,
 * eliminando duplicación de código según las mejores prácticas de Clean Code y principios SOLID.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public abstract class TarifaConDescuentoStrategy implements TarifaStrategy {

    /** Fracción decimal del descuento aplicado (ej. 0.15 para 15%) */
    private final double porcentajeDescuento;

    /**
     * Construye la estrategia con el porcentaje de descuento especificado.
     * 
     * @param porcentajeDescuento Porcentaje en formato decimal (entre 0.0 y 1.0).
     */
    protected TarifaConDescuentoStrategy(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Aplica el porcentaje de descuento sobre el costo acumulado del examen.
     * 
     * @param costoAcumulado Costo total acumulado con los servicios clínicos.
     * @return Precio con el descuento deducido.
     */
    @Override
    public double calcularPrecioFinal(double costoAcumulado) {
        return costoAcumulado * (1.0 - porcentajeDescuento);
    }

    /**
     * Retorna el porcentaje de descuento configurado.
     * 
     * @return Valor decimal del descuento.
     */
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}
