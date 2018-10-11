package com.josedlpozo.galileo.realm.realmbrowser.basemvp;

public interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}
