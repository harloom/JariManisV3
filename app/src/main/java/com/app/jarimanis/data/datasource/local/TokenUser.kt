package com.app.jarimanis.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import com.app.jarimanis.utils.Key

object TokenUser {
    private const val NAME = "tokenUser"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val token = Pair("secret-token", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var server_token: String?
        // custom getter to get a preference of a desired type, with a predefined default value
            get() = preferences.getString(Key.TOKEN,"")

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(Key.TOKEN, value)
        }

}