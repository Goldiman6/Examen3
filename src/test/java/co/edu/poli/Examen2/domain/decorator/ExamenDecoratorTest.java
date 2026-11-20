package co.edu.poli.Examen2.domain.decorator;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el Patrón GoF Decorator implementado en:
 * - {@link ExamenDecorator} (Decorador base abstracto)
 * - {@link UrgenciaDecorator} (Servicio prioritario con recargo porcentual)
 * - {@link DomicilioDecorator} (Toma de muestra a domicilio con tarifa fija)
 *
 * Verifica la transparencia de tipos, el cálculo acumulativo de costos
 * y la combinación dinámica en cadena (chaining).
 *
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
@DisplayName("Patrón Decorator — Servicios adicionales clínicos")
class ExamenDecoratorTest {

    private Examen examenBase;

    @BeforeEach
    void setUp() throws DomainValidationException {
        // Examen base de $100.00 para facilitar cálculos de porcentajes exactos
        examenBase = new ExamenSangre("EXM-DEC-01", "Laura Gómez", 100.00, TipoSangre.O, FactorRH.POSITIVO);
        examenBase.validar();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Pruebas individuales de Decoradores
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("UrgenciaDecorator añade recargo del 25% por defecto al costo base")
    void urgenciaDecoratorAplicaRecargoPorDefecto() {
        Examen examenConUrgencia = new UrgenciaDecorator(examenBase);

        // Costo base 100 + 25% urgencia = 125.00
        assertEquals(125.00, examenConUrgencia.calcularCostoTotal(), 0.001);
        assertEquals(100.00, examenConUrgencia.getCostoBase(), 0.001);
        assertEquals("EXM-DEC-01", examenConUrgencia.getCodigo());
        assertEquals("Laura Gómez", examenConUrgencia.getNombrePaciente());
        assertTrue(examenConUrgencia.getServiciosAplicados().contains("Urgencia (+25%)"));
    }

    @Test
    @DisplayName("UrgenciaDecorator soporta recargo personalizado")
    void urgenciaDecoratorSoportaRecargoPersonalizado() {
        // 50% de recargo
        Examen examenConUrgencia50 = new UrgenciaDecorator(examenBase, 0.50);

        assertEquals(150.00, examenConUrgencia50.calcularCostoTotal(), 0.001);
        assertTrue(examenConUrgencia50.getServiciosAplicados().contains("50%"));
    }

    @Test
    @DisplayName("DomicilioDecorator añade tarifa fija estándar de $25.00")
    void domicilioDecoratorAplicaTarifaFijaPorDefecto() {
        Examen examenConDomicilio = new DomicilioDecorator(examenBase);

        // Costo base 100 + 25 fijo domicilio = 125.00
        assertEquals(125.00, examenConDomicilio.calcularCostoTotal(), 0.001);
        assertEquals(100.00, examenConDomicilio.getCostoBase(), 0.001);
        assertTrue(examenConDomicilio.getServiciosAplicados().contains("Domicilio"));
    }

    @Test
    @DisplayName("DomicilioDecorator soporta tarifa fija personalizada")
    void domicilioDecoratorSoportaTarifaPersonalizada() {
        Examen examenDomicilioCustom = new DomicilioDecorator(examenBase, 40.00);

        assertEquals(140.00, examenDomicilioCustom.calcularCostoTotal(), 0.001);
        assertEquals(40.00, ((DomicilioDecorator) examenDomicilioCustom).getRecargoFijoDomicilio(), 0.001);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Composición en Cadena (Chaining) de Decoradores
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Encadenamiento: Urgencia + Domicilio acumula correctamente ambos servicios")
    void encadenamientoUrgenciaYDomicilioAcumulaCostos() {
        // Base: $100.00
        // + Urgencia (25% sobre base = +$25.00) = $125.00
        // + Domicilio (tarifa fija = +$25.00)   = $150.00
        Examen examenDecorado = new DomicilioDecorator(new UrgenciaDecorator(examenBase));

        assertEquals(150.00, examenDecorado.calcularCostoTotal(), 0.001);
        assertEquals(100.00, examenDecorado.getCostoBase(), 0.001);

        String servicios = examenDecorado.getServiciosAplicados();
        assertTrue(servicios.contains("Urgencia"));
        assertTrue(servicios.contains("Domicilio"));
        assertTrue(servicios.contains("+"));
    }

    @Test
    @DisplayName("El orden de encadenamiento no altera el costo final acumulado")
    void ordenDeEncadenamientoProduceMismoCosto() {
        Examen urgenciaPrimero = new DomicilioDecorator(new UrgenciaDecorator(examenBase));
        Examen domicilioPrimero = new UrgenciaDecorator(new DomicilioDecorator(examenBase));

        assertEquals(urgenciaPrimero.calcularCostoTotal(), domicilioPrimero.calcularCostoTotal(), 0.001);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Transparencia e invariantes de Examen
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("El decorador preserva la identidad y detalle clínico del examen original")
    void decoradorPreservaIdentidadYDetalleClinico() {
        Examen decorado = new UrgenciaDecorator(new DomicilioDecorator(examenBase));

        assertEquals("EXM-DEC-01", decorado.getCodigo());
        assertEquals("Laura Gómez", decorado.getNombrePaciente());
        assertEquals(examenBase.getDetalleClinico(), decorado.getDetalleClinico());
        assertEquals(examenBase, decorado, "Dos exámenes con el mismo código deben ser equivalentes por equals");
    }

    @Test
    @DisplayName("El decorador lanza excepción si el examen a decorar es nulo")
    void decoradorLanzaExcepcionSiExamenEsNulo() {
        assertThrows(NullPointerException.class, () -> new UrgenciaDecorator(null));
        assertThrows(NullPointerException.class, () -> new DomicilioDecorator(null));
    }
}
