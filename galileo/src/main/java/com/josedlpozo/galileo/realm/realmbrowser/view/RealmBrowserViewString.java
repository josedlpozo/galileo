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
import android.widget.EditText;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;

class RealmBrowserViewString extends RealmBrowserViewField {

    EditText fieldEditText;

    public RealmBrowserViewString(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context, realmObjectSchema, field);
        if (!Utils.INSTANCE.isString(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getFieldTypeString() {
        return getField().getType().getSimpleName();
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_edittext);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        fieldEditText = findViewById(R.id.realm_browser_field_edittext);
        fieldEditText.setMaxLines(4);
    }

    @Override
    public Object getValue() {
        if (getRealmObjectSchema().isNullable(getField().getName()) && isFieldIsNullCheckBoxChecked()) {
            return null;
        }
        return fieldEditText.getText().toString();
    }

    @Override
    public void toggleAllowInput(boolean allow) {
        fieldEditText.setEnabled(allow);
    }

    @Override
    public boolean isInputValid() {
        return true;
    }

    @Override
    public void setRealmObject(@NonNull DynamicRealmObject realmObject) {
        if (Utils.INSTANCE.isString(getField())) {
            if (realmObject.getString(getField().getName()) == null) {
                updateFieldIsNullCheckBoxValue(true);
            } else {
                fieldEditText.setText(realmObject.getString(getField().getName()));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}