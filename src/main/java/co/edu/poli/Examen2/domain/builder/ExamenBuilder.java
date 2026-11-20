package co.edu.poli.Examen2.domain.builder;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;

/**
 * Interfaz formal del Patrón de Diseño Creacional Builder (GoF).
 * Define los pasos contractuales para la construcción gradual, fluida y validada
 * de un examen clínico sin exponer la complejidad de su instanciación interna.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface ExamenBuilder {

    /**
     * Asigna el código identificador único del examen.
     * 
     * @param codigo Código alfanumérico (ej. EXM-001).
     * @return Esta misma instancia del builder para encadenamiento fluido.
     */
    ExamenBuilder conCodigo(String codigo);

    /**
     * Asigna el nombre completo del paciente.
     * 
     * @param nombrePaciente Nombre del paciente evaluado.
     * @return Esta misma instancia del builder para encadenamiento fluido.
     */
    ExamenBuilder conNombrePaciente(String nombrePaciente);

    /**
     * Asigna el costo económico base del examen.
     * 
     * @param costoBase Valor monetario base mayor a cero.
     * @return Esta misma instancia del builder para encadenamiento fluido.
     */
    ExamenBuilder conCostoBase(double costoBase);

    /**
     * Asigna el tipo de sangre del paciente según el sistema ABO.
     * 
     * @param tipoSangre Grupo sanguíneo (A, B, AB, O).
     * @return Esta misma instancia del builder para encadenamiento fluido.
     */
    ExamenBuilder conTipoSangre(TipoSangre tipoSangre);

    /**
     * Asigna el factor Rhesus (Rh) del paciente.
     * 
     * @param factorRH Factor Rh (POSITIVO o NEGATIVO).
     * @return Esta misma instancia del builder para encadenamiento fluido.
     */
    ExamenBuilder conFactorRH(FactorRH factorRH);

    /**
     * Valida de manera exhaustiva las reglas del negocio del examen
     * y retorna la instancia definitiva del producto si todos los datos son válidos.
     * 
     * @return Instancia completa e inmutable en sus invariantes de {@link ExamenSangre}.
     * @throws DomainValidationException Si alguno de los datos obligatorios está ausente o es inconsistente.
     */
    ExamenSangre build() throws DomainValidationException;

    /**
     * Restablece el estado interno del builder para permitir la construcción de un nuevo examen.
     */
    void reset();
}
