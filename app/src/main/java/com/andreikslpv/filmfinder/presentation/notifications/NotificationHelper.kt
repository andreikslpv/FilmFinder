package com.andreikslpv.filmfinder.presentation.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.andreikslpv.filmfinder.R
import com.andreikslpv.filmfinder.domain.models.FilmDomainModel
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.NOTIFICATION_ID
import com.andreikslpv.filmfinder.presentation.notifications.NotificationConstants.REQUEST_CODE
import com.andreikslpv.filmfinder.presentation.ui.BUNDLE_KEY_FILM
import com.andreikslpv.filmfinder.presentation.ui.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object NotificationHelper {
    @SuppressLint("UnspecifiedImmutableFlag")
    fun createNotification(context: Context, film: FilmDomainModel) {
        val intent = Intent(context, MainActivity::class.java)
        //Кладем переданный фильм в intent
        intent.putExtra(BUNDLE_KEY_FILM, film)


        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_watch_later_border)
            setContentTitle(context.getString(R.string.app_notification_title))
            setContentText(film.title)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(context)

        Glide.with(context)
            //говорим что нужен битмап
            .asBitmap()
            //указываем откуда загружать, это ссылка как на загрузку с API
            .load(film.posterDetails)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                //Этот коллбэк отрабатоет когда мы успешно получим битмап
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //Создаем нотификации в стиле big picture
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    //Обновляем нотификацю
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
                }
            })
        //Отправляем изначальную нотификацю в стандартном исполнении
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}