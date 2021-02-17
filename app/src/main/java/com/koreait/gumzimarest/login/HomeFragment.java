package com.koreait.gumzimaregist.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.gumzimaregist.MainActivity;
import com.koreait.gumzimaregist.R;

public class HomeFragment extends Fragment {
    View view;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity=(MainActivity)this.getContext();
        return view;
    }
}