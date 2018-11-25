/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
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
import android.support.annotation.Nullable;

import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractorImpl;
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.GalileoRealmModel;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

class ModelsInteractor extends BaseInteractorImpl<ModelsContract.Presenter> implements ModelsContract.Interactor {

    @ModelsContract.SortMode private int sortMode = ModelsContract.SortMode.ASC;
    private String filter;
    @Nullable private RealmConfiguration configuration = (RealmConfiguration) DataHolder.getInstance().retrieve(DataHolder.DATA_HOLDER_KEY_CONFIG);

    ModelsInteractor(ModelsContract.Presenter presenter) {
        super(presenter);
    }

    @Override public void requestForContentUpdate() {
        if (configuration == null) { return; }
        getPresenter().updateWithModels(sortPojos(filterPojos(getAllModelPojos(configuration), filter), sortMode), sortMode);
    }

    @Override public void updateWithFilter(String filter) {
        this.filter = filter;
        requestForContentUpdate();
    }

    @Override public void updateWithSortModeChanged() {
        this.sortMode = ( this.sortMode + 1 ) % 2;
        requestForContentUpdate();
    }

    @Override public void onShareSelected() {
        if (configuration != null) { getPresenter().presentShareDialog(configuration.getPath()); }
    }

    @NonNull private static ArrayList<GalileoRealmModel> getAllModelPojos(@NonNull RealmConfiguration configuration) {
        DynamicRealm realm = DynamicRealm.getInstance(configuration);
        ArrayList<Class<? extends RealmModel>> realmModelClasses = new ArrayList<>(realm.getConfiguration().getRealmObjectClasses());
        ArrayList<GalileoRealmModel> pojos = new ArrayList<>();
        try {
            for (Class<? extends RealmModel> klass : realmModelClasses) {
                final String fullName = Class.forName(klass.getName()).getPackage().getName() + ".";
                final String tableName = klass.getName().replace(fullName, "");
                pojos.add(new GalileoRealmModel(klass, realm.where(tableName).findAll().size()));
            }
        } catch (Exception exception) { }

        realm.close();

        return pojos;
    }

    @NonNull private static ArrayList<GalileoRealmModel> sortPojos(@NonNull ArrayList<GalileoRealmModel> pojos, @ModelsContract.SortMode int sortMode) {
        Collections.sort(pojos, (o1, o2) -> o1.getKlass().getSimpleName().compareTo(o2.getKlass().getSimpleName()));
        if (sortMode == ModelsContract.SortMode.ASC) {
            Collections.reverse(pojos);
        }
        return pojos;
    }

    @NonNull private static ArrayList<GalileoRealmModel> filterPojos(@NonNull ArrayList<GalileoRealmModel> pojos, @Nullable String filter) {
        ArrayList<GalileoRealmModel> filteredPojos = null;
        if (filter != null && !filter.isEmpty()) {
            filteredPojos = new ArrayList<>();
            for (GalileoRealmModel pojo : pojos) {
                if (pojo.getKlass().getSimpleName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredPojos.add(pojo);
                }
            }
        }
        return filteredPojos == null ? pojos : filteredPojos;
    }

}