package com.example.franck.myapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franck.myapp.contract.CryptoAnchors;

import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback, AdapterView.OnItemClickListener {
    private static final String TAG = "BluetoothGattActivity";

    private static final String DEVICE_NAME = "SensorTag";
    private BluetoothGatt mBluetoothGatt;
    /* Humidity Service */
    private static final UUID HUMIDITY_SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    private static final UUID HUMIDITY_DATA_CHAR = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
    private static final UUID HUMIDITY_CONFIG_CHAR = UUID.fromString("f000aa22-0451-4000-b000-000000000000");
    /* Barometric Pressure Service */
    private static final UUID PRESSURE_SERVICE = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    private static final UUID PRESSURE_DATA_CHAR = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    private static final UUID PRESSURE_CONFIG_CHAR = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    private static final UUID PRESSURE_CAL_CHAR = UUID.fromString("f000aa43-0451-4000-b000-000000000000");
    /* Client Configuration Descriptor */
    private static final UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter mBluetoothAdapter;
    private SparseArray<BluetoothDevice> mDevices;

    private BluetoothGatt mConnectedGatt;

    private TextView mTemperature;
    private Button button;
    private ImageView visibleTextView;
    private Button info;
    public Set<BluetoothDevice> mBTDevices = new HashSet<BluetoothDevice>();
    private DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    private Button bondes;
    private Button writeSign;
    private Button readPublic;
    String path;
    private TextView scan_bluetooth;


    private ProgressDialog mProgress;
    private String deviceAddress;
    private ProgressBar progressBar;
    private TextView visibleTextInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.blue_activity);
        setProgressBarIndeterminate(true);

        visibleTextView = findViewById(R.id.visible);

        lvNewDevices = (ListView) findViewById(R.id.list_item);
        mBTDevices = new HashSet<>();

        /*
         * We are going to display the results in some text fields
         */
        button = findViewById(R.id.scan);
        progressBar = findViewById(R.id.progress_bar);
        /*
         * Bluetooth in Android 4.3 is accessed via the BluetoothManager, rather than
         * the old static BluetoothAdapter.getInstance()
         */
        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        bondes = findViewById(R.id.bondes);
        readPublic = findViewById(R.id.read_public);
        writeSign = findViewById(R.id.write_sign);
        visibleTextInfo = findViewById(R.id.textInvisible);
        mDevices = new SparseArray<BluetoothDevice>();
        lvNewDevices.setOnItemClickListener(BluetoothActivity.this);
        /*
         * A progress dialog will be needed while the connection process is
         * taking place
         */
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        scan_bluetooth = findViewById(R.id.scan_device);

        bondes.setClickable(false);
        writeSign.setClickable(false);
        readPublic.setClickable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new MyTask().execute();

                try {

                    String filename;

                    filename = "UTC--2018-04-21T17-43-12_948279000Z--59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2";
                    String string = jsonUTC;

                    File f = new File(getApplicationContext().getFilesDir(), filename);
                    FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    fos.write(string.getBytes());
                    fos.flush();
                    fos.close();
                    Log.d("ETHR", "jsonUTC");
                    path = f.getAbsolutePath();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                btnDiscover(v);

            }
        });
    }

    public void btnDiscover(View view) {
        visibleTextView.setVisibility(View.GONE);
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if (mBluetoothAdapter.isDiscovering()) {
            button.setText("Cancel");
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            button.setText("Scan");
            //check BT permissions in manifest

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, new ArrayList<BluetoothDevice>(mBTDevices));
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        bondes.setClickable(false);
        writeSign.setClickable(false);
        readPublic.setClickable(false);
        bondes.setVisibility(View.GONE);
        writeSign.setVisibility(View.GONE);
        readPublic.setVisibility(View.GONE);
        bondes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPublic.setVisibility(View.GONE);
                writeSign.setVisibility(View.GONE);
                visibleTextInfo.setVisibility(View.VISIBLE);
               new MyTask().execute();
                mBluetoothAdapter.cancelDiscovery();
                setbondes(v.getContext());
            }
        });

        readPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.cancelDiscovery();
                connectGatt(deviceAddress,  mGattCallback1);
                    Toast.makeText(getApplicationContext(), "Read public from anchor", Toast.LENGTH_LONG).show();

                }

            }
        );

        writeSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "Write sign from anchor", Toast.LENGTH_LONG).show();

            }
        });


        /*
         * We need to enforce that Bluetooth is first enabled, and take the
         * user to settings to enable it if they have not done so.
         */
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //Bluetooth is disabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }

        /*
         * Check for Bluetooth LE Support.  In production, our manifest entry will keep this
         * from installing on these devices, but this will allow test devices or other
         * sideloads to report whether or not the feature exists.
         */
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        clearDisplayValues();
    }

    private void setbondes(Context context) {
        visibleTextView.setVisibility(View.GONE);
        if (mDeviceListAdapter != null) {
            mDeviceListAdapter.clear();
            mDeviceListAdapter.notifyDataSetChanged();
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, new ArrayList<BluetoothDevice>(pairedDevices));
        lvNewDevices.setAdapter(mDeviceListAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Make sure dialog is hidden
        mProgress.dismiss();
        //Cancel any scans in progress
        mHandler.removeCallbacks(mStopRunnable);
        mHandler.removeCallbacks(mStartRunnable);
//        mBluetoothAdapter.stopLeScan(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Disconnect from any active tag connection
        if (mConnectedGatt != null) {
            mConnectedGatt.disconnect();
            mConnectedGatt = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the "scan" option to the menu
        getMenuInflater().inflate(R.menu.main, menu);
        //Add any device elements we've discovered to the overflow menu
        for (int i = 0; i < mDevices.size(); i++) {
            BluetoothDevice device = mDevices.valueAt(i);
            menu.add(0, mDevices.keyAt(i), 0, device.getName());
        }

        return true;
    }

    private void clearDisplayValues() {

    }

    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };

    private void startScan() {
        mBluetoothAdapter.startLeScan(this);
        setProgressBarIndeterminateVisibility(true);

        mHandler.postDelayed(mStopRunnable, 2500);
    }

    private void stopScan() {
        mBluetoothAdapter.stopLeScan(this);
        setProgressBarIndeterminateVisibility(false);
    }

    /* BluetoothAdapter.LeScanCallback */

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi);
        /*
         * We are looking for SensorTag devices only, so validate the name
         * that each device reports before adding it to our collection
         */
        if (DEVICE_NAME.equals(device.getName())) {
            mDevices.put(device.hashCode(), device);
            //Update the overflow menu
            invalidateOptionsMenu();
        }
    }

    /*
     * In this callback, we've created a bit of a state machine to enforce that only
     * one characteristic be read or written at a time until all of our sensors
     * are enabled and we are registered to get notifications.
     */
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /* State Machine Tracking */
        private int mState = 0;

        private void reset() {
            mState = 0;
        }

        private void advance() {
            mState++;
        }

        /*
         * Send an enable command to each sensor by writing a configuration
         * characteristic.  This is specific to the SensorTag to keep power
         * low by disabling sensors you aren't using.
         */
        private void enableNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Enabling pressure cal");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_CONFIG_CHAR);
                    characteristic.setValue(new byte[] {0x02});
                    break;
                case 1:
                    Log.d(TAG, "Enabling pressure");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_CONFIG_CHAR);
                    characteristic.setValue(new byte[] {0x01});
                    break;
                case 2:
                    Log.d(TAG, "Enabling humidity");
                    characteristic = gatt.getService(HUMIDITY_SERVICE)
                            .getCharacteristic(HUMIDITY_CONFIG_CHAR);
                    characteristic.setValue(new byte[] {0x01});
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }

            gatt.writeCharacteristic(characteristic);
        }

        /*
         * Read the data characteristic's value for each sensor explicitly
         */
        private void readNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Reading pressure cal");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_CAL_CHAR);
                    break;
                case 1:
                    Log.d(TAG, "Reading pressure");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_DATA_CHAR);
                    break;
                case 2:
                    Log.d(TAG, "Reading humidity");
                    characteristic = gatt.getService(HUMIDITY_SERVICE)
                            .getCharacteristic(HUMIDITY_DATA_CHAR);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }

            gatt.readCharacteristic(characteristic);
        }

        /*
         * Enable notification of changes on the data characteristic for each sensor
         * by writing the ENABLE_NOTIFICATION_VALUE flag to that characteristic's
         * configuration descriptor.
         */
        private void setNotifyNextSensor(BluetoothGatt gatt) {
            BluetoothGattCharacteristic characteristic;
            switch (mState) {
                case 0:
                    Log.d(TAG, "Set notify pressure cal");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_CAL_CHAR);
                    break;
                case 1:
                    Log.d(TAG, "Set notify pressure");
                    characteristic = gatt.getService(PRESSURE_SERVICE)
                            .getCharacteristic(PRESSURE_DATA_CHAR);
                    break;
                case 2:
                    Log.d(TAG, "Set notify humidity");
                    characteristic = gatt.getService(HUMIDITY_SERVICE)
                            .getCharacteristic(HUMIDITY_DATA_CHAR);
                    break;
                default:
                    mHandler.sendEmptyMessage(MSG_DISMISS);
                    Log.i(TAG, "All Sensors Enabled");
                    return;
            }

            //Enable local notifications
            gatt.setCharacteristicNotification(characteristic, true);
            //Enabled remote notifications
            BluetoothGattDescriptor desc = characteristic.getDescriptor(CONFIG_DESCRIPTOR);
            desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(desc);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "Connection State Change: " + status + " -> " + connectionState(newState));
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                /*
                 * Once successfully connected, we must next discover all the services on the
                 * device before we can read and write their characteristics.
                 */
                gatt.discoverServices();
                mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Discovering Services..."));
            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                /*
                 * If at any point we disconnect, send a message to clear the weather values
                 * out of the UI
                 */
                mHandler.sendEmptyMessage(MSG_CLEAR);
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                /*
                 * If there is a failure at any stage, simply disconnect
                 */
                gatt.disconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "Services Discovered: " + status);
            mHandler.sendMessage(Message.obtain(null, MSG_PROGRESS, "Enabling Sensors..."));
            /*
             * With services discovered, we are going to reset our state machine and start
             * working through the sensors we need to enable
             */
            reset();
            enableNextSensor(gatt);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //For each read, pass the data up to the UI thread to update the display
            if (HUMIDITY_DATA_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_HUMIDITY, characteristic));
            }
            if (PRESSURE_DATA_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_PRESSURE, characteristic));
            }
            if (PRESSURE_CAL_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_PRESSURE_CAL, characteristic));
            }

            //After reading the initial value, next we enable notifications
            setNotifyNextSensor(gatt);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //After writing the enable flag, next we read the initial value
            readNextSensor(gatt);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            /*
             * After notifications are enabled, all updates from the device on characteristic
             * value changes will be posted here.  Similar to read, we hand these up to the
             * UI thread to update the display.
             */
            if (HUMIDITY_DATA_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_HUMIDITY, characteristic));
            }
            if (PRESSURE_DATA_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_PRESSURE, characteristic));
            }
            if (PRESSURE_CAL_CHAR.equals(characteristic.getUuid())) {
                mHandler.sendMessage(Message.obtain(null, MSG_PRESSURE_CAL, characteristic));
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            //Once notifications are enabled, we move to the next sensor and start over with enable
            advance();
            enableNextSensor(gatt);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "Remote RSSI: " + rssi);
        }

        private String connectionState(int status) {
            switch (status) {
                case BluetoothProfile.STATE_CONNECTED:
                    return "Connected";
                case BluetoothProfile.STATE_DISCONNECTED:
                    return "Disconnected";
                case BluetoothProfile.STATE_CONNECTING:
                    return "Connecting";
                case BluetoothProfile.STATE_DISCONNECTING:
                    return "Disconnecting";
                default:
                    return String.valueOf(status);
            }
        }
    };

    /*
     * We have a Handler to process event results on the main thread
     */
    private static final int MSG_HUMIDITY = 101;
    private static final int MSG_PRESSURE = 102;
    private static final int MSG_PRESSURE_CAL = 103;
    private static final int MSG_PROGRESS = 201;
    private static final int MSG_DISMISS = 202;
    private static final int MSG_CLEAR = 301;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BluetoothGattCharacteristic characteristic;
            switch (msg.what) {
                case MSG_HUMIDITY:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining humidity value");
                        return;
                    }
                    updateHumidityValues(characteristic);
                    break;
                case MSG_PRESSURE:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining pressure value");
                        return;
                    }
                    updatePressureValue(characteristic);
                    break;
                case MSG_PRESSURE_CAL:
                    characteristic = (BluetoothGattCharacteristic) msg.obj;
                    if (characteristic.getValue() == null) {
                        Log.w(TAG, "Error obtaining cal value");
                        return;
                    }
                    updatePressureCals(characteristic);
                    break;
                case MSG_PROGRESS:
                    mProgress.setMessage((String) msg.obj);
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                    break;
                case MSG_DISMISS:
                    mProgress.hide();
                    break;
                case MSG_CLEAR:
                    clearDisplayValues();
                    break;
            }
        }
    };

    private String name;



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>(mBTDevices);
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mDevices.get(i).getName();
        deviceAddress = mDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.d(TAG, "Trying to pair with " + deviceName);

            Toast.makeText(getApplicationContext(), "Click on " + deviceName, Toast.LENGTH_LONG).show();

            bondes.setClickable(true);
            writeSign.setClickable(true);
            readPublic.setClickable(true);
            bondes.setVisibility(View.VISIBLE);
            writeSign.setVisibility(View.VISIBLE);
            readPublic.setVisibility(View.VISIBLE);
            //mDevices.get(i).createBond();

        }
    }


    private boolean connectGatt(final String address, BluetoothGattCallback mGattCallback) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        mBluetoothGatt = device.connectGatt(getApplicationContext(), true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        return mBluetoothGatt.connect();
    }

