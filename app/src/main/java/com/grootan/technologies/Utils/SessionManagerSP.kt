package com.grootan.technologies.Utils

import android.content.Context
import android.content.SharedPreferences

class SessionManagerSP(var _context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0
    fun setlogin(authkey: String?) {
        editor.putString("login", authkey)
        editor.commit()
    }

    fun getlogin(): String? {
        return pref.getString("login", "")
    }

    fun clearall() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "StaffSP"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}