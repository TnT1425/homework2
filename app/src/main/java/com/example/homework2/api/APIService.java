package com.example.homework2.api;

import com.example.homework2.module.Product;
import com.example.homework2.module.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("products")
    Call<ProductResponse> getProducts();

    @GET("products/{id}")
    Call<Product> getProductDetails(@Path("id") int productId);

    @GET("product/search")
    Call<ProductResponse> searchProducts(@Query("q") String query);
}
