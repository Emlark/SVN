package com.work.bottombar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class Fragment2 extends Fragment {
    private TextView textView;
    String data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,null);
        textView = (TextView)view.findViewById(R.id.textView2);
//
//        Log.e("TAG", "textViwe2");
//        if(getArguments()!=null){
//            textView.setText(getArguments().getString("Txt"));
//            Log.e("TAG", "have data2");
//
//        }else{
//            Log.e("TAG", "no data2");
//        }
        return view;
    }
    public static Fragment2 newInstance(String text){
        Fragment2 fragment = new Fragment2();
        Bundle bundle = new Bundle();
        bundle.putString("Text",text);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override

    public void onStart() {
        super.onStart();
        if(isAdded()){
            if(getArguments()!=null){
                textView.setText(getArguments().getString("Txt"));
                Log.e("TAG", "have data2");

            }else{
                Log.e("TAG", "no data2");
            }        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = ((MainActivity)getActivity()).getData();
        textView.setText(data);

    }

}
