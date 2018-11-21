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
package com.josedlpozo.galileo.picker.overlays;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.picker.qs.ColorPickerQuickSettingsTile;
import com.josedlpozo.galileo.picker.qs.OnOffTileState;
import com.josedlpozo.galileo.picker.ui.DesignerTools;
import com.josedlpozo.galileo.picker.widget.MagnifierNodeView;
import com.josedlpozo.galileo.picker.widget.MagnifierView;
import java.nio.ByteBuffer;

public class ColorPickerOverlay extends Service {

    private static final int NOTIFICATION_ID = ColorPickerOverlay.class.hashCode();
    private static final String ACTION_HIDE_PICKER = "hide_picker";
    private static final String ACTION_SHOW_PICKER = "show_picker";
    private static final float DAMPENING_FACTOR_DP = 25.0f;
    private static final String NOTIFICATION_CHANNEL_ID = "com.josedlpozo.galileo";
    private WindowManager windowManager;
    private WindowManager.LayoutParams nodeParams;
    private WindowManager.LayoutParams magnifierParams;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private MagnifierView magnifierView;
    private MagnifierNodeView magnifierNodeView;
    private Rect previewArea;
    private int previewSampleWidth;
    private int previewSampleHeight;
    private float nodeToMagnifierDistance;
    private float angle = (float) Math.PI * 1.5f;
    private PointF lastPosition;
    private PointF startPosition;
    private float dampeningFactor;
    private int currentOrientation;
    private final Object screenCaptureLock = new Object();
    private boolean animating = false;
    private MediaProjectionManager mediaProjectionManager;

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        setup();
        DesignerTools.INSTANCE.setColorPickerOn(this, true);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        teardownMediaProjection();
        mediaProjection = null;
        virtualDisplay = null;

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
        if (magnifierView != null) {
            removeViewIfAttached(magnifierView);
            magnifierView = null;
            if (magnifierNodeView != null) {
                removeViewIfAttached(magnifierNodeView);
                magnifierNodeView = null;
            }
        }
        DesignerTools.INSTANCE.setColorPickerOn(this, false);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // recreate the media projection on orientation changes
        if (currentOrientation != newConfig.orientation) {
            recreateMediaPrjection();
            currentOrientation = newConfig.orientation;
        }
    }

    private void setup() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        setupMediaProjection();

        final Resources res = getResources();
        currentOrientation = res.getConfiguration().orientation;

        int magnifierWidth = res.getDimensionPixelSize(R.dimen.galileo_picker_magnifying_ring_width);
        int magnifierHeight = res.getDimensionPixelSize(R.dimen.galileo_picker_magnifying_ring_height);

        int nodeViewSize = res.getDimensionPixelSize(R.dimen.galileo_picker_node_size);
        DisplayMetrics dm = res.getDisplayMetrics();

        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        nodeParams = new WindowManager.LayoutParams(nodeViewSize, nodeViewSize, layoutFlag,
                                                     WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                     | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                                                     | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
        nodeParams.gravity = Gravity.TOP | Gravity.LEFT;
        magnifierParams = new WindowManager.LayoutParams(magnifierWidth, magnifierHeight, layoutFlag,
                                                          WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                                          | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                                                          | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
        magnifierParams.gravity = Gravity.TOP | Gravity.LEFT;

        final int x = dm.widthPixels / 2;
        final int y = dm.heightPixels / 2;
        nodeParams.x = x - nodeViewSize / 2;
        nodeParams.y = y - nodeViewSize / 2;

        magnifierParams.x = x - magnifierWidth / 2;
        magnifierParams.y = nodeParams.y - ( magnifierHeight + nodeViewSize / 2 );

        magnifierView = (MagnifierView) View.inflate(this, R.layout.color_picker_magnifier, null);
        magnifierView.setOnTouchListener(mDampenedOnTouchListener);
        magnifierNodeView = new MagnifierNodeView(this);
        magnifierNodeView.setOnTouchListener(mOnTouchListener);
        addOverlayViewsIfDetached();
        magnifierView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {
                animateColorPickerIn();
                magnifierView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        previewSampleWidth = res.getInteger(R.integer.galileo_color_picker_sample_width);
        previewSampleHeight = res.getInteger(R.integer.galileo_color_picker_sample_height);
        previewArea =
            new Rect(x - previewSampleWidth / 2, y - previewSampleHeight / 2, x + previewSampleWidth / 2 + 1, y + previewSampleHeight / 2 + 1);

        nodeToMagnifierDistance = ( Math.min(magnifierWidth, magnifierHeight) + nodeViewSize * 2 ) / 2f;
        lastPosition = new PointF();
        startPosition = new PointF();
        dampeningFactor = DAMPENING_FACTOR_DP * dm.density;

        IntentFilter filter = new IntentFilter(ColorPickerQuickSettingsTile.ACTION_TOGGLE_STATE);
        filter.addAction(ColorPickerQuickSettingsTile.ACTION_UNPUBLISH);
        filter.addAction(ACTION_HIDE_PICKER);
        filter.addAction(ACTION_SHOW_PICKER);
        registerReceiver(mReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startOreoForeground();
        } else {
            startForeground(NOTIFICATION_ID, getPersistentNotification(true));
        }
    }

    private void startOreoForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.josedlpozo.galileo";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        startForeground(NOTIFICATION_ID, getPersistentNotification(true));
    }

    private void removeViewIfAttached(View v) {
        if (v.isAttachedToWindow()) {
            windowManager.removeView(v);
        }
    }

    private void removeOverlayViewsIfAttached() {
        removeViewIfAttached(magnifierView);
        removeViewIfAttached(magnifierNodeView);
    }

    private void addOverlayViewsIfDetached() {
        if (magnifierNodeView != null && !magnifierNodeView.isAttachedToWindow()) {
            windowManager.addView(magnifierNodeView, nodeParams);
        }
        if (magnifierView != null && !magnifierView.isAttachedToWindow()) {
            windowManager.addView(magnifierView, magnifierParams);
        }
    }

    private void animateColorPickerIn() {
        magnifierView.setScaleX(0);
        magnifierView.setScaleY(0);
        magnifierNodeView.setVisibility(View.GONE);

        final int startX = magnifierParams.x + ( magnifierParams.width - nodeParams.width ) / 2;
        final int startY = magnifierParams.y + ( magnifierParams.height - nodeParams.height ) / 2;
        final int endX = nodeParams.x;
        final int endY = nodeParams.y;
        nodeParams.x = startX;
        nodeParams.y = startY;
        windowManager.updateViewLayout(magnifierNodeView, nodeParams);
        final ValueAnimator animator = ObjectAnimator.ofFloat(null, "", 0f, 1f);
        animator.setDuration(200);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            float fraction = valueAnimator.getAnimatedFraction();
            nodeParams.x = startX + (int) ( fraction * ( endX - startX ) );
            nodeParams.y = startY + (int) ( fraction * ( endY - startY ) );
            windowManager.updateViewLayout(magnifierNodeView, nodeParams);
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                animating = true;
                magnifierNodeView.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animator animator) {
                animating = false;
            }

            @Override public void onAnimationCancel(Animator animator) {
            }

            @Override public void onAnimationRepeat(Animator animator) {
            }
        });

        magnifierView.animate().scaleX(1f).scaleY(1f).setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> animator.start());
    }

    private void animateColorPickerOut(final Runnable endAction) {
        final int endX = magnifierParams.x + ( magnifierParams.width - nodeParams.width ) / 2;
        final int endY = magnifierParams.y + ( magnifierParams.height - nodeParams.height ) / 2;
        final int startX = nodeParams.x;
        final int startY = nodeParams.y;
        nodeParams.x = startX;
        nodeParams.y = startY;
        windowManager.updateViewLayout(magnifierNodeView, nodeParams);
        final ValueAnimator animator = ObjectAnimator.ofFloat(null, "", 0f, 1f);
        animator.setDuration(200);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            float fraction = valueAnimator.getAnimatedFraction();
            nodeParams.x = startX + (int) ( fraction * ( endX - startX ) );
            nodeParams.y = startY + (int) ( fraction * ( endY - startY ) );
            windowManager.updateViewLayout(magnifierNodeView, nodeParams);
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                animating = true;
                magnifierNodeView.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animator animator) {
                animating = false;
                magnifierNodeView.setVisibility(View.GONE);
                nodeParams.x = startX;
                nodeParams.y = startY;
                magnifierView.animate().scaleX(0).scaleY(0).setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
                    if (endAction != null) {
                        endAction.run();
                    }
                });
            }

            @Override public void onAnimationCancel(Animator animator) {
            }

            @Override public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    public Bitmap getScreenBitmapRegion(Image image, Rect region) {
        if (image == null) {
            return null;
        }
        final int maxX = image.getWidth() - 1;
        final int maxY = image.getHeight() - 1;
        final int width = region.width();
        final int height = region.height();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        int rowStride = planes[0].getRowStride();
        int pixelStride = planes[0].getPixelStride();
        int color, pixelX, pixelY;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelX = region.left + x;
                pixelY = region.top + y;
                if (pixelX >= 0 && pixelX <= maxX && pixelY >= 0 && pixelY <= maxY) {
                    int index = ( pixelY * rowStride + pixelX * pixelStride );
                    buffer.position(index);
                    color = Color.argb(255, buffer.get() & 0xff, buffer.get() & 0xff, buffer.get() & 0xff);
                } else {
                    color = 0;
                }
                bmp.setPixel(x, y, color);
            }
        }
        return bmp;
    }

    private void setupMediaProjection() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final Point size = new Point();
        windowManager.getDefaultDisplay().getRealSize(size);
        imageReader = ImageReader.newInstance(size.x, size.y, PixelFormat.RGBA_8888, 2);
        imageReader.setOnImageAvailableListener(mImageAvailableListener, new Handler());
        mediaProjectionManager = getSystemService(MediaProjectionManager.class);
        mediaProjection = mediaProjectionManager.getMediaProjection(DesignerTools.INSTANCE.getScreenRecordResultCode(),
                                                                    DesignerTools.INSTANCE.getScreenRecordResultData());
        virtualDisplay = mediaProjection.createVirtualDisplay(ColorPickerOverlay.class.getSimpleName(), size.x, size.y, dm.densityDpi,
                                                              DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null,
                                                              null);
    }

    private void teardownMediaProjection() {
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
    }

    private void recreateMediaPrjection() {
        teardownMediaProjection();
        setupMediaProjection();
    }

    private void updateNotification(boolean actionIsHide) {
        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.notify(NOTIFICATION_ID, getPersistentNotification(actionIsHide));
    }

    private Notification getPersistentNotification(boolean actionIsHide) {
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(actionIsHide ? ACTION_HIDE_PICKER : ACTION_SHOW_PICKER), 0);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setPriority(Notification.PRIORITY_MIN)
               .setSmallIcon(actionIsHide ? R.drawable.ic_qs_colorpicker_on : R.drawable.ic_qs_colorpicker_off)
               .setContentTitle(getString(R.string.color_picker_qs_tile_label))
               .setContentText(getString(actionIsHide ? R.string.notif_content_hide_picker : R.string.notif_content_show_picker))
               .setStyle(new Notification.BigTextStyle().bigText(
                   getString(actionIsHide ? R.string.notif_content_hide_picker : R.string.notif_content_show_picker)))
               .setContentIntent(pi);
        return builder.build();
    }

    private void updateMagnifierViewPosition(int x, int y, float angle) {
        previewArea.left = x - previewSampleWidth / 2;
        previewArea.top = y - previewSampleHeight / 2;
        previewArea.right = x + previewSampleWidth / 2 + 1;
        previewArea.bottom = y + previewSampleHeight / 2 + 1;

        nodeParams.x = x - magnifierNodeView.getWidth() / 2;
        nodeParams.y = y - magnifierNodeView.getHeight() / 2;
        windowManager.updateViewLayout(magnifierNodeView, nodeParams);

        magnifierParams.x = (int) ( nodeToMagnifierDistance * (float) Math.cos(angle) + x ) - magnifierView.getWidth() / 2;
        magnifierParams.y = (int) ( nodeToMagnifierDistance * (float) Math.sin(angle) + y ) - magnifierView.getHeight() / 2;
        windowManager.updateViewLayout(magnifierView, magnifierParams);
    }

    private ImageReader.OnImageAvailableListener mImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override public void onImageAvailable(ImageReader reader) {
            synchronized (screenCaptureLock) {
                Image newImage = reader.acquireNextImage();
                if (newImage != null) {
                    if (!animating && magnifierView != null) {
                        magnifierView.setPixels(getScreenBitmapRegion(newImage, previewArea));
                    }
                    newImage.close();
                }
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ColorPickerQuickSettingsTile.ACTION_UNPUBLISH.equals(action)) {
                stopSelf();
            } else if (ColorPickerQuickSettingsTile.ACTION_TOGGLE_STATE.equals(action)) {
                int state = intent.getIntExtra(OnOffTileState.EXTRA_STATE, OnOffTileState.STATE_OFF);
                if (state == OnOffTileState.STATE_ON) {
                    stopSelf();
                }
            } else if (ACTION_HIDE_PICKER.equals(action)) {
                animateColorPickerOut(() -> {
                    removeOverlayViewsIfAttached();
                    teardownMediaProjection();
                    updateNotification(false);
                });
            } else if (ACTION_SHOW_PICKER.equals(action)) {
                addOverlayViewsIfDetached();
                setupMediaProjection();
                updateNotification(true);
                animateColorPickerIn();
            }
        }
    };
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    magnifierNodeView.setVisibility(View.INVISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float rawX = event.getRawX();
                    final float rawY = event.getRawY();
                    final float dx = ( magnifierParams.x + magnifierView.getWidth() / 2 ) - rawX;
                    final float dy = ( magnifierParams.y + magnifierView.getHeight() / 2 ) - rawY;
                    angle = (float) Math.atan2(dy, dx);
                    updateMagnifierViewPosition((int) rawX, (int) rawY, angle);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    magnifierNodeView.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    };
    private View.OnTouchListener mDampenedOnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    lastPosition.set(event.getRawX(), event.getRawY());
                    startPosition.set(nodeParams.x, nodeParams.y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float rawX = event.getRawX();
                    final float rawY = event.getRawY();
                    final float dx = ( rawX - lastPosition.x ) / dampeningFactor;
                    final float dy = ( rawY - lastPosition.y ) / dampeningFactor;
                    final float x = ( startPosition.x + magnifierNodeView.getWidth() / 2 ) + dx;
                    final float y = ( startPosition.y + magnifierNodeView.getHeight() / 2 ) + dy;
                    updateMagnifierViewPosition((int) x, (int) y, angle);
                    break;
            }
            return true;
        }
    };
}
