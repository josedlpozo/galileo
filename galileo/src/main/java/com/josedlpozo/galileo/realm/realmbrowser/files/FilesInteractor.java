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
        ignoreExtensionList.add(".txt");
    }


    FilesInteractor(FilesContract.Presenter presenter) {
        super(presenter);
    }

    private boolean isValidFileName(String fileName) {
        if (fileName.lastIndexOf(".") > 0) {
            return !ignoreExtensionList.contains(fileName.substring(fileName.lastIndexOf(".")));
        } else {
            return true;
        }
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
