package com.josedlpozo.galileo.picker.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import com.josedlpozo.galileo.picker.overlays.ColorPickerOverlay;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;

public class ScreenRecordRequestActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaProjectionManager mpm = getSystemService(MediaProjectionManager.class);
        startActivityForResult(mpm.createScreenCaptureIntent(), 42);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            DesignerTools.INSTANCE.setScreenRecordPermissionData(resultCode, data);
            Intent newIntent = new Intent(this, ColorPickerOverlay.class);
            this.startService(newIntent);
            PreferenceUtils.ColorPickerPreferences.setColorPickerActive(this, true);
        }
        finish();
    }
}
