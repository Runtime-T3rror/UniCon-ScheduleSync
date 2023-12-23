module com.unicon.schedulesync {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires lombok;

    opens com.unicon.schedulesync to javafx.fxml;
    exports com.unicon.schedulesync;
    exports com.unicon.schedulesync.controllers;
    exports com.unicon.schedulesync.database.models;
    opens com.unicon.schedulesync.controllers to javafx.fxml;
}