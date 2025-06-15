package com.example.homework2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button btnAddToCart;
    private Product mProduct;
    private int productId;
    private CheckBox checkBox;
    private ProductPresenterImpl productPresenter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_product_details);

        productPresenter = new ProductPresenter(this);

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

        productTitle = findViewById(R.id.tvTitle);
        productPrice = findViewById(R.id.tvPrice);
        productDes = findViewById(R.id.tvDescription);
        productImage = findViewById(R.id.ivImage);
        checkBox = findViewById(R.id.favorite_checkbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mProduct != null) {
                    mProduct.setIsFavorite(isChecked);
                    Log.d(TAG, "Product: " + mProduct.getTitle() + " ,Favorite status change to: " + isChecked);
                    Toast.makeText(ProductDetailActivity.this,
                            mProduct.getTitle() + (isChecked ? "đã được yêu thích!" : "đã bỏ yêu thích"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void getAllProductSuccess(AllProductResponse response) {

    }

    @Override
    public void getAllProductFailed(String code, String message) {

    }

    @Override
    public void searchProductSuccess(AllProductResponse response) {

    }

    @Override
    public void searchProductFailed(String code, String message) {

    }

    @Override
    public void getProductByIdSuccess(Product product) {
        if (product!=null){
            mProduct=product;
            productTitle.setText(product.getTitle());
            productPrice.setText(String.valueOf(product.getPrice()));
            productDes.setText(product.getDescription());

            if (product.getThumbnail()!=null&&!product.getThumbnail().isEmpty()){
                Picasso.get().load(product.getThumbnail()).into(productImage);
            }else if (product.getImages() != null && !product.getImages().isEmpty()) {
                Picasso.get().load(product.getImages().get(0)).into(productImage); // Sử dụng ảnh đầu tiên nếu thumbnail thiếu
            } else {
                productImage.setImageResource(R.drawable.img_product); // Ảnh placeholder
            }
            checkBox.setChecked(product.isFavorite());
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
    public void onAllCategoriesSuccess(List<String> categories) {

    }

    @Override
    public void onAllCategoriesFailed(String code, String message) {

    }
}
