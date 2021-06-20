package com.example.wifirssi;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

public class availableNetworkActivity extends AppCompatActivity {

    ListView listView;
    String[] textString;
    int[] drawableIds;
    CustomAdapter adapter;
    WifiManager mWifiManager;



    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = mWifiManager.getScanResults();
                Log.e("", "value" + mScanResults + "\n\n");
                textString = new String[ mScanResults.size()];
                drawableIds = new int[ mScanResults.size()];

                for(int i = 0; i < mScanResults.size(); i++) {
                textString[i] = mScanResults.get(i).SSID + " " + mScanResults.get(i).BSSID;
                Log.e("", "value -----------------------------------------------------------" + textString[i]);
                if (mScanResults.get(i).level <= 0 && mScanResults.get(i).level >= -50) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_4_bar_black_24dp;
                    //Best signal
                } else if (mScanResults.get(i).level < -50 && mScanResults.get(i).level >= -70) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_3_bar_black_24dp;
                    //Good signal
                } else if (mScanResults.get(i).level < -70 && mScanResults.get(i).level >= -80) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_2_bar_black_24dp;
                    //Low signal
                } else if (mScanResults.get(i).level < -80 && mScanResults.get(i).level >= -100) {
                    drawableIds[i] = R.drawable.ic_signal_wifi_1_bar_black_24dp;
                    //Very weak signal
                } else {
                    drawableIds[i] = R.drawable.ic_signal_wifi_0_bar_black_24dp;
                    // no signals
                }
            }
            adapter = new CustomAdapter(availableNetworkActivity.this,  textString, drawableIds);
            listView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_network);

        listView = (ListView) findViewById(R.id.menuList);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        startScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }

    public void startScan(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWifiManager.startScan();
                startScan();
                Log.e("","value" + "after 2 seconds ---------");
            }
        }, 1*1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


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