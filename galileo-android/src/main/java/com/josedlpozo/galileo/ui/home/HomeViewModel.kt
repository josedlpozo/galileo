package com.josedlpozo.galileo.ui.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.josedlpozo.galileo.items.GalileoItem

class HomeViewModel : ViewModel() {

    var items: MutableLiveData<List<GalileoItem>> = MutableLiveData()
        private set

    var shareText: MutableLiveData<String> = MutableLiveData()
        private set

    fun start(items: List<GalileoItem>) {
        this.items.value = items
    }

    fun share() {
        val text = items.value?.map {
            " --- " + it.name + " --- \n\n" + it.snapshot()
        } ?: listOf()

        shareText.value = text.joinToString("\n\n\n")
    }

}
