package com.nixstudio.githubuser3.model

import android.content.Context

class UserPreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val IS_REMINDER_SET = "is_reminder_set"
        private const val REMINDER_TIME = "reminder_time"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setReminder(data: ReminderModel) {
        val editor = preferences.edit()
        editor.putBoolean(IS_REMINDER_SET, data.isReminderSet)
        editor.putString(REMINDER_TIME, data.timeString)
        editor.apply()
    }

    fun getReminder(): ReminderModel {
        val model = ReminderModel()
        model.isReminderSet = preferences.getBoolean(IS_REMINDER_SET, false)
        model.timeString = preferences.getString(REMINDER_TIME, "null")
        return model
    }
}