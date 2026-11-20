package co.edu.poli.Examen2.domain.strategy;

/**
 * Estrategia Concreta (ConcreteStrategy) del Patrón GoF Strategy.
 * Representa la política de tarificación con descuento por convenio corporativo
 * con una Entidad Promotora de Salud (EPS) o aseguradora médica.
 * 
 * Aplica un descuento del 25% sobre el costo total acumulado del examen.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class TarifaConvenioEpsStrategy extends TarifaConDescuentoStrategy {

    /**
     * Construye la estrategia de tarifa por Convenio EPS con un 25% de descuento.
     */
    public TarifaConvenioEpsStrategy() {
        super(0.25);
    }

    @Override
    public String getNombreEstrategia() {
        return "Convenio EPS";
    }

    @Override
    public String getDescripcionAjuste() {
        return String.format("Descuento convenio EPS %.0f%%", getPorcentajeDescuento() * 100);
    }
}
