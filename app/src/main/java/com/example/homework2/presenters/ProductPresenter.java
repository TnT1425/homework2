package com.example.homework2.presenters;

import android.util.Log;

import com.example.homework2.api.ProductApi;
import com.example.homework2.interfaces.ProductPresenterImpl;
import com.example.homework2.interfaces.ProductViewImpl; // Interface của View
import com.example.homework2.models.AllProductResponse;
import com.example.homework2.models.Product;
import com.example.homework2.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPresenter implements ProductPresenterImpl {

    private ProductViewImpl view;

    public ProductPresenter(ProductViewImpl view) {
        this.view = view;
    }

    @Override
    public void getAllProduct() {
        RetrofitClient.getApiService().getAllProduct().enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.getAllProductSuccess(response.body());
                } else {
                    view.getAllProductFailed(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                view.getAllProductFailed("NETWORK_ERROR", t.getMessage());
                Log.e("ProductPresenter", "Error fetching all products: " + t.getMessage());
            }
        });
    }

    @Override
    public void getAllProduct(int limit) {
        RetrofitClient.getApiService().get100Product(limit).enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.getAllProductSuccess(response.body());
                } else {
                    view.getAllProductFailed(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                view.getAllProductFailed("NETWORK_ERROR", t.getMessage());
                Log.e("ProductPresenter", "Error fetching limited products: " + t.getMessage());
            }
        });
    }

    @Override
    public void getProductById(int id) {
        RetrofitClient.getApiService().getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.getProductByIdSuccess(response.body());
                } else {
                    view.getProductByIdFailed(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                view.getProductByIdFailed("NETWORK_ERROR", t.getMessage());
                Log.e("ProductPresenter", "Error fetching product by ID: " + t.getMessage());
            }
        });
    }

    @Override
    public void searchProduct(String name) { // Đổi tên biến cho rõ ràng
        RetrofitClient.getApiService().searchProduct(name).enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.searchProductSuccess(response.body());
                } else {
                    view.searchProductFailed(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                view.searchProductFailed("NETWORK_ERROR", t.getMessage());
                Log.e("ProductPresenter", "Error searching products: " + t.getMessage());
            }
        });
    }

    @Override
    public void getAllCategory() {
        RetrofitClient.getApiService().getAllCategories().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onAllCategoriesSuccess(response.body()); // Gọi phương thức mới trong View
                } else {
                    view.onAllCategoriesFailed(String.valueOf(response.code()), response.message()); // Gọi phương thức mới trong View
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                view.onAllCategoriesFailed("NETWORK_ERROR", t.getMessage()); // Gọi phương thức mới trong View
                Log.e("ProductPresenter", "Error fetching categories: " + t.getMessage());
            }
        });
    }
}