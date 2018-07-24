package com.sloydev.preferator.model

enum class Type constructor(val typeName: String) {
    BOOLEAN("boolean"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    STRING("string"),
    SET("set");


    companion object {
        fun of(value: Any?): Type = when (value) {
            is String -> STRING
            is Int -> INT
            is Boolean -> BOOLEAN
            is Long -> LONG
            is Float -> FLOAT
            is Set<*> -> SET
            null -> throw IllegalStateException("Value must not be null")
            else -> throw IllegalStateException("Not type matching found for " + value.javaClass.name)
        }
    }
}