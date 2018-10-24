package com.josedlpozo.galileo.realm.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenterImpl;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder;
import com.josedlpozo.galileo.realm.realmbrowser.models.view.ModelsActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmMigrationNeededException;
import java.util.ArrayList;

public class FilesPresenter extends BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private final FilesInteractor interactor;

    public FilesPresenter() {
        this.interactor = new FilesInteractor(this);
    }

    @Override
    public void attachView(@NonNull FilesContract.View view) {
        super.attachView(view);
    }

    @Override
    public void requestForContentUpdate(@NonNull Context context) {
        interactor.requestForContentUpdate(context);
    }

    @Override
    public void onFileSelected(FilesPojo item) {
        try {
            RealmConfiguration config = new RealmConfiguration.Builder().name(item.getName()).build();
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CONFIG, config);
            Realm realm = Realm.getInstance(config);
            realm.close();
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().getViewContext().startActivity(ModelsActivity.getIntent(getView().getViewContext(), item.getName()));
            }
        } catch (RealmMigrationNeededException e) {
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().showToast(String.format("%s %s", getView().getViewContext().getString(R.string.realm_browser_open_error), getView().getViewContext().getString(R.string.realm_browser_error_migration)));
            }
        } catch (RealmFileException e) {
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().showToast(String.format("%s %s", getView().getViewContext().getString(R.string.realm_browser_open_error), e.getMessage()));
            }
        } catch (Exception e) {
            if (isViewAttached()) {
                //noinspection ConstantConditions
                getView().showToast(String.format("%s %s", getView().getViewContext().getString(R.string.realm_browser_open_error), getView().getViewContext().getString(R.string.realm_browser_error_openinstances)));
            }
        }
    }

    @Override
    public void updateWithFiles(ArrayList<FilesPojo> filesList) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().updateWithFiles(filesList);
        }
    }
}
