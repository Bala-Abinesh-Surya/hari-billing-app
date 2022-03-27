package com.hari.billingapp.constants;

import static com.hari.billingapp.models.Product.allProductsArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.hari.billingapp.R;
import com.hari.billingapp.models.Product;

public class data {

    public String[] PRODUCT_NAMES = new String[]{
            "Pen",
            "Pencil",
            "Eraser",
            "Sharpener",
            "A4 Sheets",
            "Tip Box",
            "Gum",
            "Scale",
            "Long Scale",
            "Note (100 Pages)",
            "Note (200 Pages)",
            "Marker",
            "Color Paper",
            "Tape",
            "Scissor",
            "Test Pad",
            "Bag",
            "Pouch",
            "Lunch Bag"
    };

    public double[] PRODUCT_RATES = new double[]{
            10,
            5,
            3,
            5,
            10,
            5,
            15,
            10,
            20,
            20,
            35,
            20,
            5,
            25,
            30,
            75,
            250,
            100,
            150
    };

    public void initialiseTheProducts(){
        for(int i = 0; i < PRODUCT_NAMES.length; i++){
            String name = PRODUCT_NAMES[i].trim();
            double rate = PRODUCT_RATES[i];
          //  Drawable image = PRODUCT_DRAWABLES[i];

            // creating a Product object
            Product product = new Product(name, rate);

            // adding the product to the allProductsArrayList
            allProductsArrayList.add(product);
        }
    }
}
