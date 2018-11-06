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

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractor;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseView;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.InformationPojo;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.ModelPojo;
import java.lang.annotation.Retention;
import java.util.ArrayList;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface ModelsContract {
    @Retention(SOURCE)
    @IntDef({SortMode.ASC, SortMode.DESC})
    @interface SortMode {
        int ASC = 0;
        int DESC = 1;
    }

    interface View extends BaseView<Presenter> {
        void updateWithModels(@NonNull ArrayList<ModelPojo> filesList, @SortMode int sortMode);

        Context getViewContext();

        void presentShareDialog(@NonNull String path);

        void showInformation(@NonNull InformationPojo informationPojo);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate();

        void onModelSelected(ModelPojo item);

        void onSortModeChanged();

        void onShareSelected();

        void onFilterChanged(@NonNull String filter);

        void onInformationSelected();

        void updateWithModels(@NonNull ArrayList<ModelPojo> modelsList, @SortMode int sortMode);

        void presentShareDialog(@NonNull String path);

        void showInformation(@NonNull InformationPojo informationPojo);
    }

    interface Interactor extends BaseInteractor {
        void requestForContentUpdate();

        void updateWithFilter(String filter);

        void updateWithSortModeChanged();

        void onShareSelected();

        void onInformationSelected();
    }
}