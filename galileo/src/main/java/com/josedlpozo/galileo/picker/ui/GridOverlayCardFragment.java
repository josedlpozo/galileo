package com.josedlpozo.galileo.picker.ui;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.qs.OnOffTileState;
import com.josedlpozo.galileo.picker.utils.ColorUtils;
import com.josedlpozo.galileo.picker.utils.LaunchUtils;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;
import com.josedlpozo.galileo.picker.widget.DualColorPicker;
import com.josedlpozo.galileo.picker.widget.GridPreview;

public class GridOverlayCardFragment extends DesignerToolCardFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBox mIncludeKeylines;
    private CheckBox mIncudeCustomGrid;
    private SeekBar mColumnSizer;
    private SeekBar mRowSizer;
    private GridPreview mGridPreview;
    private DualColorPicker mDualColorPicker;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View base = super.onCreateView(inflater, container, savedInstanceState);

        final Context context = getContext();
        setTitleText(R.string.header_title_grid_overlay);
        setTitleSummary(R.string.header_summary_grid_overlay);
        setIconResource(R.drawable.ic_qs_grid_on);
        base.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorGridOverlayCardTint)));

        View v = inflater.inflate(R.layout.grid_overlay_content, mCardContent, true);
        mIncludeKeylines = v.findViewById(R.id.include_keylines);
        mIncudeCustomGrid = v.findViewById(R.id.include_custom_grid_size);
        mColumnSizer = v.findViewById(R.id.column_sizer);
        mColumnSizer.setProgress(( PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8) - 4 ) / 2);
        mRowSizer = v.findViewById(R.id.row_sizer);
        mRowSizer.setProgress(( PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8) - 4 ) / 2);
        mGridPreview = v.findViewById(R.id.grid_preview);
        mGridPreview.setColumnSize(PreferenceUtils.GridPreferences.getGridColumnSize(getContext(), 8));
        mGridPreview.setRowSize(PreferenceUtils.GridPreferences.getGridRowSize(getContext(), 8));

        mColumnSizer.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mRowSizer.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mIncludeKeylines.setChecked(PreferenceUtils.GridPreferences.getShowKeylines(context, false));
        mIncludeKeylines.setOnCheckedChangeListener(mCheckChangedListener);

        setIncludeCustomGridLines(PreferenceUtils.GridPreferences.getUseCustomGridSize(context, false));
        mIncudeCustomGrid.setOnCheckedChangeListener(mCheckChangedListener);

        mRowSizer.setOnTouchListener((v1, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v1.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    v1.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            v1.onTouchEvent(event);
            return true;
        });

        mDualColorPicker = v.findViewById(R.id.color_picker);
        mDualColorPicker.setOnClickListener(v12 -> {
            FragmentManager fm = getFragmentManager();
            DualColorPickerDialog dualColorPickerDialog = new DualColorPickerDialog();
            dualColorPickerDialog.show(fm, "color_picker_dialog");
        });

        return base;
    }

    @Override public void onResume() {
        super.onResume();
        PreferenceUtils.getShardedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        mEnabledSwitch.setChecked(DesignerTools.INSTANCE.gridOverlayOn(getContext()));
    }

    @Override public void onPause() {
        super.onPause();
        PreferenceUtils.getShardedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override protected int getCardStyleResourceId() {
        return R.style.ToolTheme_GridOverlayCard;
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == DesignerTools.INSTANCE.gridOverlayOn(getContext())) { return; }
        if (isChecked) {
            LaunchUtils.lauchGridOverlayOrPublishTile(getContext(), PreferenceUtils.GridPreferences.getGridOverlayActive(getContext(), false) ?
                                                                    OnOffTileState.STATE_ON :
                                                                    OnOffTileState.STATE_OFF);
        } else {
            LaunchUtils.cancelGridOverlayOrUnpublishTile(getContext());
        }
    }

    private void setIncludeCustomGridLines(boolean include) {
        mIncudeCustomGrid.setChecked(include);
        mColumnSizer.setEnabled(include);
        mRowSizer.setEnabled(include);
    }

    private CompoundButton.OnCheckedChangeListener mCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == mIncludeKeylines) {
                PreferenceUtils.GridPreferences.setShowKeylines(getContext(), isChecked);
            } else if (buttonView == mIncudeCustomGrid) {
                PreferenceUtils.GridPreferences.setUseCustomGridSize(getContext(), isChecked);
                if (isChecked) {
                    PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), mGridPreview.getColumnSize());
                    PreferenceUtils.GridPreferences.setGridRowSize(getContext(), mGridPreview.getRowSize());
                }
                mColumnSizer.setEnabled(isChecked);
                mRowSizer.setEnabled(isChecked);
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int size = 4 + progress * 2;
            if (seekBar == mColumnSizer) {
                mGridPreview.setColumnSize(size);
                PreferenceUtils.GridPreferences.setGridColumnSize(getContext(), size);
            } else if (seekBar == mRowSizer) {
                mGridPreview.setRowSize(size);
                PreferenceUtils.GridPreferences.setGridRowSize(getContext(), size);
            }
        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtils.GridPreferences.KEY_GRID_LINE_COLOR.equals(key)) {
            mDualColorPicker.setPrimaryColor(ColorUtils.getGridLineColor(getContext()));
        } else if (PreferenceUtils.GridPreferences.KEY_KEYLINE_COLOR.equals(key)) {
            mDualColorPicker.setSecondaryColor(ColorUtils.getKeylineColor(getContext()));
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {

        }
    };
}
