package com.sslitemscan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.sslitemscan.Constants.Constants;
import com.sslitemscan.views.barcodescannerview.ZXingScannerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigateToScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final int ZXING_CAMERA_PERMISSION = 1;

    @BindView(R.id.content_frame)
    ViewGroup contentFrame;

    private ZXingScannerView mScannerView = null;

    private String mSearchTextAndQuery;
    private String storeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_to_scan);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.STOREID)){
            storeCode = intent.getStringExtra(Constants.STOREID);
        }

        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);
        }

      //  Log.e("storeCode","*********"+storeCode);
    }


    @Override
    protected void onResume() {
        super.onResume();

    /*    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
            mScannerView.setFlash(true);
        }*/


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
       // mScannerView.setFlash(false);
       // mScannerView.resumeCameraPreview(NavigateToScanActivity.this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Added for Adobe Analytics
        if(null != mScannerView) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, true);
    }


    @Override
    public void handleResult(Result rawResult) {

        Log.e("ScannedCode", "---------------------");

        if (null != rawResult) {
            String mScannType = ResultParser.parseResult(rawResult).getType().toString();
            mSearchTextAndQuery = rawResult.getText();
            if (mScannType.equalsIgnoreCase("URI")) {//  for QR code result nevigation we'll check URI in zxing callback result
                Log.e("ScannedCode", "+++++" + Uri.parse(mSearchTextAndQuery));
            }
        }
        else {
            Log.e("ScannedCode", "**********");

         /*   Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(NavigateToScanActivity.this);
                }
            }, 2000);*/
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mScannerView = new ZXingScannerView(this);
                    contentFrame.addView(mScannerView);


                    return;
                }else{
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }


    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu != null && menu.size() > 0) {
            mFlashMenu = menu.findItem(R.id.menu_flash_onoff);
            mFlashMenu.setVisible(true);
        }
        return true;
    }*/

}
