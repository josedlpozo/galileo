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
package com.josedlpozo.galileo.realm.browser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.josedlpozo.galileo.R;
import io.realm.DynamicRealmObject;
import io.realm.RealmFieldType;
import io.realm.RealmObjectSchema;
import java.lang.reflect.Field;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;
import static android.support.v4.content.ContextCompat.getColor;
import static android.support.v4.content.ContextCompat.getDrawable;

abstract class RealmBrowserViewField extends LinearLayout {
    private TextView tvFieldName;
    private TextView tvFieldType;
    private ImageView ivFieldPrimaryKey;
    private ImageView ivFieldInfo;
    private CheckBox cbxFieldIsNull;
    private final Field field;
    private final RealmObjectSchema realmObjectSchema;


    public RealmBrowserViewField(Context context, @NonNull RealmObjectSchema realmObjectSchema, @NonNull Field field) {
        super(context);

        this.realmObjectSchema = realmObjectSchema;
        this.field = field;

        initHeaderViews(context);
        inflateViewStub();
        initViewStubView();

        tvFieldName.setText(field.getName());
        tvFieldType.setText(getFieldTypeString());

        setupNullableCheckBox(field);
        setupPrimaryKeyImageView(field);
    }

    private void initHeaderViews(Context context) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.realm_browser_fieldview, this);

        tvFieldName = this.findViewById(R.id.realm_browser_field_name);
        tvFieldType = this.findViewById(R.id.realm_browser_field_type);
        cbxFieldIsNull = this.findViewById(R.id.realm_browser_field_setnull);
        ivFieldPrimaryKey = this.findViewById(R.id.realm_browser_field_primarykey);
        ivFieldInfo = this.findViewById(R.id.realm_browser_field_info);
    }

    private void setupPrimaryKeyImageView(@NonNull Field field) {
        if (realmObjectSchema.isPrimaryKey(field.getName())) {
            ivFieldPrimaryKey.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24dp));
            ivFieldPrimaryKey.setVisibility(VISIBLE);
        } else if (realmObjectSchema.isRequired(field.getName())) {
            // TODO set key drawable with star
        } else {
            ivFieldPrimaryKey.setVisibility(GONE);
        }
    }

    private void setupNullableCheckBox(@NonNull Field field) {
        if (realmObjectSchema.isNullable(field.getName()) && realmObjectSchema.getFieldType(field.getName()) != RealmFieldType.LIST) {
            cbxFieldIsNull.setOnCheckedChangeListener((buttonView, isChecked) -> toggleAllowInput(!isChecked));
        } else {
            cbxFieldIsNull.setVisibility(GONE);
        }
    }

    public void togglePrimaryKeyError(boolean show) {
        ivFieldPrimaryKey.setImageDrawable(getDrawable(getContext(), R.drawable.realm_browser_ic_vpn_key_black_24dp));
        if (show) {
            ivFieldPrimaryKey.setColorFilter(getColor(getContext(), R.color.realm_browser_error), SRC_ATOP);
            ivFieldPrimaryKey.setOnClickListener(
                v -> Snackbar.make(RealmBrowserViewField.this, "Primary key \"" + getValue() + "\" already exists.", Snackbar.LENGTH_SHORT).show());
            setBackgroundColor(getColor(getContext(), R.color.realm_browser_error_light));
        } else {
            ivFieldPrimaryKey.setColorFilter(null);
            ivFieldPrimaryKey.setOnClickListener(null);
            setBackgroundColor(getColor(getContext(), android.R.color.transparent));
        }
    }

    protected void updateFieldIsNullCheckBoxValue(boolean checked) {
        cbxFieldIsNull.setChecked(checked);
    }

    protected String getFieldTypeString() {
        return getField().getType().getSimpleName();
    }

    protected abstract void inflateViewStub();

    public abstract void initViewStubView();

    public abstract Object getValue();

    public abstract void toggleAllowInput(boolean allow);

    public abstract boolean isInputValid();

    public abstract void setRealmObject(@NonNull DynamicRealmObject realmObject);

    protected RealmObjectSchema getRealmObjectSchema() {
        return realmObjectSchema;
    }

    public Field getField() {
        return field;
    }

    protected boolean isFieldIsNullCheckBoxChecked() {
        return cbxFieldIsNull.isChecked();
    }

    protected ImageView getFieldInfoImageView() {
        return ivFieldInfo;
    }
}