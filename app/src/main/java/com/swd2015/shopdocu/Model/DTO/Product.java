package com.swd2015.shopdocu.Model.DTO;

import com.swd2015.shopdocu.Controller.JSON.JSONObject.JSON_Product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Quang on 14-Nov-15.
 */
public class Product {
    private int ID;
    private String name;
    private Float price;
    private String description;
    private Integer category;
    private Date createDate;
    private String image;
    private String status;

    public Product(){

    }

    public Product(JSON_Product jsonProduct){
        this.setID(jsonProduct.getID());
        this.setName(jsonProduct.getName());
        this.setPrice((float) jsonProduct.getPrice());
        this.setDescription(jsonProduct.getDescription());
        this.setCategory(Integer.parseInt(jsonProduct.getCategory()));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            this.setCreateDate(df.parse(jsonProduct.getCreateDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.setImage(jsonProduct.getImage().get(0));
        this.setStatus(jsonProduct.getStatus());
    }

    public Product(int ID, String name, Float price, String description, Integer category, Date createDate, String image) {
        this.setID(ID);
        this.setName(name);
        this.setPrice(price);
        this.setDescription(description);
        this.setCategory(category);
        this.setCreateDate(createDate);
        this.setImage(image);
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
