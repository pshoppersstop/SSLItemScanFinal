package com.sslitemscan.views.barcodescannerview;

import android.hardware.Camera;

import java.util.List;

/**
 * Used to make camera use in the tutorial a bit more obvious
 * in a production environment you wouldn't make these calls static
 * as you have no way to mock them for testing
 *
 * @author paul.blundell
 */
public class CameraHelper {

    public static boolean cameraAvailable(Camera camera) {
        return camera != null;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available or doesn't exist
           // Log.d("getCamera failed", e);
        }
        return c;
    }
    public static Camera.Size findSupportedPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> allSizes = parameters.getSupportedPreviewSizes();
        Camera.Size size = allSizes.get(0); // get top size
        for (int i = 0; i < allSizes.size(); i++) {
            if (allSizes.get(i).width > size.width)
                size = allSizes.get(i);
        }
        return size;
    }
}
