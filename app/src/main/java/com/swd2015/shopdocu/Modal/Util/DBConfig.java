package com.swd2015.shopdocu.Modal.Util;

/**
 * Created by Quang on 14-Nov-15.
 */
public abstract class DBConfig {
    public static final String DATABASE_NAME = "ShopDoCu";
    public static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_ORDERED_PRODUCT = "OrderedProduct";
    public static final String TABLE_FAVORITE = "Favorite";
    public static final String TABLE_SEEN = "Seen";

    public static final String ORDER_ID = "OrderID";
    public static final String PRODUCT_ID = "ProductID";
    public static final String PRODUCT_NAME = "Name";
    public static final String PRODUCT_PRICE = "Price";
    public static final String PRODUCT_DESCRIPTION = "Description";
    public static final String PRODUCT_CATEGORY = "Category";
    public static final String PRODUCT_STATUS = "Status";
    public static final String PRODUCT_CREATEDATE = "CreateDate";
    public static final String PRODUCT_IMAGE = "Image";
    public static final String ORDER_QUANTITY = "Quantity";

    public static final String FAVORITE_ID = "ID";
    public static final String FAVORITE_PRODUCT_ID = "ProductID";

    public static final String SEEN_ID = "ID";
    public static final String SEEN_PRODUCT_ID = "ProductID";
}