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