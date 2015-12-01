package com.swd2015.shopdocu.Model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.swd2015.shopdocu.Model.DTO.OrderedProduct;
import com.swd2015.shopdocu.Model.Util.DBConfig;
import com.swd2015.shopdocu.Model.Util.DBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quang on 14-Nov-15.
 */
public class OrderedProductDAO extends DBHandler {

    public OrderedProductDAO(Context context) {
        super(context);
    }

    public int numberOfRecord() {
        String query = "SELECT  * FROM " + DBConfig.TABLE_ORDERED_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
//        db.close();
        return count;
    }

    public boolean isOrderExist(int productID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DBConfig.TABLE_ORDERED_PRODUCT +
                " where " + DBConfig.PRODUCT_ID + " = " + productID;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
//        db.close();
        return true;
    }

    public void addOrderedProduct(OrderedProduct order){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int incrementingID = 0;
        if(numberOfRecord() != 0){
            incrementingID = numberOfRecord();
            incrementingID++;
        }

//        values.put(DBConfig.ORDER_ID, incrementingID);
//        values.put(DBConfig.PRODUCT_ID, order.getID());
        values.put(DBConfig.PRODUCT_NAME, order.getName());
        values.put(DBConfig.PRODUCT_PRICE, order.getPrice());
        values.put(DBConfig.PRODUCT_DESCRIPTION, order.getDescription());
        values.put(DBConfig.PRODUCT_CATEGORY, order.getCategory());
        values.put(DBConfig.PRODUCT_STATUS, order.getStatus());
        values.put(DBConfig.PRODUCT_CREATEDATE, order.getCategory());
        values.put(DBConfig.PRODUCT_IMAGE, order.getImage());


        if (isOrderExist(order.getID())){
            values.put(DBConfig.ORDER_QUANTITY,getQuantity(order.getID()) + 1);
            db.update(DBConfig.TABLE_ORDERED_PRODUCT, values,
                    DBConfig.PRODUCT_ID + "=" + order.getID(), null);
        } else {
            values.put(DBConfig.ORDER_ID, incrementingID);
            values.put(DBConfig.PRODUCT_ID, order.getID());
            values.put(DBConfig.ORDER_QUANTITY, 1);
            db.insert(DBConfig.TABLE_ORDERED_PRODUCT, null, values);
        }

        db.close();
    }

    public int getQuantity(int productID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        int quantity = 0;
        try {
            cursor = db.rawQuery("SELECT " + DBConfig.ORDER_QUANTITY +
                    " FROM " + DBConfig.TABLE_ORDERED_PRODUCT +
                    " WHERE " + DBConfig.PRODUCT_ID + "=?", new String[] {productID + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                quantity = cursor.getInt(cursor.getColumnIndex(DBConfig.ORDER_QUANTITY));
            }
            return quantity;
        } finally {
            cursor.close();
//            db.close();
        }
    }

    public ArrayList<OrderedProduct> getAllCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<OrderedProduct> orderedProductList = new ArrayList<OrderedProduct>();
        try {
            cursor = db.rawQuery("SELECT *" +
                    " FROM " + DBConfig.TABLE_ORDERED_PRODUCT, null);
            if (cursor.moveToFirst()){
                OrderedProduct product = new OrderedProduct();
                int count = 0;
                while (cursor.isAfterLast() == false){
                    product.setOrderID(cursor.getInt(cursor.getColumnIndex(DBConfig.ORDER_ID)));
                    product.setID(cursor.getInt(cursor.getColumnIndex(DBConfig.PRODUCT_ID)));
                    product.setName(cursor.getString(cursor.getColumnIndex(DBConfig.PRODUCT_NAME)));
                    product.setCategory(cursor.getInt(cursor.getColumnIndex(DBConfig.PRODUCT_CATEGORY)));
                    product.setStatus(cursor.getString(cursor.getColumnIndex(DBConfig.PRODUCT_STATUS)));
                    product.setImage(cursor.getString(cursor.getColumnIndex(DBConfig.PRODUCT_IMAGE)));
                    product.setPrice(cursor.getFloat(cursor.getColumnIndex(DBConfig.PRODUCT_PRICE)));
                    product.setQuantity(cursor.getInt(cursor.getColumnIndex(DBConfig.ORDER_QUANTITY)));
                    orderedProductList.add(product);
                    cursor.moveToNext();
                    count++;
                    System.out.println("Hello world " + count);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }
        return orderedProductList;
    }
}
