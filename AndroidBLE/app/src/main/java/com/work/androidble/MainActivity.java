package com.work.androidble;

        import android.Manifest;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothGatt;
        import android.bluetooth.BluetoothGattCallback;
        import android.bluetooth.BluetoothGattCharacteristic;
        import android.bluetooth.BluetoothGattDescriptor;
        import android.bluetooth.BluetoothGattService;
        import android.bluetooth.BluetoothManager;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button saomiao , duanzhen , changzhen , buting , tingxia;
    private TextView jibu , dianliang , lianjiezhuangtai;
    private ListView list;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    List<BluetoothDevice> deviceList = new ArrayList<>();
    BluetoothDevice bluetoothDevice;



    //add
    List<String> serviceslist = new ArrayList<String>();
    BluetoothGattService bluetoothGattServices;
    BluetoothGattCharacteristic characteristic_zd,characteristic_jb,characteristic_dl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //蓝牙管理，这是系统服务可以通过getSystemService(BLUETOOTH_SERVICE)的方法获取实例
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        //通过蓝牙管理实例获取适配器，然后通过扫描方法（scan）获取设备(device)
        bluetoothAdapter = bluetoothManager.getAdapter();



    }

    private void initView() {
        saomiao = (Button) findViewById(R.id.saomiao);
        duanzhen = (Button) findViewById(R.id.zhendong);
        changzhen = (Button) findViewById(R.id.changzhen);
        buting = (Button) findViewById(R.id.buting);
        tingxia = (Button) findViewById(R.id.tingxia);
        list = (ListView) findViewById(R.id.list);

        jibu = (TextView) findViewById(R.id.jibu);
        dianliang = (TextView) findViewById(R.id.dianliang);
        lianjiezhuangtai = (TextView) findViewById(R.id.lianjiezhuangtai);

        saomiao.setOnClickListener(this);
        duanzhen.setOnClickListener(this);
        changzhen.setOnClickListener(this);
        buting.setOnClickListener(this);
        tingxia.setOnClickListener(this);

        //item 监听事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothDevice = deviceList.get(i);
                //连接设备的方法,返回值为bluetoothgatt类型
                bluetoothGatt = bluetoothDevice.connectGatt(MainActivity.this, false, gattcallback);
                lianjiezhuangtai.setText("连接" + bluetoothDevice.getName() + "中...");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saomiao:
                //开始扫描前开启蓝牙
                Intent turn_on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turn_on, 0);
                Toast.makeText(MainActivity.this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();

                Thread scanThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TAG", "run: saomiao ...");
                        saomiao();
                    }
                });
                scanThread.start();

                break;
            case R.id.zhendong:

                break;
            case R.id.changzhen:

                break;
            case R.id.buting:

                break;
            case R.id.tingxia:

                break;
            case R.id.list:

                break;

        }
    }


    //扫描回调
    public BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.i("TAG", "onLeScan: " + bluetoothDevice.getName() + "/t" + bluetoothDevice.getAddress() + "/t" + bluetoothDevice.getBondState());

            //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
            if (!deviceList.contains(bluetoothDevice)) {
                //将设备加入列表数据中
                deviceList.add(bluetoothDevice);

                list.setAdapter(new MyAdapter(MainActivity.this , deviceList));

            }

        }
    };
    public void saomiao(){
        deviceList.clear();
        bluetoothAdapter.startLeScan(callback);
    }

    private BluetoothGattCallback gattcallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String status;
                    switch (newState) {
                        //已经连接
                        case BluetoothGatt.STATE_CONNECTED:
                            lianjiezhuangtai.setText("已连接");

                            //该方法用于获取设备的服务，寻找服务
                            bluetoothGatt.discoverServices();
                            break;
                        //正在连接
                        case BluetoothGatt.STATE_CONNECTING:
                            lianjiezhuangtai.setText("正在连接");
                            break;
                        //连接断开
                        case BluetoothGatt.STATE_DISCONNECTED:
                            lianjiezhuangtai.setText("已断开");
                            break;
                        //正在断开
                        case BluetoothGatt.STATE_DISCONNECTING:
                            lianjiezhuangtai.setText("断开中");
                            break;
                    }
                    //pd.dismiss();
                }
            });
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status ==bluetoothGatt.GATT_SUCCESS){
                final List<BluetoothGattService> services = bluetoothGatt.getServices();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (final BluetoothGattService bluetoothGattService:services) {
                            bluetoothGattServices = bluetoothGattService;
                            Log.i("TAG", "onServicesDiscovered:" + bluetoothGattService.getUuid());
                            List<BluetoothGattCharacteristic> charc = bluetoothGattService.getCharacteristics();
                            for (BluetoothGattCharacteristic chararc : charc) {
                                Log.i("TAG", "run:" + chararc.getUuid());
                                if (chararc.getUuid().toString().equals("00002a20-0000-1000-8000-00805f9b34fb")) {
                                    characteristic_zd = chararc;
                                } else if (chararc.getUuid().toString().equals("0000fedf-0000-1000-8000-00805f9b34fb")) {
                                    //设备 步数
                                    characteristic_jb = chararc;
                                    bluetoothGatt.readCharacteristic(characteristic_jb);
                                    Log.i("TAG", "run:正在尝试读取步数");
                                } else if (chararc.getUuid().toString().equals("")) {
//                                    characteristic_dl = chararc;
//                                    bluetoothGatt.readCharacteristic(characteristic_dl);
//
//                                    Log.i("TAG", "电量");

                                }
                            }
                            serviceslist.add(bluetoothGattService.getUuid().toString());

                        }

                    }
                });
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == bluetoothGatt.GATT_SUCCESS){
                final int sum = characteristic.getValue()[0];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         jibu.setText("走了"+sum+"步");
                    }
                });
                Log.e("TAG","onCharacterListRead:"+characteristic.getValue()[0]);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);


            Log.i("TAG", "onCharacteristicChanged: 获取回调方法");

            Log.e("", "命令：" );

            final byte[] values = characteristic.getValue();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "lalaa", Toast.LENGTH_SHORT).show();
                }
            });
















        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    } ;
    private boolean enableNotification(boolean enable, BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || characteristic == null)
            return false;
        if (!bluetoothGatt.setCharacteristicNotification(characteristic, enable))
            return false;
        BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if (clientConfig == null)
            return false;

        if (enable) {
            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        return bluetoothGatt.writeDescriptor(clientConfig);
    }
}