package co.edu.poli.Examen2.ports.in;

/**
 * Puerto de Entrada (Inbound / Driver Port) que define el caso de uso
 * para persistir o serializar el estado actual de los exámenes en el medio de almacenamiento.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface GuardarExamenesUseCase {

    /**
     * Persiste la totalidad de los registros de exámenes en el medio de almacenamiento configurado.
     * 
     * @throws Exception Si ocurre un fallo durante el proceso de persistencia/serialización.
     */
    void guardarExamenes() throws Exception;
}
