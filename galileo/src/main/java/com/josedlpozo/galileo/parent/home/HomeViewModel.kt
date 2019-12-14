/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.parent.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.parent.SnapshotGenerator
import java.io.File

internal class HomeViewModel : ViewModel() {

    var items: MutableLiveData<List<GalileoItem>> = MutableLiveData()
        private set

    var shareText: MutableLiveData<File> = MutableLiveData()
        private set

    fun start(items: List<GalileoItem>) {
        this.items.value = items
    }

    fun share(file: File) {
        val items = items.value ?: return

        file.printWriter().use { out ->
            out.println(SnapshotGenerator.generate(items))
        }

        shareText.value = file
    }

}
