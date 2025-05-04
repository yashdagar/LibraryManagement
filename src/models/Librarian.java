
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Librarian {
    public final int userId;
    public final String staffId, name, approvalStatus, dateHired;

    public Librarian(int userId, String staffId, String name, String approvalStatus, String dateHired) {
        this.userId = userId;
        this.staffId = staffId;
        this.name = name;
        this.approvalStatus = approvalStatus;
        this.dateHired = dateHired;
    }

    public static ArrayList<Librarian> getLibrariansFromResultSet(ResultSet rs) {
        ArrayList<Librarian> librarians = new ArrayList<>();
        try {
            while (rs.next()) {
                librarians.add(new Librarian(
                    rs.getInt("user_id"),
                    rs.getString("staff_id"),
                    rs.getString("name"),
                    rs.getString("approval_status"),
                    rs.getString("date_hired")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing librarians");
        }
        return librarians;
    }

    public static Optional<Librarian> getFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(new Librarian(
                    rs.getInt("user_id"),
                    rs.getString("staff_id"),
                    rs.getString("name"),
                    rs.getString("approval_status"),
                    rs.getString("date_hired")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing librarian");
        }
        return Optional.empty();
    }
}
