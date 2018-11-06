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
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private final List<FilesPojo> files;
    private final OnFileSelectedListener listener;

    public FilesAdapter(@NonNull ArrayList<FilesPojo> list, @NonNull OnFileSelectedListener listener) {
        this.files = list;
        this.listener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        FilesPojo file = files.get(position);
        holder.title.setText(file.getName());
        holder.subTitle.setText(file.getSize());
        holder.itemView.setOnClickListener(createClickListener(this.files.get(position)));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals(FilesDiffUtilsCallback.KEY_NAME)) {
                    holder.title.setText(o.getString(key));
                } else if (key.equals(FilesDiffUtilsCallback.KEY_SIZE)) {
                    holder.subTitle.setText(o.getString(key));
                }
            }
        }
    }

    @Override public int getItemCount() {
        return this.files.size();
    }

    public void swapList(ArrayList<FilesPojo> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FilesDiffUtilsCallback(this.files, newList));
        diffResult.dispatchUpdatesTo(this);

        this.files.clear();
        this.files.addAll(newList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subTitle;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            subTitle = itemView.findViewById(android.R.id.text2);
        }
    }

    public interface OnFileSelectedListener {

        void onFileSelected(FilesPojo file);
    }

    private View.OnClickListener createClickListener(@NonNull final FilesPojo file) {
        return new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onFileSelected(file);
            }
        };
    }
}
