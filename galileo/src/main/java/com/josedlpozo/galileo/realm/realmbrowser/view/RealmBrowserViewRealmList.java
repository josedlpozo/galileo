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
package com.josedlpozo.galileo.realm.realmbrowser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewStub;
import android.widget.TextView;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;

class RealmBrowserViewRealmList extends RealmBrowserViewField {

    private TextView textView;
    private DynamicRealmObject realmObject;

    public RealmBrowserViewRealmList(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.INSTANCE.isParametrizedField(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected String getFieldTypeString() {
        return Utils.INSTANCE.createParametrizedName(getField());
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_textview);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = findViewById(R.id.realm_browser_field_textview);
    }

    @Override
    public Object getValue() {
        // TODO
        return realmObject.getList(getField().getName());
    }

    @Override
    public void toggleAllowInput(boolean allow) {

    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.INSTANCE.isParametrizedField(getField())) {
            this.realmObject = realmObject;
            textView.setText(String.format("Length: %s", realmObject.getList(getField().getName()).size()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}