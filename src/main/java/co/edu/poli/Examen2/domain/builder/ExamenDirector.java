package co.edu.poli.Examen2.domain.builder;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;
import java.util.Objects;

/**
 * Director del Patrón de Diseño GoF Builder.
 * Conoce y orquesta la secuencia canónica de pasos necesarios para la construcción
 * de un examen clínico garantizando que ningún paso obligatorio sea omitido.
 * 
 * Permite que clientes como controladores o servicios de aplicación deleguen
 * el ensamblado del examen clínico sin acoplarse a los detalles específicos del producto.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class ExamenDirector {

    private ExamenBuilder builder;

    /**
     * Construye un director asociado a un builder específico.
     * 
     * @param builder Instancia del constructor que implementa {@link ExamenBuilder}.
     */
    public ExamenDirector(ExamenBuilder builder) {
        this.builder = Objects.requireNonNull(builder, "El builder no puede ser nulo.");
    }

    /**
     * Permite cambiar el builder en tiempo de ejecución para reutilizar el director
     * con diferentes tipos de constructores.
     * 
     * @param builder Nuevo constructor.
     */
    public void cambiarBuilder(ExamenBuilder builder) {
        this.builder = Objects.requireNonNull(builder, "El builder no puede ser nulo.");
    }

    /**
     * Orquesta la secuencia completa de pasos para construir un examen de sangre.
     * 
     * @param codigo Código identificador del examen.
     * @param nombrePaciente Nombre del paciente.
     * @param costoBase Costo base del análisis.
     * @param tipoSangre Tipo sanguíneo (A, B, AB, O).
     * @param factorRH Factor Rhesus (+/-).
     * @return Instancia validada de {@link ExamenSangre}.
     * @throws DomainValidationException Si algún dato no cumple las invariantes de dominio.
     */
    public ExamenSangre construirExamenSangre(
            String codigo,
            String nombrePaciente,
            double costoBase,
            TipoSangre tipoSangre,
            FactorRH factorRH) throws DomainValidationException {

        builder.reset();
        return builder
                .conCodigo(codigo)
                .conNombrePaciente(nombrePaciente)
                .conCostoBase(costoBase)
                .conTipoSangre(tipoSangre)
                .conFactorRH(factorRH)
                .build();
    }
}
