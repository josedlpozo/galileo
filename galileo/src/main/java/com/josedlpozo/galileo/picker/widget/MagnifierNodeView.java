package com.josedlpozo.galileo.picker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.josedlpozo.galileo.R;

public class MagnifierNodeView extends View {
    private Paint reticlePaint;
    private Paint outlinePaint;
    private Paint fillPaint;
    private Paint clearPaint;

    private float centerX;
    private float centerY;
    private float radius;
    private float reticleRadius;
    private float density;

    public MagnifierNodeView(Context context) {
        this(context, null);
    }

    public MagnifierNodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagnifierNodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MagnifierNodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float twoDp = 2f * dm.density;
        reticlePaint = new Paint();
        reticlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        reticlePaint.setColor(0x50ffffff);
        reticlePaint.setStrokeWidth(twoDp);
        reticlePaint.setStyle(Paint.Style.STROKE);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(0x80ffffff);
        outlinePaint.setStrokeWidth(twoDp);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0x80000000);
        fillPaint.setStrokeWidth(twoDp);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));

        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setColor(0);
        clearPaint.setStrokeWidth(twoDp);
        clearPaint.setStyle(Paint.Style.FILL);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        reticleRadius = getResources().getInteger(R.integer.color_picker_sample_width) / 2 + twoDp;
        density = dm.density;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2.0f - density * 2f;
        centerX = w / 2.0f;
        centerY = h / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, fillPaint);
        canvas.drawCircle(centerX, centerY, radius, outlinePaint);
        canvas.drawCircle(centerX, centerY, reticleRadius, clearPaint);
        canvas.drawCircle(centerX, centerY, reticleRadius, reticlePaint);
    }
}
