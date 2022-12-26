package com.bestDate.data.utils

import com.bestDate.data.extension.toListOrEmpty

class SocialUtils {
    private val socialsMap: MutableMap<SocialTypes, String> = mutableMapOf()

    fun setSocials(socials: MutableList<String>?) {
        socials?.forEach {
            when {
                isValidLink(it, SocialTypes.INSTAGRAM) -> socialsMap[SocialTypes.INSTAGRAM] = it
                isValidLink(it, SocialTypes.FACEBOOK) -> socialsMap[SocialTypes.FACEBOOK] = it
                isValidLink(it, SocialTypes.TWITTER) -> socialsMap[SocialTypes.TWITTER] = it
                isValidLink(it, SocialTypes.LINKEDIN) -> socialsMap[SocialTypes.LINKEDIN] = it
            }
        }
    }

    fun setSocials(socials: String?) {
        setSocials(socials.toListOrEmpty())
    }

    fun getSocials(): String {
        return socialsMap.values.joinToString()
    }

    fun getLink(type: SocialTypes): String? {
        return socialsMap[type]
    }

    fun setLink(link: String?, type: SocialTypes) {
        link?.let {
            if (isValidLink(it, type)) socialsMap[type] = it
        }
    }

    fun isValidLink(link: String?, type: SocialTypes): Boolean {
        return link?.lowercase()?.contains(type.baseUrl) == true
    }

    fun instaAvailable() = socialsMap.containsKey(SocialTypes.INSTAGRAM)
    fun facebookAvailable() = socialsMap.containsKey(SocialTypes.FACEBOOK)
    fun twitterAvailable() = socialsMap.containsKey(SocialTypes.TWITTER)
    fun linkedinAvailable() = socialsMap.containsKey(SocialTypes.LINKEDIN)
}

enum class SocialTypes(val baseUrl: String) {
    INSTAGRAM("instagram.com"),
    FACEBOOK("facebook.com"),
    TWITTER("twitter.com"),
    LINKEDIN("linkedin.com")
}