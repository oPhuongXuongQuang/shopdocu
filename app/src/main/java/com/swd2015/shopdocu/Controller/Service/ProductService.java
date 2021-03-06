package com.swd2015.shopdocu.Controller.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.swd2015.shopdocu.Controller.Activity.FavoriteProductActivity;
import com.swd2015.shopdocu.Controller.Activity.MainActivity;
import com.swd2015.shopdocu.Controller.Activity.ProductDetailActivity;
import com.swd2015.shopdocu.Controller.Activity.SeenProductActivity;
import com.swd2015.shopdocu.Controller.Adapter.ProductAdapter;
import com.swd2015.shopdocu.Controller.Adapter.ProductDetailAdapter;
import com.swd2015.shopdocu.Controller.Adapter.ProductGridViewAdapter;
import com.swd2015.shopdocu.Controller.Adapter.ShowSearchedResultAdapter;
import com.swd2015.shopdocu.Controller.Fragment.BanGheCapheFragment;
import com.swd2015.shopdocu.Controller.Fragment.DienTuFragment;
import com.swd2015.shopdocu.Controller.Fragment.GiaDinhFragment;
import com.swd2015.shopdocu.Controller.Fragment.HomePageMainContentFragment;
import com.swd2015.shopdocu.Controller.Fragment.KhacFragment;
import com.swd2015.shopdocu.Controller.Fragment.KhachSanFragment;
import com.swd2015.shopdocu.Controller.Fragment.QuanAnFragment;
import com.swd2015.shopdocu.Controller.Fragment.SearchFragment;
import com.swd2015.shopdocu.Controller.Fragment.VanPhongFragment;
import com.swd2015.shopdocu.Controller.JSON.JSONObject.JSON_Product;
import com.swd2015.shopdocu.Controller.JSON.JSONTask.JSONProductTask;
import com.swd2015.shopdocu.Controller.JSON.JSONUtil.JSONTask;
import com.swd2015.shopdocu.Controller.Task.FavoriteTask;
import com.swd2015.shopdocu.Controller.Task.SeenTask;
import com.swd2015.shopdocu.Controller.Util.DBTask;
import com.swd2015.shopdocu.Controller.Util.FormatNameAndPrice;
import com.swd2015.shopdocu.Controller.Util.Object.ProductForAdapter;
import com.swd2015.shopdocu.Model.DAO.FavoriteDAO;
import com.swd2015.shopdocu.Model.DTO.Product;
import com.swd2015.shopdocu.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Quang on 20/11/2015.
 */
public class ProductService {
    public Activity activity;
    BaseAdapter baseAdapter;
    Fragment fragment;
    android.support.v4.app.Fragment supportv4Fragment;

    public ProductService() {
    }

    public ProductService(Activity activity, Fragment fragment) {
        this.fragment = fragment;
        this.activity = activity;
    }

    public ProductService(Fragment fragment) {
        this.fragment = fragment;
    }

    public ProductService(Activity activity) {
        this.activity = activity;
    }

    public ProductService(android.support.v4.app.Fragment supportv4Fragment) {
        this.supportv4Fragment = supportv4Fragment;
    }

