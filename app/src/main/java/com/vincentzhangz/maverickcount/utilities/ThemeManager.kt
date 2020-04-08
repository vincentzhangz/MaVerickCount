package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.util.Log
import com.vincentzhangz.maverickcount.R

class ThemeManager(var context: Context) {
    private var sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)

    fun loadTheme(): Boolean {
        val state = sharedPreferences.getBoolean("dark_mode", false)
        changeTheme(state)
        return state
    }

    fun saveTheme(state: Boolean) {
        sharedPreferences.edit().putBoolean("dark_mode", state).apply()
        changeTheme(state)
    }

    private fun changeTheme(state: Boolean) {
        if (!state) {
            context.setTheme(R.style.AppTheme)
            Log.d("Theme", "Light")
        } else {
            context.setTheme(R.style.AppThemeDark)
            Log.d("Theme", "Dark")
        }
    }
}