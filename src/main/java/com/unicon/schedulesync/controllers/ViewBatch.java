package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Batch;
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

public class ViewBatch implements Initializable {
    @FXML
    public TableColumn<Batch, Integer> idColumn;
    @FXML
    public TableColumn<Batch, String> batchNameColumn;
    @FXML
    public TableColumn<Batch, String> depNameColumn;
    @FXML
    TableView<Batch> table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Batch> batches = new ArrayList<>();
        try (Connection connection = Database.connect()) {
            String sql = "SELECT b.id, b.batch_name, d.department_name AS dep_name FROM batch b JOIN department d ON b.dep_id = d.id";
            PreparedStatement getBatch = connection.prepareStatement(sql);
            ResultSet rs = getBatch.executeQuery();
            while (rs.next()) {
                batches.add(new Batch(rs.getInt("id"), rs.getString("batch_name"), rs.getString("dep_name")));
            }
            getBatch.close();

        } catch (SQLException ignored) {
        }
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        batchNameColumn.setCellValueFactory(cellData -> cellData.getValue().batchNameProperty());
        depNameColumn.setCellValueFactory(cellData -> cellData.getValue().depNameProperty());
        ObservableList<Batch> data = FXCollections.observableArrayList(batches);
        table.setItems(data);
    }
}
