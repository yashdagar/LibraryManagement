package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Librarian {
    public int id=0;
    public String name;
    public String email;
    public String phone;

    public Librarian(int id,String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public static Optional<Librarian> getFromResultSet(ResultSet resultSet) {
        Librarian librarian = null;
        try {
            if (resultSet.next()) {
                librarian = new Librarian(
                        resultSet.getInt("id"), 
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
            }
        } catch(SQLException e) {
            System.out.println("Error occurred parsing librarian");
            e.printStackTrace();
        }
        return Optional.ofNullable(librarian);
    }

    public int getId() {
        return id;
    }
}