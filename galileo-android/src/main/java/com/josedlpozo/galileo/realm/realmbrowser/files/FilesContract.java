package com.josedlpozo.galileo.realm.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractor;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseView;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.util.ArrayList;

public interface FilesContract {

    interface View extends BaseView<Presenter> {
        void updateWithFiles(@NonNull ArrayList<FilesPojo> filesList);

        Context getViewContext();

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void requestForContentUpdate(@NonNull Context context);

        void onFileSelected(FilesPojo item);

        void updateWithFiles(ArrayList<FilesPojo> filesList);
    }


    interface Interactor extends BaseInteractor {
        void requestForContentUpdate(@NonNull Context context);
    }
}
