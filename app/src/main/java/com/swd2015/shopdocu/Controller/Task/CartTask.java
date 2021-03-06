package com.swd2015.shopdocu.Controller.Task;

import android.os.AsyncTask;

import com.swd2015.shopdocu.Controller.Activity.CartActivity;
import com.swd2015.shopdocu.Controller.Adapter.CartAdapter;
import com.swd2015.shopdocu.Controller.Service.CartService;
import com.swd2015.shopdocu.Controller.Util.DBTask;
import com.swd2015.shopdocu.Model.DAO.CartProductDAO;
import com.swd2015.shopdocu.Model.DTO.CartProduct;

import java.util.ArrayList;

/**
 * Created by quangphuong on 11/30/15.
 */
public class CartTask extends AsyncTask {
    CartService cartService;
    DBTask dbTask;
    int productID;
    int quantity;

    public CartTask(CartService cartService, DBTask dbTask){
        this.cartService = cartService;
        this.dbTask = dbTask;
    }

    public CartTask(CartService cartService, int productID, DBTask dbTask){
        this.cartService = cartService;
        this.productID = productID;
        this.dbTask = dbTask;
    }

    public CartTask(CartService cartService, int productID, int quantity, DBTask dbTask){
        this.cartService = cartService;
        this.productID = productID;
        this.dbTask = dbTask;
        this.quantity = quantity;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        CartProductDAO dao = new CartProductDAO(cartService.activity.getBaseContext());
        ArrayList<CartProduct> list;
        switch (dbTask) {
            case GET_ALL_CART:
                cartService.setCartList(dao.getAllCart());
                break;
            case DELETE_CART:
                dao.deleteCart(productID);
                list = dao.getAllCart();
                cartService.resetListView(list);
                break;
            case UPDATE_CART:
                dao.updateCartQuantity(productID, quantity);
                list = dao.getAllCart();
                cartService.resetListView(list);
                break;
        }
        return null;
    }
}
