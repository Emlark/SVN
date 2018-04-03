package com.work.bluetoothle;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import  android.view.View.OnClickListener;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements OnClickListener{
    private Button open,chuankou,changzhen,nostop,stop;
    private TextView walk_counting,energy,connect;
    private  ListView list ;
    private  static final long  SCAN_PERIOD=10000;

    private  Boolean mScanning;
    private  Boolean selected;
    private   String myDeviceName;
    private  String myDeviceAddress;


    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    BluetoothDevice bluetoothDevice;
    MyAdapter myAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Android M Permission check   Widget
//            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//            }
//        }
//
//    }
//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        if(mScanning)
        {menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.profressbar);

        }
        else{
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_scan:
                Toast.makeText(this,"scan",Toast.LENGTH_SHORT).show();

                scan(true);
//                Thread scanThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("TAG","run:scaning...");
//                        mScanning=true;
//                        scan(true);
//                    }
//                });
//                scanThread.start();
                break;
            case R.id.menu_stop:
                scan(false);
                Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


    private void initView(){
        open =(Button)findViewById(R.id.open_device);
        chuankou = (Button)findViewById(R.id.chuankou);
        changzhen = (Button)findViewById(R.id.changzhen);
        nostop = (Button)findViewById(R.id.nostop);
        stop=(Button)findViewById(R.id.stop);
       mScanning=false;
        selected=false;
            list = (ListView)findViewById(R.id.list);
          myAdapter = new MyAdapter(MainActivity.this , deviceList);
        walk_counting = (TextView)findViewById(R.id.walk_counting);
        energy = (TextView)findViewById(R.id.energy);
        connect = (TextView)findViewById(R.id.connect);

            open.setOnClickListener(this);
            chuankou.setOnClickListener(this);
            changzhen.setOnClickListener(this);
            nostop.setOnClickListener(this);
            stop.setOnClickListener(this);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    bluetoothDevice = deviceList.get(position);
                    myDeviceName = bluetoothDevice.getName();
                    myDeviceAddress = bluetoothDevice.getAddress();
                    connect.setText("已选择"+myDeviceName);
                    selected=true;
//                    bluetoothGatt = bluetoothDevice.connectGatt(MainActivity.this,false,gattcallback);
//                    connect.setText("连接"+bluetoothDevice.getName()+"中....");
                }
            });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_device:
            Intent turn_on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turn_on,0);
            Toast.makeText(MainActivity.this,"蓝牙已经开启",Toast.LENGTH_SHORT).show();


            break;
           case  R.id.chuankou:
               if(selected){
                  Send();
               }else {
                   Toast.makeText(this,"设备未连接",Toast.LENGTH_SHORT).show();
               }

            break;


            case R.id.changzhen:break;
            case R.id.nostop:break;
            case R.id.stop:break;
            default:break;
        }
    }

    public  void Send(){
        final Intent intent = new Intent(this, Send.class);
        intent.putExtra(Send.EXTRAS_DEVICE_NAME, myDeviceName);
        intent.putExtra(Send.EXTRAS_DEVICE_ADDRESS,myDeviceAddress);
        if (mScanning) {
            bluetoothAdapter.stopLeScan(callback);
            mScanning = false;
        }
        startActivity(intent);
    }


    public void scan(final  boolean enable){
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        bluetoothAdapter.stopLeScan(callback);
                        invalidateOptionsMenu();
                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            myAdapter.clear();
            mHandler.sendEmptyMessage(1);

            bluetoothAdapter.startLeScan(callback);
            // bluetoothAdapter.startScan(callback);

        }else{
            mScanning = false;
            bluetoothAdapter.stopLeScan(callback);
        }
        invalidateOptionsMenu();
    }





    private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("TAG","onLeScan :" + device.getName() + "/t"+device.getAddress()+"/t"+device.getBondState());
runOnUiThread(new Runnable() {
    @Override
    public void run() {
       // myAdapter.addDevice(device);

        if (!deviceList.contains(device)) {
            //将设备加入列表数据中
            deviceList.add(device);
            mHandler.sendEmptyMessage(1);
//            list.setAdapter(myAdapter);
            list.setAdapter(new MyAdapter(MainActivity.this , deviceList));

        }
    }
});
//            if (!deviceList.contains(device))
            //  }
        }
    };
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // Notify change
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    class MyAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> myList;
        private LayoutInflater mInflater;
        public MyAdapter(Context context , ArrayList<BluetoothDevice> list){

            myList =list;
            mInflater = LayoutInflater.from(context);
        }
        public void addDevice(BluetoothDevice device) {
            if(!myList.contains(device)) {
                myList.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return myList.get(position);
        }

        public void clear() {
            myList.clear();
        }
        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.devices_item,null);
                viewHolder.name = (TextView)convertView.findViewById(R.id.bluetoothname);
                viewHolder.uuid = (TextView) convertView.findViewById(R.id.uuid);
                viewHolder.status = (TextView)convertView.findViewById(R.id.status);

                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BluetoothDevice bd = myList.get(position);
            viewHolder.name.setText(bd.getName());
            viewHolder.uuid.setText(bd.getAddress());
            return convertView;
        }


    }
    static class ViewHolder{
        private  TextView name,uuid,status;
    }

}

