package com.example.homework2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback; // Import này cần thiết

import com.example.homework2.interfaces.ProductPresenterImpl;
import com.example.homework2.interfaces.ProductViewImpl;
import com.example.homework2.models.AllProductResponse;
import com.example.homework2.models.Product;
import com.example.homework2.presenters.ProductPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity implements ProductViewImpl {

    private static final String TAG = "ProductDetailActivity";
    private ImageView productImage;
    private TextView productTitle, productPrice, productDes;
    private ImageButton btnBack;
    private Button btnAddToCartIcon;
    private Button btnAddToCartMain;
    private Product mProduct;
    private int productId;
    private CheckBox favoriteCheckBox;
    private ProductPresenterImpl productPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details); // Đảm bảo bạn đang sử dụng product_details.xml

        productPresenter = new ProductPresenter(this);

        // Khởi tạo các View
        productTitle = findViewById(R.id.tvTitle);
        productPrice = findViewById(R.id.tvPrice);
        productDes = findViewById(R.id.tvDescription);
        productImage = findViewById(R.id.ivImage);
        favoriteCheckBox = findViewById(R.id.favorite_checkbox);

        // Khởi tạo và thiết lập Listener cho nút Back sử dụng OnBackPressedCallback
        btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Kích hoạt onBackPressedDispatcher để xử lý việc quay lại
                    getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        // Đăng ký callback cho nút back của hệ thống
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Đây là nơi bạn có thể thêm logic tùy chỉnh trước khi quay lại
                // Ví dụ: kiểm tra xem có thay đổi chưa lưu không
                // Sau đó, gọi super.onBackPressed() để thực hiện hành vi mặc định
                // Hoặc nếu bạn muốn chặn back, không gọi gì cả
                finish(); // Đơn giản là kết thúc Activity này
            }
        });


        // Khởi tạo và thiết lập Listener cho nút "Add to Cart" (icon)
        btnAddToCartIcon = findViewById(R.id.btn_add_to_cart_icon);
        if (btnAddToCartIcon != null) {
            btnAddToCartIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mProduct != null) {
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm " + mProduct.getTitle() + " vào giỏ hàng (qua icon)!", Toast.LENGTH_SHORT).show();
                        // TODO: Thêm logic thực tế để thêm sản phẩm vào giỏ hàng của bạn
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Không có sản phẩm để thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Khởi tạo và thiết lập Listener cho nút "Add to Cart" (chính)
        btnAddToCartMain = findViewById(R.id.btn_check_out);
        if (btnAddToCartMain != null) {
            btnAddToCartMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mProduct != null) {
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm " + mProduct.getTitle() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        // TODO: Thêm logic thực tế để thêm sản phẩm vào giỏ hàng của bạn
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Không có sản phẩm để thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Listener cho CheckBox yêu thích
        if (favoriteCheckBox != null) {
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mProduct != null) {
                        mProduct.setIsFavorite(isChecked);
                        Log.d(TAG, "Product: " + mProduct.getTitle() + " ,Favorite status change to: " + isChecked);
                        Toast.makeText(ProductDetailActivity.this,
                                mProduct.getTitle() + (isChecked ? " đã được yêu thích!" : " đã bỏ yêu thích"),
                                Toast.LENGTH_SHORT).show();
                        // TODO: Cập nhật trạng thái yêu thích vào cơ sở dữ liệu nếu có
                    }
                }
            });
        }


        // Logic tải dữ liệu sản phẩm theo ID
        if ((getIntent() != null)) {
            productId = getIntent().getIntExtra("productId", -1);
            if (productId != -1) {
                productPresenter.getProductById(productId);
            } else {
                Toast.makeText(this, "Không tìm thấy ID sản phẩm.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Không có dữ liệu sản phẩm.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void getAllProductSuccess(AllProductResponse response) { }

    @Override
    public void getAllProductFailed(String code, String message) { }

    @Override
    public void searchProductSuccess(AllProductResponse response) { }

    @Override
    public void searchProductFailed(String code, String message) { }

    @Override
    public void getProductByIdSuccess(Product product) {
        if (product != null) {
            mProduct = product;
            productTitle.setText(product.getTitle());
            productPrice.setText(String.format("$%.2f", product.getPrice()));
            productDes.setText(product.getDescription());

            if (product.getThumbnail() != null && !product.getThumbnail().isEmpty()) {
                Picasso.get().load(product.getThumbnail()).into(productImage);
            } else if (product.getImages() != null && !product.getImages().isEmpty()) {
                Picasso.get().load(product.getImages().get(0)).into(productImage);
            } else {
                productImage.setImageResource(R.drawable.img_product);
            }
            favoriteCheckBox.setChecked(product.isFavorite());
        } else {
            Toast.makeText(this, "Không tìm thấy chi tiết sản phẩm.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void getProductByIdFailed(String code, String message) {
        Log.e(TAG, "Failed to fetch product details: " + code + " - " + message);
        Toast.makeText(this, "Lỗi khi tải chi tiết sản phẩm: " + message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAllCategoriesSuccess(List<String> categories) { }

    @Override
    public void onAllCategoriesFailed(String code, String message) { }
}