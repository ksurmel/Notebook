package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Main;
import model.dao.NoteSQLService;
import model.dao.NoteService;
import model.entities.Note;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Asus on 31.01.2018.
 */
public class NotesController {
    private NoteService noteService;
    private NoteSQLService noteSQLService;
    private ObservableList<Note> notes;
    private ObservableList<Note> findings;
    private ObservableList<Date> dateSort;
    private ObservableList<String> projects;
    private ObservableList<String> priorities;
    private Main mainApp;
    private Stage dialogStage;


    @FXML
    private ListView<Note> noteListView;

    @FXML
    private ListView<Date> dateNote;

    @FXML
    private ListView<String> projectList;

    @FXML
    private ListView<String> priorityListView;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField noteField;

    @FXML
    private TextField searchField;

    public NotesController() {
        noteService = NoteService.getInstance();
        noteSQLService = NoteSQLService.getInstance();
        notes = FXCollections.observableArrayList(noteService.getAll());
        dateSort = FXCollections.observableArrayList(noteSQLService.getDate());
        projects = FXCollections.observableArrayList(noteSQLService.getProject());
        priorities = FXCollections.observableList(noteSQLService.getPriority());
    }


    @FXML
    private void initialize() {
        noteListView.setCellFactory(CheckBoxListCell.forListView(new Callback<Note, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Note item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->
                        //System.out.println("Check box for " + item + " changed from " + wasSelected + " to " + isNowSelected)
                        NotesController.this.deleteNote(item)
                );

                return observable;
            }
        }));
        noteListView.setItems(notes);
        dateNote.setItems(dateSort);
        projectList.setItems(projects);
        priorityListView.setItems(priorities);


        dateNote.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showSelectedDate(newValue))
        );
        projectList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedProject(newValue)
        );

        priorityListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedPriority(newValue)
        );

        noteField.setPromptText("Please enter the text of the note here...");
        searchField.setPromptText("Please enter the keyword to search here...");
    }

    private void showSelectedDate(Date input) {
        infoLabel.setText(input.toString());
        ObservableList<Note> dateNotes;
        dateNotes = FXCollections.observableArrayList(notes.stream().filter(note ->
                note.getDate().equals(input.toLocalDate())).collect(Collectors.toList()));
        noteListView.setItems(dateNotes);
    }

    private void showSelectedProject(String input) {
        infoLabel.setText(input);
        ObservableList<Note> projectNotes;
        projectNotes = FXCollections.observableArrayList(notes.stream().filter(note ->
                note.getProject().equals(input)).collect(Collectors.toList()));
        noteListView.setItems(projectNotes);
    }

    private void showSelectedPriority(String input) {
        infoLabel.setText(input);
        ObservableList<Note> priorityNotes;
        priorityNotes = FXCollections.observableArrayList(notes.stream().filter(note ->
                note.getPriority().equals(input)).collect(Collectors.toList()));
        noteListView.setItems(priorityNotes);
    }

    @FXML
    private void showAll() {
        infoLabel.setText("All");
        noteListView.setItems(notes);
    }

    private void deleteNote(Note item) {
        noteService.deleteFromTable(item);
        notes.remove(item);
        noteListView.getItems().remove(item);
        refreshLists();
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке New...
     * Открывает диалоговое окно с дополнительной информацией нового адресата.
     */
    @FXML
    private void createNote() {
        if (isNoteEntered()) {
            Note note = new Note();
            note.setText(noteField.getText());
            mainApp.showNoteCreateDialog(note);
            refreshLists();
            noteField.clear();
        }
    }

    public void refreshLists() {
        notes.clear();
        dateSort.clear();
        projects.clear();
        priorities.clear();

        notes = FXCollections.observableArrayList(noteService.getAll());
        dateSort = FXCollections.observableArrayList(noteSQLService.getDate());
        projects = FXCollections.observableArrayList(noteSQLService.getProject());
        priorities = FXCollections.observableList(noteSQLService.getPriority());

        noteListView.setItems(notes);
        dateNote.setItems(dateSort);
        projectList.setItems(projects);
        priorityListView.setItems(priorities);
    }

    public boolean isNoteEntered() {
        if (noteField.getText().length() == 0 || noteField.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("You should enter the note!");
            alert.setContentText("Please, enter the text of the note in the field below to continue");

            alert.showAndWait();
            return false;
        } else return true;
    }

    @FXML
    private void find() {
        if (isSearchTextEntered()) {
            String key = searchField.getText();
            findings = FXCollections.observableArrayList(noteService.getTextOfNote(key));
            System.out.println(findings);
            if (!findings.isEmpty()) {
                infoLabel.setText("Results of the search: '" + key + "'");
                noteListView.setItems(findings);
            } else {
                infoLabel.setText("Nothing was found for the key '" + key + "'");
                noteListView.setItems(FXCollections.observableArrayList());
            }
            searchField.clear();
        }
    }

    boolean isSearchTextEntered() {
        if (searchField.getText().length() == 0 || searchField.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("You should enter the text of the search!");
            alert.setContentText("Please, enter the text of the search in the field above to continue");

            alert.showAndWait();
            return false;
        } else return true;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}
