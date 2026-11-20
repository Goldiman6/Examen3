package co.edu.poli.Examen2.infrastructure.adapters.out.persistence;

import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.ports.out.ExamenRepositoryPort;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de Salida (Outbound / Driven Adapter) para persistencia mediante serialización binaria nativa de Java.
 * Implementa el puerto {@link ExamenRepositoryPort} permitiendo guardar y cargar colecciones de exámenes
 * desde un archivo físico en disco (ej. examenes.dat).
 * 
 * Este componente aísla los detalles de bajo nivel de java.io (flujos de bytes, excepciones I/O)
 * del núcleo de la arquitectura.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class FileSerializationAdapter implements ExamenRepositoryPort {

    private final File archivoPersistencia;
    private final List<Examen> bufferEnMemoria;

    /**
     * Construye el adaptador utilizando el nombre de archivo por defecto "examenes.dat".
     */
    public FileSerializationAdapter() {
        this("examenes.dat");
    }

    /**
     * Construye el adaptador especificando la ruta del archivo de persistencia.
     * 
     * @param rutaArchivo Ruta relativa o absoluta del archivo binario.
     */
    public FileSerializationAdapter(String rutaArchivo) {
        this.archivoPersistencia = new File(rutaArchivo);
        this.bufferEnMemoria = new ArrayList<>();
    }

    @Override
    public void guardar(Examen examen) {
        if (examen != null && !existePorCodigo(examen.getCodigo())) {
            bufferEnMemoria.add(examen);
        }
    }

    @Override
    public List<Examen> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(bufferEnMemoria));
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        if (codigo == null) return false;
        return bufferEnMemoria.stream()
                .anyMatch(e -> codigo.equalsIgnoreCase(e.getCodigo()));
    }

    @Override
    public void persistirTodos(List<Examen> examenes) throws Exception {
        if (examenes == null) {
            throw new IllegalArgumentException("La lista de exámenes no puede ser nula.");
        }

        // Crear directorio padre si no existe
        File parentDir = archivoPersistencia.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoPersistencia))) {
            oos.writeObject(new ArrayList<>(examenes));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Examen> cargarDesdeAlmacenamiento() throws Exception {
        if (!archivoPersistencia.exists() || archivoPersistencia.length() == 0) {
            throw new FileNotFoundException("El archivo de persistencia no existe o se encuentra vacío: " + archivoPersistencia.getAbsolutePath());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoPersistencia))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<Examen> recuperados = (List<Examen>) obj;
                bufferEnMemoria.clear();
                bufferEnMemoria.addAll(recuperados);
                return new ArrayList<>(recuperados);
            } else {
                throw new IOException("El formato del archivo no contiene una lista de exámenes válida.");
            }
        }
    }

    /**
     * Retorna la ruta absoluta del archivo físico utilizado.
     * 
     * @return Cadena con la ruta del archivo.
     */
    public String getRutaArchivo() {
        return archivoPersistencia.getAbsolutePath();
    }
}
