# OximeterSDK
OximeterSDK是一个用于快速与蓝牙BLE交互的工具包，仅提供给我们合作的客户下载使用，方便提升客户的开发效率。


README: [English](waiting) | 
        [中文](https://github.com/OximeterSDK/android/edit/master/README.md)

## 必要条件

    
   * API>19&&BLE 4.0  
   * [jar](https://github.com/OximeterSDK/android/tree/master/oximeter_1.0.0)

## 如何使用

### 1. 配置 build.gradle

    compile files('libs/ximeter_x.x.x.jar')  
    compile files('libs/vpbluetooth_x.x.x.jar')  
    compile files('libs/gson-x.x.x.jar') 或者 compile 'com.google.code.gson:gson:x.x.x'  

### 2. 配置 Androidmanifest.xml

    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    
    <!--Activity&Service-->
    <service android:name="com.inuker.bluetooth.library.BluetoothService" />        

### 3. 蓝牙通信连接


    操作说明:所有的操作都只是通过OxiOprateManager;
    
    3.1 获取OxiOprateManager实例： OxiOprateManager.getMangerInstance()
    3.2 扫描蓝牙设备: startScanDevice();
    3.3 连接蓝牙设备: connectDevice();
    3.4 设置连接设备的连接状态监听:registerConnectStatusListener(); //这个方法最好是在连接成功后设置
    3.5 设置系统蓝牙的开关状态监听:registerBluetoothStateListener(); //这个方法可以在任何状态下设置
    3.6 其他数据交互操作
    
    备注1：
    为了避免内存泄漏的情况，请谨慎使用context,推荐使用getApplicationContext();
    如：获取OxiOprateManager实例：OxiOprateManager.getMangerInstance(getApplicationContext())；
    
    备注2： 
    如调用扫描设备:OxiOprateManager.getMangerInstance().startScanDevice()；
    以上统一简写为:startScanDevice();
    
    备注3:
    设备不支持异步操作,当多个耗时操作同时进行时,可能会导致数据异常;因此在与设备进行交互时,尽可能避免多个操作同时进行

### 4. 蓝牙数据交互说明

    SDK在蓝牙数据的下发的设计是只需要调用方法,传入设置参数以及监听接口,当数据有返回时,接口会触发回调,以confirmDevicePwd为例 
    //调用方法,匿名内部类用于回调监听
    OxiOprateManager.getMangerInstance(mContext).readBatterInfo(writeResponse, new  OnBatteryDataListener () {
                @Override
                public void onDataChange(BatteryData batteryData) {
                    //触发回调
                    String message = "batteryData:\n" + batteryData.toString();
                    Logger.t(TAG).i(message);
                }
            });
            
### 5. 固件升级说明



## 鸣谢

* [BluetoothKit](https://github.com/dingjikerbo/BluetoothKit)  
* [Gson](https://github.com/google/gson)  



## 许可协议
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

    Copyright (C) 2010 Michael Pardo
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.








