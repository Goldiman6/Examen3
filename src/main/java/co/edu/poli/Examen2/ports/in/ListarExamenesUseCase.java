package co.edu.poli.Examen2.ports.in;

import co.edu.poli.Examen2.domain.model.Examen;
import java.util.List;

/**
 * Puerto de Entrada (Inbound / Driver Port) que define el caso de uso
 * para consultar y listar todos los exámenes registrados en el sistema.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface ListarExamenesUseCase {

    /**
     * Obtiene la lista completa de exámenes registrados en la sesión actual.
     * 
     * @return Lista inmutable o copia de los exámenes existentes.
     */
    List<Examen> listarExamenes();
}
