package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.ScheduleSync;
import com.unicon.schedulesync.database.Database;
import javafx.event.ActionEvent;
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
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTime {

    public TextField startTime;
    public TextField endTime;

    public void onClickAdd(ActionEvent actionEvent) {
        if (startTime.getText().isEmpty() || endTime.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("start time and end time cannot be empty");
            alert.showAndWait();
        } else {
            if (isTimeFormatValid(startTime.getText().trim()) && isTimeFormatValid(endTime.getText().trim())) {
                Time start = parseTime(startTime.getText().trim());
                Time end = parseTime(endTime.getText().trim());
                try (Connection connection = Database.connect()) {
                    PreparedStatement insertTime = connection.prepareStatement("INSERT INTO time_slot(start_time,end_time) VALUES(?,?)");
                    insertTime.setTime(1, start);
                    insertTime.setTime(2, end);
                    if (insertTime.executeUpdate() == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Successfully added");
                        alert.setHeaderText(null);
                        alert.setContentText(start + " to " + end + " successfully added");
                        alert.showAndWait();
                        startTime.clear();
                        endTime.clear();
                    }
                    insertTime.close();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Occurred");
                    alert.setHeaderText(null);
                    alert.setContentText(start + " to " + end + " couldn't be added");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Valid format");
                alert.setHeaderText(null);
                alert.setContentText("Please enter time in valid format HH:MM:SS");
                alert.showAndWait();
            }
        }
    }

    public void onClickView(ActionEvent actionEvent) {
        try {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            popupStage.setTitle("Time slots");
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(ScheduleSync.class.getResource("time-slot-table.fxml"))), 600, 400);
            popupStage.setResizable(false);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException ignored) {
        }
    }

    private boolean isTimeFormatValid(String time) {
        String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    private Time parseTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            java.util.Date date = sdf.parse(time);
            return new Time(date.getTime());
        } catch (ParseException ignored) {
        }
        return null;
    }
}
