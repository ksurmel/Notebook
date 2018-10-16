package controller;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Main;
import model.dao.NoteSQLService;
import model.dao.NoteService;
import model.entities.Note;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Asus on 04.02.2018.
 */
public class NoteCreateController {


    private Stage dialogStage;
    private Note note;
    private Main mainApp;
    private String project;
    private String priority;
    private LocalDate date;
    private boolean okClicked = false;
    private NoteSQLService noteSQLService = NoteSQLService.getInstance();
    private NoteService noteService = NoteService.getInstance();
    private ObservableList<String> projects;
    private ObservableList<String> priorities;

    @FXML
    private ChoiceBox<String> projectSelect;

    @FXML
    private ChoiceBox<String> prioritySelect;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField addProject;

    @FXML
    private Button submit;

    @FXML
    private Label dateMist;

    @FXML
    private Label projMist;

    @FXML
    private Label priorMist;

    @FXML
    private void initialize() {
        addProject.setVisible(false);
        submit.setVisible(false);
        datePicker.setOnAction(event -> {
            date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        loadData();
        priorities = FXCollections.observableArrayList("Very Important", "Important", "Not Important");
        prioritySelect.getItems().addAll(priorities);
        prioritySelect.setOnAction(event -> {
            priority = prioritySelect.getValue();
        });

    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void createNote(Note note) {
        this.note = note;
    }

    public boolean isOkClicked() {
        return okClicked;
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            note.setDate(date);
            note.setProject(project);
            note.setPriority(priority);
            System.out.println(note);

            noteService.insertIntoTable(note);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void projectSubmitted() {
        if (addProject.getText() == null || addProject.getText().length() == 0)
            projMist.setText("Please enter project!");
        else {
            project = addProject.getText();
            addProject.setVisible(false);
            submit.setVisible(false);
            projects.add(project);
            projectSelect.getItems().remove("+ Add new");
            projectSelect.getItems().add(project);
            projectSelect.setValue(project);
        }
    }

    private void loadData() {
        projects = FXCollections.observableArrayList(noteSQLService.getProject());
        projects.add("+ Add new");
        if (projects.size() == 1) {
            addProject.setVisible(true);
            addProject.setPromptText("New project");
            submit.setVisible(true);
        } else {
            projectSelect.getItems().addAll(projects);
            projectSelect.setOnAction(event -> {
                project = projectSelect.getValue();
                if (project.equals("+ Add new")) {
                    addProject.setVisible(true);
                    submit.setVisible(true);
                }

            });
        }
    }

    private boolean isInputValid() {
        if (datePicker.getValue() == null) {
            dateMist.setText("Please enter date!");
            return false;
        } else if (projectSelect.getValue() == null || projectSelect.getValue().equals("+ Add new")) {
            projMist.setText("Please enter project!");
            return false;
        } else if (prioritySelect.getValue() == null) {
            priorMist.setText("Please enter priority");
            return false;
        } else {
            dateMist.setText("");
            projMist.setText("");
            priorMist.setText("");
            return true;
        }
    }
}


