package com.example.homework2.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.homework2.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper; // Dùng lại DatabaseHelper đã tạo
    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PRODUCT_ID,
            DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_IMAGE_URL,
            DatabaseHelper.COLUMN_PRICE,
            // Thêm các cột cho size, quantity, color nếu dùng FavoriteItem riêng
            "size_col", // Tên cột trong DB cho size
            "quantity_col", // Tên cột trong DB cho quantity
            "color_col" // Tên cột trong DB cho color
    };

    public FavoriteDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.d("FavoriteDataSource", "Database opened.");
    }

    public void close() {
        dbHelper.close();
        Log.d("FavoriteDataSource", "Database closed.");
    }

    // Thêm sản phẩm vào danh sách yêu thích
    public FavoriteItem addFavoriteItem(FavoriteItem item) {
        // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
        FavoriteItem existingItem = getFavoriteItemByProductId(item.getProductId());
        if (existingItem != null) {
            Log.d("FavoriteDataSource", "Product already in favorites: " + item.getTitle());
            return existingItem; // Không thêm lại
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_ID, item.getProductId());
        values.put(DatabaseHelper.COLUMN_TITLE, item.getTitle());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, item.getImageUrl());
        values.put(DatabaseHelper.COLUMN_PRICE, item.getPrice());
        // Thêm các trường khác nếu dùng FavoriteItem riêng
        values.put("size_col", item.getSize());
        values.put("quantity_col", item.getQuantity());
        values.put("color_col", item.getColor());


        long insertId = database.insert(DatabaseHelper.TABLE_CART, null, values); // Sử dụng lại TABLE_CART hoặc tạo TABLE_FAVORITES
        Log.d("FavoriteDataSource", "Added new favorite item with ID: " + insertId + " for product ID: " + item.getProductId());

        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, // Sử dụng TABLE_CART hoặc TABLE_FAVORITES
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FavoriteItem newFavoriteItem = cursorToFavoriteItem(cursor);
        cursor.close();
        return newFavoriteItem;
    }

    // Xóa một sản phẩm khỏi danh sách yêu thích
    public void deleteFavoriteItem(int productId) {
        database.delete(DatabaseHelper.TABLE_CART, // Sử dụng TABLE_CART hoặc TABLE_FAVORITES
                DatabaseHelper.COLUMN_PRODUCT_ID + " = " + productId, null);
        Log.d("FavoriteDataSource", "Deleted favorite item for product ID: " + productId);
    }

    // Lấy tất cả sản phẩm yêu thích
    public List<FavoriteItem> getAllFavoriteItems() { // Hoặc List<Product>
        List<FavoriteItem> favoriteItems = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, // Sử dụng TABLE_CART hoặc TABLE_FAVORITES
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FavoriteItem favoriteItem = cursorToFavoriteItem(cursor);
            favoriteItems.add(favoriteItem);
            cursor.moveToNext();
        }
        cursor.close();
        return favoriteItems;
    }

    // Lấy một FavoriteItem theo Product ID
    public FavoriteItem getFavoriteItemByProductId(int productId) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, // Sử dụng TABLE_CART hoặc TABLE_FAVORITES
                allColumns, DatabaseHelper.COLUMN_PRODUCT_ID + " = " + productId, null,
                null, null, null);
        cursor.moveToFirst();
        FavoriteItem favoriteItem = null;
        if (!cursor.isAfterLast()) {
            favoriteItem = cursorToFavoriteItem(cursor);
        }
        cursor.close();
        return favoriteItem;
    }

    // Helper để chuyển đổi Cursor thành đối tượng FavoriteItem
    private FavoriteItem cursorToFavoriteItem(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
        // Lấy các trường mới nếu dùng FavoriteItem riêng
        String size = cursor.getString(cursor.getColumnIndexOrThrow("size_col"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity_col"));
        String color = cursor.getString(cursor.getColumnIndexOrThrow("color_col"));

        return new FavoriteItem(id, productId, title, imageUrl, price, size, quantity, color);
    }
}