/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
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
package com.josedlpozo.galileo.picker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.ContextThemeWrapper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.picker.utils.ColorUtils
import com.josedlpozo.galileo.picker.utils.PreferenceUtils
import com.larswerkman.lobsterpicker.LobsterPicker
import com.larswerkman.lobsterpicker.sliders.LobsterOpacitySlider
import com.viewpagerindicator.CirclePageIndicator

internal class DualColorPickerDialog : androidx.fragment.app.DialogFragment() {

    private lateinit var colorPickerViews: Array<ColorPickerViewHolder>

    private val mSliderTouchListener : View.OnTouchListener = View.OnTouchListener { view, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> view?.parent?.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP -> view?.parent?.requestDisallowInterceptTouchEvent(false)
        }
        view?.onTouchEvent(event)
        true
    }
    private val mClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            AlertDialog.BUTTON_POSITIVE -> {
                val context = context ?: return@OnClickListener
                PreferenceUtils.GridPreferences.setGridLineColor(context, colorPickerViews[0].picker.color)
                PreferenceUtils.GridPreferences.setKeylineColor(context, colorPickerViews[1].picker.color)
            }
            AlertDialog.BUTTON_NEGATIVE -> {
            }
        }
        dialog.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = View.inflate(context, R.layout.dialog_color_picker, null)

        initColorPickerViews()

        val viewPager = v.findViewById<ViewPager>(R.id.view_pager)
        val adapter = ColorPickerPagerAdapter()
        viewPager.adapter = adapter

        val pageIndicator = v.findViewById<CirclePageIndicator>(R.id.view_pager_indicator)
        pageIndicator.setViewPager(viewPager)
        pageIndicator.fillColor = ContextCompat.getColor(requireContext(), R.color.galileocolor_colorColorPickerCardTint)

        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.ToolDialog))
        builder.setView(v)
                .setTitle(R.string.color_picker_title)
                .setPositiveButton(R.string.color_picker_accept, mClickListener)
                .setNegativeButton(R.string.color_picker_cancel, mClickListener)

        return builder.create()
    }

    private fun initColorPickerViews() {
        colorPickerViews = Array(2) {
            ColorPickerViewHolder()
        }

        colorPickerViews[0].container = View.inflate(context, R.layout.lobsterpicker, null)
        colorPickerViews[0].picker = colorPickerViews[0].container.findViewById(R.id.lobsterpicker)
        colorPickerViews[0].slider = colorPickerViews[0].container.findViewById(R.id.opacityslider)
        colorPickerViews[0].picker.addDecorator(colorPickerViews[0].slider)
        var color = ColorUtils.getGridLineColor(requireContext())
        colorPickerViews[0].picker.color = color
        colorPickerViews[0].picker.history = color
        colorPickerViews[0].slider.setOnTouchListener(mSliderTouchListener)

        colorPickerViews[1].container = View.inflate(context, R.layout.lobsterpicker, null)
        colorPickerViews[1].picker = colorPickerViews[1].container.findViewById(R.id.lobsterpicker)
        colorPickerViews[1].slider = colorPickerViews[1].container.findViewById(R.id.opacityslider)
        colorPickerViews[1].picker.addDecorator(colorPickerViews[1].slider)
        color = ColorUtils.getKeylineColor(requireContext())
        colorPickerViews[1].picker.color = color
        colorPickerViews[1].picker.history = color
        colorPickerViews[1].slider.setOnTouchListener(mSliderTouchListener)
    }

    private inner class ColorPickerPagerAdapter : androidx.viewpager.widget.PagerAdapter() {

        override fun getCount(): Int {
            return colorPickerViews.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(colorPickerViews[position].container)
            return colorPickerViews[position].container
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                getString(R.string.color_picker_grid_page_title)
            } else {
                getString(R.string.color_picker_keyline_page_title)
            }
        }
    }

    private inner class ColorPickerViewHolder {
        internal lateinit var container: View
        internal lateinit var picker: LobsterPicker
        internal lateinit var slider: LobsterOpacitySlider
    }
}