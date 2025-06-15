package com.example.homework2.adapters; // Đảm bảo đúng package

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.R;
import com.example.homework2.models.Product; // Đảm bảo đúng package models
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductGridViewHolder> {

    private List<Product> productList;
    private OnProductClickListener productClickListener;
    private OnFavoriteClickListener favoriteClickListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Product product, boolean isFavorite);
    }

    public ProductGridAdapter(List<Product> productList, OnProductClickListener productClickListener, OnFavoriteClickListener favoriteClickListener) {
        this.productList = productList;
        this.productClickListener = productClickListener;
        this.favoriteClickListener = favoriteClickListener;
    }

    public ProductGridAdapter(List<Product> productList, OnProductClickListener productClickListener) {
        this.productList = productList;
        this.productClickListener = productClickListener;
        this.favoriteClickListener = null;
    }

    public void updateProducts(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
        return new ProductGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductGridViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productNameTextView.setText(product.getTitle());
        holder.productPriceTextView.setText(String.format("$%.2f", product.getPrice()));

        // Tải hình ảnh sản phẩm bằng Picasso
        if (product.getThumbnail() != null && !product.getThumbnail().isEmpty()) {
            Picasso.get().load(product.getThumbnail()).into(holder.productImageView);
        } else {
            holder.productImageView.setImageResource(R.drawable.img_product); // Ảnh placeholder
        }

        // Tải logo thương hiệu (nếu có, DummyJson không có logo cụ thể, dùng placeholder)
        holder.brandLogoImageView.setImageResource(R.drawable.ic_brand); // Ảnh placeholder

        // Đặt trạng thái ban đầu của CheckBox yêu thích
        holder.favoriteCheckbox.setOnCheckedChangeListener(null); // Gỡ listener cũ để tránh tái sử dụng viewholder
        holder.favoriteCheckbox.setChecked(product.isFavorite()); // Cập nhật trạng thái từ model

        // Đặt lắng nghe sự kiện thay đổi trạng thái của CheckBox
        holder.favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Cập nhật trạng thái trong model
                product.setIsFavorite(isChecked);
                // Gọi listener nếu có
                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(product, isChecked);
                }
                // Optional: Hiển thị Toast
                // Toast.makeText(buttonView.getContext(),
                //         product.getTitle() + (isChecked ? " đã được thêm vào yêu thích" : " đã bị xóa khỏi yêu thích"),
                //         Toast.LENGTH_SHORT).show();
            }
        });

        // Thiết lập sự kiện click cho toàn bộ item sản phẩm
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productClickListener != null) {
                    productClickListener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductGridViewHolder extends RecyclerView.ViewHolder {
        ImageView brandLogoImageView;
        CheckBox favoriteCheckbox;
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;

        public ProductGridViewHolder(@NonNull View itemView) {
            super(itemView);
            brandLogoImageView = itemView.findViewById(R.id.brand_logo);
            favoriteCheckbox = itemView.findViewById(R.id.favorite_checkbox);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
        }
    }
}