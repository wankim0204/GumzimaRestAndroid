package com.koreait.gumzimarest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.koreait.gumzimarest.product.CategoryFragment;
import com.koreait.gumzimarest.product.SearchFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    Fragment[] fragments=new Fragment[3];
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragments[0]=new HomeFragment();
        fragments[1]=new CategoryFragment();
        fragments[2]=new SearchFragment();
    }

    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
