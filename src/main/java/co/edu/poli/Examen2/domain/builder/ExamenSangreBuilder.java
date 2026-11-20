package co.edu.poli.Examen2.domain.builder;

import co.edu.poli.Examen2.domain.exceptions.DomainValidationException;
import co.edu.poli.Examen2.domain.model.ExamenSangre;
import co.edu.poli.Examen2.domain.model.FactorRH;
import co.edu.poli.Examen2.domain.model.TipoSangre;

/**
 * Constructor Concreto (ConcreteBuilder) del Patrón GoF Builder.
 * Implementa la interfaz {@link ExamenBuilder} acumulando los datos requeridos
 * para un examen de sangre y aplicando las validaciones pertinentes antes de
 * construir y entregar el producto final.
 * 
 * Pertenece a la capa de Dominio en la Arquitectura Hexagonal.
 * 
 * @author Jostin Santiago Perez Agudelo
 * @version 1.0
 */
public class ExamenSangreBuilder implements ExamenBuilder {

    private String codigo;
    private String nombrePaciente;
    private double costoBase;
    private TipoSangre tipoSangre;
    private FactorRH factorRH;

    /**
     * Construye una nueva instancia limpia de {@link ExamenSangreBuilder}.
     */
    public ExamenSangreBuilder() {
        this.reset();
    }

    @Override
    public ExamenBuilder conCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public ExamenBuilder conNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
        return this;
    }

    @Override
    public ExamenBuilder conCostoBase(double costoBase) {
        this.costoBase = costoBase;
        return this;
    }

    @Override
    public ExamenBuilder conTipoSangre(TipoSangre tipoSangre) {
        this.tipoSangre = tipoSangre;
        return this;
    }

    @Override
    public ExamenBuilder conFactorRH(FactorRH factorRH) {
        this.factorRH = factorRH;
        return this;
    }

    @Override
    public ExamenSangre build() throws DomainValidationException {
        // Creación del producto
        ExamenSangre examen = new ExamenSangre(codigo, nombrePaciente, costoBase, tipoSangre, factorRH);

        // Validación estricta de las invariantes de negocio
        examen.validar();

        return examen;
    }

    @Override
    public void reset() {
        this.codigo = null;
        this.nombrePaciente = null;
        this.costoBase = 0.0;
        this.tipoSangre = null;
        this.factorRH = null;
    }
}
