package co.edu.poli.Examen2.application.service;

import co.edu.poli.Examen2.domain.builder.ExamenSangreBuilder;
import co.edu.poli.Examen2.domain.decorator.DomicilioDecorator;
import co.edu.poli.Examen2.domain.decorator.UrgenciaDecorator;
import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;
import co.edu.poli.Examen2.infrastructure.adapters.out.persistence.InMemoryExamenAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la capa de Aplicación de la Arquitectura Hexagonal.
 * Evalúa {@link ExamenApplicationService} y el cumplimiento de sus casos de uso:
 * - RegistrarExamenUseCase
 * - ListarExamenesUseCase
 * - GuardarExamenesUseCase
 * - CargarExamenesUseCase
 *
 * Utiliza el adaptador en memoria {@link InMemoryExamenAdapter} para desacoplar
 * completamente la prueba del sistema de archivos físico.
 *
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
@DisplayName("Capa de Aplicación — ExamenApplicationService")
class ExamenApplicationServiceTest {

    private InMemoryExamenAdapter repositorio;
    private ExamenApplicationService servicio;

    @BeforeEach
    void setUp() {
        repositorio = new InMemoryExamenAdapter();
        servicio = new ExamenApplicationService(repositorio);
    }

    private Examen crearExamenValido(String codigo, String paciente, double costo) throws DomainValidationException {
        return new ExamenSangreBuilder()
                .conCodigo(codigo)
                .conNombrePaciente(paciente)
                .conCostoBase(costo)
                .conTipoSangre(TipoSangre.O)
                .conFactorRH(FactorRH.POSITIVO)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Caso de uso: Registrar Examen
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registra un examen válido con éxito")
    void registrarExamenValidoExitoso() throws Exception {
        Examen examen = crearExamenValido("EXM-SVC-01", "Lucía Peña", 120.0);

        Examen registrado = servicio.registrarExamen(examen);

        assertNotNull(registrado);
        assertEquals(1, servicio.listarExamenes().size());
        assertEquals("EXM-SVC-01", servicio.listarExamenes().get(0).getCodigo());
        assertTrue(repositorio.existePorCodigo("EXM-SVC-01"));
    }

    @Test
    @DisplayName("Registra un examen decorado con urgencia y domicilio de forma transparente")
    void registrarExamenDecoradoTransparente() throws Exception {
        Examen base = crearExamenValido("EXM-SVC-02", "Andrés Morales", 100.0);
        Examen decorado = new DomicilioDecorator(new UrgenciaDecorator(base));

        Examen registrado = servicio.registrarExamen(decorado);

        assertNotNull(registrado);
        assertEquals(150.00, registrado.calcularCostoTotal(), 0.001);
        assertEquals(1, servicio.listarExamenes().size());
        assertTrue(servicio.listarExamenes().get(0).getServiciosAplicados().contains("Urgencia"));
        assertTrue(servicio.listarExamenes().get(0).getServiciosAplicados().contains("Domicilio"));
    }

    @Test
    @DisplayName("Rechaza examen nulo con DomainValidationException")
    void registrarExamenNuloLanzaExcepcion() {
        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> servicio.registrarExamen(null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("nulo"));
    }

    @Test
    @DisplayName("Rechaza examen con código duplicado")
    void registrarExamenDuplicadoLanzaExcepcion() throws Exception {
        Examen ex1 = crearExamenValido("EXM-DUP-01", "Paciente Uno", 100.0);
        Examen ex2 = crearExamenValido("EXM-DUP-01", "Paciente Dos", 150.0);

        servicio.registrarExamen(ex1);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> servicio.registrarExamen(ex2),
                "No debe permitir registrar dos exámenes con el mismo código"
        );
        assertTrue(ex.getMessage().toLowerCase().contains("ya existe"));
    }

    @Test
    @DisplayName("Rechaza duplicados ignorando mayúsculas y minúsculas (case-insensitive)")
    void registrarExamenDuplicadoCaseInsensitive() throws Exception {
        Examen ex1 = crearExamenValido("exm-abc", "Paciente Uno", 100.0);
        Examen ex2 = crearExamenValido("EXM-ABC", "Paciente Dos", 150.0);

        servicio.registrarExamen(ex1);

        assertThrows(
                DomainValidationException.class,
                () -> servicio.registrarExamen(ex2),
                "Debe detectar coincidencia de código sin importar mayúsculas/minúsculas"
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Caso de uso: Listar Exámenes
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Listar retorna una lista inmodificable")
    void listarRetornaListaInmodificable() throws Exception {
        Examen ex = crearExamenValido("EXM-LST-01", "Felipe", 100.0);
        servicio.registrarExamen(ex);

        List<Examen> lista = servicio.listarExamenes();

        assertThrows(UnsupportedOperationException.class, () -> lista.remove(0),
                "La lista retornada debe ser inmodificable para proteger el estado interno");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Casos de uso: Guardar y Cargar Exámenes
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Guardar y cargar preserva los exámenes registrados")
    void guardarYCargarPreservaExamenes() throws Exception {
        servicio.registrarExamen(crearExamenValido("EXM-GC-01", "Paciente A", 100.0));
        servicio.registrarExamen(crearExamenValido("EXM-GC-02", "Paciente B", 200.0));

        servicio.guardarExamenes();

        // Creamos un segundo servicio con el mismo repositorio para simular una nueva sesión
        ExamenApplicationService nuevoServicio = new ExamenApplicationService(repositorio);
        assertEquals(0, nuevoServicio.listarExamenes().size(), "El nuevo servicio inicia con memoria vacía");

        List<Examen> cargados = nuevoServicio.cargarExamenes();
        assertEquals(2, cargados.size());
        assertEquals("EXM-GC-01", cargados.get(0).getCodigo());
        assertEquals("EXM-GC-02", cargados.get(1).getCodigo());
    }

    @Test
    @DisplayName("Guardar con lista vacía lanza IllegalStateException")
    void guardarListaVaciaLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> servicio.guardarExamenes(),
                "No debe permitir guardar si no hay exámenes registrados en memoria");
    }
}
