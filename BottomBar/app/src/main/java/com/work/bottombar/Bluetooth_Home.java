package com.work.bottombar;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Bluetooth_Home extends AppCompatActivity{

    private  ListView list ;
    private  static final long  SCAN_PERIOD=10000;

    private  Boolean mScanning;
    private   String myDeviceName;
    private  String myDeviceAddress;


    BluetoothAdapter bluetoothAdapter;
    //BluetoothGatt bluetoothGatt;
    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    BluetoothDevice bluetoothDevice;
    MyAdapter myAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_home);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        initView();
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();



    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        scan(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        if(mScanning)
        {menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.profressbar);
            Log.e("TAG", "扫描1");
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
                Log.e("TAG", "扫描2");

                scan(true);

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

        mScanning=false;
        list = (ListView)findViewById(R.id.list);
        myAdapter = new MyAdapter(Bluetooth_Home.this , deviceList);


        Intent turn_on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turn_on,0);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothDevice = deviceList.get(position);
                myDeviceName = bluetoothDevice.getName();
                myDeviceAddress = bluetoothDevice.getAddress();
                Start();
//                    bluetoothGatt = bluetoothDevice.connectGatt(MainActivity.this,false,gattcallback);
//                    connect.setText("连接"+bluetoothDevice.getName()+"中....");
            }
        });
    }




    public  void Start(){
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRAS_DEVICE_NAME, myDeviceName);
        intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS,myDeviceAddress);
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
                    if (!deviceList.contains(device)) {
                        deviceList.add(device);
                        mHandler.sendEmptyMessage(1);
                        list.setAdapter(new MyAdapter(Bluetooth_Home.this , deviceList));
                    }
                }
            });
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


