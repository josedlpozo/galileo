package com.josedlpozo.galileo.realm.realmbrowser.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenterImpl;
import com.josedlpozo.galileo.realm.realmbrowser.view.RealmObjectActivity;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

public class BrowserPresenter extends BasePresenterImpl<BrowserContract.View> implements BrowserContract.Presenter {

    private final BrowserInteractor interactor;

    public BrowserPresenter() {
        interactor = new BrowserInteractor(this);
    }


    @Override
    public void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @BrowserContract.DisplayMode int displayMode) {
        interactor.requestForContentUpdate(context, dynamicRealm, displayMode);
    }

    @Override
    public void onShowMenuSelected() {
        if (isViewAttached()) {
            getView().showMenu();
        }
    }

    @Override
    public void onFieldSelectionChanged(int fieldIndex, boolean checked) {
        interactor.onFieldSelectionChanged(fieldIndex, checked);
    }

    @Override
    public void onWrapTextOptionToggled() {
        if (isViewAttached()) {
            interactor.onWrapTextOptionToggled(getView().getViewContext());
        }
    }

    @Override
    public void onNewObjectSelected() {
        interactor.onNewObjectSelected();
    }

    @Override
    public void onInformationSelected() {
        interactor.onInformationSelected();
    }

    @Override
    public void onAboutSelected() {
        if (isViewAttached()) {
            getView().getViewContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getView().getViewContext().getString(R.string.realm_browser_git))));
        }
    }

    @Override
    public void onRowSelected(@NonNull DynamicRealmObject realmObject) {
        interactor.onRowSelected(realmObject);
    }

    @Override
    public void showNewObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        if (isViewAttached()) {
            getView().getViewContext().startActivity(RealmObjectActivity.getIntent(getView().getViewContext(), modelClass, true));
        }
    }

    @Override
    public void showObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        if (isViewAttached()) {
            getView().getViewContext().startActivity(RealmObjectActivity.getIntent(getView().getViewContext(), modelClass, false));
        }
    }

    @Override
    public void showInformation(long numberOfRows) {
        if (isViewAttached()) {
            getView().showInformation(numberOfRows);
        }
    }

    @Override
    public void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects) {
        if (isViewAttached()) {
            getView().updateWithRealmObjects(objects);
        }
    }

    @Override
    public void updateWithFABVisibility(boolean visible) {
        if (isViewAttached()) {
            getView().updateWithFABVisibility(visible);
        }
    }

    @Override
    public void updateWithTitle(@NonNull String title) {
        if (isViewAttached()) {
            getView().updateWithTitle(title);
        }
    }

    @Override
    public void updateWithTextWrap(boolean wrapText) {
        if (isViewAttached()) {
            getView().updateWithTextWrap(wrapText);
        }
    }

    @Override
    public void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices) {
        if (isViewAttached()) {
            getView().updateWithFieldList(fields, selectedFieldIndices);
        }
    }
}