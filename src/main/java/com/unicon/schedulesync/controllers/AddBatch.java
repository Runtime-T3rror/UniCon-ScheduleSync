package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.ScheduleSync;
import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddBatch implements Initializable {
    @FXML
    public TextField batchName;
    @FXML
    public ComboBox<Department> departmentBox;

    @FXML
    public void onClickAdd(ActionEvent actionEvent) {
        if (batchName.getText().isEmpty() || departmentBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("batch and department cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement insertFaculty = connection.prepareStatement("INSERT INTO batch(batch_name,dep_id) VALUES(?,?)");
                insertFaculty.setString(1, batchName.getText().trim());
                insertFaculty.setInt(2, departmentBox.getValue().getId());
                if (insertFaculty.executeUpdate() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added");
                    alert.setHeaderText(null);
                    alert.setContentText(batchName.getText() + " batch successfully added for subject " + departmentBox.getValue().getDepName());
                    alert.showAndWait();
                    batchName.clear();
                }
                insertFaculty.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText(batchName.getText() + " couldn't be added");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Department> departments = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getDepartment = connection.prepareStatement("SELECT d.id, d.department_name AS depname, f.name AS hodName FROM department d JOIN faculty f ON d.hod_id = f.id;");
            ResultSet rs = getDepartment.executeQuery();
            while (rs.next()) {
                departments.add(new Department(rs.getInt("id"), rs.getString("depname"), rs.getString("hodName")));
            }
            rs.close();
            getDepartment.close();
        } catch (SQLException ignored) {
        }
        departmentBox.getItems().clear();
        departmentBox.getItems().addAll(departments);
    }

    @FXML
    public void onClickView(ActionEvent actionEvent) {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            popupStage.setTitle("Batch");
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(ScheduleSync.class.getResource("batch-table.fxml"))), 600, 400);
            popupStage.setResizable(false);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException ignored) {
        }
    }
}
