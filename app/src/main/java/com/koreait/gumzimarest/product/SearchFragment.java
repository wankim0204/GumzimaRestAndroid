package com.koreait.gumzimarest.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.gumzimarest.MainActivity;
import com.koreait.gumzimarest.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    String TAG=this.getClass().getName();
    ArrayList<Product> productList=new ArrayList<Product>();
    GridView gridView;
    GirdViewAdapter girdViewAdapter;
    EditText t_search;
    Button bt_search;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.search_fragment, container, false);
        t_search=view.findViewById(R.id.t_search);
        bt_search=view.findViewById(R.id.bt_search);
        gridView=view.findViewById(R.id.gridView);
        girdViewAdapter=new GirdViewAdapter((MainActivity)this.getContext());
        gridView.setAdapter(girdViewAdapter);

        bt_search.setOnClickListener(e->{
            productList.removeAll(productList);
            girdViewAdapter.notifyDataSetChanged();
            gridView.invalidate();
            if(!t_search.getText().toString().equals("")){
                searchProduct(t_search.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    public void searchProduct(String text){
        SearchAsync searchAsync  = new SearchAsync(this, text);

        searchAsync.execute("http://192.168.219.101:8888/rest/searchProduct/"+text);
    }
}
