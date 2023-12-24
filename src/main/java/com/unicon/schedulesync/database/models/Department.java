package com.unicon.schedulesync.database.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Department {
    private Integer id;
    private String depName;
    private String hodName;

    @Override
    public String toString() {
        return depName;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public SimpleStringProperty depNameProperty() {
        return new SimpleStringProperty(this.depName);
    }

    public SimpleStringProperty hodNameProperty() {
        return new SimpleStringProperty(this.hodName);
    }
}