    public void connectionError(){
        if (this.activity != null) {
            new AlertDialog.Builder(activity).
                    setMessage("Xin hãy kiểm tra lại kết nối của bạn!").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).
                    show();
        }else if(fragment != null) {
            new AlertDialog.Builder(fragment.getActivity()).
                    setMessage("Xin hãy kiểm tra lại kết nối của bạn!").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).
                    show();
        } else if(supportv4Fragment != null) {
            new AlertDialog.Builder(fragment.getActivity()).
                    setMessage("Xin hãy kiểm tra lại kết nối của bạn!").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).
                    show();
        }

    }

    public ProductService(BaseAdapter baseAdapter) {
        this.baseAdapter = baseAdapter;
    }

    public void getAllProduct() {
        JSONProductTask jsonTask = new JSONProductTask(this, JSONTask.GET_ALL_PRODUCT);
        jsonTask.execute();
    }

    public void setAllProduct(ArrayList<JSON_Product> productList) {
        switch (fragment.getClass().getSimpleName()) {
            case "SearchFragment":
                SearchFragment searchFragment = (SearchFragment) fragment;

                //Set all ProductName to arrayList for search suggestion
                for (int i = 0; i < productList.size(); ++i) {
                    searchFragment.listProductsName.add(productList.get(i).getName());
                }
                searchFragment.searchTextView.setAdapter(new ArrayAdapter<String>(
                        searchFragment.getActivity().getApplicationContext(),
                        R.layout.list_searched_product_suggestion,
                        searchFragment.listProductsName));
                break;
        }
    }

    public void getProductByID(int ID) {
        JSONProductTask jsonTask = new JSONProductTask(this, JSONTask.GET_PRODUCT_BY_ID,
                String.valueOf(ID));
        jsonTask.execute();
    }

    public void setProduct(final JSON_Product product) {
        switch (activity.getClass().getSimpleName()) {
            case "ProductDetailActivity":
                ProductDetailActivity productDetailActivity = (ProductDetailActivity) activity;
                if (alreadyFavorited(product.getID())) {
                    productDetailActivity.favoriteButton.setBackgroundResource(R.drawable.ic_red_heart);
                } else {
                    productDetailActivity.favoriteButton.setBackgroundResource(R.drawable.ic_blank_heart);
                }
                productDetailActivity.product = product;
                productDetailActivity.productTitle.setText(product.getName());
                productDetailActivity.productPrice.setText(
                        FormatNameAndPrice.FormatPrice(product.getPrice()));
                productDetailActivity.productStatus.setText(product.getStatus());
                productDetailActivity.productDescription.setText(product.getDescription());

                Glide.with(productDetailActivity)
                        .load(product.getImage().get(0))
                        .into(productDetailActivity.productLargeImage);
                productDetailActivity.smallImageListView.setVisibility(View.VISIBLE);
                productDetailActivity.smallImageListView.setAdapter(new ProductDetailAdapter(productDetailActivity, product.getImage()));

                saveSeenProduct(product);
                break;
            case "MainActivity":
                MainActivity mainActivity = (MainActivity) activity;
                mainActivity.product = product;
                break;
        }
    }

    public void getSearchedProducts(String productName, int categoryID) {
        JSONProductTask jsonTask = new JSONProductTask(this,
                JSONTask.GET_SEARCHED_PRODUCTS, productName, String.valueOf(categoryID));
        jsonTask.execute();
    }

    public void getSearchedProducts(int categoryID) {
        JSONProductTask jsonTask = new JSONProductTask(this,
                JSONTask.GET_SEARCHED_PRODUCTS, String.valueOf(categoryID));

        jsonTask.execute();
    }


    public void setSearchedProducts(ArrayList<JSON_Product> productList) {
        if (fragment != null) {
            switch (fragment.getClass().getSimpleName()) {
                case "SearchFragment":
                    try {
                        SearchFragment searchFragment = (SearchFragment) fragment;

                        if (productList.size() != 0) {
                            int orderByID = searchFragment.orderBySpinner.getSelectedItemPosition();
                            if (orderByID == 0) {
                                Collections.sort(productList, new Comparator<JSON_Product>() {
                                    @Override
                                    public int compare(JSON_Product p1, JSON_Product p2) {
                                        return p1.getPrice() - p2.getPrice(); // Ascending
                                    }
                                });
                            } else if (orderByID == 1) {
                                Collections.sort(productList, new Comparator<JSON_Product>() {
                                    @Override
                                    public int compare(JSON_Product p1, JSON_Product p2) {
                                        return p2.getPrice() - p1.getPrice(); // Descending
                                    }
                                });
                            }

                            searchFragment.productFoundResult.setVisibility(View.VISIBLE);
                            searchFragment.productFoundResult.setText("Tìm thấy " + productList.size()
                                                                                    + " sản phẩm");

                            //Call adapter to show searched result to Grid View
                            searchFragment.searchResultGridView.setAdapter(
                                    new ShowSearchedResultAdapter(searchFragment.getActivity()
                                            .getApplicationContext(),
                                            productList, searchFragment.getActivity() ));
                        } else {
                            searchFragment.productFoundResult.setVisibility(View.INVISIBLE);
                            searchFragment.productNotFoundTV_1.setText(R.string.product_not_found_1);
                            searchFragment.productNotFoundTV_2.setText(R.string.product_not_found_2);
                            searchFragment.productNotFoundTV_1.setVisibility(View.VISIBLE);
                            searchFragment.productNotFoundTV_2.setVisibility(View.VISIBLE);
                            searchFragment.searchResultGridView.setVisibility(View.INVISIBLE);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
					}
                    break;
            }
        } else if (supportv4Fragment != null) {
            switch (supportv4Fragment.getClass().getSimpleName()) {
                case "BanGheCapheFragment": {
                    BanGheCapheFragment banGheCapheFragment = (BanGheCapheFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            banGheCapheFragment.getActivity().getApplicationContext(), productList,
                            banGheCapheFragment.getActivity());
                    banGheCapheFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "DienTuFragment": {
                    DienTuFragment dienTuFragment = (DienTuFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            dienTuFragment.getActivity().getApplicationContext(), productList,
                            dienTuFragment.getActivity());
                    dienTuFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "GiaDinhFragment": {
                    GiaDinhFragment giaDinhFragment = (GiaDinhFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            giaDinhFragment.getActivity().getApplicationContext(), productList,
                            giaDinhFragment.getActivity());
                    giaDinhFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "KhachSanFragment": {
                    KhachSanFragment khachSanFragment = (KhachSanFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            khachSanFragment.getActivity().getApplicationContext(), productList,
                            khachSanFragment.getActivity());
                    khachSanFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "QuanAnFragment": {
                    QuanAnFragment quanAnFragment = (QuanAnFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            quanAnFragment.getActivity().getApplicationContext(), productList,
                            quanAnFragment.getActivity());
                    quanAnFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "VanPhongFragment": {
                    VanPhongFragment vanPhongFragment = (VanPhongFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            vanPhongFragment.getActivity().getApplicationContext(), productList,
                            vanPhongFragment.getActivity());
                    vanPhongFragment.gridResult.setAdapter(adapter);
                    break;
                }
                case "KhacFragment": {
                    KhacFragment khacFragment = (KhacFragment) supportv4Fragment;
                    ShowSearchedResultAdapter adapter = new ShowSearchedResultAdapter(
                            khacFragment.getActivity().getApplicationContext(), productList,
                            khacFragment.getActivity());
                    khacFragment.gridResult.setAdapter(adapter);
                    break;
                }
            }
        }//end if supportV4Fragment !=null
    }

    public void getNewProducts() {
        JSONProductTask jsonTask = new JSONProductTask(this, JSONTask.GET_NEW_PRODUCTS);
        jsonTask.execute();
    }

    public void setNewProducts(ArrayList<JSON_Product> productList) {
        HomePageMainContentFragment homePageFragment = (HomePageMainContentFragment) supportv4Fragment;
        homePageFragment.listProduct = productList;

        homePageFragment.listDataNewProduct = new ArrayList<ProductForAdapter>();
        for (JSON_Product product : homePageFragment.listProduct) {
            homePageFragment.productForAdapter
                    = new ProductForAdapter(product.getID(), formatName(product.getName())
                    , formatPrice(product.getPrice())
                    , product.getImage().get(0).toString());
            homePageFragment.listDataNewProduct.add(homePageFragment.productForAdapter);
        }

        //New product
        homePageFragment.newProductAdapter =
                new ProductAdapter(homePageFragment.getActivity()
                        , homePageFragment.listDataNewProduct);
        homePageFragment.layoutManager = new LinearLayoutManager(homePageFragment.getActivity());
        homePageFragment.layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homePageFragment.recyclerView.setLayoutManager(homePageFragment.layoutManager);
        homePageFragment.recyclerView.setAdapter(homePageFragment.newProductAdapter);

    }

    public void getHotProduct() {
        JSONProductTask jsonTask = new JSONProductTask(this, JSONTask.GET_HOT_PRODUCTS);
        jsonTask.execute();
    }

    public void setHotProduct(ArrayList<JSON_Product> productList) {
        HomePageMainContentFragment homePageFragment2 = (HomePageMainContentFragment) supportv4Fragment;
        homePageFragment2.listDataHotProduct = new ArrayList<ProductForAdapter>();
        for (JSON_Product product : productList) {
            homePageFragment2.hotProductForAdapter
                    = new ProductForAdapter(product.getID(), formatName(product.getName())
                    , formatPrice(product.getPrice())
                    , product.getImage().get(0).toString());
            homePageFragment2.listDataHotProduct.add(homePageFragment2.hotProductForAdapter);
        }
        homePageFragment2.hotProductAdapter =
                new ProductAdapter(homePageFragment2.getActivity()
                        , homePageFragment2.listDataHotProduct);
        homePageFragment2.layoutManager2 = new LinearLayoutManager(homePageFragment2.getActivity());
        homePageFragment2.layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        homePageFragment2.recyclerViewHotProduct.setLayoutManager(homePageFragment2.layoutManager2);
        homePageFragment2.recyclerViewHotProduct.setAdapter(homePageFragment2.hotProductAdapter);
    }


    private String formatName(String name) {
        if (name.length() >= 15) {
            name = name.substring(0, 13) + "...";
        }
        return name;
    }

    private String formatPrice(int price) {
        double amount = price;
        DecimalFormat formatter = new DecimalFormat("#,###");
        String sPrice = formatter.format(amount);
        sPrice = sPrice.replace(',', '.');
        return sPrice + " VND";
    }

    public void saveSeenProduct(JSON_Product jsonProduct) {
        SeenTask seenTask = new SeenTask(this, new Product(jsonProduct), DBTask.ADD_SEEN_PRODUCT);
        seenTask.execute();
    }

    public void getAllSeenProduct() {
        SeenTask seenTask = new SeenTask(this, DBTask.GET_SEEN_PRODUCT);
        seenTask.execute();
    }

    public void setAllSeenProduct(final List<Product> productList) {
        switch (activity.getClass().getSimpleName()) {
            case "SeenProductActivity":
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SeenProductActivity seenProductActivity = (SeenProductActivity) activity;
                        seenProductActivity.gridView.setAdapter(new ProductGridViewAdapter(seenProductActivity, productList));
                    }
                });
                break;
        }
    }

    public boolean alreadyFavorited(int productID) {
        FavoriteDAO favoriteDAO = new FavoriteDAO(this.activity);
        return favoriteDAO.isProductExist(productID);
    }

    public void toggleFavorite(JSON_Product jsonProduct) {
        FavoriteTask favoriteTask = new FavoriteTask(this, new Product(jsonProduct), DBTask.TOGGLE_FAVORITE);
        favoriteTask.execute();
    }

    public void getAllFavoriteProduct() {
        FavoriteTask favoriteTask = new FavoriteTask(this, DBTask.GET_FAVORITE_PRODUCT);
        favoriteTask.execute();
    }

    public void setAllFavoriteProduct(final List<Product> productList) {
        switch (activity.getClass().getSimpleName()) {
            case "FavoriteProductActivity":
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FavoriteProductActivity favoriteProductActivity = (FavoriteProductActivity) activity;
                        favoriteProductActivity.gridView.setAdapter(new ProductGridViewAdapter(favoriteProductActivity, productList));
                    }
                });
                break;
        }
    }
}
