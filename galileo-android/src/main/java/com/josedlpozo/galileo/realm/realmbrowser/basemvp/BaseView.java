package com.josedlpozo.galileo.realm.realmbrowser.basemvp;

import android.support.annotation.Nullable;

public interface BaseView<P extends BasePresenter> {
    void attachPresenter(@Nullable P presenter);
}
