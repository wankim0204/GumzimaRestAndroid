package com.koreait.gumzimarest.product;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchAsync extends AsyncTask<String, Void, String> {
    String TAG=this.getClass().getName();
    SearchFragment searchFragment;
    String text;

    public SearchAsync(SearchFragment searchFragment, String text){
        this.searchFragment=searchFragment;
        this.text=text;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        String requestUrl = params[0];
        BufferedReader buffr=null;
        StringBuilder sb=null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection  con = (HttpURLConnection)url.openConnection();
            buffr  = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            sb = new StringBuilder();
            String data=null;

            while(true){
                data = buffr.readLine();
                if(data==null)break;
                sb.append(data);
            }
            con.getResponseCode(); //요청 후 응답받음.

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(buffr!=null){
                try {
                    buffr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            ArrayList<Product> galleryList = new ArrayList<Product>();

            for(int i=0;i<jsonArray.length();i++){
                JSONObject json = (JSONObject) jsonArray.get(i);

                //VO에 옮겨담기
                Product product = new Product();
                product.setP_subcategory_id(json.getInt("p_subcategory_id"));
                product.setProduct_id(json.getInt("product_id"));
                product.setProduct_name(json.getString("product_name"));
                product.setPrice(json.getInt("price"));
                product.setDetail(json.getString("detail"));
                product.setFilename(json.getString("filename"));
                product.setStock(json.getInt("stock"));
                product.setHit(json.getInt("hit"));
                product.setRegdate(json.getString("regdate"));

                SearchImageDownLoaderAsync downAsync = new SearchImageDownLoaderAsync(product, searchFragment);
                downAsync.execute("http://192.168.219.101:8888/resources/data/basic/"+product.getProduct_id()+"."+product.getFilename());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}