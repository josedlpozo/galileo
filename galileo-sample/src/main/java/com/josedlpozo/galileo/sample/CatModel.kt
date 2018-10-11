package com.josedlpozo.galileo.sample

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CatModel(@PrimaryKey var id: Int = 0, var name: String = ""): RealmObject()