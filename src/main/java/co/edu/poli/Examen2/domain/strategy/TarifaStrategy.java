package co.edu.poli.Examen2.domain.strategy;

/**
 * Interfaz del Patrón de Diseño de Comportamiento Strategy (GoF).
 * Define el contrato para las distintas políticas de tarificación o descuento
 * que pueden aplicarse al costo total de un examen médico de laboratorio.
 * 
 * Permite encapsular cada algoritmo de cálculo de precio final en su propia clase,
 * haciéndolo intercambiable en tiempo de ejecución sin modificar las entidades
 * del dominio ni el controlador. Resuelve la violación del principio Open/Closed
 * que ocurriría al usar estructuras condicionales {@code if-else} o {@code switch}
 * dentro del dominio o del servicio de aplicación.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface TarifaStrategy {

    /**
     * Calcula el precio final del examen aplicando la política de tarificación
     * o descuento propia de esta estrategia sobre el costo total acumulado.
     * 
     * @param costoAcumulado Costo total del examen incluyendo los recargos de servicios adicionales (Decorators).
     * @return Precio final a cobrar al paciente según la política de esta estrategia.
     */
    double calcularPrecioFinal(double costoAcumulado);

    /**
     * Retorna el nombre descriptivo de la estrategia de tarificación.
     * 
     * @return Nombre de la política de tarifa (ej. "Particular", "Tercera Edad", "Convenio EPS").
     */
    String getNombreEstrategia();

    /**
     * Retorna la descripción del beneficio o ajuste aplicado.
     * 
     * @return Texto descriptivo del ajuste (ej. "Sin descuento", "Descuento 15%").
     */
    String getDescripcionAjuste();
}
