package com.unicon.schedulesync.database.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class TimeSlot {
    private Integer id;
    private Time startTime;
    private Time endTime;

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.id);
    }

    public SimpleObjectProperty<Time> startTimeProperty() {
        return new SimpleObjectProperty<>(startTime);
    }

    public SimpleObjectProperty<Time> endTimeProperty() {
        return new SimpleObjectProperty<>(endTime);
    }
}
