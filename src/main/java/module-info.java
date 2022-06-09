module com.example.projektto1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires spring.web;
    requires lombok;

    opens com.example.projektto1 to javafx.fxml;
    exports com.example.projektto1;
    exports com.example.projektto1.controller;
    exports com.example.projektto1.dto;
    exports com.example.projektto1.factory;
    exports com.example.projektto1.rest;
    exports com.example.projektto1.table;
    exports com.example.projektto1.handler;
}