package com.vphealthy.oximeterdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.vphealthy.oximetersdk.OxiOprateManager;
import com.vphealthy.oximetersdk.listener.base.IABleConnectStatusListener;
import com.vphealthy.oximetersdk.listener.base.IABluetoothStateListener;
import com.vphealthy.oximetersdk.listener.base.IBleWriteResponse;
import com.vphealthy.oximetersdk.listener.data.OnACKDataListener;
import com.vphealthy.oximetersdk.listener.data.OnAdcDataListener;
import com.vphealthy.oximetersdk.listener.data.OnBatteryDataListener;
import com.vphealthy.oximetersdk.listener.data.OnDetectDataListener;
import com.vphealthy.oximetersdk.listener.data.OnDeviceDataListener;
import com.vphealthy.oximetersdk.model.data.AckData;
import com.vphealthy.oximetersdk.model.data.AdcCurveData;
import com.vphealthy.oximetersdk.model.data.BatteryData;
import com.vphealthy.oximetersdk.model.data.DetectData;
import com.vphealthy.oximetersdk.model.data.DeviceData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpateActivity extends Activity {
    private final static String TAG = OpateActivity.class.getSimpleName();

    private IBleWriteResponse getBleWriteResponse() {
        return new IBleWriteResponse() {
            @Override
            public void onResponse(int state) {
                if (state == Code.REQUEST_SUCCESS) {
                    Log.i(TAG, getString(R.string.write_success));
                } else {
                    Log.i(TAG, getString(R.string.write_fail));
                }
            }
        };
    }

    @BindView(R.id.sys_bluetooth_state)
    TextView mSystemBlueInfoTv;
    @BindView(R.id.connect_state)
    TextView mConnectInfoTv;
    @BindView(R.id.result_baseinfo)
    TextView mResultBaseinfoTv;
    @BindView(R.id.result_battery_info)
    TextView mResultBatteryinfoTv;
    @BindView(R.id.start_recevice_data_tv)
    TextView mReceviceDataTv;
    @BindView(R.id.detect_result_tv)
    TextView mDetectResultTv;
    @BindView(R.id.curve_result_tv)
    TextView mDetectCurveTv;
    String mac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mac = getIntent().getStringExtra("mac");
        listenState(mac);
    }


    private void listenState(String mac) {
        OxiOprateManager.getMangerInstance(getApplicationContext()).registerBluetoothStateListener(new IABluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean isOpen) {
                String msg;
                if (isOpen) {
                    msg = getString(R.string.sys_blue_state_open);
                } else {
                    msg = getString(R.string.sys_blue_state_close);
                }
                Log.i(TAG, msg);
                mSystemBlueInfoTv.setText(msg);
            }
        });
        OxiOprateManager.getMangerInstance(getApplicationContext()).registerConnectStatusListener(mac, new IABleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int code) {
                if (code == Constants.STATUS_CONNECTED) {
                    String connectStr = getString(R.string.bluetooth_state_connect);
                    Log.i(TAG, connectStr);
                    mConnectInfoTv.setText(connectStr);
                } else {
                    String unConnectStr = getString(R.string.bluetooth_state_disconnect);
                    Log.i(TAG, unConnectStr);
                    mConnectInfoTv.setText(unConnectStr);
                }
            }
        });
    }

    @OnClick(R.id.oprate_readbase_info)
    public void readBasenfo() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).readDeviceBaseInfo(getBleWriteResponse(), new OnDeviceDataListener() {
            @Override
            public void onDataChange(DeviceData deviceData) {
                Log.i(TAG, deviceData.toString());
                mResultBaseinfoTv.setText(getString(R.string.base_info) + ":" + deviceData.toString());
            }
        });
    }


    @OnClick(R.id.oprate_readbattery_info)
    public void readBatterynfo() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).readBatterInfo(getBleWriteResponse(), new OnBatteryDataListener() {
            @Override
            public void onDataChange(BatteryData batteryData) {
                Log.i(TAG, batteryData.toString());
                mResultBatteryinfoTv.setText(getString(R.string.battery_info) + ":" + batteryData.toString());
            }
        });
    }

    @OnClick(R.id.start_recevice_data)
    public void start_recevice_data() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).startListenTestData(getBleWriteResponse(), new OnACKDataListener() {
            @Override
            public void onDataChange(AckData ackData) {
                mReceviceDataTv.setText(getString(R.string.start_recevice_data) + ":" + ackData.toString());
            }
        });
    }

    @OnClick(R.id.stop_recevice_data)
    public void stop_recevice_data() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).stopListenTestData(getBleWriteResponse(), new OnACKDataListener() {
            @Override
            public void onDataChange(AckData ackData) {
                mReceviceDataTv.setText(getString(R.string.stop_recevice_data) + ":" + ackData.toString());
            }
        });
    }

    @OnClick(R.id.get_curve_data)
    public void getCurveData() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).setLightDataCallBack(true, new OnAdcDataListener() {
            @Override
            public void onDataChange(AdcCurveData adcCurveData) {
                mDetectCurveTv.setText(getString(R.string.curve_result) + ":" + adcCurveData.toString());
            }
        });
    }

    @OnClick(R.id.detect_result)
    public void listenDetectResult() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).setOnDetectDataListener(new OnDetectDataListener() {
            @Override
            public void onDataChange(DetectData detectData) {
                mDetectResultTv.setText(getString(R.string.detect_result) + ":" + detectData.toString());
            }
        });
    }

    @OnClick(R.id.disconnect_device)
    public void disconnectDevice() {
        OxiOprateManager.getMangerInstance(getApplicationContext()).disconnectWatch(mac);
        finish();
    }

}
