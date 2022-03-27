package com.hari.billingapp.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Hashtable;

public class Product {
    /*
    *
    * class which holds the attributes of each and every product*/

    private String productName;
    private double productPrize;
    private Drawable productImage;

    public static ArrayList<Product> allProductsArrayList = new ArrayList<>();

    public static Hashtable<String, Integer> productsBought = new Hashtable<>();

    public static double overAllRate = 0;

    // Constructor
    public Product(String productName, double productPrize, Drawable productImage) {
        this.productName = productName;
        this.productPrize = productPrize;
        this.productImage = productImage;
    }

    public Product(String productName, double productPrize) {
        this.productName = productName;
        this.productPrize = productPrize;
    }

    public Product(){

    }

    // getter and setter methods
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(double productPrize) {
        this.productPrize = productPrize;
    }

    public Drawable getProductImage() {
        return productImage;
    }

    public void setProductImage(Drawable productImage) {
        this.productImage = productImage;
    }
}
