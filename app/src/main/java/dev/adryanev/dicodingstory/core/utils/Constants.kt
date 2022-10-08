package dev.adryanev.dicodingstory.core.utils

class PreferenceConstants {
    companion object {
        const val PREFERENCE_NAME: String = "DicodingPreference"
        const val USER = "user"
    }
}

class NetworkConstants {
    companion object {
        const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    }
}

class ViewConstants {
    companion object {
        const val THRESHOLD_CLICK_TIME = 250
    }
}

class DatabaseConstants {
    companion object {
        const val DATABASE_NAME = "dicodingstory.db"
        const val STORY_TABLE = "stories"
        const val STORY_REMOTE_KEY = "story_remote_keys"
    }
}