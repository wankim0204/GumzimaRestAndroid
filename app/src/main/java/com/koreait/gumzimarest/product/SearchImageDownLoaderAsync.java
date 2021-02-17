package com.koreait.gumzimarest.product;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchImageDownLoaderAsync extends AsyncTask<String, Void, Bitmap> {
    String TAG=this.getClass().getName();
    Product product;
    SearchFragment searchFragment;

    public SearchImageDownLoaderAsync(Product product, SearchFragment searchFragment){
        this.product=product;
        this.searchFragment=searchFragment;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... param) {
        Bitmap bitmap=null;
        try {
            URL url=new URL(param[0]);
            InputStream is = url.openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(Bitmap bitmap) {
        product.setBitmap(bitmap);

        searchFragment.productList.add(product);
        searchFragment.girdViewAdapter.productList=searchFragment.productList;

        searchFragment.girdViewAdapter.notifyDataSetChanged();
        searchFragment.gridView.invalidate();
    }

}