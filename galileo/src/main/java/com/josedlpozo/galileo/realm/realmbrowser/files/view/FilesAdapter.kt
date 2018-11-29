/*
 * The MIT License (MIT)
 *
 * Original Work: Copyright (c) 2015 Danylyk Dmytro
 *
 * Modified Work: Copyright (c) 2015 Rottmann, Jonas
 *
 * Modified Work: Copyright (c) 2018 vicfran
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
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
package com.josedlpozo.galileo.realm.realmbrowser.files.view

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.josedlpozo.galileo.realm.realmbrowser.files.model.RealmFile

internal class FilesAdapter(private val files: MutableList<RealmFile>,
                            private val onClick: (RealmFile) -> Unit) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(files[position])
        holder.itemView.setOnClickListener {
            onClick(files[position])
        }
    }

    override fun getItemCount(): Int = files.size

    fun swapList(newList: List<RealmFile>) {
        val diffResult = DiffUtil.calculateDiff(FilesDiffUtilsCallback(files, newList))
        diffResult.dispatchUpdatesTo(this)

        files.clear()
        files.addAll(newList)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        private val subTitle: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(realmFile: RealmFile) = with(realmFile) {
            title.text = name
            subTitle.text = size
        }
    }
}