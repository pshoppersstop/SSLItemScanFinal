package com.sslitemscan;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sslitemscan.Constants.Constants;
import com.sslitemscan.global.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.edittext_store_code)
    EditText mETStoreCode;

    @BindView(R.id.button_scan_code)
    Button mButtonScanCode;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);

        if ( PreferencesManager.getInstance().getBoolean(Constants.ISLOADED,false)){
            LoadNextActivity();
        }
    }

    private void LoadNextActivity(){
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(Constants.STOREID,PreferencesManager.getInstance().getString(Constants.STOREID));
        startActivity(intent);
    }

    @OnClick(R.id.button_scan_code)
    public void saveStoreCode(){
        if(!TextUtils.isEmpty(mETStoreCode.getText())){
            PreferencesManager.getInstance().setBoolean(Constants.ISLOADED,true);
            PreferencesManager.getInstance().setString(Constants.STOREID,mETStoreCode.getText().toString());
            LoadNextActivity();
        }else {
            Toast.makeText(mContext," Please Enter StoreCode",Toast.LENGTH_SHORT).show();
        }
    }

}
