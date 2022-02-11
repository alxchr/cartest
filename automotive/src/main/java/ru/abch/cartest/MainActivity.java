package ru.abch.cartest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadsetClient;

import android.bluetooth.BluetoothProfile;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private BluetoothHeadsetClient mBluetoothHeadsetClient;
    public final int HEADSET_CLIENT = 16;
    BluetoothAdapter mAdapter;
    private static final int REQ_PERMISSIONS = 1230, REQUEST_ENABLE_BT = 1231;
    List<BluetoothDevice> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> requestPermissionsList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissionsList.add(Manifest.permission.BLUETOOTH);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissionsList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsList.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        if(requestPermissionsList.size() > 0) {
            String[] requestPermissionsArray = new String[requestPermissionsList.size()];
            requestPermissionsArray = requestPermissionsList.toArray(requestPermissionsArray);
            ActivityCompat.requestPermissions(this, requestPermissionsArray, REQ_PERMISSIONS);
        }
        mAdapter = getSystemService(BluetoothAdapter.class);
        if(mAdapter != null) {
            Log.d(TAG, "BT adapter " + mAdapter.getName() + " " + mAdapter.ge);
        } else {
            Log.d(TAG, "No BT adapter");
        }

        mBluetoothHeadsetClient = getSystemService(BluetoothHeadsetClient.class);
        if(mBluetoothHeadsetClient != null) list = mBluetoothHeadsetClient.getConnectedDevices();
        if (list == null) {
            Log.d(TAG, "No BT devices");
        } else {
            for (BluetoothDevice dev : list) {
                Log.d(TAG, "BT device " + dev.getName() + " type " + dev.getType());
            }
        }
        Log.d(TAG,"HSP client" + mAdapter.getProfileProxy(getApplicationContext(), mHfpServiceListener,
                HEADSET_CLIENT));
    }
    private final BluetoothProfile.ServiceListener mHfpServiceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == HEADSET_CLIENT) {
                mBluetoothHeadsetClient = (BluetoothHeadsetClient) proxy;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == HEADSET_CLIENT) {
                mBluetoothHeadsetClient = null;
            }
        }
    };
}