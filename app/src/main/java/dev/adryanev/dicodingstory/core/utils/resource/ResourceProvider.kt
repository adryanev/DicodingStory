package dev.adryanev.dicodingstory.core.utils.resource

interface ResourceProvider {
    fun getString(resourceId: Int): String
    fun getString(resourceId: Int, vararg args: Any?): String
}