//    private final BluetoothGattCallback mGattCallback3 = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                //bluetooth is connected so discover services
//                mBluetoothGatt.discoverServices();
//
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                //Bluetooth is disconnected
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
////                gatt.readCharacteristic(gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0f-0000-1000-8000-00805f9b34fb")));
////                gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
//
//                BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
//                gatt.setCharacteristicNotification(characteristic,true);
//                characteristic.setValue(new byte[] {1,2,3,5});
//                gatt.writeCharacteristic(characteristic);
//
//            }
//
//
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt,
//                BluetoothGattCharacteristic characteristic,
//                int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt,
//                BluetoothGattCharacteristic characteristic) {
//            byte[] b = new byte[] {1,2,3,5};
//            gatt.setCharacteristicNotification(characteristic,true);
//            characteristic.setValue(new byte[] {1,2,3,5});
//            Toast.makeText(getApplicationContext(), b.toString(), Toast.LENGTH_LONG).show();
//            gatt.writeCharacteristic(characteristic);
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicWrite(gatt, characteristic, status);
//
//        }
//    };

//    private final BluetoothGattCallback mGattCallback2 = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                //bluetooth is connected so discover services
//                mBluetoothGatt.discoverServices();
//
//
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                //Bluetooth is disconnected
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
////                gatt.readCharacteristic(gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0f-0000-1000-8000-00805f9b34fb")));
////                gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
//
////                BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
////                gatt.setCharacteristicNotification(characteristic,true);
////                characteristic.setValue(new byte[] {1,2,3,5});
////                gatt.writeCharacteristic(characteristic);
//                BluetoothGattCharacteristic characteristic1 = gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
//
////                onCharacteristicRead(gatt, characteristic2, status);
////                onCharacteristicRead(gatt, characteristic1, status);
//
//                gatt.readCharacteristic(characteristic1);
//
//
//            }
//
//
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt,
//                BluetoothGattCharacteristic characteristic,
//                int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                final byte[] dataInput1 = characteristic.getValue();
//                Toast.makeText(getApplicationContext(), dataInput1.toString(), Toast.LENGTH_LONG).show();
////                final byte[] dataInput = characteristic.getValue();
//
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt,
//                BluetoothGattCharacteristic characteristic) {
//
//            gatt.setCharacteristicNotification(characteristic,true);
//            characteristic.setValue(new byte[] {1,2,3,5});
//            gatt.writeCharacteristic(characteristic);
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicWrite(gatt, characteristic, status);
//
//        }
//    };

    private final BluetoothGattCallback mGattCallback1 = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //bluetooth is connected so discover services
                mBluetoothGatt.discoverServices();
                onServicesDiscovered(gatt, status);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //Bluetooth is disconnected
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                gatt.readCharacteristic(gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0f-0000-1000-8000-00805f9b34fb")));
//                gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));

//                BluetoothGattCharacteristic characteristic = gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0e-0000-1000-8000-00805f9b34fb"));
//                gatt.setCharacteristicNotification(characteristic,true);
//                characteristic.setValue(new byte[] {1,2,3,5});
//                gatt.writeCharacteristic(characteristic);

                BluetoothGattCharacteristic characteristic2 = gatt.getService(UUID.fromString("0000ec00-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ec0f-0000-1000-8000-00805f9b34fb"));
//                onCharacteristicRead(gatt, characteristic2, status);
//                onCharacteristicRead(gatt, characteristic1, status);


                gatt.readCharacteristic(characteristic2);
                onCharacteristicRead(gatt, characteristic2, status);

            }


        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                final byte[] dataInput1 = characteristic.getValue();
                Toast.makeText(getApplicationContext(), dataInput1.toString(), Toast.LENGTH_LONG).show();
