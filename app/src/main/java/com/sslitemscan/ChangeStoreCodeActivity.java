package com.sslitemscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;

public class ChangeStoreCodeActivity extends AppCompatActivity {

    @BindView(R.id.edittext_store_code)
    EditText mETStoreCode;

    @BindView(R.id.edittext_reenter_store_code)
    EditText mETReEnterStoreCode;

    @BindView(R.id.button_save)
    Button mButtonSaveDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_store_code);
    }
}
