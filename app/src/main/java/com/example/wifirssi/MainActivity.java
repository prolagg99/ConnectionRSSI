package com.example.wifirssi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // for wifi list
    TextView tvWifiList;
    WifiManager wifiManager;


    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;
    TextView tvCellIdCode1, tvWifiSignal, tvRouterIpAddress, tvRouterMacAddress,
            tvNetworkType, tvCellRSSI, tvCellIDCode2;
    ImageView ivWifiSignal, ivWifiSignalList;
    Button btnCellId, wifiRSSIActivity;


    ListView listView;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;


    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("app","Network connectivity change");

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
            levelOfSignal(level, ivWifiSignal);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                Intent intent = new Intent(MainActivity.this, wifiRSSI.class);
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


        // for wifi list
        tvWifiList = (TextView) findViewById(R.id.tvWifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        tvCellIdCode1 = (TextView) findViewById(R.id.tvCellIDCode1);
        tvWifiSignal = (TextView) findViewById(R.id.tvWifiSignal);
        tvRouterIpAddress = (TextView) findViewById(R.id.tvRouterIpAddress);
        tvRouterMacAddress = (TextView) findViewById(R.id.tvRouterMacAddress);
        ivWifiSignal = (ImageView) findViewById(R.id.ivWifiSignal);
        btnCellId = (Button) findViewById(R.id.btnCellID);


        listView = (ListView) findViewById(R.id.wifiList);
        adapter =  new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(networkChangeReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        btnCellId.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                // for wifi list
                // check if GPS turned OFF
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
//                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//                    buildAlertMessageNoGps();
//                }
//                if (mainWifi.isWifiEnabled() == false)
//                {
//                    // If wifi disabled then enable it
//                    Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
//                            Toast.LENGTH_LONG).show();
//                    mainWifi.setWifiEnabled(true);
//                }







                // get the SSID & RSSI & ip_@ of wireless network wifi
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

//                String ssid = wifiInfo.getSSID();
//                int rssi = wifiInfo.getRssi();
//                tvWifiSignal.setText(getResources().getString(R.string.wifi_signal, ssid, rssi));

//                Toast.makeText(ConnectivityActivity.this,"SSID " + ssid + " RSSI " + rssi, Toast.LENGTH_LONG).show();

                // get the Mac @ of router
//                String BSSID = wifiInfo.getBSSID();
//                Toast.makeText(MainActivity.this,"BSSID " + BSSID, Toast.LENGTH_LONG).show();
//                tvRouterMacAddress.setText(getResources().getString(R.string.mac_address, BSSID));

                // get the ip_@ of wifi
//                final DhcpInfo dhcp = wifiManager.getDhcpInfo();
//                int ipAddress = dhcp.gateway;
//                ipAddress = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
//                        Integer.reverseBytes(ipAddress) : ipAddress;
//                byte[] ipAddressByte = BigInteger.valueOf(ipAddress).toByteArray();
//                try {
//                    InetAddress myAddr = InetAddress.getByAddress(ipAddressByte);
//                    tvRouterIpAddress.setText(getResources().getString(R.string.ip_address, myAddr.getHostAddress()));
////                    Toast.makeText(ConnectivityActivity.this,"ip address " + myAddr.getHostAddress(), Toast.LENGTH_LONG).show();
//                }catch (Exception e){
//                }

                // get the signal level and display it as icon
//                int numberOfLevels = 5;
//                int level = WifiManager.compareSignalLevel(wifiInfo.getRssi(), numberOfLevels);
//                Toast.makeText(ConnectivityActivity.this,"level " + level, Toast.LENGTH_LONG).show();

//                levelOfSignal(level, ivWifiSignal);

//                Excellent >-50 dBm
//                Good -50 to -60 dBm
//                Fair -60 to -70 dBm
//                Weak < -70 dBm


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(networkChangeReceiver);
    }

    private void levelOfSignal(int level, ImageView ivSignalLevel) {
        if (level <= 0 && level >= -50) {
            //Best signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
        } else if (level < -50 && level >= -70) {
            //Good signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
        } else if (level < -70 && level >= -80) {
            //Low signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
        } else if (level < -80 && level >= -100) {
            //Very weak signal
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
        } else {
            // no signals
            ivSignalLevel.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
        }
    }


}

//        part[0] = "Signalstrength:"  _ignore this, it's just the title_
//
//        parts[1] = GsmSignalStrength
//
//        parts[2] = GsmBitErrorRate
//
//        parts[3] = CdmaDbm
//
//        parts[4] = CdmaEcio
//
//        parts[5] = EvdoDbm
//
//        parts[6] = EvdoEcio
//
//        parts[7] = EvdoSnr
//
//        parts[8] = LteSignalStrength
//
//        parts[9] = LteRsrp
//
//        parts[10] = LteRsrq
//
//        parts[11] = LteRssnr
//
//        parts[12] = LteCqi
//
//        parts[13] = gsm|lte|cdma
//
//        parts[14] = _not really sure what this number is_
