package com.example.homework2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.adapters.FavoriteAdapter;
import com.example.homework2.models.FavoriteDataSource;
import com.example.homework2.models.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.OnFavoriteItemActionListener {

    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Product> favoriteItems; // Hoặc List<FavoriteItem> nếu bạn tạo model riêng
    private ImageButton backButton;

    // Khai báo các ImageView cho Bottom Nav Bar
    private ImageView bottomNavHomeIcon;
    private ImageView bottomNavFavoritesIcon;
    private ImageView bottomNavNotificationsIcon;
    private ImageView bottomNavProfileIcon;

    private FavoriteDataSource favoriteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites); // Đảm bảo bạn đã có layout này

        // Ánh xạ View
        backButton = findViewById(R.id.back_button);
        favoriteRecyclerView = findViewById(R.id.favorite_items_recycler_view);

        // Ánh xạ các icon Bottom Nav Bar
        bottomNavHomeIcon = findViewById(R.id.bottom_nav_home_icon);
        bottomNavFavoritesIcon = findViewById(R.id.bottom_nav_favorites_icon);
        bottomNavNotificationsIcon = findViewById(R.id.bottom_nav_notifications_icon);
        bottomNavProfileIcon = findViewById(R.id.bottom_nav_profile_icon);


        // Khởi tạo FavoriteDataSource (bạn cần tạo lớp này và model FavoriteItem)
        favoriteDataSource = new FavoriteDataSource(this); // Giả sử bạn tạo FavoriteDataSource tương tự CartDataSource

        favoriteItems = new ArrayList<>();
        // TODO: Thay thế FavoriteAdapter bằng adapter thực tế khi bạn tạo nó
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
        });

        bottomNavNotificationsIcon.setOnClickListener(v -> {
            Toast.makeText(FavoritesActivity.this, "Mở màn hình Notifications", Toast.LENGTH_SHORT).show();
            // TODO: Triển khai Navigation tới NotificationsActivity
        });

        bottomNavProfileIcon.setOnClickListener(v -> {
            Toast.makeText(FavoritesActivity.this, "Mở màn hình Profile", Toast.LENGTH_SHORT).show();
            // TODO: Triển khai Navigation tới ProfileActivity
        });
    }

    private void loadFavoriteItems() {
        // Đây là nơi bạn sẽ lấy dữ liệu từ FavoriteDataSource
        // Ví dụ: List<Product> items = favoriteDataSource.getAllFavoriteProducts();
        // favoriteItems.clear();
        // favoriteItems.addAll(items);
        // favoriteAdapter.updateFavorites(favoriteItems); // Cần phương thức này trong FavoriteAdapter
        // Nếu chưa có FavoriteDataSource và FavoriteItem model, bạn có thể tạo dữ liệu giả để kiểm tra layout trước
        if (favoriteItems.isEmpty()) { // Chỉ thêm giả định nếu danh sách trống
            for (int i = 0; i < 5; i++) {
                Product dummyProduct = new Product();
                dummyProduct.setId(100 + i);
                dummyProduct.setTitle("Dummy Product " + (i + 1));
                dummyProduct.setPrice(19.99 + i);
                dummyProduct.setThumbnail("https://i.dummyjson.com/data/products/2/thumbnail.jpg"); // Ảnh mẫu
                // Bạn cần thêm các trường size, quantity, color vào Product hoặc FavoriteItem model nếu muốn hiển thị
                favoriteItems.add(dummyProduct);
            }
            favoriteAdapter.updateFavorites(favoriteItems);
            Toast.makeText(this, "Tải sản phẩm yêu thích giả định.", Toast.LENGTH_SHORT).show();
        }

    }

    // --- Triển khai các phương thức từ FavoriteAdapter.OnFavoriteItemActionListener ---
    // (Bạn sẽ định nghĩa các phương thức này trong FavoriteAdapter của mình)
    @Override
    public void onRemoveFavorite(Product item) { // Hoặc FavoriteItem item
        Toast.makeText(this, item.getTitle() + " đã được xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
        // favoriteDataSource.deleteFavoriteItem(item.getProductId()); // Giả định có phương thức này
        favoriteItems.remove(item);
        favoriteAdapter.updateFavorites(favoriteItems);
    }
}