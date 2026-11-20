package co.edu.poli.Examen2.domain.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el Patrón GoF Strategy implementado en:
 * - {@link TarifaStrategy} (Interfaz de la estrategia)
 * - {@link TarifaParticularStrategy} (Sin descuento - 100%)
 * - {@link TarifaTerceraEdadStrategy} (15% de descuento)
 * - {@link TarifaConvenioEpsStrategy} (25% de descuento)
 *
 * Verifica la correcta conmutabilidad de las políticas de precios y el cálculo
 * matemático de los descuentos sobre diferentes montos base acumulados.
 *
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
@DisplayName("Patrón Strategy — Políticas de tarificación")
class TarifaStrategyTest {

    // ─────────────────────────────────────────────────────────────────────────
    // TarifaParticularStrategy
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TarifaParticularStrategy cobra el 100% sin aplicar descuento")
    void tarifaParticularCobraCienPorCiento() {
        TarifaStrategy estrategia = new TarifaParticularStrategy();

        assertEquals(100.00, estrategia.calcularPrecioFinal(100.00), 0.001);
        assertEquals(250.50, estrategia.calcularPrecioFinal(250.50), 0.001);
        assertEquals("Particular", estrategia.getNombreEstrategia());
        assertNotNull(estrategia.getDescripcionAjuste());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TarifaTerceraEdadStrategy (15% descuento)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TarifaTerceraEdadStrategy aplica exactamente el 15% de descuento")
    void tarifaTerceraEdadAplicaQuincePorCiento() {
        TarifaStrategy estrategia = new TarifaTerceraEdadStrategy();

        // 100 - 15% = 85.00
        assertEquals(85.00, estrategia.calcularPrecioFinal(100.00), 0.001);
        // 200 - 15% = 170.00
        assertEquals(170.00, estrategia.calcularPrecioFinal(200.00), 0.001);
        assertEquals("Tercera Edad", estrategia.getNombreEstrategia());
        assertTrue(estrategia.getDescripcionAjuste().contains("15%"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TarifaConvenioEpsStrategy (25% descuento)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TarifaConvenioEpsStrategy aplica exactamente el 25% de descuento")
    void tarifaConvenioEpsAplicaVeinticincoPorCiento() {
        TarifaStrategy estrategia = new TarifaConvenioEpsStrategy();

        // 100 - 25% = 75.00
        assertEquals(75.00, estrategia.calcularPrecioFinal(100.00), 0.001);
        // 200 - 25% = 150.00
        assertEquals(150.00, estrategia.calcularPrecioFinal(200.00), 0.001);
        assertEquals("Convenio EPS", estrategia.getNombreEstrategia());
        assertTrue(estrategia.getDescripcionAjuste().contains("25%"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Pruebas parametrizadas para múltiples montos
    // ─────────────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "Monto acumulado ${0} con Convenio EPS (25%) -> esperado ${1}")
    @CsvSource({
            "0.0, 0.0",
            "50.0, 37.5",
            "100.0, 75.0",
            "150.0, 112.5",
            "300.0, 225.0"
    })
    @DisplayName("Convenio EPS calcula correctamente para múltiples montos")
    void convenioEpsCalculaVariosMontos(double costoAcumulado, double esperado) {
        TarifaStrategy estrategia = new TarifaConvenioEpsStrategy();
        assertEquals(esperado, estrategia.calcularPrecioFinal(costoAcumulado), 0.001);
    }

    @ParameterizedTest(name = "Monto acumulado ${0} con Tercera Edad (15%) -> esperado ${1}")
    @CsvSource({
            "0.0, 0.0",
            "50.0, 42.5",
            "100.0, 85.0",
            "150.0, 127.5",
            "200.0, 170.0"
    })
    @DisplayName("Tercera Edad calcula correctamente para múltiples montos")
    void terceraEdadCalculaVariosMontos(double costoAcumulado, double esperado) {
        TarifaStrategy estrategia = new TarifaTerceraEdadStrategy();
        assertEquals(esperado, estrategia.calcularPrecioFinal(costoAcumulado), 0.001);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Intercambiabilidad en tiempo de ejecución (Polimorfismo de Strategy)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Las estrategias son completamente intercambiables sobre el mismo costo")
    void estrategiasSonIntercambiablesEnTiempoDeEjecucion() {
        double costoAcumulado = 100.00;

        TarifaStrategy estrategia = new TarifaParticularStrategy();
        assertEquals(100.00, estrategia.calcularPrecioFinal(costoAcumulado), 0.001);

        estrategia = new TarifaTerceraEdadStrategy();
        assertEquals(85.00, estrategia.calcularPrecioFinal(costoAcumulado), 0.001);

        estrategia = new TarifaConvenioEpsStrategy();
        assertEquals(75.00, estrategia.calcularPrecioFinal(costoAcumulado), 0.001);
    }
}
