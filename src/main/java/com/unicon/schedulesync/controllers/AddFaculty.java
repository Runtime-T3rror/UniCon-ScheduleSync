package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.ScheduleSync;
import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Subject;
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

public class AddFaculty implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public TextField shortName;
    @FXML
    public ComboBox<Subject> subjectBox;

    @FXML
    public void onClickAdd(ActionEvent actionEvent) {
        if (name.getText().isEmpty() || shortName.getText().isEmpty() || subjectBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("name, shortname and subject cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement insertFaculty = connection.prepareStatement("INSERT INTO faculty(name,shortname,subject_id) VALUES(?,?,?)");
                insertFaculty.setString(1, name.getText().trim());
                insertFaculty.setString(2, shortName.getText().trim());
                insertFaculty.setInt(3, subjectBox.getValue().getId());
                if (insertFaculty.executeUpdate() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added");
                    alert.setHeaderText(null);
                    alert.setContentText(name.getText() + " faculty successfully added for subject " + subjectBox.getValue().getName());
                    alert.showAndWait();
                    name.clear();
                    shortName.clear();
                }
                insertFaculty.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText(name.getText() + " couldn't be added");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getSubject = connection.prepareStatement("SELECT * from subject");
            ResultSet rs = getSubject.executeQuery();
            while (rs.next()) {
                subjects.add(new Subject(rs.getInt("id"), rs.getString("name"), rs.getString("shortname")));
            }
            getSubject.close();
        } catch (SQLException ignored) {
        }
        subjectBox.getItems().clear();
        subjectBox.getItems().addAll(subjects);
    }

    @FXML
    public void onClickView(ActionEvent actionEvent) {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            popupStage.setTitle("Faculties");
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(ScheduleSync.class.getResource("faculty-table.fxml"))), 600, 400);
            popupStage.setResizable(false);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException ignored) {
        }
    }
}
