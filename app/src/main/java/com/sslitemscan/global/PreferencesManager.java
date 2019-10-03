package com.sslitemscan.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sslitemscan.Constants.Constants;
import com.sslitemscan.SSLApplication;
import com.sslitemscan.views.barcodescannerview.ScanData;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class PreferencesManager {
    private final static String PREF_FILE = "SSLPreference";

    private SharedPreferences mSharedPreferences;
    private static volatile PreferencesManager sPreferencesManagerInstance;

    private PreferencesManager() {
        mSharedPreferences = SSLApplication.getInstance().getContext().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if (sPreferencesManagerInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sPreferencesManagerInstance == null) { //if there is no instance available... create new one
            synchronized (PreferencesManager.class) {
                if (sPreferencesManagerInstance == null)
                    sPreferencesManagerInstance = new PreferencesManager();
            }
        }
        return sPreferencesManagerInstance;
    }

    //Make singleton from serialize and deserialize operation.
    protected PreferencesManager readResolve() {
        return getInstance();
    }

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Get a string shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference
     *                 isn't found.
     * @return value - String containing value of the shared preference if
     * found.
     */
    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public String getString(String key) {
        return getString(key, Constants.EMPTY_STRING);
    }

    /**
     * Get a integer shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference
     *                 isn't found.
     * @return value - String containing value of the shared preference if
     * found.
     */
    public int getInt(Context context, String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * Validate Key Exist or not in shared preference file.
     *
     * @param key - Key to look up in shared preferences.
     */
    public Boolean isKeyExist(String key) {
        if (mSharedPreferences.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove record from shared preference
     *
     * @param key - Key to look up in shared preferences.
     */
    public void removeKey(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * Get a boolean shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference
     *                 isn't found.
     * @return value - String containing value of the shared preference if
     * found.
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }


    /**
     * Set a long shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Get a long shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference
     *                 isn't found.
     * @return value - long containing value of the shared preference if
     * found.
     */
    public long getLong(final String key, final long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public boolean saveDataMap(final WeakHashMap<String, String> dataMap) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Iterator it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey() != null) {
                String value = (pair.getValue() != null) ? pair.getValue().toString() : Constants.EMPTY_STRING;
                editor.putString(pair.getKey().toString(), value);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return editor.commit();
    }

    public void saveHashMap(WeakHashMap<String, ScanData> inputMap) {
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(inputMap);
        prefsEditor.putString("ScanDataHashMap", json);
        prefsEditor.commit();;     // This line is IMPORTANT !!!
    }


    public void clearAll() {
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * Check whether the product exist in the shared preference (Normally used to check whether product is in the
     * user Wishlist.
     * @param productCode
     * @param productBase
     * @return true if product exist, false otherwise.
     */
    public boolean isProductExist(String productCode, String productBase) {
        if (null != productCode && mSharedPreferences.contains(productCode)) {
            return true;
        }
//        if (null != productBase && mSharedPreferences.contains(productBase)) {
//            return true;
//        }
        return false;

    }

    /**
     * return the productcode of the product and if product code is not saved then return product base product of the product.
     * In some cases the productCode for the product is intechanged with productBase so we need to save both.
     * @param productCode
     * @param productBase
     * @return
     */
    public String getProductSavedCode(String productCode, String productBase) {
        String valueToReturn = mSharedPreferences.getString(productCode, null);
        if (null == valueToReturn || 0 == valueToReturn.length()) {
            valueToReturn = mSharedPreferences.getString(productBase, Constants.EMPTY_STRING);
        }
        return valueToReturn;
    }

    /**
     * To add the product to wishlist we need to add both productCode and productBase.
     * @param productCode
     * @param productBase
     */
    public void saveProductCode(String productCode, String productBase) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (null != productCode) {
            editor.putString(productCode, productCode);
        }
        if (null != productBase) {
            editor.putString(productBase, productBase);
        }
        editor.commit();
    }

    /**
     * To remove the product from the wishlist we need to remove both the productcode and productBase.
     * @param productCode
     * @param productBase
     */
    public void removeSavedProductCode(String productCode, String productBase) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (null != productCode) {
            editor.remove(productCode);
        }
        if (null != productBase) {
            editor.remove(productBase);
        }
        editor.commit();
    }

    public void clearMessages(String keyValue){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(keyValue);
        editor.apply();
    }

}