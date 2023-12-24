package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Department;
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

public class ViewDepartment implements Initializable {
    @FXML
    TableView<Department> table;
    @FXML
    TableColumn<Department, Integer> idColumn;
    @FXML
    TableColumn<Department, String> depNameColumn;
    @FXML
    TableColumn<Department, String> hodNameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Department> departments = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            PreparedStatement getDepartment = connection.prepareStatement("SELECT d.id, d.department_name AS depname, f.name AS hodName FROM department d JOIN faculty f ON d.hod_id = f.id;");
            ResultSet rs = getDepartment.executeQuery();
            while (rs.next()) {
                departments.add(new Department(
                    rs.getInt("id"),
                    rs.getString("depname"),
                    rs.getString("hodName")
                ));
            }

            getDepartment.close();

        } catch (SQLException ignored) {
        }
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        depNameColumn.setCellValueFactory(cellData -> cellData.getValue().depNameProperty());
        hodNameColumn.setCellValueFactory(cellData -> cellData.getValue().hodNameProperty());
        ObservableList<Department> data = FXCollections.observableArrayList(departments);
        table.setItems(data);
    }
}
