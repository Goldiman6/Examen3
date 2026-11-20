package co.edu.poli.Examen2.application.service;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.ports.in.CargarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.GuardarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.ListarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.RegistrarExamenUseCase;
import co.edu.poli.Examen2.ports.out.ExamenRepositoryPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de la Capa de Aplicación en la Arquitectura Hexagonal.
 * Implementa los casos de uso del sistema (puertos de entrada) y orquesta la lógica
 * coordinando las entidades del dominio y los puertos de salida (repositorio).
 * 
 * Es totalmente independiente de cualquier framework visual (JavaFX) o tecnología de base de datos.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class ExamenApplicationService implements
        RegistrarExamenUseCase,
        ListarExamenesUseCase,
        GuardarExamenesUseCase,
        CargarExamenesUseCase {

    private final ExamenRepositoryPort repositoryPort;
    private final List<Examen> examenesEnMemoria;

    /**
     * Construye el servicio inyectando el puerto de salida del repositorio.
     * 
     * @param repositoryPort Implementación del puerto de persistencia (no nulo).
     */
    public ExamenApplicationService(ExamenRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "El repositorio no puede ser nulo.");
        this.examenesEnMemoria = new ArrayList<>();
    }

    /**
     * Caso de Uso: Registrar Examen.
     * Valida las reglas de negocio en la entidad de dominio y verifica que no existan códigos duplicados.
     * 
     * @param examen Examen a registrar.
     * @return Examen registrado.
     * @throws DomainValidationException Si falla alguna regla de validación o el código ya existe.
     */
    @Override
    public Examen registrarExamen(Examen examen) throws DomainValidationException {
        if (examen == null) {
            throw new DomainValidationException("El examen proporcionado no puede ser nulo.");
        }

        // Ejecutar invariantes de dominio de la propia entidad
        examen.validar();

        // Regla de aplicación: Código único en memoria o repositorio
        boolean existeEnMemoria = examenesEnMemoria.stream()
                .anyMatch(e -> e.getCodigo().equalsIgnoreCase(examen.getCodigo()));

        if (existeEnMemoria || repositoryPort.existePorCodigo(examen.getCodigo())) {
            throw new DomainValidationException(String.format("Ya existe un examen registrado con el código '%s'.", examen.getCodigo()));
        }

        // Guardar en la sesión de memoria y notificar al repositorio
        examenesEnMemoria.add(examen);
        repositoryPort.guardar(examen);

        return examen;
    }

    /**
     * Caso de Uso: Listar Exámenes.
     * Retorna una vista inmodificable de los exámenes registrados en memoria.
     * 
     * @return Lista de exámenes.
     */
    @Override
    public List<Examen> listarExamenes() {
        return Collections.unmodifiableList(new ArrayList<>(examenesEnMemoria));
    }

    /**
     * Caso de Uso: Guardar / Serializar Exámenes.
     * Persiste la lista actual de exámenes a través del puerto de salida.
     * 
     * @throws Exception Si ocurre un fallo en el mecanismo de persistencia.
     */
    @Override
    public void guardarExamenes() throws Exception {
        if (examenesEnMemoria.isEmpty()) {
            throw new IllegalStateException("No hay exámenes en memoria para serializar o persistir.");
        }
        repositoryPort.persistirTodos(examenesEnMemoria);
    }

    /**
     * Caso de Uso: Cargar / Deserializar Exámenes.
     * Recupera los exámenes mediante el puerto de salida y actualiza el estado en memoria.
     * 
     * @return Lista de exámenes recuperados.
     * @throws Exception Si ocurre un error durante la carga.
     */
    @Override
    public List<Examen> cargarExamenes() throws Exception {
        List<Examen> cargados = repositoryPort.cargarDesdeAlmacenamiento();
        examenesEnMemoria.clear();
        if (cargados != null) {
            examenesEnMemoria.addAll(cargados);
        }
        return listarExamenes();
    }
}
