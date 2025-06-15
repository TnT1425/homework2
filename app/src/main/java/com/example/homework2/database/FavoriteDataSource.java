package com.example.homework2.database; // Thay đổi gói thành database

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.homework2.models.FavoriteItem; // Import FavoriteItem model

import java.util.ArrayList;
import java.util.List;

public class FavoriteDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PRODUCT_ID,
            DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_IMAGE_URL,
            DatabaseHelper.COLUMN_PRICE,
            DatabaseHelper.COLUMN_FAV_SIZE, // Sử dụng hằng số
            DatabaseHelper.COLUMN_FAV_QUANTITY, // Sử dụng hằng số
            DatabaseHelper.COLUMN_FAV_COLOR // Sử dụng hằng số
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
        // Lưu ý: Với UNIQUE trên product_id trong DB, chúng ta chỉ cho phép 1 item/product_id
        // Nếu bạn muốn favorite nhiều biến thể (size/color) của cùng 1 product_id,
        // bạn cần thay đổi ràng buộc UNIQUE trong DatabaseHelper
        FavoriteItem existingItem = getFavoriteItemByProductId(item.getProductId());
        if (existingItem != null) {
            Log.d("FavoriteDataSource", "Product already in favorites: " + item.getTitle());
            // Bạn có thể chọn cập nhật các trường khác ở đây nếu muốn
            // Ví dụ: updateFavoriteItem(item);
            return existingItem;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_ID, item.getProductId());
        values.put(DatabaseHelper.COLUMN_TITLE, item.getTitle());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, item.getImageUrl());
        values.put(DatabaseHelper.COLUMN_PRICE, item.getPrice());
        values.put(DatabaseHelper.COLUMN_FAV_SIZE, item.getSize()); // Sử dụng hằng số
        values.put(DatabaseHelper.COLUMN_FAV_QUANTITY, item.getQuantity()); // Sử dụng hằng số
        values.put(DatabaseHelper.COLUMN_FAV_COLOR, item.getColor()); // Sử dụng hằng số


        long insertId = database.insert(DatabaseHelper.TABLE_FAVORITES, null, values); // Đã sửa thành TABLE_FAVORITES
        Log.d("FavoriteDataSource", "Added new favorite item with ID: " + insertId + " for product ID: " + item.getProductId());

        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, // Đã sửa thành TABLE_FAVORITES
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FavoriteItem newFavoriteItem = cursorToFavoriteItem(cursor);
        cursor.close();
        return newFavoriteItem;
    }

    // Xóa một sản phẩm khỏi danh sách yêu thích
    public void deleteFavoriteItem(int productId) {
        database.delete(DatabaseHelper.TABLE_FAVORITES, // Đã sửa thành TABLE_FAVORITES
                DatabaseHelper.COLUMN_PRODUCT_ID + " = " + productId, null);
        Log.d("FavoriteDataSource", "Deleted favorite item for product ID: " + productId);
    }

    // Lấy tất cả sản phẩm yêu thích
    public List<FavoriteItem> getAllFavoriteItems() {
        List<FavoriteItem> favoriteItems = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, // Đã sửa thành TABLE_FAVORITES
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
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, // Đã sửa thành TABLE_FAVORITES
                allColumns, DatabaseHelper.COLUMN_PRODUCT_ID + " = " + productId, null,
                null, null, null);
        FavoriteItem favoriteItem = null;
        if (cursor != null && cursor.moveToFirst()) { // Kiểm tra cursor != null và di chuyển đến dòng đầu tiên
            favoriteItem = cursorToFavoriteItem(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return favoriteItem;
    }

    // Helper để chuyển đổi Cursor thành đối tượng FavoriteItem
    private FavoriteItem cursorToFavoriteItem(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE_URL));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
        String size = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_SIZE)); // Sử dụng hằng số
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_QUANTITY)); // Sử dụng hằng số
        String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FAV_COLOR)); // Sử dụng hằng số

        return new FavoriteItem(id, productId, title, imageUrl, price, size, quantity, color);
    }

    // Bạn có thể thêm các phương thức khác như:
    // public void updateFavoriteItem(FavoriteItem item) { ... }
    // public boolean isFavorite(int productId) { ... } // Kiểm tra sản phẩm có phải là yêu thích không
}