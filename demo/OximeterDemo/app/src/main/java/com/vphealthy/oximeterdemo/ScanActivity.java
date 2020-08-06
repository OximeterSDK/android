package com.vphealthy.oximeterdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.vphealthy.oximetersdk.OxiOprateManager;
import com.vphealthy.oximetersdk.listener.base.IConnectResponse;
import com.vphealthy.oximetersdk.listener.base.INotifyResponse;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends Activity implements AdapterView.OnItemClickListener {
    private final static String TAG = ScanActivity.class.getSimpleName();
    private Activity mActivity = ScanActivity.this;
    @BindView(R.id.lay_listview)
    ListView mListView;
    LinkedList<SearchResult> mBleList = new LinkedList<>();
    LinkedList<String> mBleListStr = new LinkedList<>();
    ScanListAdapter mScanListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        checkScanPermission();
        initAdapter();
    }

    private void checkScanPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请模糊定位权限
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0x10);
            }
        }
    }

    private void initAdapter() {

        mScanListAdapter = new ScanListAdapter(getApplicationContext(), mBleList);
        mListView.setAdapter(mScanListAdapter);
        mListView.setOnItemClickListener(this);
        mScanListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.scanlist_scan_start)
    public void startScans() {
        mBleListStr.clear();
        mBleList.clear();
        mScanListAdapter.notifyDataSetChanged();
        startScan();
    }

    @OnClick(R.id.scanlist_scan_stop)
    public void stopScans() {
        stopScan();
    }


    private void startScan() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).startScanDevice(new SearchResponse() {
            @Override
            public void onSearchStarted() {
                Log.i(TAG, "onSearchStarted");
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                String address = searchResult.getAddress();
                Log.i(TAG, "onDeviceFounded:" + address);
                if (!mBleListStr.contains(address)) {
                    mBleListStr.add(address);
                    mBleList.add(searchResult);
                    mScanListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSearchStopped() {
                Log.i(TAG, "onSearchStopped");
            }

            @Override
            public void onSearchCanceled() {
                Log.i(TAG, "onSearchCanceled");
            }
        });
    }

    private void stopScan() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).stopScanDevice();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchResult searchResult = mBleList.get(position);
        connect(searchResult);
    }

    private void connect(SearchResult searchResult) {
        OxiOprateManager.getMangerInstance(getApplicationContext()).connectDevice(searchResult.getAddress(), searchResult.getName(), new IConnectResponse() {
            @Override
            public void connectState(int code, BleGattProfile bleGattProfile, boolean isUpdateModel) {
                if (code == Code.REQUEST_SUCCESS) {
                    //蓝牙与设备的连接状态
                    Log.i(TAG, getString(R.string.connect_success));
                } else {
                    Log.i(TAG, getString(R.string.connect_fail));
                }
            }
        }, new INotifyResponse() {
            @Override
            public void notifyState(int state) {
                if (state == Code.REQUEST_SUCCESS) {
                    Log.i(TAG, getString(R.string.nofity_success));
                    Intent intent = new Intent(ScanActivity.this, OpateActivity.class);
                    intent.putExtra("mac", searchResult.getAddress());
                    startActivity(intent);
                } else {
                    Log.i(TAG, getString(R.string.nofity_fail));
                }
            }
        });
    }
}
