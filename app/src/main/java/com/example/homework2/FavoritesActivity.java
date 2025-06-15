package com.example.homework2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.adapters.FavoriteAdapter; // Import FavoriteAdapter của bạn
import com.example.homework2.database.FavoriteDataSource;
import com.example.homework2.models.FavoriteItem; // Đảm bảo import FavoriteItem model
import com.example.homework2.models.Product;
// import com.example.homework2.models.Product; // Không cần thiết nếu bạn dùng FavoriteItem

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.OnFavoriteItemActionListener {

    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<FavoriteItem> favoriteItems; // Đã đổi từ Product sang FavoriteItem
    private ImageButton backButton;

    // Khai báo các ImageView cho Bottom Nav Bar
    private ImageView bottomNavHomeIcon;
    private ImageView bottomNavFavoritesIcon;
    private ImageView bottomNavNotificationsIcon;
    private ImageView bottomNavProfileIcon;

    private FavoriteDataSource favoriteDataSource;

    private static final String TAG = "FavoritesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites); // Đảm bảo bạn đã có layout này

        // Ánh xạ View
        backButton = findViewById(R.id.back_button);
        favoriteRecyclerView = findViewById(R.id.favorite_items_recycler_view);

        // Ánh xạ các icon Bottom Nav Bar
        bottomNavHomeIcon = findViewById(R.id.bottom_nav_home_icon); // ID từ activity_main.xml
        bottomNavFavoritesIcon = findViewById(R.id.bottom_nav_favorites_icon); // ID từ activity_main.xml
        // Lưu ý: Các ID này có thể cần được điều chỉnh nếu layout của activity_favorites khác activity_main
        // Nếu activity_favorites có bottom nav bar riêng, các ID này phải là của layout đó.
        bottomNavNotificationsIcon = findViewById(R.id.bottom_nav_notifications_icon); // Giả định ID
        bottomNavProfileIcon = findViewById(R.id.bottom_nav_profile_icon); // Giả định ID

        // Khởi tạo FavoriteDataSource
        favoriteDataSource = new FavoriteDataSource(this);

        favoriteItems = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(favoriteItems, this); // 'this' là listener cho Adapter
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        // Thiết lập Listener cho nút Back
        backButton.setOnClickListener(v -> finish()); // Quay lại màn hình trước đó

        // Thiết lập OnClickListener cho Bottom Navigation Bar
        setupBottomNavListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoriteDataSource.open();
        loadFavoriteItems(); // Tải lại dữ liệu yêu thích mỗi khi Activity được resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        favoriteDataSource.close();
    }

    private void setupBottomNavListeners() {
        bottomNavHomeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng FavoritesActivity để tránh tạo nhiều instance của MainActivity
        });

        bottomNavFavoritesIcon.setOnClickListener(v -> {
            // Đã ở FavoritesActivity, có thể refresh hoặc không làm gì
            Toast.makeText(FavoritesActivity.this, "Bạn đang ở trang Favorites", Toast.LENGTH_SHORT).show();
            // Nếu bạn muốn refresh khi click vào chính nó:
            // loadFavoriteItems();
        });

        bottomNavNotificationsIcon.setOnClickListener(v -> {
            Toast.makeText(FavoritesActivity.this, "Mở màn hình Notifications", Toast.LENGTH_SHORT).show();
            // TODO: Triển khai Navigation tới NotificationsActivity
            // Intent intent = new Intent(FavoritesActivity.this, NotificationsActivity.class);
            // startActivity(intent);
        });

        bottomNavProfileIcon.setOnClickListener(v -> {
            Toast.makeText(FavoritesActivity.this, "Mở màn hình Profile", Toast.LENGTH_SHORT).show();
            // TODO: Triển khai Navigation tới ProfileActivity
            // Intent intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
            // startActivity(intent);
        });
    }

    private void loadFavoriteItems() {
        Log.d(TAG, "Loading favorite items...");
        List<FavoriteItem> items = favoriteDataSource.getAllFavoriteItems(); // Lấy dữ liệu từ DB
        favoriteItems.clear();
        if (items != null) {
            favoriteItems.addAll(items);
            Log.d(TAG, "Loaded " + favoriteItems.size() + " items from database.");
        } else {
            Log.d(TAG, "No items found in database.");
        }
        favoriteAdapter.updateFavorites(favoriteItems); // Cập nhật Adapter
        if (favoriteItems.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm yêu thích nào.", Toast.LENGTH_SHORT).show();
        }
    }

    // --- Triển khai các phương thức từ FavoriteAdapter.OnFavoriteItemActionListener ---

    @Override
    public void onRemoveFavorite(FavoriteItem item) { // Đã sửa tham số từ Product sang FavoriteItem
        Log.d(TAG, "Attempting to remove favorite: " + item.getTitle() + " (Product ID: " + item.getProductId() + ")");
        favoriteDataSource.deleteFavoriteItem(item.getProductId()); // Xóa khỏi DB
        Toast.makeText(this, item.getTitle() + " đã được xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
        // Sau khi xóa khỏi DB, tải lại danh sách để UI được cập nhật
        loadFavoriteItems();
    }
}