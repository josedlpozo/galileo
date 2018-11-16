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
package com.josedlpozo.galileo.realm.realmbrowser.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {

    public static final String DATA_HOLDER_KEY_FIELD = "field";
    public static final String DATA_HOLDER_KEY_OBJECT = "obj";
    public static final String DATA_HOLDER_KEY_CONFIG = "config";
    public static final String DATA_HOLDER_KEY_CLASS = "class";

    private static final DataHolder instance = new DataHolder();

    @NonNull
    public static DataHolder getInstance() {
        return instance;
    }

    private DataHolder() {
    }

    private Map<String, SoftReference<Object>> data = new HashMap<>();

    public void save(@NonNull String id, @Nullable Object object) {
        data.put(id, new SoftReference<>(object));
    }

    @Nullable
    public Object retrieve(@NonNull String id) {
        SoftReference<Object> objectWeakReference = data.get(id);
        Object o = objectWeakReference.get();
        return o;
    }
}
