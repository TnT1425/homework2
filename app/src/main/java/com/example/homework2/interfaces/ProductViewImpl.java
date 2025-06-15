package com.example.homework2.interfaces;

import com.example.homework2.models.AllProductResponse;
import com.example.homework2.models.Product;

import java.util.List;

public interface ProductViewImpl {
    void getAllProductSuccess(AllProductResponse response);
    void getAllProductFailed(String code, String message);
    void searchProductSuccess(AllProductResponse response);
    void searchProductFailed(String code, String message);
    void getProductByIdSuccess(Product product);
    void getProductByIdFailed(String code, String message);
    void onAllCategoriesSuccess(List<String> categories);
    void onAllCategoriesFailed(String code, String message);
}
