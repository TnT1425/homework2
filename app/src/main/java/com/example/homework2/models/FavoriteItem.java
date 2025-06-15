package com.example.homework2.models;

// Đây là một lớp FavoriteItem mẫu, bạn có thể điều chỉnh cho phù hợp
public class FavoriteItem {
    private int id; // ID cục bộ trong DB
    private int productId; // ID của sản phẩm từ API
    private String title;
    private String imageUrl;
    private double price;
    private String size; // Thêm trường size
    private int quantity; // Thêm trường quantity
    private String color; // Thêm trường color

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String thumbnail;


    // Constructor đầy đủ
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

    // Constructor khi thêm mới (ID chưa có)
    public FavoriteItem(int productId, String title, String imageUrl, double price, String size, int quantity, String color) {
        this.productId = productId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.color = color;
    }

    // Constructor mặc định (nếu cần)
    public FavoriteItem() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getColor() {
        return color;
    }

    // Setters (nếu bạn muốn thay đổi giá trị sau khi tạo đối tượng)
    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setColor(String color) {
        this.color = color;
    }
}