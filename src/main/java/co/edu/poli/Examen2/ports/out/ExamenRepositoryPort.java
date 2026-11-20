package co.edu.poli.Examen2.ports.out;

import co.edu.poli.Examen2.domain.model.Examen;
import java.util.List;

/**
 * Puerto de Salida (Outbound / Driven Port) que define el contrato de persistencia
 * para los exámenes médicos.
 * 
 * Este puerto aísla el núcleo del dominio y la capa de aplicación de los detalles técnicos
 * de almacenamiento (archivos binarios, bases de datos SQL/NoSQL, JSON, etc.).
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public interface ExamenRepositoryPort {

    /**
     * Agrega un nuevo examen al repositorio en memoria o persistente.
     * 
     * @param examen Examen a registrar.
     */
    void guardar(Examen examen);

    /**
     * Obtiene todos los exámenes actualmente presentes en el repositorio.
     * 
     * @return Lista de exámenes.
     */
    List<Examen> obtenerTodos();

    /**
     * Verifica si ya existe un examen registrado con el código especificado.
     * 
     * @param codigo Identificador a consultar.
     * @return {@code true} si ya existe un registro con ese código, de lo contrario {@code false}.
     */
    boolean existePorCodigo(String codigo);

    /**
     * Persiste la lista de exámenes en el medio físico definitivo (ej. archivo serializado).
     * 
     * @param examenes Lista de exámenes a persistir.
     * @throws Exception Si ocurre un error de I/O o serialización.
     */
    void persistirTodos(List<Examen> examenes) throws Exception;

    /**
     * Carga y reconstruye la lista de exámenes desde el medio físico de persistencia.
     * 
     * @return Lista de exámenes recuperados.
     * @throws Exception Si ocurre un error de lectura o deserialización.
     */
    List<Examen> cargarDesdeAlmacenamiento() throws Exception;
}
