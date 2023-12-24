package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Batch;
import com.unicon.schedulesync.database.models.Faculty;
import com.unicon.schedulesync.database.models.Room;
import com.unicon.schedulesync.database.models.TimeSlot;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddTimeTable implements Initializable {
    public ComboBox<TimeSlot> timeBox;
    public ComboBox<String> dayBox;
    public ComboBox<Batch> departmentBatchBox;
    public ComboBox<Faculty> facultyBox;
    public ComboBox<Room> roomBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dayBox.getItems().clear();
        dayBox.getItems().setAll(new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")));
        timeBox.getItems().clear();
        departmentBatchBox.getItems().clear();
        facultyBox.getItems().clear();
        roomBox.getItems().clear();
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getTimeSlot = connection.prepareStatement("SELECT * from time_slot");
            ResultSet rs = getTimeSlot.executeQuery();
            while (rs.next()) {
                timeSlots.add(new TimeSlot(rs.getInt("id"), rs.getTime("start_time"), rs.getTime("end_time")));
            }
            rs.close();
            getTimeSlot.close();
        } catch (SQLException ignored) {
        }
        timeBox.getItems().addAll(timeSlots);
        ArrayList<Batch> batches = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            String sql = "SELECT b.id, b.batch_name, d.department_name AS dep_name FROM batch b JOIN department d ON b.dep_id = d.id";
            PreparedStatement getBatch = connection.prepareStatement(sql);
            ResultSet rs = getBatch.executeQuery();
            while (rs.next()) {
                batches.add(new Batch(rs.getInt("id"), rs.getString("batch_name"), rs.getString("dep_name")));
            }
            rs.close();
            getBatch.close();
        } catch (SQLException ignored) {
        }
        departmentBatchBox.getItems().addAll(batches);
        ArrayList<Faculty> faculties = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getFaculty = connection.prepareStatement("SELECT f.id AS faculty_id, f.name AS faculty_name, f.shortname AS faculty_shortname, s.shortname AS subject_shortname FROM faculty f JOIN subject s ON f.subject_id = s.id;");
            ResultSet rs = getFaculty.executeQuery();
            while (rs.next()) {
                faculties.add(new Faculty(rs.getInt("faculty_id"), rs.getString("faculty_name"), rs.getString("faculty_shortname"), rs.getString("subject_shortname")));
            }
            rs.close();
            getFaculty.close();
        } catch (SQLException ignored) {
        }
        facultyBox.getItems().addAll(faculties);
        ArrayList<Room> rooms = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getRooms = connection.prepareStatement("SELECT * FROM room");
            ResultSet rs = getRooms.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(rs.getInt("id"), rs.getString("room_no"), rs.getString("room_type")));
            }
            rs.close();
            getRooms.close();
        } catch (SQLException ignored) {
        }
        roomBox.getItems().addAll(rooms);
    }

    public void onClickAdd(ActionEvent actionEvent) {
        if (timeBox.getValue() == null || dayBox.getValue() == null || departmentBatchBox.getValue() == null || facultyBox.getValue() == null || roomBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement addTimeTable = connection.prepareStatement("INSERT INTO timetable (time_id, day, bat_id, fac_id, room_id) VALUES (?,?::day_of_week,?,?,?)");
                addTimeTable.setInt(1, timeBox.getValue().getId());
                addTimeTable.setString(2, dayBox.getValue());
                addTimeTable.setInt(3, departmentBatchBox.getValue().getId());
                addTimeTable.setInt(4, facultyBox.getValue().getId());
                addTimeTable.setInt(5, roomBox.getValue().getId());
                if (addTimeTable.executeUpdate() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added");
                    alert.setHeaderText(null);
                    alert.setContentText(" successfully added");
                    alert.showAndWait();
                }
                System.out.println(addTimeTable);
                addTimeTable.close();
            } catch (SQLException ignored) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occurred");
                alert.setHeaderText(null);
                alert.setContentText("couldn't be added");
                alert.showAndWait();
            }
        }
    }

    public void onClickView(ActionEvent actionEvent) {
        if (dayBox.getValue() == null || departmentBatchBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Select Batch and Day to view timetable");
            alert.showAndWait();
        } else {
            try (Connection connection = Database.connect()) {
                PreparedStatement getTimetable = connection.prepareStatement("SELECT time_slot.start_time AS StartTime, time_slot.end_time AS EndTime, faculty.shortname AS FacultyShortName, subject.shortname AS SubjectShortName, room.room_no AS RoomNo FROM timetable JOIN time_slot ON timetable.time_id = time_slot.id JOIN batch ON timetable.bat_id = batch.id JOIN department ON batch.dep_id = department.id JOIN faculty ON timetable.fac_id = faculty.id JOIN room ON timetable.room_id = room.id JOIN subject ON faculty.subject_id = subject.id WHERE batch.id = ? AND timetable.day = ?::day_of_week");
                getTimetable.setInt(1, departmentBatchBox.getValue().getId());
                getTimetable.setString(2, dayBox.getValue());
                ResultSet resultSet = getTimetable.executeQuery();
                StringBuilder tableBuilder = new StringBuilder();
                while (resultSet.next()) {
                    String startTime = resultSet.getString("StartTime");
                    String endTime = resultSet.getString("EndTime");
                    String facultyShortName = resultSet.getString("FacultyShortName");
                    String subjectShortName = resultSet.getString("SubjectShortName");
                    String roomNo = resultSet.getString("RoomNo");
                    tableBuilder.append(String.format("%-36s %-36s %-18s\n", startTime + " - " + endTime, facultyShortName + "(" + subjectShortName + ")", roomNo));
                }
                resultSet.close();
                getTimetable.close();
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                popupStage.setTitle("Rooms");
                TextArea textArea = new TextArea(tableBuilder.toString());
                textArea.setEditable(false);
                textArea.setStyle("-fx-text-fill: #eeeeee; -fx-control-inner-background: #00ADB5;");
                Scene scene = new Scene(new VBox(textArea), 400, 200);
                popupStage.setResizable(false);
                popupStage.setScene(scene);
                popupStage.showAndWait();
            } catch (SQLException ignored) {
            }
        }
    }
}
