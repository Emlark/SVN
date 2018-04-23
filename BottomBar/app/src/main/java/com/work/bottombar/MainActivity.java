package com.work.bottombar;
import java.util.ArrayList;
import java.util.List;
//import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;


import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;



public class MainActivity extends AppCompatActivity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";


    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BottomNavigationView bottomNavigationView;
    private String showdata= "0";
    Fragment fragment=null;
//    private TextView textView;
//
//    private Button button;
 // private Noslide myViewPager;
   // private ViewPager mViewPager;
//    private List<Fragment> list;
//    private TabFragmentPagerAdapter adapter;
//    private FragmentTransaction transaction;
//    private FragmentManager manager;
//

    public String getData(){
        return showdata;
    }
    public void setData(String data){
        this.showdata = data;
    }




    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {  //连接成功
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                mConnected = false;
                invalidateOptionsMenu();

                // clearUI();
            }else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //可以开始干活了
            {
                mConnected = true;

                show();


                invalidateOptionsMenu();
            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                if (data != null) {
                    Log.e("TT","data is coming");

                    showdata = data;

                   // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2.newInstance(showdata)).commit();

                }else{
                    showdata = "0";
                }
                if( fragment==null){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Txt",showdata);
//                    fragment.setArguments(bundle);
//                    FragmentManager manager = getSupportFragmentManager();
//                    FragmentTransaction transaction = manager.beginTransaction();
                   // transaction.replace(R.id.fragment_container,fragment);
                    Log.e("TT","fragment is null");

                }else {
                    Log.e("TT","fragment is not null");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Txt",showdata);
//                    fragment.setArguments(bundle);
//                    FragmentManager manager1 = getSupportFragmentManager();
//                    FragmentTransaction transaction1 = manager1.beginTransaction();
//                     transaction1.replace(R.id.fragment_container,fragment);
//                    transaction1.commit();

                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                              //点击按钮
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                if(mConnected)
                {
                    mBluetoothLeService.disconnect();
                    mConnected = false;
                }
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBluetoothLeService != null)
        {
            mBluetoothLeService.close();
            mBluetoothLeService = null;
        }
    }


    private void show(){
        Toast.makeText(this,"连接成功",Toast.LENGTH_SHORT).show();

    }

    private static IntentFilter makeGattUpdateIntentFilter() {                        //注册接收的事件
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        fragment1 = new fragment1();
//        fragment2 = new Fragment2();
//        fragment3 = new Fragment3();
//        fragment4 = new Fragment4();
//        manager = getSupportFragmentManager();
       // FragmentTransaction fragmentTransaction = manager.beginTransaction();


//        fragmentTransaction.add(R.id.main_layout,fragment1);
//        fragmentTransaction.addToBackStack(null);



//        textView = (TextView) findViewById(R.id.textView);
//        button = (Button) findViewById(R.id.button);
      //  myViewPager = (Noslide) findViewById(R.id.myViewPager);
     //  myViewPager = (ViewPager)findViewById(R.id.mViewPager);
       // myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        init();





//        textView1 = (TextView)findViewById(R.id.textView1);
//        textView2 = (TextView)findViewById(R.id.textView2);
//        textView3 = (TextView)findViewById(R.id.textView3);
//        textView4 = (TextView)findViewById(R.id.textView4);

  //      list = new ArrayList<>();

//        list.add(new  fragment1());
//        list.add(new  Fragment2());
//        list.add(new  Fragment3());
//        list.add(new  Fragment4());
        String[] titles ={"温度","湿度","甲醛","GitHub"};



   //     adapter = new TabFragmentPagerAdapter(manager,list,titles);
//        myViewPager.setAdapter(adapter);
//        myViewPager.setCurrentItem(0);
//        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                bottomNavigationView.selectTab(position);
//            }
//
//
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        //  myViewPager.setCanScroll(false);







        int[] image = {R.drawable.temp, R.drawable.water,
                R.drawable.gas, R.drawable.github_circle};
        int[] color = {ContextCompat.getColor(this, R.color.firstColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.fourthColor)};

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(false);
          //  bottomNavigationView.disableViewPagerSlide();
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.text_active));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.text_inactive));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.firstColor));
            bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_normal.ttf"));
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("温度", color[0], image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("湿度", color[1], image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("甲醛", color[2], image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("GitHub", color[3], image[3]);

        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
//                myViewPager.setCurrentItem(index);

                switch (index) {
                    case 0:
                        fragment = new fragment1();
                        break;
                    case 1:
                        fragment = new Fragment2();
                        break;
                    case 2:
                        fragment = new Fragment3();
                         break;
                    case 3:
                        fragment = new Fragment4();
                       break;
                }

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container,fragment);

                transaction.commit();
               // Toast.makeText(MainActivity.this, "Item " +index +" clicked", Toast.LENGTH_SHORT).show();

                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }
        });

//        bottomNavigationView.setUpWithViewPager(myViewPager, color,image);

    }

   public void init(){
       fragment = new fragment1();
       FragmentManager manager = getSupportFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.fragment_container,fragment);

       transaction.commit();
       final Intent intent = getIntent();
       mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
       mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
       Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
       bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);//绑定服务

       registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
      // mBluetoothLeService.connect(mDeviceAddress);
   }



}
