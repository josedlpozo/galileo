package com.josedlpozo.galileo.realm.realmbrowser.files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import com.josedlpozo.galileo.realm.realmbrowser.basemvp.BaseInteractorImpl;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class FilesInteractor extends BaseInteractorImpl<FilesContract.Presenter> implements FilesContract.Interactor {
    private static List<String> ignoreExtensionList = new ArrayList<>();

    static {
        ignoreExtensionList.add(".log");
        ignoreExtensionList.add(".log_a");
        ignoreExtensionList.add(".log_b");
        ignoreExtensionList.add(".lock");
        ignoreExtensionList.add(".management");
        ignoreExtensionList.add(".temp");
    }


    FilesInteractor(FilesContract.Presenter presenter) {
        super(presenter);
    }

    private boolean isValidFileName(String fileName) {
        return fileName.lastIndexOf(".") > 0 && !ignoreExtensionList.contains(fileName.substring(fileName.lastIndexOf(".")));
    }

    @Override
    public void requestForContentUpdate(@NonNull Context context) {
        File dataDir = new File(context.getApplicationInfo().dataDir, "files");
        File[] files = dataDir.listFiles();
        ArrayList<FilesPojo> fileList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (isValidFileName(fileName)) {
                    fileList.add(new FilesPojo(fileName, Formatter.formatShortFileSize(context, file.length()), file.length()));
                }
            }
        }
        getPresenter().updateWithFiles(fileList);
    }
}
