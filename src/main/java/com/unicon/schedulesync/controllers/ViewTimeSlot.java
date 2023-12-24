package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.TimeSlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewTimeSlot implements Initializable {
    public TableView<TimeSlot> table;
    public TableColumn<TimeSlot, Integer> idColumn;
    public TableColumn<TimeSlot, Time> startTimeColumn;
    public TableColumn<TimeSlot, Time> endTimeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getTimeSlot = connection.prepareStatement("SELECT * from time_slot");
            ResultSet rs = getTimeSlot.executeQuery();
            while (rs.next()) {
                timeSlots.add(new TimeSlot(rs.getInt("id"), rs.getTime("start_time"), rs.getTime("end_time")));
            }
            getTimeSlot.close();
        } catch (SQLException ignored) {
        }
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        ObservableList<TimeSlot> data = FXCollections.observableArrayList(timeSlots);
        table.setItems(data);
    }
}
