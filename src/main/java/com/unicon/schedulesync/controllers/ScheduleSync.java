package com.unicon.schedulesync.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ScheduleSync {
    @FXML
    BorderPane MainPane;

    @FXML
    public void onClickAddSubject(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-subject.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddFaculty(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-faculty.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddDepartment(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-department.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddBatch(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-batch.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddTimeSlot(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-time-slot.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddRoom(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-room.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }

    @FXML
    public void onClickAddTimeTable(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(com.unicon.schedulesync.ScheduleSync.class.getResource("add-timetable.fxml"));
            MainPane.setCenter(fxmlLoader.load());
        } catch (IOException ignored) {
        }
    }
}