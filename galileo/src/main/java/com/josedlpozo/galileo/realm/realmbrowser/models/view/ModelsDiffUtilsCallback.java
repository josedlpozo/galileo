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
package com.josedlpozo.galileo.realm.realmbrowser.models.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.josedlpozo.galileo.realm.realmbrowser.models.model.ModelPojo;
import java.util.List;

class ModelsDiffUtilsCallback extends DiffUtil.Callback {

    public static final String KEY_CLASS = "KEY_CLASS";
    public static final String KEY_COUNT = "KEY_COUNT";

    private List<ModelPojo> mOldList;
    private List<ModelPojo> mNewList;

    ModelsDiffUtilsCallback(List<ModelPojo> oldList, List<ModelPojo> newList) {
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
        return mNewList.get(newItemPosition).getKlass().equals(mOldList.get(oldItemPosition).getKlass());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        ModelPojo newProduct = mNewList.get(newItemPosition);
        ModelPojo oldProduct = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newProduct.getKlass().equals(oldProduct.getKlass())) {
            diffBundle.putSerializable(KEY_CLASS, newProduct.getKlass());
        }
        if (newProduct.getCount() != oldProduct.getCount()) {
            diffBundle.putLong(KEY_COUNT, newProduct.getCount());
        }
        if (diffBundle.size() == 0) {
            return null;
        }
        return diffBundle;
    }
}
