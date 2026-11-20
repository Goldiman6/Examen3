package co.edu.poli.Examen2.domain.strategy;

/**
 * Estrategia Concreta (ConcreteStrategy) del Patrón GoF Strategy.
 * Representa la política de tarificación con descuento preferencial
 * para pacientes de la tercera edad o adultos mayores.
 * 
 * Aplica un descuento del 15% sobre el costo total acumulado del examen.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class TarifaTerceraEdadStrategy extends TarifaConDescuentoStrategy {

    /**
     * Construye la estrategia de tarifa para Tercera Edad con un 15% de descuento.
     */
    public TarifaTerceraEdadStrategy() {
        super(0.15);
    }

    @Override
    public String getNombreEstrategia() {
        return "Tercera Edad";
    }

    @Override
    public String getDescripcionAjuste() {
        return String.format("Descuento preferencial %.0f%%", getPorcentajeDescuento() * 100);
    }
}
