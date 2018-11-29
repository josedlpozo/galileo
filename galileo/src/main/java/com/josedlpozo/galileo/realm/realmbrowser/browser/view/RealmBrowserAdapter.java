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
package com.josedlpozo.galileo.realm.realmbrowser.browser.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils;
import io.realm.DynamicRealmObject;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

class RealmBrowserAdapter extends RecyclerView.Adapter<RealmBrowserAdapter.ViewHolder> {

    private final Context context;
    private final Listener listener;
    private AbstractList<? extends DynamicRealmObject> dynamicRealmObjects;
    private List<Field> fieldList;

    RealmBrowserAdapter(@NonNull Context context, @NonNull AbstractList<? extends DynamicRealmObject> realmObjects,
                        @NonNull List<Field> fieldList, @NonNull Listener listener) {
        this.context = context;
        this.dynamicRealmObjects = realmObjects;
        this.fieldList = fieldList;
        this.listener = listener;
    }

    void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }


    void setRealmList(AbstractList<? extends DynamicRealmObject> realmObjects) {
        this.dynamicRealmObjects = realmObjects;
    }

    @NonNull @Override
    public RealmBrowserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.realm_browser_item_realm_browser, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return dynamicRealmObjects == null ? 0 : dynamicRealmObjects.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.realm_browser_grey));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.realm_browser_white));
        }

        if (fieldList.isEmpty()) {
            holder.txtIndex.setText(null);
            holder.txtColumn1.setText(null);
            holder.txtColumn2.setText(null);
            holder.txtColumn3.setText(null);
        } else {
            holder.txtIndex.setText(String.valueOf(position));

            DynamicRealmObject realmObject = dynamicRealmObjects.get(position);
            initRowWeight(holder);
            initRowText(holder, realmObject);
        }
    }

    private void initRowWeight(ViewHolder holder) {
        LinearLayout.LayoutParams layoutParams2 = createLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = createLayoutParams();

        if (fieldList.size() == 1) {
            layoutParams2.weight = 0;
            layoutParams3.weight = 0;
        } else if (fieldList.size() == 2) {
            layoutParams2.weight = 1;
            layoutParams3.weight = 0;
        } else if (fieldList.size() == 3) {
            layoutParams2.weight = 1;
            layoutParams3.weight = 1;
        }
        holder.txtColumn2.setLayoutParams(layoutParams2);
        holder.txtColumn3.setLayoutParams(layoutParams3);
    }

    private void initRowText(ViewHolder holder, DynamicRealmObject realmObject) {
        if (fieldList.size() == 1) {
            initFieldText(holder.txtColumn1, realmObject, fieldList.get(0));
            holder.txtColumn2.setText(null);
            holder.txtColumn3.setText(null);
        } else if (fieldList.size() == 2) {
            initFieldText(holder.txtColumn1, realmObject, fieldList.get(0));
            initFieldText(holder.txtColumn2, realmObject, fieldList.get(1));
            holder.txtColumn3.setText(null);
        } else if (fieldList.size() == 3) {
            initFieldText(holder.txtColumn1, realmObject, fieldList.get(0));
            initFieldText(holder.txtColumn2, realmObject, fieldList.get(1));
            initFieldText(holder.txtColumn3, realmObject, fieldList.get(2));
        }
    }

    private void initFieldText(TextView txtColumn, DynamicRealmObject realmObject, Field field) {
        txtColumn.setText(Utils.getFieldValueString(realmObject, field));
        txtColumn.setOnClickListener(createClickListener(realmObject));
    }

    private View.OnClickListener createClickListener(@NonNull final DynamicRealmObject realmObject) {
        return v -> listener.onRowClicked(realmObject);
    }

    private LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public interface Listener {
        void onRowClicked(@NonNull DynamicRealmObject realmObject);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtIndex;
        final TextView txtColumn1;
        final TextView txtColumn2;
        final TextView txtColumn3;

        ViewHolder(View v) {
            super(v);
            txtIndex = v.findViewById(R.id.realm_browser_txtIndex);
            txtColumn1 = v.findViewById(R.id.realm_browser_txtColumn1);
            txtColumn2 = v.findViewById(R.id.realm_browser_txtColumn2);
            txtColumn3 = v.findViewById(R.id.realm_browser_txtColumn3);
        }
    }
}