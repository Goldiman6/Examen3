package co.edu.poli.Examen2.domain.decorator;

import co.edu.poli.Examen2.domain.model.Examen;

/**
 * Decorador Concreto (ConcreteDecorator) del Patrón GoF Decorator.
 * 
 * Agrega el servicio clínico de "Toma de Muestra a Domicilio" al examen médico.
 * Incorpora una tarifa fija adicional al costo total del examen correspondiente
 * al desplazamiento logístico del bacteriólogo o enfermero hasta el domicilio del paciente.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class DomicilioDecorator extends ExamenDecorator {

    private static final long serialVersionUID = 1L;

    /** Tarifa fija por toma a domicilio (por defecto $25.00) */
    private final double recargoFijoDomicilio;

    /**
     * Construye un decorador de domicilio con la tarifa estándar de $25.00.
     * 
     * @param examenDecorado Examen al cual se le añade el servicio a domicilio.
     */
    public DomicilioDecorator(Examen examenDecorado) {
        this(examenDecorado, 25.0);
    }

    /**
     * Construye un decorador de domicilio con un costo fijo personalizado.
     * 
     * @param examenDecorado Examen al cual se le añade el servicio.
     * @param recargoFijoDomicilio Monto fijo a adicionar por el desplazamiento.
     */
    public DomicilioDecorator(Examen examenDecorado, double recargoFijoDomicilio) {
        super(examenDecorado);
        this.recargoFijoDomicilio = recargoFijoDomicilio;
    }

    /**
     * Calcula el costo total sumando al costo acumulado la tarifa fija de toma a domicilio.
     * 
     * @return Costo total con el servicio de domicilio adicionado.
     */
    @Override
    public double calcularCostoTotal() {
        return examenDecorado.calcularCostoTotal() + recargoFijoDomicilio;
    }

    /**
     * Obtiene el valor del recargo fijo por domicilio.
     * 
     * @return Valor del recargo fijo.
     */
    public double getRecargoFijoDomicilio() {
        return recargoFijoDomicilio;
    }

    @Override
    public String getServiciosAplicados() {
        String previo = examenDecorado.getServiciosAplicados();
        String servicio = String.format("Domicilio (+$%.2f)", recargoFijoDomicilio);
        return "Estándar".equals(previo) ? servicio : previo + " + " + servicio;
    }
}
