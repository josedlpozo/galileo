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
package com.josedlpozo.galileo.realm.realmbrowser.files.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.util.List;

class FilesDiffUtilsCallback extends DiffUtil.Callback {

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_SIZE = "KEY_SIZE";

    private List<FilesPojo> mOldList;
    private List<FilesPojo> mNewList;

    FilesDiffUtilsCallback(List<FilesPojo> oldList, List<FilesPojo> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).getName().equals(mOldList.get(oldItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        FilesPojo newProduct = mNewList.get(newItemPosition);
        FilesPojo oldProduct = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newProduct.getName().equals(oldProduct.getName())) {
            diffBundle.putString(KEY_NAME, newProduct.getName());
        }
        if (newProduct.getSizeInByte() != oldProduct.getSizeInByte()) {
            diffBundle.putString(KEY_SIZE, newProduct.getSize());
        }
        if (diffBundle.size() == 0) {
            return null;
        }
        return diffBundle;
    }
}
