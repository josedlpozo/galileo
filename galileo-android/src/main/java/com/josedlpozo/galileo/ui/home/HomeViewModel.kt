package com.josedlpozo.galileo.ui.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.josedlpozo.galileo.items.GalileoItem

class HomeViewModel : ViewModel() {

    var prueba: MutableLiveData<List<GalileoItem>> = MutableLiveData()
        private set

    var shareText: MutableLiveData<String> = MutableLiveData()
        private set

    fun start(items: List<GalileoItem>) {
        prueba.value = items
    }

    fun share() {
        val text = prueba.value?.map {
            " --- " + it.name + " --- \n\n" + it.snapshot()
        } ?: listOf()

        shareText.value = text.joinToString("\n\n\n")
    }

}
