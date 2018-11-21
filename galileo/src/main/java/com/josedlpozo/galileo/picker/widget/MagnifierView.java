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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.josedlpozo.galileo.R;

public class MagnifierView extends FrameLayout {
    private TextView colorValueTextView;

    private Drawable magnifyingLens;
    private Bitmap pixels;
    private Paint bitmapPaint;
    private Paint gridPaint;
    private Paint pixelOutlinePaint;

    private Rect sourcePreviewRect;
    private Rect destinationPreviewRect;
    private RectF targetPixelOutline;

    private Point insets;
    private Path previewClipPath;

    private int centerPixelColor;

    public MagnifierView(Context context) {
        this(context, null);
    }

    public MagnifierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagnifierView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MagnifierView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        magnifyingLens = context.getDrawable(R.drawable.loop_ring);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setDither(true);
        bitmapPaint.setFilterBitmap(false);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(0xff000000);
        gridPaint.setAlpha(128);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f * dm.density);
        gridPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));

        pixelOutlinePaint = new Paint();
        pixelOutlinePaint.setColor(0xff000000);
        pixelOutlinePaint.setStyle(Paint.Style.STROKE);
        pixelOutlinePaint.setStrokeWidth(2f * dm.density);
        pixelOutlinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));

        final Resources res = getResources();
        insets = new Point(res.getDimensionPixelSize(R.dimen.magnified_image_horizontal_inset),
                           res.getDimensionPixelSize(R.dimen.magnified_image_vertical_inset));

        int previewSize = res.getDimensionPixelSize(R.dimen.magnified_image_size);
        destinationPreviewRect = new Rect(insets.x, insets.y, insets.x + previewSize,
                                          insets.y + previewSize);
        previewClipPath = new Path();
        previewClipPath.addCircle(destinationPreviewRect.exactCenterX(),
                                  destinationPreviewRect.exactCenterY(), previewSize / 2f, Path.Direction.CCW);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        colorValueTextView = findViewById(R.id.color_value);
        colorValueTextView.setOnClickListener(v -> {
            ClipboardManager cm = getContext().getSystemService(ClipboardManager.class);
            CharSequence text = colorValueTextView.getText();
            ClipData primaryClip = cm.getPrimaryClip();
            if (primaryClip == null || primaryClip.getItemAt(0) == null ||
                    !text.equals(cm.getPrimaryClip().getItemAt(0).coerceToText(getContext()))) {
                ClipData clip = ClipData.newPlainText("color", text);
                cm.setPrimaryClip(clip);
                Toast.makeText(getContext(), R.string.color_copied_to_clipboard, Toast.LENGTH_SHORT)
                     .show();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        magnifyingLens.setBounds(0, 0, w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        magnifyingLens.draw(canvas);
        if (pixels != null) {
            canvas.clipPath(previewClipPath);
            canvas.drawBitmap(pixels, sourcePreviewRect, destinationPreviewRect, bitmapPaint);
            drawGrid(canvas);
            canvas.drawRect(targetPixelOutline, pixelOutlinePaint);
        }
        super.dispatchDraw(canvas);
    }

    private void drawGrid(Canvas canvas) {
        final float stepSize = destinationPreviewRect.width() / (float) sourcePreviewRect.width();

        for (float x = destinationPreviewRect.left + stepSize;
             x <= destinationPreviewRect.right; x += stepSize) {
            canvas.drawLine(x, destinationPreviewRect.top, x, destinationPreviewRect.bottom, gridPaint);
        }
        for (float y = destinationPreviewRect.top + stepSize;
             y <= destinationPreviewRect.bottom; y += stepSize) {
            canvas.drawLine(destinationPreviewRect.left, y, destinationPreviewRect.right, y, gridPaint);
        }
    }

    public void setPixels(Bitmap pixels) {
        this.pixels = pixels;
        sourcePreviewRect = new Rect(0, 0, pixels.getWidth(), pixels.getHeight());
        centerPixelColor = pixels.getPixel(pixels.getWidth() / 2, pixels.getHeight() / 2);

        if (targetPixelOutline == null) {
            float pixelSize = (float) destinationPreviewRect.width() / pixels.getWidth();
            float x = ( this.pixels.getWidth() - 1) / 2f * pixelSize;
            float y = ( this.pixels.getHeight() - 1) / 2f * pixelSize;
            targetPixelOutline = new RectF(destinationPreviewRect.left + x,
                                            destinationPreviewRect.top + y, destinationPreviewRect.left + x + pixelSize,
                                            destinationPreviewRect.top + y + pixelSize);
        }

        if (colorValueTextView != null) {
            colorValueTextView.setText(String.format("#%06X", centerPixelColor & 0x00ffffff));
        }
        invalidate();
    }
}
