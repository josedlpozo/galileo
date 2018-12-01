package com.josedlpozo.galileo.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DeveloperModel(@PrimaryKey var id: Int = 0, var name: String = ""): RealmObject()

open class TeamModel(@PrimaryKey var id: Int = 0, var name: String = ""): RealmObject()

val developers = listOf(
    DeveloperModel(0, "fpulido"),
    DeveloperModel(1, "jmdelpozo"),
    DeveloperModel(2, "vfrancisco"),
    DeveloperModel(3, "jaznar"),
    DeveloperModel(4, "molmedo"),
    DeveloperModel(5, "efau"))

val teams = listOf(
    TeamModel(0, "android"),
    TeamModel(1, "ios")
)
