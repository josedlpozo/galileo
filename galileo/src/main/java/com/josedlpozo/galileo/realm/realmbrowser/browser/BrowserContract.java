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
package com.josedlpozo.galileo.realm.realmbrowser.browser;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractor;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseView;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

public interface BrowserContract {

    @IntDef({DisplayMode.REALM_CLASS, DisplayMode.REALM_LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayMode {
        int REALM_CLASS = 0;
        int REALM_LIST = 1;
    }

    interface View extends BaseView<Presenter> {
        Context getViewContext();

        void showMenu();

        void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects);

        void updateWithFABVisibility(boolean visible);

        void updateWithTitle(@NonNull String title);

        void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices);

        void showInformation(long numberOfRows);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @DisplayMode int displayMode);

        void onShowMenuSelected();

        void onFieldSelectionChanged(int fieldIndex, boolean checked);

        void onNewObjectSelected();

        void onInformationSelected();

        void onRowSelected(@NonNull DynamicRealmObject realmObject);

        void showNewObjectActivity(@NonNull final Class<? extends RealmModel> modelClass);

        void showObjectActivity(@NonNull final Class<? extends RealmModel> modelClass);

        void showInformation(long numberOfRows);

        void updateWithRealmObjects(AbstractList<? extends DynamicRealmObject> objects);

        void updateWithFABVisibility(boolean visible);

        void updateWithTitle(@NonNull String title);

        void updateWithFieldList(@NonNull List<Field> fields, Integer[] selectedFieldIndices);
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@NonNull Context context, @Nullable DynamicRealm dynamicRealm, @DisplayMode int displayMode);

        void onNewObjectSelected();

        void onInformationSelected();

        void onRowSelected(@NonNull DynamicRealmObject realmObject);

        void onFieldSelectionChanged(int fieldIndex, boolean checked);
    }
}