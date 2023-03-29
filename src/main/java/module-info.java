module eus.ehu.faculty {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens faculty.project.controllers to javafx.fxml;

    exports  faculty.project.controllers;
}
