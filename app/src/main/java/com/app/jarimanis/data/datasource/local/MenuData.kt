package com.app.jarimanis.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.app.jarimanis.utils.DebugKey.key
import com.app.jarimanis.utils.Key

object MenuData {
  private const val NAME = "MenuNotif"
  private const val MODE = Context.MODE_PRIVATE
  private lateinit var preferences: SharedPreferences



  fun init(context: Context) {
    preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
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

  var chatExits  :Boolean
  get() =  preferences.getBoolean(Key.CHATEXITS, false)
  set(value : Boolean) = preferences.edit{
    it.putBoolean(Key.CHATEXITS , value)

  }

  var firstSetup : Boolean
  get() = preferences.getBoolean(Key.SETUP , false)
  set(value) = preferences.edit{
    it.putBoolean(Key.SETUP,value)
  }

  var sportLineHome : Boolean
  get() = preferences.getBoolean("spotLiteHome" , false)
  set(value : Boolean) = preferences.edit {
    it.putBoolean("spotLiteHome",value)
  }

  var sportLineDashboard : Boolean
    get() = preferences.getBoolean("spotLiteDashboard" , false)
    set(value : Boolean) = preferences.edit {
      it.putBoolean("spotLiteDashboard",value)
    }




}