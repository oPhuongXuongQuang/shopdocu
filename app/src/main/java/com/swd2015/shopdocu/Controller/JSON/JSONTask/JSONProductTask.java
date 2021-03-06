package com.swd2015.shopdocu.Controller.JSON.JSONTask;

import com.swd2015.shopdocu.Controller.JSON.JSONObject.JSON_Product;

import com.google.gson.Gson;
import com.swd2015.shopdocu.Controller.JSON.JSONUtil.JSONParser;
import com.swd2015.shopdocu.Controller.JSON.JSONUtil.JSONTask;
import com.swd2015.shopdocu.Controller.Service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Quang on 20/11/2015.
 */
public class JSONProductTask extends JSONParser {
    ProductService productService;

    public JSONProductTask(ProductService productService, JSONTask task){
        this.productService = productService;
        this.API = task;
    }

    public JSONProductTask(ProductService productService, JSONTask task, String... params) {
        this.productService = productService;
        this.API = task;
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append("/" + param);
            this.ID = sb.toString();
        }
    }

    @Override
    protected void onPostExecute(String json) {
        if(json == null || json.trim().length() == 0){
            productService.connectionError();
        }
        Gson gson = new Gson();
        switch (this.API){
            case GET_ALL_PRODUCT:
                JSON_Product[] products = gson.fromJson(json, JSON_Product[].class);
                if(products != null) {
                    productService.setAllProduct(new ArrayList<>(Arrays.asList(products)));
                }
                break;
            case GET_PRODUCT_BY_ID:
                JSON_Product p = gson.fromJson(json, JSON_Product.class);
                productService.setProduct(p);
                break;
            case GET_SEARCHED_PRODUCTS:
                JSON_Product[] searchedProducts = gson.fromJson(json, JSON_Product[].class);
                if (searchedProducts != null){
                    productService.setSearchedProducts(new ArrayList<>(Arrays.asList(searchedProducts)));
                }
                break;
            case GET_NEW_PRODUCTS:
                JSON_Product[] newProducts = gson.fromJson(json, JSON_Product[].class);
                productService.setNewProducts(new ArrayList<>(Arrays.asList(newProducts)));
                break;
            case GET_HOT_PRODUCTS:
                JSON_Product[] hotProducts = gson.fromJson(json, JSON_Product[].class);
                productService.setHotProduct(new ArrayList<>(Arrays.asList(hotProducts)));
                break;
        }
    }


}
