package com.josedlpozo.galileo.picker.service.qs;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.ui.DesignerTools;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;

public class GridOverlayTileService extends TileService {

    public GridOverlayTileService() {
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
        boolean isOn = DesignerTools.INSTANCE.gridOverlayOn(this);
        updateTile(isOn);
    }

    @Override public void onStopListening() {
        super.onStopListening();
    }

    @Override public void onClick() {
        super.onClick();
        boolean isOn = DesignerTools.INSTANCE.gridOverlayOn(this);
        if (isOn) {
            LaunchUtils.cancelGridOverlay(this);
        } else {
            LaunchUtils.launchGridOverlay(this);
        }
        updateTile(!isOn);
    }

    private void updateTile(boolean isOn) {
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(this, isOn ? R.drawable.ic_qs_grid_on : R.drawable.ic_qs_grid_off));
        tile.updateTile();
    }
}
