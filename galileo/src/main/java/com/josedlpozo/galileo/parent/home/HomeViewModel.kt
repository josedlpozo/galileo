package com.josedlpozo.galileo.parent.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.parent.SnapshotGenerator
import java.io.File

class HomeViewModel : ViewModel() {

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
