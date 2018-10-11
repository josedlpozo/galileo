package com.josedlpozo.galileo.realm.realmbrowser.object.view;

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
        if (!Utils.isParametrizedField(getField())) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected String getFieldTypeString() {
        return Utils.createParametrizedName(getField());
    }

    @Override
    public void inflateViewStub() {
        ViewStub stub = (ViewStub) findViewById(R.id.realm_browser_stub);
        stub.setLayoutResource(R.layout.realm_browser_fieldview_textview);
        stub.inflate();
    }

    @Override
    public void initViewStubView() {
        textView = (TextView) findViewById(R.id.realm_browser_field_textview);
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
        if (Utils.isParametrizedField(getField())) {
            this.realmObject = realmObject;
            textView.setText(String.format("Length: %s", realmObject.getList(getField().getName()).size()));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