//                final byte[] dataInput = characteristic.getValue();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic) {

            gatt.setCharacteristicNotification(characteristic,true);
            characteristic.setValue(new byte[] {1,2,3,5});
            gatt.writeCharacteristic(characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

        }
    };
    /* Methods to extract sensor data and update the UI */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        if(mBroadcastReceiver3 != null && mBroadcastReceiver4 != null) {
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private void updateHumidityValues(BluetoothGattCharacteristic characteristic) {
        double humidity = SensorTagData.extractHumidity(characteristic);

    }

    private int[] mPressureCals;

    private void updatePressureCals(BluetoothGattCharacteristic characteristic) {
        mPressureCals = SensorTagData.extractCalibrationCoefficients(characteristic);
    }

    private void updatePressureValue(BluetoothGattCharacteristic characteristic) {
        if (mPressureCals == null) return;
        double pressure = SensorTagData.extractBarometer(characteristic, mPressureCals);
        double temp = SensorTagData.extractBarTemperature(characteristic, mPressureCals);

        mTemperature.setText(String.format("%.1f\u00B0C", temp));
    }

    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }
    public static byte[] StringHexToByteArray(String x) throws Exception {
        if (x.startsWith("0x")) {
            x = x.substring(2);
        }
        if (x.length() % 2 != 0) x = "0" + x;
        return Hex.decode(x);
    }
    private void updateState() throws Exception
    {
        }

    public static String jsonUTC =
            "{\"address\":\"59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2\",\"id\":\"65af76b4-3d1a-4abe-954d-7c6f9a3a68e8\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"0029f1a85a9475e5037939628876a65bb8081eabe8b7356d4714b290b991840d\",\"cipherparams\":{\"iv\":\"d80368a31abda00525dfea21d22f4460\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"9c9b94800e9c09d255e309048a7204c319318cf8f829786ea55988ee145889ee\"},\"mac\":\"ff237552eeb625c008f131824fd39b6f207ec90fd1aa541f824e799dceebc8c7\"}}\n";
    public static String jsonClient =
            "{\"address\":\"09a5dacb427cc8fd596e5b1640fa539dac1a5d6d\",\"id\":\"0f633608-267f-4e94-b4ef-4f4ba635ef89\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"4fc75a27ac790c3308f11c02a356e46cfab8796c866812a5dea9c368c19f6b56\",\"cipherparams\":{\"iv\":\"137a3d2c8c934f1afd108bea643a9ce7\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"e07cbff898dfc8db886058435d6409d32c11c247b3a9d7544ccb85894ed370e0\"},\"mac\":\"94c0986542d05b14aa481a46752d59c77ebf8443103678c44e2aa8899c326ccb\"}}";



    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            Log.d("ETHR", "ok pre exec");
            progressBar.setVisibility(View.VISIBLE);
            visibleTextInfo.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                try {
                    Web3j web3j = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/1p6X1Vby9WW11tNcTTg0"));
                    BigInteger _price = BigInteger.ONE;
                    _price = BigInteger.valueOf(10);

                    BigInteger _steps = BigInteger.ONE;
                    _steps = BigInteger.valueOf(3);
                    // We then need to load our Ethereum wallet file
                    // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
                    Credentials credentials1 = Credentials.create("9c215ede75b688ce2f30372140068c815b78b2eedfe8bd9af04d8d7fddd8ef2e");
                    CryptoAnchors contract1 = CryptoAnchors.load("0x4296b2dc215cb8e29c8fc81234dd0103f3af6e25", web3j, credentials1, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
                    name = contract1.TypeName().send();

                    Log.d("dw", contract1.TypeName().send());


                    BigInteger _latitude = new BigInteger("123");
                    BigInteger _longitude = new BigInteger("123");
                    BigInteger _v = new BigInteger("1c", 16);
                    byte[] _r = StringHexToByteArray("0xf444a383acba466a2aed2582895c614323bb97aa6b74e04f97922b176bc1aa2c");
                    byte[] _s = StringHexToByteArray("0x4f23d377cb19cc04f408a2ea4fb219d69e3bcb0a5585d06561a34712185a0e77");
                    byte[] _signedHash = StringHexToByteArray("0x8dfe9be33ccb1c830e048219729e8c01f54c768004d8dc035105629515feb38e");


                    CryptoAnchors contract = CryptoAnchors.load(
                            "0x4296b2dc215cb8e29c8fc81234dd0103f3af6e25",
                            web3j, credentials1,
                            ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
                    );

                    contract.UpdateState("asdf", _latitude, _longitude, _v, _r, _s, _signedHash, "0xf6f45356002Eee48a0333c480da441dAdFcE1373").send();





                } catch (Exception e){e.printStackTrace();}
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            visibleTextInfo.setVisibility(View.VISIBLE);
            scan_bluetooth.setText("Product: " + name);
//        Log.d("ETHR", contractAdmin.getContractAddress());
            Log.d("ETHR", "ok post Exec");
        }
    }


    class MyTaskCallback extends AsyncTask<Object, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Object... params) {
            connectGatt((String) params[0], (BluetoothGattCallback) params[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    class MyTaskCallback2 extends AsyncTask<Object, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Object... params) {
            connectGatt((String) params[0], (BluetoothGattCallback) params[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    class MyTaskCallback3 extends AsyncTask<Object, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Object... params) {
            connectGatt((String) params[0], (BluetoothGattCallback) params[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
