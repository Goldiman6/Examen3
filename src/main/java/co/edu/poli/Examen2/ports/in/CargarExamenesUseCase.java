package co.edu.poli.Examen2.ports.in;

import co.edu.poli.Examen2.domain.model.Examen;
import java.util.List;

/**
 * Puerto de Entrada (Inbound / Driver Port) que define el caso de uso
 * para cargar o deserializar los exámenes previamente almacenados en disco.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface CargarExamenesUseCase {

    /**
     * Recupera y carga la colección de exámenes almacenada en el medio de persistencia.
     * 
     * @return Lista de exámenes recuperados.
     * @throws Exception Si ocurre un error de lectura o deserialización.
     */
    List<Examen> cargarExamenes() throws Exception;
}
