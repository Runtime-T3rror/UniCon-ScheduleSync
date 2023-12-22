package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSubject {
    @FXML
    TextField name;
    @FXML
    TextField shortName;

    public void onClickAdd(ActionEvent actionEvent) {
        if (name.getText().isEmpty() || shortName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("name and shortname cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement insertSubject = connection.prepareStatement("INSERT INTO subject(name,shortname) VALUES(?,?)");
                insertSubject.setString(1, name.getText().trim());
                insertSubject.setString(2, shortName.getText().trim());
                if (insertSubject.executeUpdate() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added");
                    alert.setHeaderText(null);
                    alert.setContentText(name.getText() + " successfully added");
                    alert.showAndWait();
                    name.clear();
                    shortName.clear();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText(name.getText() + " couldn't be added");
                alert.showAndWait();
            }
        }
    }
}
