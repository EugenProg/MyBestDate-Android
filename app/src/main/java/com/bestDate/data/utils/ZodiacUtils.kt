package com.bestDate.data.utils

import androidx.annotation.StringRes
import com.bestDate.R

class ZodiacUtils {
    fun getZodiacSignByDate(birthday: String?): ZodiacSign {
        if (birthday.isNullOrBlank() || birthday.length < 10) return ZodiacSign.ARIES
        val date = birthday.substring(5..9)
        return when {
            ZodiacSign.ARIES.isThisZodiac(date) -> ZodiacSign.ARIES
            ZodiacSign.TAURUS.isThisZodiac(date) -> ZodiacSign.TAURUS
            ZodiacSign.GEMINI.isThisZodiac(date) -> ZodiacSign.GEMINI
            ZodiacSign.CANCER.isThisZodiac(date) -> ZodiacSign.CANCER
            ZodiacSign.LEO.isThisZodiac(date) -> ZodiacSign.LEO
            ZodiacSign.VIRGO.isThisZodiac(date) -> ZodiacSign.VIRGO
            ZodiacSign.LIBRA.isThisZodiac(date) -> ZodiacSign.LIBRA
            ZodiacSign.SCORPIO.isThisZodiac(date) -> ZodiacSign.SCORPIO
            ZodiacSign.SAGITTARIUS.isThisZodiac(date) -> ZodiacSign.SAGITTARIUS
            ZodiacSign.AQUARIUS.isThisZodiac(date) -> ZodiacSign.AQUARIUS
            ZodiacSign.PISCES.isThisZodiac(date) -> ZodiacSign.PISCES
            else -> ZodiacSign.CAPRICORN
        }
    }
}

enum class ZodiacSign(@StringRes val title: Int, val start: String, val end: String) {
    ARIES(R.string.aries, "03-21", "04-20"),
    TAURUS(R.string.taurus, "04-21", "05-21"),
    GEMINI(R.string.gemini, "05-22", "06-21"),
    CANCER(R.string.cancer, "06-22", "07-22"),
    LEO(R.string.leo, "07-23", "08-23"),
    VIRGO(R.string.virgo, "08-24", "09-22"),
    LIBRA(R.string.libra, "09-23", "10-23"),
    SCORPIO(R.string.aries, "10-24", "11-22"),
    SAGITTARIUS(R.string.aries, "11-23", "12-21"),
    CAPRICORN(R.string.aries, "12-22", "01-20"),
    AQUARIUS(R.string.aries, "01-21", "02-18"),
    PISCES(R.string.aries, "02-19", "03-20");

    fun isThisZodiac(date: String): Boolean {
        return if (this != CAPRICORN) {
            date in start..end
        } else {
            date >= start || date <= end
        }
    }
}