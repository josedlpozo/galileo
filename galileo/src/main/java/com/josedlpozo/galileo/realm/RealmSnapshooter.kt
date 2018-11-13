package com.josedlpozo.galileo.realm

import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo
import io.realm.DynamicRealm
import io.realm.RealmConfiguration
import io.realm.RealmFieldType
import java.util.ArrayList

class RealmSnapshooter {

    fun snapshoot(files: ArrayList<FilesPojo>): String {
        var snapshoot = ""
        snapshoot = snapshoot.plus("There are ${files.size} Realm databases used by this app\n\n")
        files.map {
            val realm = DynamicRealm.getInstance(RealmConfiguration.Builder().name(it.name).build())
            val realmClasses = realm.configuration.realmObjectClasses
            snapshoot = snapshoot.plus("Database ${it.name} has ${realmClasses.size} tables\n")
            realmClasses.forEach {
                val tableName = it.name.replace(Class.forName(it.name).getPackage().name + ".", "")
                realm.executeTransaction {
                    val rows = it.where(tableName).findAll()
                    val columns = realm.schema[tableName]?.fieldNames
                    snapshoot = snapshoot.plus("- $tableName has ${columns?.size ?: 0} columns and ${rows.size} rows\n")

                    columns?.forEach {
                        snapshoot = snapshoot.plus("| $it |")
                    }
                    snapshoot = snapshoot.plus("\n")

                    if (rows.isValid && rows.size > 0) {
                        rows.forEach { row ->
                            columns?.forEach { column ->
                                row.let {
                                    val fieldType = it.getFieldType(column)
                                    val fieldValue = when (fieldType) {
                                        RealmFieldType.INTEGER -> it.getInt(column).toString()
                                        RealmFieldType.BOOLEAN -> it.getBoolean(column).toString()
                                        RealmFieldType.STRING -> it.getString(column).toString()
                                        RealmFieldType.DATE -> it.getDate(column).toString()
                                        RealmFieldType.FLOAT -> it.getFloat(column).toString()
                                        RealmFieldType.DOUBLE -> it.getDouble(column).toString()
                                        else -> ""
                                    }
                                    snapshoot = snapshoot.plus("$fieldValue ")
                                }
                            }
                            snapshoot = snapshoot.plus("\n")
                        }
                    }
                }
            }
            snapshoot = snapshoot.plus("\n\n")
        }
        return snapshoot
    }

}