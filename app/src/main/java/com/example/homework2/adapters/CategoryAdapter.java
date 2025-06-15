package com.example.homework2.adapters; // Đảm bảo đúng package

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Vị trí danh mục đang được chọn

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName, int position);
    }

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String categoryName = categories.get(position);
        holder.categoryNameTextView.setText(categoryName);

        // Xử lý trạng thái được chọn/không được chọn cho background và text color
        if (position == selectedPosition) {
            holder.categoryNameTextView.setSelected(true); // Kích hoạt trạng thái 'selected' trong selector
            holder.categoryNameTextView.setTextColor(Color.WHITE); // Màu chữ khi được chọn
        } else {
            holder.categoryNameTextView.setSelected(false);
            holder.categoryNameTextView.setTextColor(Color.BLACK); // Màu chữ khi không được chọn
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int oldSelectedPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition(); // Cập nhật vị trí được chọn
                    notifyItemChanged(oldSelectedPosition); // Cập nhật item cũ
                    notifyItemChanged(selectedPosition); // Cập nhật item mới

                    listener.onCategoryClick(categoryName, selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_name_text_view);
        }
    }
}