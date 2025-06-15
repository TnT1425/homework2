package com.example.homework2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shopping_cart.db"; // Tên database có thể đổi thành "app_data.db"
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột cho Cart
    public static final String TABLE_CART = "cart_items";
    public static final String COLUMN_ID = "_id"; // Bắt buộc phải là _id cho CursorAdapter
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";

    // Tên bảng và các cột cho Favorites (MỚI)
    public static final String TABLE_FAVORITES = "favorite_items";
    // COLUMN_ID, COLUMN_PRODUCT_ID, COLUMN_TITLE, COLUMN_IMAGE_URL, COLUMN_PRICE sẽ được dùng chung
    public static final String COLUMN_FAV_SIZE = "size_col"; // Cột mới cho Favorite
    public static final String COLUMN_FAV_QUANTITY = "quantity_col"; // Cột mới cho Favorite (mặc định 1)
    public static final String COLUMN_FAV_COLOR = "color_col"; // Cột mới cho Favorite

    // Câu lệnh tạo bảng CART (giữ nguyên)
    private static final String TABLE_CREATE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_ID + " INTEGER NOT NULL UNIQUE, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL);";

    // Câu lệnh tạo bảng FAVORITES (MỚI)
    private static final String TABLE_CREATE_FAVORITES =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_ID + " INTEGER NOT NULL UNIQUE, " + // UNIQUE để tránh thêm cùng sản phẩm 2 lần
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_FAV_SIZE + " TEXT, " +
                    COLUMN_FAV_QUANTITY + " INTEGER, " +
                    COLUMN_FAV_COLOR + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_CART); // Tạo bảng giỏ hàng
        db.execSQL(TABLE_CREATE_FAVORITES); // Tạo bảng yêu thích
        Log.d("DatabaseHelper", "Tables " + TABLE_CART + " and " + TABLE_FAVORITES + " created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES); // Xóa bảng yêu thích khi upgrade
        onCreate(db);
    }
}