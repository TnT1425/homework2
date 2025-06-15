package com.example.homework2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.adapters.CategoryAdapter;
import com.example.homework2.adapters.ProductGridAdapter;
import com.example.homework2.api.ProductApi;
import com.example.homework2.interfaces.ProductPresenterImpl;
import com.example.homework2.interfaces.ProductViewImpl;
import com.example.homework2.models.AllProductResponse;
import com.example.homework2.database.FavoriteDataSource;
import com.example.homework2.models.FavoriteItem;
import com.example.homework2.models.Product;
import com.example.homework2.presenters.ProductPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductViewImpl,
        ProductGridAdapter.OnProductClickListener,
        ProductGridAdapter.OnFavoriteClickListener {


    private RecyclerView productGridRecyclerView;
    private ProductGridAdapter productGridAdapter;
    private EditText searchEditText;

    private ProductPresenterImpl productPresenter;

    private List<Product> currentProductList;

    private FavoriteDataSource favoriteDataSource;

    private static final String TAG = "HomeActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private final long DEBOUNCE_DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productPresenter = new ProductPresenter(this);
        favoriteDataSource = new FavoriteDataSource(this);
        ImageView navFavoriteIcon = findViewById(R.id.nav_favorite_icon);
        FrameLayout cartButtonContainer = findViewById(R.id.cart_button_container);

        if (navFavoriteIcon != null) {
            navFavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển đến FavoriteListActivity
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "Navigating to FavoriteListActivity");
                }
            });
        }

        if (cartButtonContainer != null) {
            cartButtonContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển đến CartActivity (Bạn cần tạo CartActivity tương tự FavoriteListActivity)
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "Navigating to CartActivity");
                }
            });
        }

        productGridRecyclerView = findViewById(R.id.product_grid_recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);

        currentProductList = new ArrayList<>(); // Khởi tạo danh sách rỗng, dữ liệu sẽ được tải từ API
        productGridAdapter = new ProductGridAdapter(currentProductList, this, this); // 'this' là 2 listener
        GridLayoutManager productGridLayoutManager = new GridLayoutManager(this, 2); // 2 cột
        productGridRecyclerView.setLayoutManager(productGridLayoutManager);
        productGridRecyclerView.setAdapter(productGridAdapter);

        productPresenter.getAllProduct();

        setupSearchListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoriteDataSource.open();
        syncFavoriteStatusWithUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        favoriteDataSource.close();
    }

    private void syncFavoriteStatusWithUI() {
        if (currentProductList != null && !currentProductList.isEmpty()) {
            List<FavoriteItem> favoriteItemsInDb = favoriteDataSource.getAllFavoriteItems();
            for (Product product : currentProductList) {
                boolean isCurrentlyFavorite = false;
                for (FavoriteItem favoriteItem:favoriteItemsInDb){
                    if (favoriteItem.getProductId()==product.getId())
                    {
                        isCurrentlyFavorite=true;break;
                    }
                }
                product.setIsFavorite(isCurrentlyFavorite);
            }
            productGridAdapter.updateProducts((currentProductList));
        }
    }

    private void setupSearchListener() {
        // Lắng nghe sự kiện khi người dùng nhấn nút 'Search' trên bàn phím ảo
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(searchEditText.getText().toString());
                    return true; // Đánh dấu sự kiện đã được xử lý
                }
                return false; // Để hệ thống xử lý các hành động khác
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không làm gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi văn bản đang thay đổi, xóa bỏ callback tìm kiếm cũ (nếu có)
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Sau khi văn bản đã thay đổi, đặt một callback mới để chạy sau DEBOUNCE_DELAY
                searchRunnable = () -> performSearch(s.toString());
                handler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
            }
        });

    }

    private void performSearch(String query) {
        query = query.trim(); // Loại bỏ khoảng trắng ở đầu và cuối

        // Ẩn bàn phím sau khi tìm kiếm
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        searchEditText.clearFocus(); // Xóa focus khỏi EditText

        if (query.isEmpty()) {
            Toast.makeText(this, "Bạn đã xóa từ khóa, hiển thị tất cả sản phẩm.", Toast.LENGTH_SHORT).show();
            productPresenter.getAllProduct(); // Nếu từ khóa trống, tải lại tất cả sản phẩm
        } else {
            Toast.makeText(this, "Đang tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
            productPresenter.searchProduct(query); // Gọi Presenter để thực hiện tìm kiếm
        }
    }

    @Override
    public void onFavoriteClick(Product product, boolean isFavorite) {
        if (isFavorite) {
            // Thêm vào favorites
            // Sử dụng các giá trị mặc định cho size, quantity, color vì Product model không có chúng
            FavoriteItem favoriteItem = new FavoriteItem(
                    product.getId(), // productId
                    product.getTitle(),
                    product.getThumbnail(),
                    product.getPrice(),
                    "M", // kích thước mặc định
                    1,   // số lượng mặc định
                    "Black" // màu sắc mặc định
            );
            favoriteDataSource.addFavoriteItem(favoriteItem);
            Toast.makeText(this, product.getTitle() + " đã được thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            // Xóa khỏi favorites
            favoriteDataSource.deleteFavoriteItem(product.getId());
            Toast.makeText(this, product.getTitle() + " đã được bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getId()); // Truyền ID sản phẩm
        startActivity(intent);
        Toast.makeText(this, "Mở chi tiết sản phẩm: " + product.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getAllProductSuccess(AllProductResponse response) {
        currentProductList.clear(); // Xóa danh sách cũ
        if (response != null && response.getProducts() != null) {
            // Khởi tạo trạng thái isFavorite cho các sản phẩm mới lấy về
            for (Product p : response.getProducts()) {
                p.setIsFavorite(false); // Mặc định là không yêu thích
            }
            currentProductList.addAll(response.getProducts()); // Thêm sản phẩm mới vào danh sách
        }
        productGridAdapter.updateProducts(currentProductList); // Cập nhật Adapter để hiển thị lên RecyclerView
        Log.d(TAG, "Fetched " + currentProductList.size() + " products.");
        if (currentProductList.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm nào để hiển thị.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getAllProductFailed(String code, String message) {
        Log.e(TAG, "Failed to fetch products: " + code + " - " + message);
        Toast.makeText(this, "Lỗi khi tải sản phẩm: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void searchProductSuccess(AllProductResponse response) {
        currentProductList.clear(); // Xóa kết quả cũ
        if (response != null && response.getProducts() != null) {
            for (Product p : response.getProducts()) {
                p.setIsFavorite(false); // Reset trạng thái yêu thích
            }
            currentProductList.addAll(response.getProducts()); // Thêm kết quả tìm kiếm vào
        }
        productGridAdapter.updateProducts(currentProductList); // Cập nhật RecyclerView

        if (currentProductList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sản phẩm nào với từ khóa này.", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Search results: " + currentProductList.size() + " products.");
    }

    @Override
    public void searchProductFailed(String code, String message) {
        Log.e(TAG, "Search failed: " + code + " - " + message);
        Toast.makeText(this, "Lỗi tìm kiếm: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getProductByIdSuccess(Product product) {

    }

    @Override
    public void getProductByIdFailed(String code, String message) {

    }

    @Override
    public void onAllCategoriesSuccess(List<String> categories) {

    }

    @Override
    public void onAllCategoriesFailed(String code, String message) {

    }
}