package model.entities;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.Formatter;

/**
 * Created by Asus on 29.01.2018.
 */
public class Note {

    private IntegerProperty id;
    private StringProperty text;
    private ObjectProperty<LocalDate> date;
    private StringProperty priority;
    private StringProperty project;


    public Note() {
        id = new SimpleIntegerProperty();
        text = new SimpleStringProperty();
        date = new SimpleObjectProperty<>();
        priority = new SimpleStringProperty();
        project = new SimpleStringProperty();
    }


    public Note(Date date) {
        this.date = new SimpleObjectProperty<>(date.toLocalDate());
    }

    public Note(int id, String text) {
        this.id = new SimpleIntegerProperty(id);
        this.text = new SimpleStringProperty(text);
    }

    public Note(int id, String text, Date date) {
        this(id, text);
        this.date = new SimpleObjectProperty<>(date.toLocalDate());
    }

    public Note(int id, String text, Date date, String project) {
        this(id, text, date);
        this.project = new SimpleStringProperty(project);
    }

    public Note(int id, String text, Date date, String priority, String project) {
        this(id, text, date, project);
        this.priority = new SimpleStringProperty(priority);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getPriority() {
        return priority.get();
    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getProject() {
        return project.get();
    }

    public StringProperty projectProperty() {
        return project;
    }

    public void setProject(String project) {
        this.project.set(project);
    }

    @Override
    public String toString() {
//        return getClass().getSimpleName() + " { id: " + id.intValue() +
//                ", text: " + text.getValue() + ", date: " + date.getValue()
//                + ", priority: " + priority.getValue() + ", project: "
//                + project.getValue() + " }";
        Formatter f = new Formatter();
        return f.format("%-55s %-10s", text.getValue(), date.getValue()).toString();
        //Ошибка с format
    }
}
