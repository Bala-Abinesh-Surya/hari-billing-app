package com.hari.billingapp.adapters;

import static com.hari.billingapp.models.Product.allProductsArrayList;
import static com.hari.billingapp.models.Product.overAllRate;
import static com.hari.billingapp.models.Product.productsBought;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.hari.billingapp.R;
import com.hari.billingapp.constants.data;
import com.hari.billingapp.models.Product;

public class ProductAdapter extends RecyclerView.Adapter {

    private final Context context;
    private Button button;
    private data data;

    // Constructor
    public ProductAdapter(Context context, Button button, data data) {
        this.context = context;
        this.button = button;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_list, parent, false);
        return new ProductViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == ProductViewHolderClass.class){
            // displaying the product
            Product product = allProductsArrayList.get(position);

            // name
            String productName = product.getProductName().trim();
            ((ProductViewHolderClass) holder).name.setText(productName);

            // image
            ((ProductViewHolderClass) holder).product.setImageDrawable(returnProductImage(productName));

            // rate
            String rate = "Rs." + product.getProductPrize() + "0/-";
            ((ProductViewHolderClass) holder).prize.setText(rate);

            // on click listener for the add image
            ((ProductViewHolderClass) holder).add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // making the minus, button and quantity text visible
                    ((ProductViewHolderClass) holder).minus.setVisibility(View.VISIBLE);
                    ((ProductViewHolderClass) holder).quantityText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);

                    // incrementing the quantity
                    if(productsBought.containsKey(productName)){
                        // incrementing the quality by one
                        int currentQuantity = productsBought.get(productName);

                        productsBought.put(productName, currentQuantity + 1);
                    }
                    else{
                        // adding the entry to the hash table
                        productsBought.put(productName, 1);
                    }

                    // setting the quantity text
                    ((ProductViewHolderClass) holder).quantityText.setText("Quantity : " + productsBought.get(productName));

                    // updating the text of the button
                    button.setText("Proceed to Checkout (Rs." + returnTotalCost() + "0/-)");
                }
            });

            // on click listener for the minus button
            ((ProductViewHolderClass) holder).minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // decrementing the quantity
                    // the product must be there, if this minus image is visible
                    int currentQuantity = productsBought.get(productName);

                    productsBought.put(productName, currentQuantity - 1);

                    if(currentQuantity - 1 == 0){
                        // removing the product from the hash table
                        productsBought.remove(productName);

                        // then making the quantity text and minus button
                        ((ProductViewHolderClass) holder).minus.setVisibility(View.GONE);
                        ((ProductViewHolderClass) holder).quantityText.setVisibility(View.GONE);
                    }
                    else{
                        // updating the quantity text
                        ((ProductViewHolderClass) holder).quantityText.setText("Quantity : " + (currentQuantity - 1));
                    }

                    // updating the text of the button
                    if(returnTotalCost() == 0){
                        button.setVisibility(View.GONE);
                    }
                    else{
                        button.setText("Proceed to Checkout (Rs." + returnTotalCost() + "0/-)");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allProductsArrayList.size();
    }

    // view holder class
    public class ProductViewHolderClass extends RecyclerView.ViewHolder{
        // UI Elements
        private final TextView name, quantityText, prize;
        private final ImageView product, add, minus;

        public ProductViewHolderClass(@NonNull View itemView) {
            super(itemView);

            // initialising the UI Elements
            // text views
            name = itemView.findViewById(R.id.product_name);
            quantityText = itemView.findViewById(R.id.product_quantity);
            prize = itemView.findViewById(R.id.product_rate);

            // image view
            product = itemView.findViewById(R.id.product_image);
            add = itemView.findViewById(R.id.product_add);
            minus = itemView.findViewById(R.id.product_minus);

            // hiding the minus and quantity text by default
            minus.setVisibility(View.GONE);
            quantityText.setVisibility(View.GONE);
        }
    }

    // method to return the product image for the recycler view
    private Drawable returnProductImage(String productName){
        switch (productName){
            case "Pencil":{
                return (AppCompatResources.getDrawable(context, R.drawable.pencil));
            }
            case "Eraser":{
                return (AppCompatResources.getDrawable(context, R.drawable.eraser));
            }
            case "Sharpener":{
                return (AppCompatResources.getDrawable(context, R.drawable.sharpener));
            }
            case "A4 Sheets":{
                return (AppCompatResources.getDrawable(context, R.drawable.a4sheets));
            }
            case "Tip Box":{
                return (AppCompatResources.getDrawable(context, R.drawable.tipbox));
            }
            case "Gum":{
                return (AppCompatResources.getDrawable(context, R.drawable.gum));
            }
            case "Scale":{
                return (AppCompatResources.getDrawable(context, R.drawable.scale15));
            }
            case "Long Scale":{
                return (AppCompatResources.getDrawable(context, R.drawable.scale30cm));
            }
            case "Note (100 Pages)":{
                return (AppCompatResources.getDrawable(context, R.drawable.note100));
            }
            case "Note (200 Pages)":{
                return (AppCompatResources.getDrawable(context, R.drawable.note200));
            }
            case "Marker":{
                return (AppCompatResources.getDrawable(context, R.drawable.marker));
            }
            case "Color Paper":{
                return (AppCompatResources.getDrawable(context, R.drawable.color_paper));
            }
            case "Tape":{
                return (AppCompatResources.getDrawable(context, R.drawable.tape));
            }
            case "Scissor":{
                return (AppCompatResources.getDrawable(context, R.drawable.scissor));
            }
            case "Test Pad":{
                return (AppCompatResources.getDrawable(context, R.drawable.pad));
            }
            case "Bag":{
                return (AppCompatResources.getDrawable(context, R.drawable.bag));
            }
            case "Pouch":{
                return (AppCompatResources.getDrawable(context, R.drawable.pouch));
            }
            case "Lunch Bag":{
                return (AppCompatResources.getDrawable(context, R.drawable.lunch));
            }
            default:{
                return (AppCompatResources.getDrawable(context, R.drawable.pen));
            }
        }
    }

    // method to calculate the total cost of the purchase
    private double returnTotalCost(){
        overAllRate = 0;

        for(int i = 0; i < data.PRODUCT_NAMES.length; i++){
            String name = data.PRODUCT_NAMES[i];
            double rate = data.PRODUCT_RATES[i];

            // checking if the current product is in the productsBought hash table
            if(productsBought.containsKey(name)){
                // getting the quantity of the product
                int quantity = productsBought.get(name);

                overAllRate = overAllRate + (quantity * rate);
            }
        }

        return overAllRate;
    }
}
