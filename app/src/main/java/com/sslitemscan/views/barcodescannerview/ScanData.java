package com.sslitemscan.views.barcodescannerview;

public class ScanData {

    private String barCode;
    private String timeStamp;
    private String storeId;

    public ScanData(String barCode, String timeStamp,String storeId) {
        this.barCode = barCode;
        this.timeStamp = timeStamp;
        this.storeId= storeId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
