package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.ScheduleSync;
import com.unicon.schedulesync.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AddDepartment {

    @FXML
    public TextField hodShortName;
    @FXML
    public TextField departmentName;

    public void onClickAdd(ActionEvent actionEvent) {
        if (hodShortName.getText().isEmpty() || departmentName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("hod name and department name cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement getHodId = connection.prepareStatement("SELECT id from faculty where lower(shortname) = lower(?)");
                getHodId.setString(1, hodShortName.getText().trim());
                ResultSet rs = getHodId.executeQuery();
                int id = 0;
                while (rs.next()) {
                    id = rs.getInt("id");
                }
                if (id != 0) {
                    PreparedStatement insertDepartment = connection.prepareStatement("INSERT INTO department(department_name,hod_id) VALUES(?,?)");
                    insertDepartment.setString(1, departmentName.getText().trim());
                    insertDepartment.setInt(2, id);
                    if (insertDepartment.executeUpdate() == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successfully added");
                        alert.setHeaderText(null);
                        alert.setContentText(departmentName.getText() + " successfully added");
                        alert.showAndWait();
                        departmentName.clear();
                        hodShortName.clear();
                    }
                    insertDepartment.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Occurred");
                    alert.setHeaderText(null);
                    alert.setContentText("Couldn't find hod");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText(departmentName.getText() + " couldn't be added");
                alert.showAndWait();
            }
        }
    }

    public void onClickView(ActionEvent actionEvent) {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            popupStage.setTitle("Department");
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(ScheduleSync.class.getResource("department-table.fxml"))), 600, 400);
            popupStage.setResizable(false);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException ignored) {
        }
    }
}
