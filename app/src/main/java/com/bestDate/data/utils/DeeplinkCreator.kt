package com.bestDate.data.utils

import android.net.Uri
import com.bestDate.network.NetworkModule
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import java.net.URI

class DeeplinkCreator(private val id: Int?, private val name: String?) {
    fun get(): String {
        return Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://best-date.com/$id")
            domainUriPrefix = "https://mybestdate.page.link"
            androidParameters("com.bestDate") { }
            iosParameters("com.bestDate") {
                appStoreId = "1635182272"
            }
            socialMetaTagParameters {
                title = "Link to $name's profile in My Best Date"
                description = "This link opens the user profile in My Best Date application"
                imageUrl = Uri.parse("${NetworkModule.providesCoreBaseURL()}/images/ic_launcher-playstore.png")
            }
        }.uri.toString()
    }
}