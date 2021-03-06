package com.swd2015.shopdocu.Controller.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.swd2015.shopdocu.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageBannerFragment extends Fragment {
     String imgURL ;
    ImageView imgView;

    public ImageBannerFragment(){

    }

    public static final ImageBannerFragment newInstance(String imgURL){
        ImageBannerFragment fragment=new ImageBannerFragment();
        Bundle bundle=new Bundle();
        bundle.putString("imgURL", imgURL);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_image_banner, container, false);
        imgView=(ImageView) v.findViewById(R.id.imgBanner);

        Glide.with(getContext())
                .load(imgURL)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_close_search)
                .into(imgView);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        //new LoadImageFromInternet().execute(imgURL);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgURL= getArguments().getString("imgURL");

    }

    private class LoadImageFromInternet extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url= null;
            try {
                url = new URL(params[0]);
                Bitmap bmp= BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView.setImageBitmap(bitmap);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

}