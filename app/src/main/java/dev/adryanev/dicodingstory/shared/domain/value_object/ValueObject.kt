package dev.adryanev.dicodingstory.shared.domain.value_object

@JvmInline
value class EmailAddress private constructor(private val value: String) {
    init {
        require(validateEmailAddress(this.value)) { "Invalid Email Address" }
    }

}

@JvmInline
value class Password private constructor(private val value: String) {
    init {
        require(validatePasswordMinimumLength(this.value))
        { "Password should be at least 8 characters" }
    }
}