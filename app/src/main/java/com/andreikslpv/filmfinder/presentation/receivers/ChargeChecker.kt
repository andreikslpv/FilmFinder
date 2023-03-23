package com.andreikslpv.filmfinder.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.presentation.ui.utils.makeToast

class ChargeChecker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //Если интента нет, то выходим из метода
        if (intent == null) return
        //Проверяем, какой пришел action
        when (intent.action) {
            //Если пришел низкий заряд батарее
            Intent.ACTION_BATTERY_LOW -> {
                context!!.getString(R.string.main_message_battery_low)
                    .makeToast(context)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            //Если пришло подключение к зарядке
            Intent.ACTION_POWER_CONNECTED -> {
                context!!.getString(R.string.main_message_power_connected)
                    .makeToast(context)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}