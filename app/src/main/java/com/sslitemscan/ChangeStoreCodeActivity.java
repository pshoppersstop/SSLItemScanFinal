package com.sslitemscan;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sslitemscan.Constants.Constants;
import com.sslitemscan.global.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeStoreCodeActivity extends AppCompatActivity {

    @BindView(R.id.edittext_store_code)
    EditText mETStoreCode;

    @BindView(R.id.edittext_reenter_store_code)
    EditText mETReEnterStoreCode;

    @BindView(R.id.button_save)
    Button mButtonSaveDetails;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_store_code);
        mContext = this;
        ButterKnife.bind(this);
    }


    @OnClick(R.id.button_save)
    public void changeStoreId(){

        if( !TextUtils.isEmpty(mETStoreCode.getText().toString()) && !(TextUtils.isEmpty(mETReEnterStoreCode.getText().toString())) ){

            if (  (mETStoreCode.getText().toString()).equals(mETReEnterStoreCode.getText().toString())  ){
                PreferencesManager.getInstance().setString(Constants.STOREID,mETStoreCode.getText().toString());
                Toast.makeText(mContext,"Store Code Changed Sucessfully",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext,"Store Code Should Be Same",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(mContext,"Store Code Should Not Be Empty",Toast.LENGTH_SHORT).show();

        }
    }
}
