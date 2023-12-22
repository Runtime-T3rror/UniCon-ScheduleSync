module com.unicon.schedulesync {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.unicon.schedulesync to javafx.fxml;
    exports com.unicon.schedulesync;
    exports com.unicon.schedulesync.controllers;
    opens com.unicon.schedulesync.controllers to javafx.fxml;
}