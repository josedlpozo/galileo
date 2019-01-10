package com.josedlpozo.galileo.screenshooter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.os.Environment
import android.os.Handler
import android.widget.Toast
import com.josedlpozo.galileo.realm.realmbrowser.helper.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import com.josedlpozo.galileo.R

class Shooter(private val context: Context, private val activity: Activity) {

    companion object {
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/galileo-screenshots")
    }

    private val supportedLocales = listOf("es", "en", "pt", "it", "fr", "de")

    fun shoot() {
        Toast.makeText(context, R.string.shoot, Toast.LENGTH_LONG).show()
        Handler().postDelayed({
            changeLocale("es")
            screenshot()
            Handler().postDelayed({
                changeLocale("en")
                screenshot()
                Handler().postDelayed({
                    changeLocale("it")
                    screenshot()
                    Handler().postDelayed({
                        changeLocale("fr")
                        screenshot()
                        Handler().postDelayed({
                            changeLocale("de")
                            screenshot()
                            Handler().postDelayed({
                                changeLocale("pt")
                                screenshot()
                            }, 1000)
                        }, 1000)
                    }, 1000)
                }, 1000)
            }, 1000)
        }, 1000)
    }

    private fun screenshot() {
        try {
            val view = activity.window.decorView.rootView
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false

            if (!folder.exists()) folder.mkdirs()

            val file = System.nanoTime().toString() + "galileo-screenshot.png"
            val outputStream = FileOutputStream(File(folder, file))
            val quality = 100
            bitmap.compress(PNG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            //openScreenshot(File(file))
        } catch (exception: Exception) {
            exception.toString()
        }
    }

    private fun openScreenshot(screenshot: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val uri = FileProvider.getUriForFile(context, "${context.applicationInfo.packageName}.com.josedlpozo.galileo.provider", screenshot)
        intent.setDataAndType(uri, "image/*")
        context.startActivity(intent)
    }

    private fun changeLocale(locale: String) {
        val locale = Locale(locale)
        val resources = activity.resources
        val displayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, displayMetrics)
        activity.recreate()
    }

}