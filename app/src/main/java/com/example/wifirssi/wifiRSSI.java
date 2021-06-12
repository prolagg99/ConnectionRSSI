package com.example.wifirssi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class wifiRSSI extends AppCompatActivity {

    private int mInterval = 2000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 13;
    IntentFilter intentFilter = new IntentFilter();
    WifiManager wifiManager;
    List<ScanResult> wifiList;
    ListView listView;
    String[] textString;
    CustomAdapter adapter;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("app","Network connectivity change  ----------------------------- available network");

            wifiManager.startScan();
            wifiList = wifiManager.getScanResults();
            textString = new String[wifiList.size()];
            int[] drawableIds = new int[wifiList.size()];

            for(int i = 0; i < wifiList.size(); i++) {
                textString[i] = wifiList.get(i).SSID;
                if (wifiList.get(i).level <= 0 && wifiList.get(i).level >= -50) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_4_bar_black_24dp;
                    //Best signal
                } else if (wifiList.get(i).level < -50 && wifiList.get(i).level >= -70) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_3_bar_black_24dp;
                    //Good signal
                } else if (wifiList.get(i).level < -70 && wifiList.get(i).level >= -80) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_2_bar_black_24dp;
                    //Low signal
                } else if (wifiList.get(i).level < -80 && wifiList.get(i).level >= -100) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_1_bar_black_24dp;
                    //Very weak signal
                } else {
                    drawableIds[i] = R.drawable.ic_signal_wifi_0_bar_black_24dp;
                    // no signals
                }
            }
            adapter = new CustomAdapter(wifiRSSI.this,  textString, drawableIds);
            listView.setAdapter(adapter);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_rssi);

        mHandler = new Handler();
        startRepeatingTask();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(networkChangeReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        listView = (ListView) findViewById(R.id.menuList);
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

    // class of adapter to display each element of listView with icon
    public class CustomAdapter extends BaseAdapter {

        private Context mContext;
        private String[]  Title;
        private int[] imge;

        public CustomAdapter(Context context, String[] text1,int[] imageIds) {
            mContext = context;
            Title = text1;
            imge = imageIds;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return Title.length;
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.row, parent, false);
            TextView title;
            ImageView i1;
            i1 = (ImageView) row.findViewById(R.id.imgIcon);
            title = (TextView) row.findViewById(R.id.txtTitle);
            title.setText(Title[position]);
            i1.setImageResource(imge[position]);
            return (row);
        }
    }

}
