package com.app.jarimanis.ui.auth.utilRegister

data class RegisterResult (
    var onSuccess : Boolean? = false,
    var onError : String? = null,
    var onEmailEror : Boolean =  false,
    var onPasswordErr : Boolean =  false,
    var onRePasswordErr : Boolean =  false
)