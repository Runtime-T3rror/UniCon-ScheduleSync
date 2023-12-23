package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Faculty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewFaculty implements Initializable {
    @FXML
    TableView<Faculty> table;
    @FXML
    TableColumn<Faculty, Integer> idColumn;
    @FXML
    TableColumn<Faculty, String> nameColumn;
    @FXML
    TableColumn<Faculty, String> shortNameColumn;
    @FXML
    TableColumn<Faculty, String> subjectShortNameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Faculty> faculties = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getSubject = connection.prepareStatement("SELECT f.id AS faculty_id, f.name AS faculty_name, f.shortname AS faculty_shortname, s.shortname AS subject_shortname FROM faculty f JOIN subject s ON f.subject_id = s.id;");
            ResultSet rs = getSubject.executeQuery();
            while (rs.next()) {
                faculties.add(new Faculty(rs.getInt("faculty_id"), rs.getString("faculty_name"), rs.getString("faculty_shortname"), rs.getString("subject_shortname")));
            }
            getSubject.close();
        } catch (SQLException ignored) {
        }
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        shortNameColumn.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());
        subjectShortNameColumn.setCellValueFactory(cellData -> cellData.getValue().subjectShortNameProperty());
        ObservableList<Faculty> data = FXCollections.observableArrayList(faculties);
        table.setItems(data);
    }
}
