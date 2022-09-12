package dev.adryanev.dicodingstory.core.utils.resource

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext

class ResourceProviderImpl(
    @ActivityContext private val context: Context
) : ResourceProvider {
    override fun getString(resourceId: Int): String = context.getString(resourceId)

    override fun getString(resourceId: Int, vararg args: Any?): String {
        if (args.isEmpty()) {
            return context.getString(resourceId)
        }
        return context.getString(resourceId, *args)
    }
}