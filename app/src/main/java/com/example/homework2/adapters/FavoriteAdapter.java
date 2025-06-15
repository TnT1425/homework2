package com.example.homework2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homework2.R;
import com.example.homework2.models.Product;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Product> favoriteItems;
    private OnFavoriteItemActionListener listener;

    public interface OnFavoriteItemActionListener {
        void onRemoveFavorite(Product item); // Hoặc FavoriteItem item
        // Có thể thêm onFavoriteItemClick nếu muốn mở chi tiết sản phẩm
    }

    public FavoriteAdapter(List<Product> favoriteItems, OnFavoriteItemActionListener listener) {
        this.favoriteItems = favoriteItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product item = favoriteItems.get(position); // Hoặc FavoriteItem
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    public void updateFavorites(List<Product> newFavoriteItems) { // Hoặc List<FavoriteItem>
        this.favoriteItems = newFavoriteItems;
        notifyDataSetChanged();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemSize;
        TextView itemQuantity;
        TextView itemColor;
        TextView itemPrice;
        ImageButton removeButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.favorite_item_image);
            itemTitle = itemView.findViewById(R.id.favorite_item_title);
            itemSize = itemView.findViewById(R.id.favorite_item_size);
            itemQuantity = itemView.findViewById(R.id.favorite_item_quantity);
            itemColor = itemView.findViewById(R.id.favorite_item_color);
            itemPrice = itemView.findViewById(R.id.favorite_item_price);
            removeButton = itemView.findViewById(R.id.remove_favorite_button);
        }

        public void bind(Product item) { // Hoặc FavoriteItem item
            itemTitle.setText(item.getTitle());
            itemPrice.setText(String.format("$%.2f", item.getPrice()));

            // Lưu ý: Các trường size, quantity, color không có sẵn trong Product model từ DummyJSON
            // Bạn cần thêm chúng vào Product.java hoặc tạo một FavoriteItem model riêng
            // Ví dụ tạm thời:
            itemSize.setText("Size: M (Temp)");
            itemQuantity.setText("Quantity: 01 (Temp)");
            itemColor.setText("Color: Black (Temp)");


            Glide.with(itemView.getContext())
                    .load(item.getThumbnail()) // Hoặc item.getImageUrl() nếu dùng FavoriteItem
                    .placeholder(R.drawable.img_product)
                    .error(R.drawable.img_product)
                    .into(itemImage);

            removeButton.setOnClickListener(v -> {
                listener.onRemoveFavorite(item);
            });
            // Thêm click listener cho toàn bộ item nếu muốn mở chi tiết sản phẩm
            itemView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Mở chi tiết: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                // TODO: Gọi listener.onFavoriteItemClick(item); và triển khai trong FavoritesActivity
            });
        }
    }
}