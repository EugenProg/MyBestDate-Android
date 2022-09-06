package com.bestDate.fragment.passRecovery

import com.bestDate.fragment.registration.RegistrationType

object PassRecoveryDataHolder {
    var type: RegistrationType = RegistrationType.EMAIL
    var login: String = ""
    var password: String = ""
    var code: String = ""
}