package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class User {
    public final String name, email, password, program;
    public final int sapID, yearOfStudy, borrowedBooks, id;

    public User(String name, String email, String password, String program, int sapID, int yearOfStudy, int borrowedBooks, int id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.program = program;
        this.sapID = sapID;
        this.yearOfStudy = yearOfStudy;
        this.borrowedBooks = borrowedBooks;
    }

    static public ArrayList<User> getUsersFromResultSet (ResultSet resultSet){
        ArrayList<User> users = new ArrayList<>();

        try {
            while (resultSet.next()){
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("program"),
                        resultSet.getInt("sapID"),
                        resultSet.getInt("yearOfStudy"),
                        resultSet.getInt("borrowedBooks"),
                        resultSet.getInt("id")
                );
                users.add(user);
            }
        }catch(SQLException e){
            System.out.println("Error occurred parsing user");
        }

        return users;
    }

    public static Optional<User> getFromResultSet(ResultSet resultSet){
        User user = null;

        try {
            if (resultSet.next()){
                user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("program"),
                        resultSet.getInt("sapID"),
                        resultSet.getInt("yearOfStudy"),
                        resultSet.getInt("borrowedBooks"),
                        resultSet.getInt("id")
                );
            }
        }catch(SQLException e){
            System.out.println("Error occurred parsing user");
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }
}