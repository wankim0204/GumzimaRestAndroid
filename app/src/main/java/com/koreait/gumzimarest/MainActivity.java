package com.koreait.gumzimarest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=this;
        this.getSupportActionBar().setTitle("Gumzima Shop");
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(this.getSupportFragmentManager(), 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        bottomNavigationView=this.findViewById(R.id.navi_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:showPage(0);mainActivity.getSupportActionBar().setTitle("Gumzima Shop");break;
                    case R.id.category:showPage(1);mainActivity.getSupportActionBar().setTitle("카테고리별 상품");break;
                    case R.id.search:showPage(2);mainActivity.getSupportActionBar().setTitle("상품명 검색");break;
                }
                return true;
            }
        });
        showPage(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showPage(int position){
        viewPager.setCurrentItem(position);
    }
}