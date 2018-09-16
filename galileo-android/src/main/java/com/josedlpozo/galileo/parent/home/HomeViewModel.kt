package com.josedlpozo.galileo.parent.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.josedlpozo.galileo.items.GalileoItem
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
        val text = items.value?.map {
            " --- " + it.name + " --- \n\n" + it.snapshot()
        } ?: listOf()

        file.printWriter().use { out ->
            out.println(text.joinToString("\n\n\n"))
        }
        shareText.value = file
    }

}
