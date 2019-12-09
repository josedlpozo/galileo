package com.josedlpozo.galileo.realm

import io.realm.RealmList
import io.realm.RealmObject
import java.util.Date

open class RealmTestModel(
    var anInteger: Int = 0,
    var aBoxedInteger: Int? = null,
    var aLong: Long = 0,
    var aBoxedLong: Long? = null,
    var aShort: Short = 0,
    var aBoxedShort: Short? = null,
    var aByte: Byte = 0,
    var aBoxedByte: Byte? = null,
    var aDouble: Double = 0.toDouble(),
    var aBoxedDouble: Double? = null,
    var aFloat: Float = 0.toFloat(),
    var aBoxedFloat: Float? = null,
    var aBool: Boolean = false,
    var aBoxedBool: Boolean? = null,
    var aString: String? = null,
    var aBlob: ByteArray? = null,
    var aDate: Date? = null,
    var aStringList: RealmList<String>? = null,
    var aRealmList: RealmList<RealmTestModel>? = null,
    var anObject: RealmTestModel? = null
) : RealmObject()