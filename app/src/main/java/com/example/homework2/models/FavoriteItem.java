package com.example.homework2.models; // Hoặc com.example.homework2.database.models;

public class FavoriteItem {
    private int id; // ID duy nhất cho mỗi mục yêu thích trong DB
    private int productId; // ID của sản phẩm gốc (tham chiếu đến Product)
    private String title;
    private String imageUrl; // Thumbnail của sản phẩm
    private double price;
    private String size; // Thêm trường size
    private int quantity; // Thêm trường quantity (thường là 1 cho favorite)
    private String color; // Thêm trường color

    // Constructor khi thêm mới
    public FavoriteItem(int productId, String title, String imageUrl, double price, String size, int quantity, String color) {
        this.productId = productId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.color = color;
    }

    // Constructor khi lấy từ database (có ID)
    public FavoriteItem(int id, int productId, String title, String imageUrl, double price, String size, int quantity, String color) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.color = color;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return "FavoriteItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", color='" + color + '\'' +
                '}';
    }
}