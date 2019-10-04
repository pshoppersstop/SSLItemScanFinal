package com.sslitemscan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.sslitemscan.Constants.Constants;
import com.sslitemscan.global.PreferencesManager;
import com.sslitemscan.views.barcodescannerview.ScanData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    BarcodeReader barcodeReader;
    private ArrayList<ScanData> name = new ArrayList<ScanData>();
    WeakHashMap<String, ScanData> h = new WeakHashMap<String, ScanData>();
    private SharedPreferences mSharedPreferences;
    private final static String PREF_FILE = "SSLPreference";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        mSharedPreferences = SSLApplication.getInstance().getContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

        getAllScannedData();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        Log.e("MainActivity", "Current Timestamp: " + format+"*****STOREID:"+PreferencesManager.getInstance().getString(Constants.STOREID));

        h.put(format,new ScanData(barcode.displayValue,format,PreferencesManager.getInstance().getString(Constants.STOREID)));

        PreferencesManager.getInstance().saveHashMap(h);

        this.runOnUiThread(new Runnable() {
            public void run() {
                showDialouge(barcode.displayValue);
            }
        });

    }
    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {

    }


    private void showDialouge(String productCode){

        new AlertDialog.Builder(this)
                .setMessage("Scanned Product Code: \n\n"+productCode)
                .setPositiveButton("Scan Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getAllScannedData(){


        String scanDataHashMapValue = null;

            Map<String, ?> allEntries = mSharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
             scanDataHashMapValue =(String)allEntries.get("ScanDataHashMap");

            Log.e("map values", entry.getKey() + ": " + entry.getValue().toString());
            Log.e("ScanDataHashMapValues","****"+scanDataHashMapValue);
        }


        JSONObject jsonObject = null;
        String scanString = "";
        try {
            jsonObject = new JSONObject(scanDataHashMapValue.trim());
            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    // do something with jsonObject here
                   // Log.e("jsonObject","****"+((JSONObject) jsonObject.get(key)).getString("barCode"));
                    scanString = scanString+""+((JSONObject) jsonObject.get(key)).getString("barCode")+"."+((JSONObject) jsonObject.get(key)).getString("storeId")
                            +"."+((JSONObject) jsonObject.get(key)).getString("timeStamp")+"|";
                }
            }

            Log.e("scanString","+++++"+scanString);
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
