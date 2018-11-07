package com.josedlpozo.galileo.sample.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DeveloperModel(@PrimaryKey var id: Int = 0, var name: String = ""): RealmObject()

open class TeamModel(@PrimaryKey var id: Int = 0, var name: String = ""): RealmObject()