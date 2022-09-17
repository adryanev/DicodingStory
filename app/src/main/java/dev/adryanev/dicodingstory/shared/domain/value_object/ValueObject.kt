package dev.adryanev.dicodingstory.shared.domain.value_object

@JvmInline
value class EmailAddress private constructor(private val value: String) {
    fun value() = this.value
    init {
        require(validateEmailAddress(this.value)) { "Invalid Email Address" }
    }

    companion object {
        operator fun invoke(value: String): EmailAddress{
            return EmailAddress(value)
        }
    }


}

@JvmInline
value class Password private constructor(private val value: String) {

    fun value() = this.value

    init {
        require(validatePasswordMinimumLength(this.value))
        { "Password should be at least 8 characters" }
    }

    companion object {
        operator fun invoke(value: String) = Password(value)
    }
}