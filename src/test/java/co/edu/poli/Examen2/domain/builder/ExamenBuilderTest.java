package co.edu.poli.Examen2.domain.builder;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el Patrón GoF Builder implementado en
 * {@link ExamenSangreBuilder} y coordinado por {@link ExamenDirector}.
 *
 * Verifica que el builder construye objetos válidos y lanza
 * {@link DomainValidationException} cuando se violan las invariantes de negocio.
 *
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
@DisplayName("Patrón Builder — ExamenSangreBuilder")
class ExamenBuilderTest {

    private ExamenSangreBuilder builder;

    /**
     * Reinicia el builder antes de cada prueba para garantizar un estado limpio.
     */
    @BeforeEach
    void setUp() {
        builder = new ExamenSangreBuilder();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Escenarios exitosos
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Build exitoso con todos los campos válidos")
    void deberiaCrearExamenConCamposValidos() throws DomainValidationException {
        ExamenSangre examen = builder
                .conCodigo("EXM-001")
                .conNombrePaciente("Juan Pérez")
                .conCostoBase(150.00)
                .conTipoSangre(TipoSangre.A)
                .conFactorRH(FactorRH.POSITIVO)
                .build();

        assertNotNull(examen, "El examen construido no debe ser nulo");
        assertEquals("EXM-001", examen.getCodigo());
        assertEquals("Juan Pérez", examen.getNombrePaciente());
        assertEquals(150.00, examen.getCostoBase(), 0.001);
        assertEquals(TipoSangre.A, examen.getTipoSangre());
        assertEquals(FactorRH.POSITIVO, examen.getFactorRH());
    }

    @Test
    @DisplayName("El costo total base es igual al costo base cuando no hay decoradores")
    void costoTotalSinDecoradorEsIgualACostoBase() throws DomainValidationException {
        ExamenSangre examen = builder
                .conCodigo("EXM-002")
                .conNombrePaciente("María Gómez")
                .conCostoBase(200.00)
                .conTipoSangre(TipoSangre.O)
                .conFactorRH(FactorRH.NEGATIVO)
                .build();

        assertEquals(200.00, examen.calcularCostoTotal(), 0.001,
                "Sin decoradores el costo total debe ser igual al costo base");
    }

    @Test
    @DisplayName("Reset limpia el estado del builder correctamente")
    void resetLimpiaElEstadoDelBuilder() {
        builder.conCodigo("EXM-999").conNombrePaciente("Temporal").conCostoBase(50.0);
        builder.reset();

        // Después del reset, build() debe lanzar excepción por campos nulos/vacíos
        assertThrows(DomainValidationException.class, builder::build,
                "Después de reset() el builder no puede construir un examen válido");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Escenarios de validación — DomainValidationException esperada
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Lanza excepción si el código está vacío")
    void deberiaLanzarExcepcionSiCodigoEstaVacio() {
        builder.conCodigo("")
               .conNombrePaciente("Carlos López")
               .conCostoBase(100.00)
               .conTipoSangre(TipoSangre.B)
               .conFactorRH(FactorRH.POSITIVO);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                builder::build,
                "Código vacío debe lanzar DomainValidationException"
        );
        assertTrue(ex.getMessage().toLowerCase().contains("código"),
                "El mensaje debe mencionar el campo 'código'");
    }

    @Test
    @DisplayName("Lanza excepción si el código es nulo")
    void deberiaLanzarExcepcionSiCodigoEsNulo() {
        builder.conNombrePaciente("Pedro Martínez")
               .conCostoBase(100.00)
               .conTipoSangre(TipoSangre.AB)
               .conFactorRH(FactorRH.NEGATIVO);
        // código queda nulo (nunca se llama conCodigo)

        assertThrows(DomainValidationException.class, builder::build,
                "Código nulo debe lanzar DomainValidationException");
    }

    @Test
    @DisplayName("Lanza excepción si el costo base es cero")
    void deberiaLanzarExcepcionSiCostoBaseEsCero() {
        builder.conCodigo("EXM-003")
               .conNombrePaciente("Ana Ruiz")
               .conCostoBase(0.0)
               .conTipoSangre(TipoSangre.O)
               .conFactorRH(FactorRH.POSITIVO);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                builder::build,
                "Costo base 0 debe lanzar DomainValidationException"
        );
        assertTrue(ex.getMessage().toLowerCase().contains("costo"),
                "El mensaje debe mencionar el campo 'costo'");
    }

    @Test
    @DisplayName("Lanza excepción si el costo base es negativo")
    void deberiaLanzarExcepcionSiCostoBaseEsNegativo() {
        builder.conCodigo("EXM-004")
               .conNombrePaciente("Luis Castro")
               .conCostoBase(-50.0)
               .conTipoSangre(TipoSangre.A)
               .conFactorRH(FactorRH.POSITIVO);

        assertThrows(DomainValidationException.class, builder::build,
                "Costo base negativo debe lanzar DomainValidationException");
    }

    @Test
    @DisplayName("Lanza excepción si el nombre del paciente está vacío")
    void deberiaLanzarExcepcionSiNombreEstaVacio() {
        builder.conCodigo("EXM-005")
               .conNombrePaciente("   ")
               .conCostoBase(100.00)
               .conTipoSangre(TipoSangre.A)
               .conFactorRH(FactorRH.POSITIVO);

        assertThrows(DomainValidationException.class, builder::build,
                "Nombre con solo espacios debe lanzar DomainValidationException");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Director
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("ExamenDirector crea examen estándar con valores por defecto")
    void directorDeberiaCrearExamenEstandar() throws DomainValidationException {
        ExamenDirector director = new ExamenDirector(builder);
        ExamenSangre examen = director.construirExamenSangre(
                "EXM-DIR-01", "Roberto Silva", 180.00,
                TipoSangre.O, FactorRH.POSITIVO
        );

        assertNotNull(examen);
        assertEquals("EXM-DIR-01", examen.getCodigo());
        assertEquals(180.00, examen.getCostoBase(), 0.001);
    }
}
