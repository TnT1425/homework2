package com.example.homework2.interfaces;

public interface ProductPresenterImpl {
    void getAllProduct();
    void getAllProduct(int limit);
    void getProductById(int id);
    void searchProduct(String name);
    void getAllCategory();
}
