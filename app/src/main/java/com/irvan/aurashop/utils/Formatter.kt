package com.github.dimsmith.auraparfum.utils

import java.text.NumberFormat
import java.util.*

object Formatter {
    fun currency(value: Double, withLocale: Boolean = false): String {
        val nf = if (withLocale) {
            val locale = Locale("in", "ID")
            NumberFormat.getCurrencyInstance(locale)
        } else {
            NumberFormat.getNumberInstance()
        }
        return nf.format(value).toString()
    }
}