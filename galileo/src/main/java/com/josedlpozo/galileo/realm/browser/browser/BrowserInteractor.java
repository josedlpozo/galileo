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
package com.josedlpozo.galileo.realm.browser.browser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.josedlpozo.galileo.realm.browser.helper.DataHolder;
import com.josedlpozo.galileo.realm.browser.helper.RealmPreferences;
import com.josedlpozo.galileo.realm.browser.helper.Utils;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.josedlpozo.galileo.realm.browser.helper.DataHolder.DATA_HOLDER_KEY_CLASS;
import static com.josedlpozo.galileo.realm.browser.helper.DataHolder.DATA_HOLDER_KEY_FIELD;
import static com.josedlpozo.galileo.realm.browser.helper.DataHolder.DATA_HOLDER_KEY_OBJECT;

class BrowserInteractor {
    @Nullable
    private Class<? extends RealmModel> realmModelClass = null;
    @Nullable
    private DynamicRealm dynamicRealm;
    @Nullable
    private List<Field> fields;
    @Nullable
    private ArrayList<Integer> selectedFieldIndices;

    private BrowserPresenter presenter;

    BrowserInteractor(BrowserPresenter presenter) {
        this.presenter = presenter;
    }

    public void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, int displayMode) {
        if (dynamicRealm == null || dynamicRealm.isClosed()) return;
        this.dynamicRealm = dynamicRealm;

        if (displayMode == 0) {
            this.realmModelClass = (Class<? extends RealmModel>) DataHolder.Companion.getInstance().retrieve(DATA_HOLDER_KEY_CLASS);
            presenter.updateWithRealmObjects(dynamicRealm.where(this.realmModelClass.getSimpleName()).findAll());
        } else if (displayMode == 1) {
            DynamicRealmObject dynamicRealmObject = (DynamicRealmObject) DataHolder.Companion.getInstance().retrieve(DATA_HOLDER_KEY_OBJECT);
            Field field = (Field) DataHolder.Companion.getInstance().retrieve(DATA_HOLDER_KEY_FIELD);
            if (dynamicRealmObject != null && field != null) {
                presenter.updateWithRealmObjects(dynamicRealmObject.getList(field.getName()));
                if (Utils.INSTANCE.isParametrizedField(field)) {
                    this.realmModelClass = (Class<? extends RealmObject>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                } else {
                    throw new IllegalStateException("This field must be parametrized.");
                }
            } else {
                throw new IllegalStateException("No object or field have been saved to DataHolder.");
            }
        } else {
            throw new IllegalStateException("Unsupported display mode.");
        }

        presenter.updateWithFABVisibility(this.realmModelClass != null);

        presenter.updateWithTitle(String.format("%s", this.realmModelClass.getSimpleName()));

        fields = getFieldsList(dynamicRealm, this.realmModelClass);
        selectedFieldIndices = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            if (i < 3) selectedFieldIndices.add(i);
        }
        updateSelectedFields();

        presenter.updateWithTextWrap(new RealmPreferences(context).shouldWrapText());
    }

    public void onWrapTextOptionToggled(@NonNull Context context) {
        RealmPreferences realmPreferences = new RealmPreferences(context);
        realmPreferences.setShouldWrapText(!realmPreferences.shouldWrapText());
        presenter.updateWithTextWrap(realmPreferences.shouldWrapText());
    }

    public void onNewObjectSelected() {
        if (this.realmModelClass != null) {
            presenter.showNewObjectActivity(this.realmModelClass);
        } else {
            throw new IllegalStateException();
        }
    }

    public void onInformationSelected() {
        if (dynamicRealm != null && !dynamicRealm.isClosed() && realmModelClass != null) {
            presenter.showInformation(dynamicRealm.where(realmModelClass.getSimpleName()).count());
        }
    }

    public void onRowSelected(@NonNull DynamicRealmObject dynamicRealmObject) {
        if (this.realmModelClass != null) {
            DataHolder.Companion.getInstance().save(DATA_HOLDER_KEY_OBJECT, dynamicRealmObject);
            presenter.showObjectActivity(this.realmModelClass);
        }
    }

    public void onFieldSelectionChanged(int fieldIndex, boolean checked) {
        if (selectedFieldIndices != null) {
            if (checked && !selectedFieldIndices.contains(fieldIndex)) {
                selectedFieldIndices.add(fieldIndex);
            } else if (selectedFieldIndices.contains(fieldIndex)) {
                selectedFieldIndices.remove((Integer) fieldIndex);
            }
            updateSelectedFields();
        }
    }

    @NonNull
    private static List<Field> getFieldsList(@NonNull DynamicRealm dynamicRealm, @NonNull Class<? extends RealmModel> realmModelClass) {
        RealmObjectSchema schema = dynamicRealm.getSchema().get(realmModelClass.getSimpleName());
        ArrayList<Field> fieldsList = new ArrayList<>();
        for (String s : schema.getFieldNames()) {
            try {
                fieldsList.add(realmModelClass.getDeclaredField(s));
            } catch (NoSuchFieldException e) { }
        }
        return fieldsList;
    }

    private void updateSelectedFields() {
        if (selectedFieldIndices != null && fields != null) {
            Integer[] selectedFieldIndicesArray = new Integer[selectedFieldIndices.size()];
            selectedFieldIndices.toArray(selectedFieldIndicesArray);
            presenter.updateWithFieldList(fields, selectedFieldIndicesArray);
        }
    }
}