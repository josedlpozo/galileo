package com.josedlpozo.galileo.realm

import com.josedlpozo.galileo.realm.realmbrowser.files.model.RealmFile
import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmConfiguration
import io.realm.RealmFieldType.BOOLEAN
import io.realm.RealmFieldType.DATE
import io.realm.RealmFieldType.DOUBLE
import io.realm.RealmFieldType.FLOAT
import io.realm.RealmFieldType.INTEGER
import io.realm.RealmFieldType.STRING
import io.realm.RealmModel

internal class RealmSnapshooter {

    private var snapshoot = ""

    fun shoot(files: List<RealmFile>): String {
        add("There are ${files.size} Realm databases used by this app\n\n")
        files.map {
            val realm = DynamicRealm.getInstance(RealmConfiguration.Builder().name(it.name).build())
            val realmClasses = realm.configuration.realmObjectClasses
            add("Database ${it.name} has ${realmClasses.size} tables\n")
            realmClasses.forEach {
                addTables(it, realm)
            }
            add("\n\n")
        }
        return snapshoot
    }

    private fun addTables(realmModelClass: Class<out RealmModel>, realm: DynamicRealm) {
        val tableName = realmModelClass.name.replace(Class.forName(realmModelClass.name).getPackage().name + ".", "")
        realm.executeTransaction {
            val rows = it.where(tableName).findAll()
            val columns = realm.schema[tableName]?.fieldNames ?: emptySet()
            add("- $tableName has ${columns.size} columns and ${rows.size} rows\n")

            addColumns(columns)

            if (rows.isValid && rows.size > 0) {
                rows.forEach { row ->
                    columns.forEach { column ->
                        addElement(row, column)
                    }
                    add("\n")
                }
            }
        }
    }

    private fun addColumns(columns: Collection<String>) {
        columns.forEach {
            add("| $it |")
        }
        add("\n")
    }

    private fun addElement(dynamicRealmObject: DynamicRealmObject, column: String) {
        with (dynamicRealmObject) {
            val fieldType = getFieldType(column)
            val fieldValue = when (fieldType) {
                INTEGER -> getInt(column).toString()
                BOOLEAN -> getBoolean(column).toString()
                STRING -> getString(column).toString()
                DATE -> getDate(column).toString()
                FLOAT -> getFloat(column).toString()
                DOUBLE -> getDouble(column).toString()
                else -> ""
            }
            add("$fieldValue ")
        }
    }

    private fun add(shot: String) {
        snapshoot = snapshoot.plus(shot)
    }

}