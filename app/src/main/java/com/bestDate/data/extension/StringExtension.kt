package com.bestDate.data.extension

fun String.isPhoneNumber(): Boolean {
    if (this.isBlank()) return false

    return this.replace("+", "")
        .replace(" ", "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
        .length in 10..14
}

fun String.isAEmail(): Boolean {
    return this.matches(Regex("\\S+@\\S+\\.[a-zA-Z]{2,3}"))
}