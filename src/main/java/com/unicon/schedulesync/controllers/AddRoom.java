package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.ScheduleSync;
import com.unicon.schedulesync.database.Database;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddRoom implements Initializable {
    public TextField roomNo;
    public ComboBox<String> roomType;

    public void onClickAdd(ActionEvent actionEvent) {
        if (roomNo.getText().isEmpty() || roomType.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("roomNo and roomType cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement insertRoom = connection.prepareStatement("INSERT INTO room (room_no, room_type) VALUES (?,?::room_type)");
                insertRoom.setString(1, roomNo.getText().trim());
                insertRoom.setString(2, roomType.getValue());
                if (insertRoom.executeUpdate() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added");
                    alert.setHeaderText(null);
                    alert.setContentText(roomNo.getText() + " successfully added");
                    alert.showAndWait();
                    roomNo.clear();
                }
                insertRoom.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText(roomNo.getText() + " couldn't be added");
                alert.showAndWait();
            }
        }
    }

    public void onClickView(ActionEvent actionEvent) {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            popupStage.setTitle("Rooms");
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(ScheduleSync.class.getResource("rooms-table.fxml"))), 600, 400);
            popupStage.setResizable(false);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomType.getItems().clear();
        roomType.getItems().add("Classroom");
        roomType.getItems().add("Physics-Lab");
        roomType.getItems().add("Computer-Lab");
    }
}
