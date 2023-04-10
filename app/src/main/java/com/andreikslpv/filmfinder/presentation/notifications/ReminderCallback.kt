package com.andreikslpv.filmfinder.presentation.notifications

interface ReminderCallback {
    fun onSuccess(reminderTime: Long)
    fun onFailure()
}