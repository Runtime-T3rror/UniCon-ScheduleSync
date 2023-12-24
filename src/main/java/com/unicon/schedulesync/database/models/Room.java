package com.unicon.schedulesync.database.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Room {
    private Integer id;
    private String roomNo;
    private String roomType;

    @Override
    public String toString() {
        return roomNo + "    {" + roomType + "}";
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public SimpleStringProperty roomTypeProperty() {
        return new SimpleStringProperty(this.roomType);
    }

    public SimpleStringProperty roomNoProperty() {
        return new SimpleStringProperty(this.roomNo);
    }
}
