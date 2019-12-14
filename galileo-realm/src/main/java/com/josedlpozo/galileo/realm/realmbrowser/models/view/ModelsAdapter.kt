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
package com.josedlpozo.galileo.realm.realmbrowser.models.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.josedlpozo.galileo.realm.realmbrowser.models.model.GalileoRealmModel

internal class ModelsAdapter(private val onClick: (GalileoRealmModel) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<ModelsAdapter.ViewHolder>() {

    private val files: MutableList<GalileoRealmModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = files[position]
        holder.bind(model)
        holder.itemView.setOnClickListener {
            onClick(model)
        }
    }

    override fun getItemCount(): Int {
        return this.files.size
    }

    fun swapList(newList: List<GalileoRealmModel>) {
        val diffResult = DiffUtil.calculateDiff(ModelsDiffUtilsCallback(this.files, newList))
        diffResult.dispatchUpdatesTo(this)

        this.files.clear()
        this.files.addAll(newList)
    }

    internal class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        private val subTitle: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(model: GalileoRealmModel) = with(model) {
            title.text = klass.simpleName
            subTitle.text = count.toString()
        }
    }
}