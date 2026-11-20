package co.edu.poli.Examen2.ports.in;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;

/**
 * Puerto de Entrada (Inbound / Driver Port) que define el caso de uso
 * para registrar un nuevo examen médico en el sistema.
 * 
 * Permite a cualquier adaptador de entrada (interfaz gráfica JavaFX, CLI, etc.)
 * solicitar el registro sin acoplarse a la implementación interna.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface RegistrarExamenUseCase {

    /**
     * Registra un nuevo examen en el sistema tras verificar sus reglas de negocio.
     * 
     * @param examen Instancia del examen a registrar.
     * @return El examen registrado satisfactoriamente.
     * @throws DomainValidationException Si los datos del examen infringen las reglas del dominio o el código ya existe.
     */
    Examen registrarExamen(Examen examen) throws DomainValidationException;
}
