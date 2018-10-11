package com.josedlpozo.galileo.realm.realmbrowser.basemvp;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import java.lang.ref.WeakReference;

public abstract class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {

    @Nullable
    private WeakReference<V> viewRef;

    @Override
    @CallSuper
    public void attachView(@NonNull V view) {
        viewRef = new WeakReference<>(view);
    }

    @UiThread
    @Nullable
    protected final V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    @UiThread
    protected final boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @UiThread
    @Override
    @CallSuper
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }
}
