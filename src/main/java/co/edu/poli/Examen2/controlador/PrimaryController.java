package co.edu.poli.Examen2.controlador;

import co.edu.poli.Examen2.application.service.ExamenApplicationService;
import co.edu.poli.Examen2.domain.builder.ExamenDirector;
import co.edu.poli.Examen2.domain.builder.ExamenSangreBuilder;
import co.edu.poli.Examen2.domain.decorator.DomicilioDecorator;
import co.edu.poli.Examen2.domain.decorator.UrgenciaDecorator;
import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.Examen;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;
import co.edu.poli.Examen2.domain.strategy.TarifaConvenioEpsStrategy;
import co.edu.poli.Examen2.domain.strategy.TarifaParticularStrategy;
import co.edu.poli.Examen2.domain.strategy.TarifaStrategy;
import co.edu.poli.Examen2.domain.strategy.TarifaTerceraEdadStrategy;
import co.edu.poli.Examen2.infrastructure.adapters.out.persistence.FileSerializationAdapter;
import co.edu.poli.Examen2.ports.in.CargarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.GuardarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.ListarExamenesUseCase;
import co.edu.poli.Examen2.ports.in.RegistrarExamenUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Adaptador de Entrada (Inbound / Driving Adapter) para la interfaz gráfica JavaFX.
 * 
 * Se encarga de traducir las interacciones y eventos del usuario en llamadas a los
 * Puertos de Entrada (Casos de Uso de la Aplicación) y proyectar las respuestas y errores
 * en los componentes visuales de la vista.
 * 
 * No contiene reglas de negocio ni interactúa directamente con archivos físicos o bases de datos,
 * cumpliendo con los principios de la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class PrimaryController {

    @FXML
    private TextField txtCodigoExamen;

    @FXML
    private TextField txtNombrePaciente;

    @FXML
    private TextField txtCosto;

    @FXML
    private ComboBox<String> cmbTipoSangre;

    @FXML
    private RadioButton rbPositivo;

    @FXML
    private RadioButton rbNegativo;

    @FXML
    private ToggleGroup tgFactorRH;

    @FXML
    private CheckBox chkUrgente;

    @FXML
    private CheckBox chkDomicilio;

    @FXML
    private ComboBox<String> cmbTarifa;

    @FXML
    private TextArea txtAreaMensajes;

    // Dependencias hacia los Puertos de Entrada (Inbound Ports / Use Cases)
    private RegistrarExamenUseCase registrarExamenUseCase;
    private ListarExamenesUseCase listarExamenesUseCase;
    private GuardarExamenesUseCase guardarExamenesUseCase;
    private CargarExamenesUseCase cargarExamenesUseCase;

    /**
     * Inicializa el controlador y configura las dependencias de la Arquitectura Hexagonal.
     */
    @FXML
    public void initialize() {
        // En un entorno de producción se podría emplear un inyector como CDI o Spring;
        // aquí instanciamos el adaptador de persistencia y el servicio de aplicación.
        FileSerializationAdapter repositoryAdapter = new FileSerializationAdapter("examenes_laboratorio.dat");
        ExamenApplicationService applicationService = new ExamenApplicationService(repositoryAdapter);

        // Inyección de los casos de uso
        this.registrarExamenUseCase = applicationService;
        this.listarExamenesUseCase = applicationService;
        this.guardarExamenesUseCase = applicationService;
        this.cargarExamenesUseCase = applicationService;

        mostrarMensaje("=== SISTEMA DE LABORATORIO CLÍNICO INICIADO ===\n" +
                "Arquitectura Hexagonal inicializada correctamente.\n" +
                "Persistencia configurada: Archivo serializado 'examenes_laboratorio.dat'\n");
    }

    /**
     * Manejador del evento Guardar.
     * Captura los datos de los controles gráficos, construye la entidad de dominio
     * y delega el registro al caso de uso correspondiente.
     */
    @FXML
    private void handleGuardar() {
        try {
            String codigo = txtCodigoExamen.getText();
            String paciente = txtNombrePaciente.getText();
            String costoStr = txtCosto.getText();
            String tipoSangreStr = cmbTipoSangre.getValue();

            if (costoStr == null || costoStr.trim().isEmpty()) {
                throw new DomainValidationException("El costo es obligatorio.");
            }

            double costo;
            try {
                costo = Double.parseDouble(costoStr.trim());
            } catch (NumberFormatException e) {
                throw new DomainValidationException("El costo debe ser un valor numérico válido (ej. 150.00).");
            }

            if (tipoSangreStr == null || tipoSangreStr.trim().isEmpty()) {
                throw new DomainValidationException("Debe seleccionar un tipo de sangre.");
            }

            TipoSangre tipoSangre = TipoSangre.fromString(tipoSangreStr);
            FactorRH factorRH = rbPositivo.isSelected() ? FactorRH.POSITIVO : FactorRH.NEGATIVO;

            // ─── PATRÓN CREACIONAL: Builder con Director ───────────────────────────────
            ExamenDirector director = new ExamenDirector(new ExamenSangreBuilder());
            Examen examenFinal = director.construirExamenSangre(codigo, paciente, costo, tipoSangre, factorRH);

            // ─── PATRÓN ESTRUCTURAL: Decorator (servicios adicionales opcionales) ──────
            if (chkUrgente != null && chkUrgente.isSelected()) {
                examenFinal = new UrgenciaDecorator(examenFinal);
            }
            if (chkDomicilio != null && chkDomicilio.isSelected()) {
                examenFinal = new DomicilioDecorator(examenFinal);
            }

            // ─── PATRÓN DE COMPORTAMIENTO: Strategy (política de tarificación) ─────────
            TarifaStrategy estrategia = resolverEstrategia(
                    cmbTarifa != null ? cmbTarifa.getValue() : null);
            double precioFinal = estrategia.calcularPrecioFinal(examenFinal.calcularCostoTotal());

            // Invocar el caso de uso con el examen decorado
            Examen registrado = registrarExamenUseCase.registrarExamen(examenFinal);

            mostrarMensaje(String.format(
                    "✅ ÉXITO: Examen registrado correctamente.%n" +
                    "   %s%n" +
                    "   Tarifa: %-15s | %s%n" +
                    "   💰 PRECIO FINAL A COBRAR: $%.2f",
                    registrado.toString(),
                    estrategia.getNombreEstrategia(),
                    estrategia.getDescripcionAjuste(),
                    precioFinal));
            limpiarCamposFormulario();

        } catch (DomainValidationException e) {
            mostrarMensaje("⚠️ ERROR DE VALIDACIÓN: " + e.getMessage());
        } catch (Exception e) {
            mostrarMensaje("❌ ERROR INESPERADO AL REGISTRAR: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento Listar.
     * Invoca el caso de uso de consulta y formatea la lista en el área de mensajes.
     */
    @FXML
    private void handleListar() {
        try {
            List<Examen> lista = listarExamenesUseCase.listarExamenes();
            if (lista.isEmpty()) {
                mostrarMensaje("ℹ️ No hay exámenes registrados en la sesión actual.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("📋 LISTADO DE EXÁMENES REGISTRADOS (").append(lista.size()).append(" en total):\n");
            sb.append("--------------------------------------------------------------------------------\n");
            for (int i = 0; i < lista.size(); i++) {
                sb.append(String.format("%02d. %s\n", (i + 1), lista.get(i).toString()));
            }
            sb.append("--------------------------------------------------------------------------------\n");
            mostrarMensaje(sb.toString());

        } catch (Exception e) {
            mostrarMensaje("❌ ERROR AL LISTAR: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento Serializar.
     * Invoca el caso de uso para persistir los exámenes en disco.
     */
    @FXML
    private void handleSerializar() {
        try {
            guardarExamenesUseCase.guardarExamenes();
            mostrarMensaje("📦 ÉXITO: Datos serializados y guardados en el archivo de almacenamiento local.");
        } catch (IllegalStateException e) {
            mostrarMensaje("⚠️ AVISO: " + e.getMessage());
        } catch (Exception e) {
            mostrarMensaje("❌ ERROR AL SERIALIZAR EN DISCO: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento Deserializar.
     * Invoca el caso de uso para cargar los exámenes guardados previamente.
     */
    @FXML
    private void handleDeserializar() {
        try {
            List<Examen> recuperados = cargarExamenesUseCase.cargarExamenes();
            StringBuilder sb = new StringBuilder();
            sb.append("📂 ÉXITO: Se recuperaron ").append(recuperados.size()).append(" exámenes desde el archivo serializado:\n");
            for (Examen ex : recuperados) {
                sb.append("   • ").append(ex.toString()).append("\n");
            }
            mostrarMensaje(sb.toString());
        } catch (Exception e) {
            mostrarMensaje("❌ ERROR AL DESERIALIZAR: " + e.getMessage());
        }
    }

    /**
     * Manejador del evento Limpiar.
     * Restablece los campos del formulario y el área de mensajes.
     */
    @FXML
    private void handleLimpiar() {
        limpiarCamposFormulario();
        txtAreaMensajes.clear();
        mostrarMensaje("Formulario y consola limpiados.");
    }

    /**
     * Limpia los campos de entrada de la interfaz.
     */
    private void limpiarCamposFormulario() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.getSelectionModel().clearSelection();
        rbPositivo.setSelected(true);
        if (chkUrgente != null) chkUrgente.setSelected(false);
        if (chkDomicilio != null) chkDomicilio.setSelected(false);
        if (cmbTarifa != null) cmbTarifa.getSelectionModel().selectFirst();
        txtCodigoExamen.requestFocus();
    }

    /**
     * Resuelve la estrategia de tarificación a partir del valor seleccionado en el ComboBox.
     * Implementa el Patrón de Comportamiento GoF Strategy: cada política de tarifa
     * es una clase concreta intercambiable que implementa {@link TarifaStrategy}.
     * 
     * @param seleccion Texto de la opción seleccionada en el ComboBox de tarifa.
     * @return Estrategia concreta correspondiente; por defecto retorna {@link TarifaParticularStrategy}.
     */
    private TarifaStrategy resolverEstrategia(String seleccion) {
        if (seleccion == null) return new TarifaParticularStrategy();
        switch (seleccion) {
            case "Tercera Edad": return new TarifaTerceraEdadStrategy();
            case "Convenio EPS": return new TarifaConvenioEpsStrategy();
            default:            return new TarifaParticularStrategy();
        }
    }

    /**
     * Muestra un mensaje en el TextArea principal agregando un salto de línea.
     * 
     * @param mensaje Contenido a desplegar.
     */
    private void mostrarMensaje(String mensaje) {
        if (txtAreaMensajes != null) {
            txtAreaMensajes.appendText(mensaje + "\n\n");
        }
    }
}
