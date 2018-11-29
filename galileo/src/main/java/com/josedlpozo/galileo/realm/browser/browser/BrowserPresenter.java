package com.josedlpozo.galileo.realm.browser.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.browser.view.RealmObjectActivity;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

public class BrowserPresenter {

    private final BrowserView view;
    private final BrowserInteractor interactor;

    public BrowserPresenter(final BrowserView view) {
        this.view = view;
        interactor = new BrowserInteractor(this);
    }

    public void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, int displayMode) {
        interactor.requestForContentUpdate(context, dynamicRealm, displayMode);
    }

    public void onShowMenuSelected() {
        view.showMenu();
    }

    public void onFieldSelectionChanged(int fieldIndex, boolean checked) {
        interactor.onFieldSelectionChanged(fieldIndex, checked);
    }

    public void onWrapTextOptionToggled() {
        interactor.onWrapTextOptionToggled(view.getViewContext());
    }

    public void onNewObjectSelected() {
        interactor.onNewObjectSelected();
    }

    public void onInformationSelected() {
        interactor.onInformationSelected();
    }

    public void onAboutSelected() {
        view.getViewContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(view.getViewContext().getString(R.string.realm_browser_git))));
    }

    public void onRowSelected(@NonNull DynamicRealmObject realmObject) {
        interactor.onRowSelected(realmObject);
    }

    public void showNewObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        view.getViewContext().startActivity(RealmObjectActivity.getIntent(view.getViewContext(), modelClass, true));
    }

    public void showObjectActivity(@NonNull Class<? extends RealmModel> modelClass) {
        view.getViewContext().startActivity(RealmObjectActivity.getIntent(view.getViewContext(), modelClass, false));
    }

    public void showInformation(long numberOfRows) {
        view.showInformation(numberOfRows);
    }

    public void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects) {
        view.updateWithRealmObjects(objects);
    }

    public void updateWithFABVisibility(boolean visible) {
        view.updateWithFABVisibility(visible);
    }

    public void updateWithTitle(@NonNull String title) {
        view.updateWithTitle(title);
    }

    public void updateWithTextWrap(boolean wrapText) {
        view.updateWithTextWrap(wrapText);
    }

    public void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices) {
        view.updateWithFieldList(fields, selectedFieldIndices);
    }
}