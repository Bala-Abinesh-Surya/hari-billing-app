package com.hari.billingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hari.billingapp.R;
import com.hari.billingapp.adapters.ProductAdapter;
import com.hari.billingapp.constants.data;
import com.hari.billingapp.models.Product;
import com.hari.billingapp.pdfExport.export;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    // UI Elements
    private RecyclerView recyclerView;
    private Button check;

    // adapter
    private ProductAdapter adapter;

    private data data;
    private File file;

    private static final int PERMISSION_REQUEST_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checking if the app has permission to write to the external storage
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // if yes, creating a directory
            createDirectory();
        }
        else{
            // else, asking the user for the permission
            askPermission();
        }

        // method to initialise the UI Elements
        initialiseUIElements();

        // setting up the adapter for the recycler view
        adapter = new ProductAdapter(this, check, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // on click listener for the button
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export export = new export(MainActivity.this);

                reset();
            }
        });
    }

    // method which returns the result for our askPermission() method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createDirectory();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // method to ask the permissions that are needed for the application to perform
    private void askPermission(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE
        );
    }

    // method to create a separate directory for the app
    private void createDirectory() {
        // creating a directory for the Paint app
        // let the name be My Paintings
        folderNameDetermination();

        if(! file.exists()){
            // creating a folder if the folder does not already exist
            if(file.mkdir()){
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Folder not created", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show();
        }
    }

    // determining the folder destination for the application
    // for android 11 and above devices, creating a folder in the Documents folder
    // else, creating a folder in the outer main region
    private void folderNameDetermination(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Bills");
        }
        else{
            file = new File(Environment.getExternalStorageDirectory(), "Bills");
        }
    }

    // method to initialise the UI Elements
    private void initialiseUIElements(){
        // recycler view
        recyclerView = findViewById(R.id.main_rec_view);

        // button
        check = findViewById(R.id.check);

        data = new data();
        data.initialiseTheProducts();
    }

    // method to reset everything
    private void reset(){
        // making the overallRate to zero
        Product.overAllRate = 0;

        // clearing the items bought hash table
        Product.productsBought.clear();

        adapter.notifyDataSetChanged();

        // making the button invisible
        check.setVisibility(View.GONE);
    }
}