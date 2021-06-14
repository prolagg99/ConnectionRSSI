package com.example.wifirssi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    private int mInterval = 2000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    IntentFilter intentFilter = new IntentFilter();

    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;
    TextView tvWifiSignal, tvRouterMacAddress;
    ImageView ivWifiSignal;
    Button wifiRSSIActivity, gsmRSSIActivity;


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("app","Network connectivity change ----------------------------- conected wifi");

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            // get SSID
            String ssid = wifiInfo.getSSID();
            int rssi = wifiInfo.getRssi();
            tvWifiSignal.setText(getResources().getString(R.string.wifi_signal, ssid, rssi));

            // get @ MAC
            String BSSID = wifiInfo.getBSSID();
            tvRouterMacAddress.setText(getResources().getString(R.string.mac_address, BSSID));

            // get RSSI
            int numberOfLevels = 5;
            int level = WifiManager.compareSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//            levelOfSignal(level, ivWifiSignal);

                if (wifiInfo.getRssi() <= 0 && wifiInfo.getRssi() >= -50) {
                    ivWifiSignal.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
                    //Best signal
                } else if (wifiInfo.getRssi() < -50 && wifiInfo.getRssi() >= -70) {
                    ivWifiSignal.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
                    //Good signal
                } else if (wifiInfo.getRssi() < -70 && wifiInfo.getRssi() >= -80) {
                    ivWifiSignal.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
                    //Low signal
                } else if (wifiInfo.getRssi() < -80 && wifiInfo.getRssi() >= -100) {
                    ivWifiSignal.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
                    //Very weak signal
                } else {
                    ivWifiSignal.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
                    // no signals
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        startRepeatingTask();

        wifiRSSIActivity = (Button) findViewById(R.id.wifiRSSIActivity);
        wifiRSSIActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request permission in run time
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                            },
                            REQUEST_CODE_ASK_PERMISSIONS);
                }

                Intent intent = new Intent(MainActivity.this, availableNetworkActivity.class);
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(intent);
            }
        });

        tvWifiSignal = (TextView) findViewById(R.id.tvWifiSignal);
        tvRouterMacAddress = (TextView) findViewById(R.id.tvRouterMacAddress);
        ivWifiSignal = (ImageView) findViewById(R.id.ivWifiSignal);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(networkChangeReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();


        gsmRSSIActivity = (Button) findViewById(R.id.gsmRSSIActivity);
        gsmRSSIActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CellularActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                registerReceiver(networkChangeReceiver, intentFilter);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

//    private void levelOfSignal(int level, ImageView ivSignalLevel) {
//        if (level <= 0 && level >= -50) {
//            //Best signal
//            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
//        } else if (level < -50 && level >= -70) {
//            //Good signal
//            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
//        } else if (level < -70 && level >= -80) {
//            //Low signal
//            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
//        } else if (level < -80 && level >= -100) {
//            //Very weak signal
//            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
//        } else {
//            // no signals
//            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
//        }
//    }
}

