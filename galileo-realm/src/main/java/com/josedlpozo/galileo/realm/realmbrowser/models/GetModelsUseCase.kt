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
package com.josedlpozo.galileo.realm.realmbrowser.models

import com.josedlpozo.galileo.realm.realmbrowser.models.model.Asc
import com.josedlpozo.galileo.realm.realmbrowser.models.model.Desc
import com.josedlpozo.galileo.realm.realmbrowser.models.model.GalileoRealmModel
import com.josedlpozo.galileo.realm.realmbrowser.models.model.Sort
import io.realm.DynamicRealm
import io.realm.RealmConfiguration

internal class GetModelsUseCase(private val configuration: RealmConfiguration) {

    fun all(filter: String, sort: Sort, callback: (List<GalileoRealmModel>) -> Unit) {
        val realm = DynamicRealm.getInstance(configuration)
        val models = realm.configuration.realmObjectClasses.map {
            val name = Class.forName(it.name).`package`?.name + "."
            val tableName = it.name.replace(name, "")
            GalileoRealmModel(it, realm.where(tableName).findAll().size)
        }

        realm.close()
        callback(when (sort) {
                     Asc -> models.sortedBy { it.klass.canonicalName }
                     Desc -> models.sortedByDescending { it.klass.canonicalName }
                 }.filter { it.klass.simpleName.toLowerCase().contains(filter.toLowerCase()) })
    }
}