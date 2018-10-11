package com.josedlpozo.galileo.realm.realmbrowser.basemvp;

public abstract class BaseInteractorImpl<P extends BasePresenter> implements BaseInteractor {
    private final P presenter;

    protected P getPresenter() {
        return presenter;
    }

    protected BaseInteractorImpl(P presenter) {
        this.presenter = presenter;
    }
}
