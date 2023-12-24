package com.unicon.schedulesync.controllers;

import com.unicon.schedulesync.database.Database;
import com.unicon.schedulesync.database.models.Room;
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

public class ViewRoom implements Initializable {
    @FXML
    public TableView<Room> table;
    @FXML
    public TableColumn<Room, Integer> idColumn;
    @FXML
    public TableColumn<Room, String> roomNoColumn;
    @FXML
    public TableColumn<Room, String> roomTypeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        roomNoColumn.setCellValueFactory(cellData -> cellData.getValue().roomNoProperty());
        roomTypeColumn.setCellValueFactory(cellData -> cellData.getValue().roomTypeProperty());
        ObservableList<Room> data = FXCollections.observableArrayList(rooms);
        table.setItems(data);
    }
}
