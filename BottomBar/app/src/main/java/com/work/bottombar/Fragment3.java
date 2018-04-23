package com.work.bottombar;

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

public class Fragment3 extends Fragment {
    private Button button ;
    private TextView textView;
    private  TextView getter;
    String data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3,null);
        Log.e("TAG", "textViwe3");
        textView = (TextView)view.findViewById(R.id.textView3);

        return view;
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
//        if(isAdded()){
//            if(getArguments()!=null){
//                // textView.setText(getArguments().getString("Text"));
//                textView.setText(getArguments().getString("Txt"));
//                Log.e("TAG", "have data3");
//
//            }else{
//                Log.e("TAG", "no data3");
//            }        }
    }
}
