open module co.edu.poli.Examen2 {
    requires javafx.controls;
    requires javafx.fxml;

    exports co.edu.poli.Examen2.vista;
    exports co.edu.poli.Examen2.controlador;
    exports co.edu.poli.Examen2.domain.model;
    exports co.edu.poli.Examen2.domain.exceptions;
    exports co.edu.poli.Examen2.domain.builder;
    exports co.edu.poli.Examen2.domain.decorator;
    exports co.edu.poli.Examen2.domain.strategy;
    exports co.edu.poli.Examen2.ports.in;
    exports co.edu.poli.Examen2.ports.out;
    exports co.edu.poli.Examen2.application.service;
    exports co.edu.poli.Examen2.infrastructure.adapters.out.persistence;
}
