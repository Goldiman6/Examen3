package co.edu.poli.Examen2.infrastructure.adapters.out.persistence;

import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.ports.out.ExamenRepositoryPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de Salida en memoria (In-Memory Driven Adapter).
 * Implementa {@link ExamenRepositoryPort} para pruebas unitarias rápidas y entornos
 * donde no se requiera I/O físico a disco.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class InMemoryExamenAdapter implements ExamenRepositoryPort {

    private final List<Examen> datos = new ArrayList<>();

    @Override
    public void guardar(Examen examen) {
        if (examen != null && !existePorCodigo(examen.getCodigo())) {
            datos.add(examen);
        }
    }

    @Override
    public List<Examen> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(datos));
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        if (codigo == null) return false;
        return datos.stream().anyMatch(e -> codigo.equalsIgnoreCase(e.getCodigo()));
    }

    @Override
    public void persistirTodos(List<Examen> examenes) {
        datos.clear();
        if (examenes != null) {
            datos.addAll(examenes);
        }
    }

    @Override
    public List<Examen> cargarDesdeAlmacenamiento() {
        return new ArrayList<>(datos);
    }
}
