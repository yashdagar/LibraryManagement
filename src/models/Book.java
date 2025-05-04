package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Book {
    public final int bookId, categoryId, totalCopies, availableCopies, addedBy;
    public final String isbn, title, author, publisher, shelfLocation;
    public final int publicationYear;
    public final String addedOn;

    public Book(int bookId, String isbn, String title, String author, String publisher, int publicationYear,
                int categoryId, int totalCopies, int availableCopies, String shelfLocation,
                int addedBy, String addedOn) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.categoryId = categoryId;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.shelfLocation = shelfLocation;
        this.addedBy = addedBy;
        this.addedOn = addedOn;
    }

    public static ArrayList<Book> getBooksFromResultSet(ResultSet rs) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("publication_year"),
                        rs.getInt("category_id"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies"),
                        rs.getString("shelf_location"),
                        rs.getInt("added_by"),
                        rs.getString("added_on")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing books");
        }
        return books;
    }

    public static Optional<Book> getFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(new Book(
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("publication_year"),
                        rs.getInt("category_id"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies"),
                        rs.getString("shelf_location"),
                        rs.getInt("added_by"),
                        rs.getString("added_on")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing book");
        }
        return Optional.empty();
    }
}
