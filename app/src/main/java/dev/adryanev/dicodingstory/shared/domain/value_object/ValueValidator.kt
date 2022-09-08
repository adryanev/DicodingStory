package dev.adryanev.dicodingstory.shared.domain.value_object


fun validateEmailAddress(value: String): Boolean {
    val emailRegex =
        Regex("^[a-zA-Z0-9.!#\$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
    return emailRegex.matches(value);
}

fun validatePasswordMinimumLength(value: String): Boolean {
    return value.length >= 8
}

