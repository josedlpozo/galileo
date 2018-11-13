/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.josedlpozo.galileo.realm.realmbrowser.models;

import android.support.annotation.NonNull;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenterImpl;
import com.josedlpozo.galileo.realm.realmbrowser.browser.BrowserContract;
import com.josedlpozo.galileo.realm.realmbrowser.browser.view.RealmBrowserActivity;
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.InformationPojo;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.ModelPojo;
import java.util.ArrayList;

public class ModelsPresenter extends BasePresenterImpl<ModelsContract.View> implements ModelsContract.Presenter {
    private final ModelsInteractor interactor;

    public ModelsPresenter() {
        interactor = new ModelsInteractor(this);
    }

    @Override
    public void onSortModeChanged() {
        interactor.updateWithSortModeChanged();
    }

    @Override
    public void requestForContentUpdate() {
        interactor.requestForContentUpdate();
    }

    @Override
    public void onFilterChanged(@NonNull String filter) {
        interactor.updateWithFilter(filter);
    }

    @Override
    public void onInformationSelected() {
        interactor.onInformationSelected();
    }

    @Override
    public void onModelSelected(ModelPojo item) {
        if (isViewAttached()) {
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CLASS, item.getKlass());
            RealmBrowserActivity.start(getView().getViewContext(), BrowserContract.DisplayMode.REALM_CLASS);
        }
    }

    @Override
    public void onShareSelected() {
        interactor.onShareSelected();
    }

    @Override
    public void updateWithModels(@NonNull ArrayList<ModelPojo> modelsList, @ModelsContract.SortMode int sortMode) {
        if (isViewAttached()) {
            getView().updateWithModels(modelsList, sortMode);
        }
    }

    @Override
    public void presentShareDialog(@NonNull String path) {
        if (isViewAttached()) {
            getView().presentShareDialog(path);
        }
    }

    @Override
    public void showInformation(@NonNull InformationPojo informationPojo) {
        if (isViewAttached()) {
            getView().showInformation(informationPojo);
        }
    }
}