package com.josedlpozo.galileo.realm.realmbrowser.browser

import com.josedlpozo.galileo.realm.realmbrowser.helper.DataHolder.*
import com.josedlpozo.galileo.realm.realmbrowser.helper.Utils
import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmModel
import io.realm.RealmObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.*

class BrowserPresenter(private val view: BrowserContract.View) {

    private var realmModelClass: Class<out RealmModel>? = null
    private var dynamicRealm: DynamicRealm? = null
    private var fields: List<Field> = listOf()
    private var selectedFieldIndices: ArrayList<Int> = arrayListOf()

    fun requestForContentUpdate(dynamicRealm: DynamicRealm?, @BrowserContract.DisplayMode displayMode: Int) {
        if (dynamicRealm == null || dynamicRealm.isClosed) return
        this.dynamicRealm = dynamicRealm

        if (displayMode == BrowserContract.DisplayMode.REALM_CLASS) {
            realmModelClass =
                getInstance().retrieve(DATA_HOLDER_KEY_CLASS) as Class<out RealmModel>?
            updateWithRealmObjects(dynamicRealm.where(this.realmModelClass!!.simpleName).findAll())
        } else if (displayMode == BrowserContract.DisplayMode.REALM_LIST) {
            val dynamicRealmObject =
                getInstance().retrieve(DATA_HOLDER_KEY_OBJECT) as DynamicRealmObject?
            val field = getInstance().retrieve(DATA_HOLDER_KEY_FIELD) as Field?
            if (dynamicRealmObject != null && field != null) {
                updateWithRealmObjects(dynamicRealmObject.getList(field.name))
                if (Utils.isParametrizedField(field)) {
                    realmModelClass =
                        (field.genericType as ParameterizedType).actualTypeArguments[0] as Class<out RealmObject>
                }
            }
        }

        view.updateWithFABVisibility(realmModelClass != null)
        updateWithTitle(String.format("%s", realmModelClass?.simpleName))

        fields = getFieldsList(dynamicRealm, realmModelClass)
        selectedFieldIndices = ArrayList()
        for (i in fields.indices) {
            if (i < 3) selectedFieldIndices.add(i)
        }
        updateSelectedFields()
    }

    fun onShowMenuSelected() {
        view.showMenu()
    }

    fun onFieldSelectionChanged(fieldIndex: Int, checked: Boolean) {
        if (checked && !selectedFieldIndices.contains(fieldIndex)) {
            selectedFieldIndices.add(fieldIndex)
        } else if (selectedFieldIndices.contains(fieldIndex)) {
            selectedFieldIndices.remove(fieldIndex)
        }
        updateSelectedFields()
    }

    fun onNewObjectSelected() {
        realmModelClass?.let {
            view.goToNewObject(it, false)
        }
    }

    fun onInformationSelected() {
        if (dynamicRealm != null && realmModelClass != null) {
            if (!dynamicRealm!!.isClosed) {
                view.showInformation(dynamicRealm!!.where(realmModelClass!!.simpleName).count())
            }
        }
    }

    fun onRowSelected(realmObject: DynamicRealmObject) {
        if (this.realmModelClass != null) {
            getInstance().save(DATA_HOLDER_KEY_OBJECT, realmObject)
            view.goToNewObject(realmModelClass as Class<out RealmModel>, false)
        }
    }

    private fun updateWithRealmObjects(objects: AbstractList<out DynamicRealmObject>) {
        view.updateWithRealmObjects(objects)
    }

    private fun updateWithTitle(title: String) {
        view.updateWithTitle(title)
    }

    private fun updateWithFieldList(fields: List<Field>, selectedFieldIndices: Array<Int>) {
        view.updateWithFieldList(fields, selectedFieldIndices)
    }

    private fun getFieldsList(
        dynamicRealm: DynamicRealm,
        realmModelClass: Class<out RealmModel>?
    ): List<Field> {
        if (realmModelClass == null) return emptyList()
        val schema = dynamicRealm.schema.get(realmModelClass.simpleName)
        val fieldsList = ArrayList<Field>()
        for (s in schema!!.fieldNames) {
            try {
                fieldsList.add(realmModelClass.getDeclaredField(s))
            } catch (e: NoSuchFieldException) {
            }

        }
        return fieldsList
    }

    private fun updateSelectedFields() {
        updateWithFieldList(fields, selectedFieldIndices.toTypedArray())
    }
}