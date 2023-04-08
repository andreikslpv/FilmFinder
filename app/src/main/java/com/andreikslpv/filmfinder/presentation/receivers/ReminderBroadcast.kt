package com.andreikslpv.filmfinder.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.notifications.NotificationHelper
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_FILM

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val bundle = intent?.getBundleExtra(BUNDLE_KEY_FILM)
        val film: FilmDomainModel = bundle?.get(BUNDLE_KEY_FILM) as FilmDomainModel

        NotificationHelper.createNotification(context!!, film)
    }
}