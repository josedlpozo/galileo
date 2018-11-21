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
package com.josedlpozo.galileo.picker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.utils.PreferenceUtils;

public class DualColorPicker extends View {
    private static final float STROKE_WIDTH = 5f;
    private static final float COLOR_DARKEN_FACTOR = 0.8f;

    private Paint primaryFillPaint;
    private Paint secondaryFillPaint;
    private Paint primaryStrokePaint;
    private Paint secondaryStrokePaint;

    public DualColorPicker(Context context) {
        this(context, null);
    }

    public DualColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DualColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DualColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DualColorPicker, 0, 0);
        int primaryColor = ta.getColor(R.styleable.DualColorPicker_primaryColor,
                                       PreferenceUtils.GridPreferences.getGridLineColor(context, getResources()
                .getColor(R.color.dualColorPickerDefaultPrimaryColor)));
        int secondaryColor = ta.getColor(R.styleable.DualColorPicker_primaryColor,
                                         PreferenceUtils.GridPreferences.getKeylineColor(context, getResources()
                .getColor(R.color.dualColorPickerDefaultSecondaryColor)));

        primaryFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        primaryFillPaint.setStyle(Paint.Style.FILL);
        primaryFillPaint.setColor(primaryColor);
        primaryStrokePaint = new Paint(primaryFillPaint);
        primaryStrokePaint.setStyle(Paint.Style.STROKE);
        primaryStrokePaint.setStrokeWidth(STROKE_WIDTH);
        primaryStrokePaint.setColor(getDarkenedColor(primaryColor));

        secondaryFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        secondaryFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        secondaryFillPaint.setColor(secondaryColor);
        secondaryStrokePaint = new Paint(secondaryFillPaint);
        secondaryStrokePaint.setStyle(Paint.Style.STROKE);
        secondaryStrokePaint.setStrokeWidth(STROKE_WIDTH);
        secondaryStrokePaint.setColor(getDarkenedColor(secondaryColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float width = getWidth();
        final float height = getHeight();
        final float widthDiv2 = width / 2f;
        final float heightDiv2 = height / 2f;
        final float radius = Math.min(widthDiv2, heightDiv2) * 0.9f;

        // erase everything
        canvas.drawColor(0);

        // draw the left half
        canvas.clipRect(0, 0, widthDiv2, height, Region.Op.REPLACE);
        canvas.drawCircle(widthDiv2, heightDiv2, radius, primaryFillPaint);
        canvas.drawCircle(widthDiv2, heightDiv2, radius, primaryStrokePaint);
        canvas.drawLine(widthDiv2 - STROKE_WIDTH / 2f, heightDiv2 - radius,
                widthDiv2 - STROKE_WIDTH / 2f, heightDiv2 + radius, primaryStrokePaint);

        /// draw the right half
        canvas.clipRect(widthDiv2, 0, width, height, Region.Op.REPLACE);
        canvas.drawCircle(widthDiv2, heightDiv2, radius, secondaryFillPaint);
        canvas.drawCircle(widthDiv2, heightDiv2, radius, secondaryStrokePaint);
        canvas.drawLine(widthDiv2 + STROKE_WIDTH / 2f, heightDiv2 - radius,
                widthDiv2 + STROKE_WIDTH / 2f, heightDiv2 + radius, secondaryStrokePaint);
    }

    private int getDarkenedColor(int color) {
        int a = Color.alpha(color);
        int r = (int) ( Color.red(color) * COLOR_DARKEN_FACTOR);
        int g = (int) ( Color.green(color) * COLOR_DARKEN_FACTOR);
        int b = (int) ( Color.blue(color) * COLOR_DARKEN_FACTOR);

        return Color.argb(a, r, g, b);
    }

    public void setPrimaryColor(int color) {
        primaryFillPaint.setColor(color);
        primaryStrokePaint.setColor(getDarkenedColor(color));
        invalidate();
    }

    public int getPrimaryColor() {
        return primaryFillPaint.getColor();
    }

    public void setSecondaryColor(int color) {
        secondaryFillPaint.setColor(color);
        secondaryStrokePaint.setColor(getDarkenedColor(color));
        invalidate();
    }

    public int getSecondaryColor() {
        return secondaryFillPaint.getColor();
    }
}
