package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Subject;
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

public class ViewSubject implements Initializable {
    @FXML
    TableView<Subject> table;
    @FXML
    TableColumn<Subject, Integer> idColumn;
    @FXML
    TableColumn<Subject, String> nameColumn;
    @FXML
    TableColumn<Subject, String> shortNameColumn;

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
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        shortNameColumn.setCellValueFactory(cellData -> cellData.getValue().shortNameProperty());
        ObservableList<Subject> data = FXCollections.observableArrayList(subjects);
        table.setItems(data);
    }
}
