package com.bestDate.data.extension

import java.text.DecimalFormat

fun Double?.decimalAfterDot(): String {
    val df = DecimalFormat("#.#")
    return df.format(this)
}