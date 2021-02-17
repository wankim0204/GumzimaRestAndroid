package com.koreait.gumzimarest.product;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.gumzimarest.MainActivity;
import com.koreait.gumzimarest.R;

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

public class CategoryFragment extends Fragment {
    String TAG=this.getClass().getName();
    Spinner topSpinner, subSpinner;
    ArrayList topCategory_NameList;
    ArrayList<TopCategory> topCategoryList;
    ArrayList subCategory_NameList;
    ArrayList<SubCategory> subCategoryList;
    ArrayAdapter topAdapter;
    ArrayAdapter subAdapter;
    TopCategory topCategory;
    SubCategory subCategory;
    Handler topHandler;
    Handler subHandler;
    ArrayList<Product> productList=new ArrayList<Product>();
    GridView gridView;
    GirdViewAdapter girdViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.category_fragment, container, false);
        topSpinner=view.findViewById(R.id.topcategory);
        subSpinner=view.findViewById(R.id.subcategory);
        topCategoryList=new ArrayList<TopCategory>();
        subCategoryList=new ArrayList<SubCategory>();
        gridView=view.findViewById(R.id.gridView);
        girdViewAdapter=new GirdViewAdapter((MainActivity)this.getContext());
        gridView.setAdapter(girdViewAdapter);

        //상위 카테고리
        topCategory_NameList=new ArrayList();
        topAdapter=new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, topCategory_NameList);
        topSpinner.setAdapter(topAdapter);
        //하위 카테고리
        subCategory_NameList=new ArrayList();
        subCategory_NameList.add("-하위 카테고리-");
        subAdapter=new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, subCategory_NameList);
        subSpinner.setAdapter(subAdapter);
        //탑 카테고리 핸들러
        topHandler=new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg) {
                topAdapter.notifyDataSetChanged();
                topSpinner.invalidate();
            }
        };
        subHandler=new Handler(Looper.getMainLooper()){
            public void handleMessage(@NonNull Message msg) {
                subAdapter.notifyDataSetChanged();
                subSpinner.invalidate();
                subSpinner.setSelection(0);
            }
        };
        getTopCategory();
        getProduct(0);
        topSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    int index=position-1;
                    getSubCategory(topCategoryList.get(index).getTopCategory_id());
                    productList.removeAll(productList);
                    getProduct(topCategoryList.get(index).getTopCategory_id());
                }else{
                    subCategory_NameList.removeAll(subCategory_NameList);
                    subCategory_NameList.add("-하위 카테고리-");
                    subHandler.sendEmptyMessage(0);
                    productList.removeAll(productList);
                    getProduct(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    int index=position-1;
                    getSubProduct(subCategoryList.get(index).getSubCategory_id());
                }else{
                    girdViewAdapter.productList=productList;
                    girdViewAdapter.notifyDataSetChanged();
                    gridView.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    public void getTopCategory(){
        Thread thread = new Thread(){
            public void run() {
                BufferedReader buffr=null;
                try {
                    URL url=new URL("http://192.168.219.101:8888/rest/topcategory");
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    buffr = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
                    StringBuilder sb = new StringBuilder(); //data값이 누적될 객체선언
                    String data =null;
                    while(true){
                        data = buffr.readLine();
                        if(data==null)break;
                        sb.append(data);
                    }
                    con.getResponseCode();
                    try {
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        topCategory_NameList.add("-상위 카테고리-");
                        for(int i=0;i<jsonArray.length();i++){
                            topCategory=new TopCategory();
                            JSONObject json =(JSONObject) jsonArray.get(i);
                            topCategory_NameList.add(json.getString("name"));
                            topCategory.setName(json.getString("name"));
                            topCategory.setTopCategory_id(json.getInt("p_topcategory_id"));
                            topCategoryList.add(topCategory);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    topHandler.sendEmptyMessage(0);
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
            }
        };
        thread.start();
    }

    public void getSubCategory(int topcategory_id){
        Thread thread = new Thread(){
            public void run() {
                BufferedReader buffr=null;
                try {
                    URL url=new URL("http://192.168.219.101:8888/rest/subcategory/"+topcategory_id);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    buffr = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
                    StringBuilder sb = new StringBuilder(); //data값이 누적될 객체선언
                    String data =null;
                    while(true){
                        data = buffr.readLine();
                        if(data==null)break;
                        sb.append(data);
                    }
                    con.getResponseCode();
                    try {
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        subCategory_NameList.removeAll(subCategory_NameList);
                        subCategoryList.removeAll(subCategoryList);
                        subCategory_NameList.add("-전체-");
                        for(int i=0;i<jsonArray.length();i++){
                            subCategory=new SubCategory();
                            JSONObject json =(JSONObject) jsonArray.get(i);
                            subCategory_NameList.add(json.getString("name"));
                            subCategory.setName(json.getString("name"));
                            subCategory.setTopCategory_id(json.getInt("p_topcategory_id"));
                            subCategory.setSubCategory_id(json.getInt("p_subcategory_id"));
                            subCategoryList.add(subCategory);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    subHandler.sendEmptyMessage(0);
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
            }
        };
        thread.start();
    }

    public void getProduct(int topcategory_id){
        CategoryAsync categoryAsync  = new CategoryAsync(this, topcategory_id); //비동기 객체 생성

        categoryAsync.execute("http://192.168.219.101:8888/rest/getProduct/"+topcategory_id);
    }

    public void getSubProduct(int subcategory_id){
        ArrayList<Product> subProductList=new ArrayList<Product>();
        for(int i=0; i<productList.size();i++){
            Product product=productList.get(i);
            if(product.getP_subcategory_id()==subcategory_id){
                subProductList.add(product);
            }
            girdViewAdapter.productList=subProductList;
            girdViewAdapter.notifyDataSetChanged();
            gridView.invalidate();
        }
    }
}
