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
package com.josedlpozo.galileo.realm.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.RealmSnapshooter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BasePresenterImpl;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder;
import com.josedlpozo.galileo.realm.realmbrowser.models.view.ModelsActivity;
import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmMigrationNeededException;
import java.util.ArrayList;

public class FilesPresenter extends BasePresenterImpl<FilesContract.View> implements FilesContract.Presenter {

    private final FilesInteractor interactor;
    private final RealmSnapshooter realmSnapshooter = new RealmSnapshooter();
    private ArrayList<FilesPojo> files;

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
            DynamicRealm realm = DynamicRealm.getInstance(config);
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
            files = filesList;
            getView().updateWithFiles(filesList);
        }
    }

    @Override public String generateSnapshot() {
        return realmSnapshooter.snapshoot(files);
    }
}
