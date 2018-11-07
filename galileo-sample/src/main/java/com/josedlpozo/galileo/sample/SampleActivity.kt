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
package com.josedlpozo.galileo.sample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.josedlpozo.galileo.Galileo
import com.josedlpozo.galileo.sample.SampleApiService.Data
import kotlinx.android.synthetic.main.activity_sample.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        prefillPreferences()
        doHttpActivity()
        fillLogcat()

        actionButton.setOnClickListener {
            Intent(this, SecondActivity::class.java).apply {
                putExtra("Hello", "World")
                putExtra("I am", "josedlpozo")
            }.also(::startActivity)
        }
    }

    private fun fillLogcat() {
        Log.e("SampleActivity", "Galileo sample log error")
        Log.e("SampleActivity", "Galileo sample log error2")
    }

    private fun prefillPreferences() {
        fill(getPreferences(Context.MODE_PRIVATE))
        fill(PreferenceManager.getDefaultSharedPreferences(this))
        fill(getSharedPreferences("another file", Context.MODE_PRIVATE))
        fill(getSharedPreferences("com.google.pruebas", Context.MODE_PRIVATE))
    }

    private fun fill(preferences: SharedPreferences) {
        preferences.edit()
                .putString("some_string", "a string value")
                .putInt("some_int", 42)
                .putLong("some_long", System.currentTimeMillis())
                .putBoolean("some_boolean", true)
                .putFloat("some_float", 3.14f)
                .putStringSet("some_set", HashSet<String>(Arrays.asList<String>("a", "b", "c")))
                .apply()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(Galileo.interceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    private fun doHttpActivity() {
        val api = SampleApiService.getInstance(getClient())
        val cb = object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }
        }
        api.get().enqueue(cb)
        api.post(Data("posted")).enqueue(cb)
        api.patch(Data("patched")).enqueue(cb)
        api.put(Data("put")).enqueue(cb)
        api.delete().enqueue(cb)
        api.status(201).enqueue(cb)
        api.status(401).enqueue(cb)
        api.status(500).enqueue(cb)
        api.delay(9).enqueue(cb)
        api.delay(15).enqueue(cb)
        api.redirectTo("https://http2.akamai.com").enqueue(cb)
        api.redirect(3).enqueue(cb)
        api.redirectRelative(2).enqueue(cb)
        api.redirectAbsolute(4).enqueue(cb)
        api.stream(500).enqueue(cb)
        api.streamBytes(2048).enqueue(cb)
        api.image("image/png").enqueue(cb)
        api.gzip().enqueue(cb)
        api.xml().enqueue(cb)
        api.utf8().enqueue(cb)
        api.deflate().enqueue(cb)
        api.cookieSet("v").enqueue(cb)
        api.basicAuth("me", "pass").enqueue(cb)
        api.drip(512, 5, 1, 200).enqueue(cb)
        api.deny().enqueue(cb)
        api.cache("Mon").enqueue(cb)
        api.cache(30).enqueue(cb)
    }
}
