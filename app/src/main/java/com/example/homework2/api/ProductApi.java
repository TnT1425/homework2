package com.example.homework2.api;

import com.example.homework2.constant.ConstantApi;
import com.example.homework2.models.AllProductResponse;
import com.example.homework2.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET(ConstantApi.GET_ALL_PRODUCT)
    Call<AllProductResponse> getAllProduct();
    @GET(ConstantApi.GET_ALL_PRODUCT)
    Call<AllProductResponse> get100Product(@Query("limit") int limit);
    @GET(ConstantApi.GET_SINGLE_PRODUCT)
    Call<Product> getProductById(@Path("id")int id);
    @GET(ConstantApi.SEARCH_PRODUCT)
    Call<AllProductResponse> searchProduct(@Query("q") String productName);
    @GET("products/categories")
    Call<List<String>> getAllCategories();
}
