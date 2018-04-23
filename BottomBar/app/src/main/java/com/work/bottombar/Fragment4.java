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

public class Fragment4 extends Fragment {
    String data;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4,null);
        textView = (TextView)view.findViewById(R.id.textView4);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        data = ((MainActivity)getActivity()).getData();
        textView.setText(data);
//        if(isAdded()){
//            if(getArguments()!=null){
//                // textView.setText(getArguments().getString("Text"));
//                textView.setText(getArguments().getString("Txt"));
//                Log.e("TAG", "have data4");
//
//            }else{
//                Log.e("TAG", "no data4");
//            }        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
