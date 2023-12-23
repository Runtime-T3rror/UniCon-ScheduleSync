package com.unicon.schedulesync.database.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Faculty {
    Integer id;
    String name;
    String shortName;
    String subjectShortName;

    @Override
    public String toString() {
        return name;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public SimpleStringProperty shortNameProperty() {
        return new SimpleStringProperty(this.shortName);
    }

    public SimpleStringProperty subjectShortNameProperty() {
        return new SimpleStringProperty(this.subjectShortName);
    }
}
