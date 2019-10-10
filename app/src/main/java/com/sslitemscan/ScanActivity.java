package com.sslitemscan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.sslitemscan.Constants.Constants;
import com.sslitemscan.global.PreferencesManager;
import com.sslitemscan.utils.DisplayUtils;
import com.sslitemscan.utils.PermissionUtil;
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
import java.util.Properties;
import java.util.WeakHashMap;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    BarcodeReader barcodeReader;
    private ArrayList<ScanData> name = new ArrayList<ScanData>();
    WeakHashMap<String, ScanData> h = new WeakHashMap<String, ScanData>();
    private SharedPreferences mSharedPreferences;
    private final static String PREF_FILE = "SSLPreference";

    private static final String senderEmail = "testershoppers2@gmail.com";
    private static final String receiptEmail = "testershoppers2@gmail.com";
    private static final String password = "cmoljryykrusoatt";
    private static final String fileName = "product.csv";
    int CAMERA_PERMISSIONS_REQUEST = 9998;
    private Toolbar mToolbar;
    private Context context;
    String scanString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        context = this;
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);


        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        mSharedPreferences = SSLApplication.getInstance().getContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

       // getAllScannedData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //barcodeReader.proceedAfterPermission();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        barcodeReader.pauseScanning();
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        Log.e("MainActivity", "Current Timestamp: " + format+"*****STOREID:"+PreferencesManager.getInstance().getString(Constants.STOREID));

        h.put(format,new ScanData(barcode.displayValue,format,PreferencesManager.getInstance().getString(Constants.STOREID)));

        PreferencesManager.getInstance().saveHashMap(h);

        this.runOnUiThread(new Runnable() {
            public void run() {
                 //barcodeReader.resumeScanning();
                showDialouge(barcode.displayValue);
            }
        });

       // barcodeReader.pauseScanning();
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


    private void showDialouge(final String productCode){

        new AlertDialog.Builder(this)
                .setMessage("Scanned Product Code: \n\n"+productCode)
                .setPositiveButton("Scan Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        barcodeReader.resumeScanning();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("DeleteProductCode","****"+productCode);
                        barcodeReader.resumeScanning();
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }

    private void getAllScannedData() {


        String scanDataHashMapValue = null;

        Map<String, ?> allEntries = mSharedPreferences.getAll();

        if (null != allEntries && allEntries.size() > 0) {

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            scanDataHashMapValue = (String) allEntries.get("ScanDataHashMap");

            Log.e("map values", entry.getKey() + ": " + entry.getValue().toString());
            Log.e("ScanDataHashMapValues", "****" + scanDataHashMapValue);
        }


        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(scanDataHashMapValue.trim());
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    // do something with jsonObject here
                    // Log.e("jsonObject","****"+((JSONObject) jsonObject.get(key)).getString("barCode"));
                    scanString = scanString + "" + ((JSONObject) jsonObject.get(key)).getString("barCode") + "," + ((JSONObject) jsonObject.get(key)).getString("storeId")
                            + "," + ((JSONObject) jsonObject.get(key)).getString("timeStamp") + "\n";
                }
            }

            Log.e("scanString", "+++++" + scanString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }else {
           Toast.makeText(context,"No scanned data found",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.day_closing) {
            alertDialog();
            return true;
        }else if(id == R.id.change_settings){
            navigatetoChangeStore();
        }


        return super.onOptionsItemSelected(item);
    }

    private void navigatetoChangeStore(){
        Intent intent = new Intent(this, ChangeStoreCodeActivity.class);
        startActivity(intent);
    }

    private void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to share the scan log for the day?");
        dialog.setPositiveButton("Send Email",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {


                        handlePermission();

                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void sendMail() {
        Session session = createSessionObject();

        try {
            MimeMessage message = createMimeMessage(session);
            new SendMailTask().execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SendMailTask extends AsyncTask<MimeMessage, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Please wait", "Sending mail", true, false);
        }


        @Override
        protected Void doInBackground(MimeMessage... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(context,"Email sent !!",Toast.LENGTH_SHORT).show();
        }
    }


    private MimeMessage createMimeMessage(Session session) {
        try {
            // Create a default MimeMessage object.
            MimeMessage messages = new MimeMessage(session);

            // Set From: header field of the header.
            messages.setFrom(new InternetAddress(senderEmail));

            // Set To: header field of the header.
            messages.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiptEmail));

            // Set Subject: header field
            messages.setSubject("Product Scan Details");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("List of scan product code based on the store code");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(DisplayUtils.getFile(context, fileName));//filepath
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);//filepath
            multipart.addBodyPart(messageBodyPart);


            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            // Send the complete message parts
            messages.setContent(multipart);
            return messages;
            // Send message
            // Transport.send(messages);
            // System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return null;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {//shoppersstop@123


                return new PasswordAuthentication(senderEmail, password);// email , password
            }
        });
    }


    private void handlePermission() {
        PermissionUtil.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {
                        ActivityCompat.requestPermissions(
                                (Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSIONS_REQUEST
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        ActivityCompat.requestPermissions(
                                (Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSIONS_REQUEST
                        );
                    }

                    @Override
                    public void onPermissionDisabled() {
                        DisplayUtils.neverAskAlert(context, context.getString(R.string.permission_msg));
                    }

                    @Override
                    public void onPermissionGranted() {
                        // onItemClickListener.onItemClick(getString(R.string.camera));
                        //getGalleryPermission(getString(R.string.app_name));
                        getAllScannedData();
                        DisplayUtils.writeToFile(context, fileName, scanString);

                        if(isNetworkConnected()){
                            sendMail();
                        }else {
                            Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        }


                    }
                }) ;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
