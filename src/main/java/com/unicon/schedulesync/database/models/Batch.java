package com.unicon.schedulesync.database.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Batch {
    private Integer id;
    private String batchName;
    private String depName;

    @Override
    public String toString() {
        return batchName;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public SimpleStringProperty depNameProperty() {
        return new SimpleStringProperty(this.depName);
    }

    public SimpleStringProperty batchNameProperty() {
        return new SimpleStringProperty(this.batchName);
    }
}
