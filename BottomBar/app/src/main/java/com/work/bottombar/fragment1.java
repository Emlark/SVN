package com.work.bottombar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class fragment1 extends Fragment {
    FragmentCallBack fragmentCallBack = null;
    TextView textView,textView3;
    Button button ;
    String data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment1,null);
        Log.e("TAG", "textViwe");
        textView = (TextView)view.findViewById(R.id.textView1);

        return view;
    }



    public static fragment1 newInstance(String text){
        fragment1 fragment = new fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("Text",text);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = ((MainActivity)getActivity()).getData();
        textView.setText(data);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(isAdded()){
              //  Log.e("TAG", "Txt1,isAdded"+getArguments().getString("Txt"));
//            if(getArguments()!=null){
//                textView.setText(getArguments().getString("Txt"));
//                Log.e("TAG", "have data");
//
//            }else{
//                Log.e("TAG", "no data");
//            }

        }
    }
}
