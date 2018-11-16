package com.josedlpozo.galileo.picker.service.qs;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.qs.OnOffTileState;
import com.josedlpozo.galileo.picker.ui.DesignerTools;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;

public class ColorPickerTileService extends TileService {

    public ColorPickerTileService() {
        super();
    }

    @Override public void onTileAdded() {
        super.onTileAdded();
    }

    @Override public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override public void onStartListening() {
        super.onStartListening();
        updateTile(DesignerTools.INSTANCE.colorPickerOn(this));
    }

    @Override public void onStopListening() {
        super.onStopListening();
    }

    @Override public void onClick() {
        super.onClick();
        boolean isOn = DesignerTools.INSTANCE.colorPickerOn(this);
        if (isOn) {
            LaunchUtils.publishColorPickerTile(this, OnOffTileState.STATE_OFF);
            LaunchUtils.cancelColorPickerOverlay(this);
        } else {
            LaunchUtils.publishColorPickerTile(this, OnOffTileState.STATE_ON);
            LaunchUtils.launchColorPickerOverlay(this);
        }
        updateTile(!isOn);
    }

    private void updateTile(boolean isOn) {
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(this, isOn ? R.drawable.ic_qs_colorpicker_on : R.drawable.ic_qs_colorpicker_off));
        tile.updateTile();
    }
}
