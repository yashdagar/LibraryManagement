package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Librarian {
    public String name;
    public String email;
    public String phone;

    public Librarian(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public static Optional<Librarian> getFromResultSet(ResultSet resultSet) {
        Librarian librarian = null;
        try {
            if (resultSet.next()) {
                librarian = new Librarian(
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
}