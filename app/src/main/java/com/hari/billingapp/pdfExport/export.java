package com.hari.billingapp.pdfExport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.hari.billingapp.constants.data;
import com.hari.billingapp.models.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class export extends Activity {
    // Constructor
    public export(){
        determineLength();

        createPDF();
    }

    private data data = new data();

    // Strings used in the pdf
    private static final String STORE_NAME = "H2H Stores";
    private static final String COLLEGE_NAME = "Amrita College of Engineering and Technology";
    private static final String DATE = "Date : ";
    private static final String TIME = "Time : ";
    private static final String S_NO = "S. No";
    private static final String ITEMS = "Items";
    private static final String QUANTITY = "Quantity";
    private static final String PRIZE = "Prize";
    private static final String TOTAL = "Total";
    private static final String TAXES_INCLUDED = "*** All Taxes Included ***";
    private static final String THANK_YOU = "Thank you for shopping with us !";
    private static final String HARI = "Hari Prasath";
    private static final String HARSHA = "Harsha";

    private final int pageWidth = 300;
    private int pageLength = 0;

    // method to create the pdf
    private void createPDF(){
        // creating an instance to PdfDocument class
        PdfDocument pdfDocument = new PdfDocument();

        // specifying the height, width and number of pages of the pdf
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageLength, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // creating the paint object
        Paint paint = new Paint();

        // getting the surface to paint
        Canvas canvas = page.getCanvas();

        /**************************************************************************************************************************************/
        /*
        *
        *
        * PAINTING STARTS
        *
        *
        *
        * */

        // Store name
        // H2H Stores
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText(STORE_NAME, pageWidth/2, 40, paint);

        // college name
        paint.setTextSize(10f);
        canvas.drawText(COLLEGE_NAME, pageWidth/2, 55, paint);

        // date text
        // date :
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(8f);

        canvas.drawText(DATE, 25, 80, paint);

        // actual date
        LocalDateTime localDateTime = LocalDateTime.now();

        String dateText = localDateTime.toLocalDate().toString();
        canvas.drawText(dateText, 60, 80, paint);

        // time text
        canvas.drawText(TIME, 240, 80, paint);

        // actual time
        String hour = localDateTime.getHour()+"";
        if(hour.length() == 1){
            hour = "0" + hour;
        }

        String minute = localDateTime.getMinute()+"";
        if(minute.length() == 1){
            minute = "0" + minute;
        }

        String time = hour + " : " + minute;

        canvas.drawText(time, 265, 80, paint);

        // s.no, items, quantity, prize texts
        canvas.drawText(S_NO, 30, 100, paint);
        canvas.drawText(ITEMS, 100, 100, paint);
        canvas.drawText(QUANTITY, 210, 100, paint);
        canvas.drawText(PRIZE, 270, 100, paint);

        // drawing a line below these texts
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawLine(20, 105, 290, 105, paint);

        // printing all the items bought
        paint.setStyle(Paint.Style.FILL);

        ArrayList<String> itemsBought = itemsBought();
        int startY = 120;

        for(int i = 0; i < itemsBought.size(); i++){
            // serial number
            canvas.drawText((i + 1)+"", 30, startY, paint);

            // product name
            String name = itemsBought.get(i);
            canvas.drawText(name, 100, startY, paint);

            // quantity
            int quantity = returnQuantity(name);
            canvas.drawText(quantity+"", 210, startY, paint);

            // prize
            canvas.drawText(returnPrizeOfEachProduct(name, quantity)+"", 270, startY, paint);

            startY = startY + 20;
        }

        // drawing the divider lines
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        // after serial number
        canvas.drawLine(50, 90, 50, startY, paint);
        // after items
        canvas.drawLine(175, 90, 175, startY, paint);
        // after quantity
        canvas.drawLine(240, 90, 240, startY, paint);

        // drawing a line below the list of products
        canvas.drawLine(20, startY, 290, startY, paint);

        // total text
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(TOTAL, 230, startY + 15, paint);

        // total prize
        canvas.drawText(Product.overAllRate+"", 270, startY + 15, paint);

        // all taxes included text
        canvas.drawText(TAXES_INCLUDED, pageWidth/2, startY + 30, paint);

        // thank you for shopping with us text
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(THANK_YOU, 20, startY + 50, paint);

        // names
        canvas.drawText(HARI, 20, startY + 65, paint);
        canvas.drawText(HARSHA, 20, startY + 80, paint);

        /*
        *
        *
        * PAINTING OVER
        *
        *
        *
        * */

        pdfDocument.finishPage(page);

        // writing to the external storage
        // creating a File object
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Bills");
        }
        else{
            file = new File(Environment.getExternalStorageDirectory(), "Bills");
        }

        String fileName = file + "/" + "Bill - " + localDateTime + ".pdf";

        // writing the pdf to the file created
        try{
            pdfDocument.writeTo(new FileOutputStream(fileName));
            //Toast.makeText(getApplicationContext(), "created", Toast.LENGTH_LONG).show();
        }
        catch (Exception exception){
            exception.printStackTrace();
            //Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();
    }

    // method to return the items bought by the customer
    private ArrayList<String> itemsBought(){
        ArrayList<String> itemsBought = new ArrayList<>();

        // going through the all products list
        for(String product : data.PRODUCT_NAMES){
            if(Product.productsBought.containsKey(product)){
                itemsBought.add(product);
            }
        }

        return itemsBought;
    }

    // method to return the quantity of a particular item
    private int returnQuantity(String productName){
        return Product.productsBought.get(productName);
    }

    // method to return the prize of each product based on the quantity
    private double returnPrizeOfEachProduct(String productName, int quantity){
        // going through the all products
        double rate = 0;

        for(int i = 0; i < data.PRODUCT_NAMES.length; i++){
            String namex = data.PRODUCT_NAMES[i];

            if(namex.equals(productName)){
                rate = quantity * data.PRODUCT_RATES[i];

                return rate;
            }
        }

        // unreachable statement
        return rate;
    }

    // method to determine the length of the pdf
    private void determineLength(){
        pageLength = 120 + itemsBought().size() * 20 + 20 + 100;
    }
}
