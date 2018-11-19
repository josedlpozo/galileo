package com.josedlpozo.galileo.picker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.josedlpozo.galileo.R;

public class GridPreview extends View {
    // default line width in dp
    private static final float DEFAULT_LINE_WIDTH = 1f;
    // default column size in dp
    private static final int DEFAULT_COLUMN_SIZE = 8;
    // default row size in dp
    private static final int DEFAULT_ROW_SIZE = 8;
    private static final int BACKGROUND_COLOR = 0x1f000000;

    private float gridLineWidth;
    private float columnSize;
    private float rowSize;
    private float density;
    private int columnSizeDp;
    private int rowSizeDp;

    private Paint gridLinePaint;
    private Paint gridSizeTextPaint;

    public GridPreview(Context context) {
        this(context, null);
    }

    public GridPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GridPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        density = getResources().getDisplayMetrics().density;

        gridLineWidth = DEFAULT_LINE_WIDTH * density;
        columnSizeDp = DEFAULT_COLUMN_SIZE;
        columnSize = columnSizeDp * density;
        rowSizeDp = DEFAULT_ROW_SIZE;
        rowSize = rowSizeDp * density;

        gridLinePaint = new Paint();
        gridLinePaint.setColor(context.getColor(R.color.colorGridOverlayCardTint));
        gridLinePaint.setStrokeWidth(gridLineWidth);

        gridSizeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        gridSizeTextPaint.setTextSize(
                getResources().getDimensionPixelSize(R.dimen.grid_preview_text_size));
        gridSizeTextPaint.setColor(BACKGROUND_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        canvas.drawColor(BACKGROUND_COLOR);
        for (float x = columnSize; x < width; x += columnSize) {
            canvas.drawLine(x, 0, x, height, gridLinePaint);
        }
        for (float y = rowSize; y < height; y += rowSize) {
            canvas.drawLine(0, y, width, y, gridLinePaint);
        }

        String text = String.format("%d x %d", columnSizeDp, rowSizeDp);
        Rect bounds = new Rect();
        gridSizeTextPaint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, (width - bounds.width()) / 2f, (height + bounds.height()) / 2f, gridSizeTextPaint);
    }

    public void setColumnSize(int columnSize) {
        columnSizeDp = columnSize;
        this.columnSize = columnSizeDp * density;
        invalidate();
    }

    public int getColumnSize() {
        return columnSizeDp;
    }

    public void setRowSize(int rowSize) {
        rowSizeDp = rowSize;
        this.rowSize = rowSizeDp * density;
        invalidate();
    }

    public int getRowSize() {
        return rowSizeDp;
    }
}
