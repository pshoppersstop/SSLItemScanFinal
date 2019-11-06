package com.sslitemscan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    @BindView(R.id.edittext_username)
    EditText mETUsername;

    @BindView(R.id.edittext_password)
    EditText mETPassword;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);

        if ( null != PreferencesManager.getInstance().getString(Constants.STOREID) && PreferencesManager.getInstance().getString(Constants.STOREID).length()>0){
            //LoadNextActivity();
            mETStoreCode.setText(PreferencesManager.getInstance().getString(Constants.STOREID));
            mETStoreCode.setTextColor(Color.WHITE);
            mETStoreCode.setEnabled(false);
        }else {
            mETStoreCode.setEnabled(true);
        }
    }

    private void LoadNextActivity(){
        hideKeyboard(this);
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(Constants.STOREID,PreferencesManager.getInstance().getString(Constants.STOREID));
        startActivity(intent);
        finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick(R.id.button_scan_code)
    public void saveStoreCode(){
        String username = null;
        String password = null;
        if(null != mETUsername.getText()){
             username =  mETUsername.getText().toString();
        }

        if(null != mETPassword.getText()){
             password = mETPassword.getText().toString();
        }

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            if(   ( (username.equals("U636") && password.equals("P908")) ||
                    (username.equals("U725") && password.equals("P175")) ||
                    (username.equals("U336") && password.equals("P008")) ||
                    (username.equals("U918") && password.equals("P754")) ||
                    (username.equals("U141") && password.equals("P423")) ||
                    (username.equals("U196") && password.equals("P588")) ||
                    (username.equals("U196") && password.equals("P588")) ||
                    (username.equals("U700") && password.equals("P100")) ||
                    (username.equals("U989") && password.equals("P967"))||
                    (username.equals("U474") && password.equals("P422"))||
                    (username.equals("U647") && password.equals("P941")) ||
                    (username.equals("U920") && password.equals("P760")) ||
                    (username.equals("U839") && password.equals("P517")) ||
                    (username.equals("U126") && password.equals("P378")) ||
                    (username.equals("U683") && password.equals("P049")) ||
                    (username.equals("U477") && password.equals("P431")) ||
                    (username.equals("U743") && password.equals("P229")) ||
                    (username.equals("U983") && password.equals("P949")) ||
                    (username.equals("U832") && password.equals("P496"))||
                    (username.equals("U201") && password.equals("P603")) )) {

                if(!TextUtils.isEmpty(mETStoreCode.getText())){
                    PreferencesManager.getInstance().setBoolean(Constants.ISLOADED,true);
                    PreferencesManager.getInstance().setString(Constants.STOREID,mETStoreCode.getText().toString().trim());
                    LoadNextActivity();
                }else {
                    Toast.makeText(mContext," Please Enter StoreCode",Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(mContext," Username and Password does not match ",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(mContext," Username and Password cannot be empty",Toast.LENGTH_SHORT).show();
        }


    }

}
