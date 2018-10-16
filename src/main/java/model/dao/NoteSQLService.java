package model.dao;

import model.config.DBWorker;
import model.entities.Note;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 02.02.2018.
 */
public class NoteSQLService {
    private Statement statement;
    private static NoteSQLService instance;

    private NoteSQLService() {
        DBWorker dbWorker = DBWorker.getInstance();
        try {
            statement = dbWorker.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NoteSQLService getInstance() {
        return instance = new NoteSQLService();
    }

    public List<Date> getDate() {
        List<Date> dates = new ArrayList<>();

        String query = "select date from storage group by date order by date";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Date date = resultSet.getDate(1);
                dates.add(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    public List<String> getProject() {
        List<String> projects = new ArrayList<>();

        String query = "select project from storage group by project order by project";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String project = resultSet.getString(1);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<String> getPriority() {
        List<String> priority = new ArrayList<>();

        String query = "select priority from storage group by priority";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
                priority.add(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priority;
    }
}
