package com.fimi.kernel.connect.ble.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.fimi.kernel.utils.LogUtil;


public class JellyBeanBleScanner extends BaseBleScanner {
    private static final String TAG = JellyBeanBleScanner.class.getName();
    public BluetoothAdapter mBluetooth;
    private SimpleScanCallback mScanCallback;
    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            JellyBeanBleScanner.this.mScanCallback.onBleScan(device, rssi, scanRecord);
        }
    };

    public JellyBeanBleScanner(Context context, SimpleScanCallback callback) {
        this.mBluetooth = null;
        this.mScanCallback = null;
        this.mScanCallback = callback;
        BluetoothManager bluetoothMgr = (BluetoothManager) context.getSystemService("bluetooth");
        this.mBluetooth = bluetoothMgr.getAdapter();
    }

    @Override
    public void onStartBleScan(long timeoutMillis) {
        long delay = timeoutMillis == 0 ? 10000L : timeoutMillis;
        if (this.mBluetooth != null) {
            this.isScanning = this.mBluetooth.startLeScan(this.leScanCallback);
            this.timeoutHandler.postDelayed(this.timeoutRunnable, delay);
            LogUtil.i(TAG, "mBluetooth.startLeScan() " + this.isScanning);
            return;
        }
        this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
    }

    @Override
    public void onStartBleScan() {
        if (this.mBluetooth != null) {
            this.isScanning = this.mBluetooth.startLeScan(this.leScanCallback);
            LogUtil.i(TAG, "mBluetooth.startLeScan() " + this.isScanning);
            return;
        }
        this.mScanCallback.onBleScanFailed(BleScanState.BLUETOOTH_OFF);
    }

    @Override
    public void onStopBleScan() {
        this.isScanning = false;
        if (this.mBluetooth != null) {
            this.mBluetooth.stopLeScan(this.leScanCallback);
        }
    }

    @Override
    public void onBleScanFailed(BleScanState scanState) {
        this.mScanCallback.onBleScanFailed(scanState);
    }
}
