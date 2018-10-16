package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.config.DBWorker;
import model.entities.Note;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Извлекает данные из таблицы storage
 */
public class NoteService {

    private static NoteService instance;
    private Statement statement;

    private NoteService() {
        DBWorker dbWorker = DBWorker.getInstance();
        try {
            statement = dbWorker.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NoteService getInstance() {
        return instance = new NoteService();
    }

    public List<Note> getAll() {
        List<Note> notes = new ArrayList<>();
        String query = "select * from storage order by id desc";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Note note = new Note(resultSet.getInt(5),
                        resultSet.getString(1),
                        resultSet.getDate(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }


    public void insertIntoTable(Note note) {
        System.out.println(Date.valueOf(note.getDate()));
        String query = "insert into storage (text, date, priority, project) " +
                "values ('" + note.getText()
                + "', '" + Date.valueOf(note.getDate())
                + "', '" + note.getPriority()
                + "', '" + note.getProject()
                + "')";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromTable(Note note) {
        String query = "delete FROM storage where id = " + note.getId();
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Note> getTextOfNote(String key) {
        Note note;
        List<Note> notes = new ArrayList<>();
        String query = "select * from storage where text like '%" + key.trim() + "%'";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                note = new Note(resultSet.getInt(5),
                        resultSet.getString(1),
                        resultSet.getDate(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }


}

