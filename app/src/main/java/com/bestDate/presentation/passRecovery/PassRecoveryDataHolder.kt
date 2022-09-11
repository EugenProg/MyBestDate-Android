package com.bestDate.presentation.passRecovery

import com.bestDate.presentation.registration.RegistrationType

object PassRecoveryDataHolder {
    var type: RegistrationType = RegistrationType.EMAIL
    var login: String = ""
    var password: String = ""
    var code: String = ""
}