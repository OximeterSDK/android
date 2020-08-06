package com.vphealthy.oximeterdemo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

public class ScanListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<SearchResult> itemData;

    public ScanListAdapter(Context mContext, List<SearchResult> itemData) {
        this.mContext = mContext;
        this.itemData = itemData;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (itemData != null) {
            return itemData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return itemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScanItemHolder mScanItemHolder;
        if (convertView == null) {
            mScanItemHolder = new ScanItemHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_connect_device, null);
            mScanItemHolder.mBleAddress = convertView.findViewById(R.id.ble_list_address);
            mScanItemHolder.mBleName = convertView.findViewById(R.id.ble_list_name);
            mScanItemHolder.mBleRssi = convertView.findViewById(R.id.ble_list_rssi);
            convertView.setTag(mScanItemHolder);
        } else {
            mScanItemHolder = (ScanItemHolder) convertView.getTag();
        }
        SearchResult bleDevice = itemData.get(position);
        mScanItemHolder.mBleName.setText(bleDevice.getName());
        mScanItemHolder.mBleAddress.setText(bleDevice.getAddress());
        mScanItemHolder.mBleRssi.setText("");
        return convertView;
    }

    static class ScanItemHolder {
        TextView mBleName;
        TextView mBleAddress;
        TextView mBleRssi;
    }
}
