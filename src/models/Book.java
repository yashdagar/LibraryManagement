package models;

public class Book {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private int quantity;
    private String author;
    private String category;
    private String publicationYear;
    private boolean available;

    public Book(String name, String description, String imageUrl, int quantity, String author, String category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.author = author;
        this.category = category;
        this.available = quantity > 0;
    }

    // Additional constructor that includes id and publication year
    public Book(int id, String name, String description, String imageUrl, int quantity,
                String author, String category, String publicationYear) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.author = author;
        this.category = category;
        this.publicationYear = publicationYear;
        this.available = quantity > 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
            available = quantity > 0;
        }
    }

    public void incrementQuantity() {
        quantity++;
        available = true;
    }
}