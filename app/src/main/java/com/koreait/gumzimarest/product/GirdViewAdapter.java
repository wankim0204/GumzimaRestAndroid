package com.koreait.gumzimarest.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koreait.gumzimarest.MainActivity;
import com.koreait.gumzimarest.R;

import java.util.ArrayList;

public class GirdViewAdapter extends BaseAdapter{
    MainActivity mainActivity;
    ArrayList<Product> productList=new ArrayList<Product>();
    LayoutInflater layoutInflater;
    public GirdViewAdapter(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        layoutInflater=mainActivity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getProduct_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null; //이 메서드에서 최종 반환할 뷰

        if(convertView == null){//레이아웃 뷰가 존재하지 않는다면.. 인플레이션 시킴
            //false의 의미 지정한 parent에 부착하지 않고, 인플레이션 대상  xml의 최상위를 반환
            view = layoutInflater.inflate(R.layout.product_item, parent, false);
        }else{//이미 존재한다면, 기존 뷰 그래도 재사용함
            view=convertView;
        }

        //리스트에 들어있는 position 번째 Gallery 추출
        Product product  =productList.get(position);

        ImageView img = view.findViewById(R.id.img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView price = view.findViewById(R.id.price);

        img.setImageBitmap(product.getBitmap());
        product_name.setText(product.getProduct_name());
        price.setText(product.getPrice()+" 원");

        img.setOnClickListener(e->{
            //Toast.makeText(mainActivity, gallery.getGallery_id()+" 선택했어?", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
