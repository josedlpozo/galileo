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
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.josedlpozo.galileo.Galileo
import com.josedlpozo.galileo.sample.SampleApiService.Data
import kotlinx.android.synthetic.main.activity_sample.*
import com.josedlpozo.galileo.sample.realm.DeveloperModel
import com.josedlpozo.galileo.sample.realm.TeamModel
import io.realm.Realm
import io.realm.RealmConfiguration
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
        prefillRealm()

        fillLogcat()

        actionButton.setOnClickListener {
            Intent(this, SecondActivity::class.java).apply {
                putExtra("Hello", "World")
                putExtra("I am", "josedlpozo")
                putExtra("Fistrum", LONG_TEXT)
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

    private fun prefillRealm() {
        Realm.init(this)
        with(Realm.getInstance(RealmConfiguration.Builder().name("database_developers").schemaVersion(1).deleteRealmIfMigrationNeeded().build())) {
            executeTransaction {
                copyToRealmOrUpdate(DeveloperModel(1, "jmdelpozo"))
                copyToRealmOrUpdate(DeveloperModel(2, "fpulido"))
                copyToRealmOrUpdate(DeveloperModel(3, "vfrancisco"))
            }
            close()
        }
        with(Realm.getInstance(RealmConfiguration.Builder().name("database_teams").deleteRealmIfMigrationNeeded().build())) {
            executeTransaction {
                copyToRealmOrUpdate(TeamModel(1, "android"))
                copyToRealmOrUpdate(TeamModel(2, "ios"))
            }
            close()
        }
    }

    companion object {
        val LONG_TEXT = """ Lorem fistrum te va a hasé pupitaa sexuarl diodeno la caidita. Me cago en tus muelas me cago en tus muelas condemor fistro va usté muy cargadoo quietooor caballo blanco caballo negroorl está la cosa muy malar la caidita ahorarr. Caballo blanco caballo negroorl a gramenawer pupita no te digo trigo por no llamarte Rodrigor está la cosa muy malar de la pradera mamaar ese hombree. Hasta luego Lucas a gramenawer ahorarr apetecan me cago en tus muelas. Diodeno apetecan torpedo torpedo amatomaa te voy a borrar el cerito papaar papaar. Mamaar torpedo amatomaa ese hombree a gramenawer se calle ustée ese pedazo de está la cosa muy malar se calle ustée.

A wan me cago en tus muelas jarl sexuarl por la gloria de mi madre qué dise usteer pupita fistro a peich. A wan me cago en tus muelas benemeritaar amatomaa torpedo quietooor mamaar. Amatomaa a gramenawer quietooor fistro hasta luego Lucas torpedo ahorarr. Llevame al sircoo amatomaa la caidita te voy a borrar el cerito. Pupita a peich llevame al sircoo ahorarr caballo blanco caballo negroorl mamaar. Diodeno a wan caballo blanco caballo negroorl mamaar tiene musho peligro condemor. A peich benemeritaar no puedor fistro por la gloria de mi madre fistro fistro fistro. Amatomaa diodenoo pecador apetecan mamaar ese que llega se calle ustée caballo blanco caballo negroorl te va a hasé pupitaa al ataquerl.

Torpedo diodeno hasta luego Lucas pupita apetecan te va a hasé pupitaa. No puedor apetecan amatomaa a peich sexuarl. Pecador diodenoo no puedor te va a hasé pupitaa te va a hasé pupitaa pecador. Al ataquerl pecador sexuarl se calle ustée. Me cago en tus muelas ese hombree sexuarl benemeritaar papaar papaar. La caidita me cago en tus muelas ese pedazo de me cago en tus muelas te va a hasé pupitaa papaar papaar benemeritaar condemor ese pedazo de ese hombree te voy a borrar el cerito. Pupita amatomaa benemeritaar va usté muy cargadoo ese hombree ese hombree qué dise usteer condemor. Jarl la caidita está la cosa muy malar de la pradera de la pradera.
                """.trimIndent()
    }
}
