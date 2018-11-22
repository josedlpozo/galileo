/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.picker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.josedlpozo.galileo.R;
import github.chenupt.springindicator.SpringIndicator;

public class DualColorPickerDialog extends DialogFragment {

    private ColorPickerViewHolder[] colorPickerViews;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private SpringIndicator pageIndicator;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = View.inflate(getContext(), R.layout.dialog_color_picker, null);

        initColorPickerViews();

        viewPager = v.findViewById(R.id.view_pager);
        adapter = new ColorPickerPagerAdapter();
        viewPager.setAdapter(adapter);

        pageIndicator = v.findViewById(R.id.view_pager_indicator);
        pageIndicator.setViewPager(viewPager);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.ToolDialog));
        builder.setView(v)
               .setTitle(R.string.color_picker_title)
               .setPositiveButton(R.string.color_picker_accept, mClickListener)
               .setNegativeButton(R.string.color_picker_cancel, mClickListener);

        return builder.create();
    }

    private void initColorPickerViews() {
        colorPickerViews = new ColorPickerViewHolder[2];

        colorPickerViews[0] = new ColorPickerViewHolder();
        colorPickerViews[0].container = View.inflate(getContext(), R.layout.lobsterpicker, null);
        /*colorPickerViews[0].picker = colorPickerViews[0].container.findViewById(R.id.lobsterpicker);
        colorPickerViews[0].slider = colorPickerViews[0].container.findViewById(R.id.opacityslider);
        colorPickerViews[0].picker.addDecorator(colorPickerViews[0].slider);
        int color = ColorUtils.getGridLineColor(getContext());
        colorPickerViews[0].picker.setColor(color);
        colorPickerViews[0].picker.setHistory(color);
        colorPickerViews[0].slider.setOnTouchListener(mSliderTouchListener);*/

        colorPickerViews[1] = new ColorPickerViewHolder();
        colorPickerViews[1].container = View.inflate(getContext(), R.layout.lobsterpicker, null);
        /*colorPickerViews[1].picker = colorPickerViews[1].container.findViewById(R.id.lobsterpicker);
        colorPickerViews[1].slider = colorPickerViews[1].container.findViewById(R.id.opacityslider);
        colorPickerViews[1].picker.addDecorator(colorPickerViews[1].slider);
        color = ColorUtils.getKeylineColor(getContext());
        colorPickerViews[1].picker.setColor(color);
        colorPickerViews[1].picker.setHistory(color);
        colorPickerViews[1].slider.setOnTouchListener(mSliderTouchListener);*/
    }

    private View.OnTouchListener mSliderTouchListener = (v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        v.onTouchEvent(event);
        return true;
    };
    private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    /*PreferenceUtils.GridPreferences.setGridLineColor(getContext(), colorPickerViews[0].picker.getColor());
                    PreferenceUtils.GridPreferences.setKeylineColor(getContext(), colorPickerViews[1].picker.getColor());*/
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
            }
            dialog.dismiss();
        }
    };

    private class ColorPickerPagerAdapter extends PagerAdapter {

        @Override public int getCount() {
            return colorPickerViews.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            container.addView(colorPickerViews[position].container);

            return colorPickerViews[position].container;
        }

        @Override public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getContext().getString(R.string.color_picker_grid_page_title);
            } else {
                return getContext().getString(R.string.color_picker_keyline_page_title);
            }
        }
    }

    private class ColorPickerViewHolder {

        View container;
    }
}
