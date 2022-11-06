package com.andreikslpv.filmfinder.presentation

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.concurrent.Executors
import kotlin.math.hypot
import kotlin.math.roundToInt

object AnimationHelper {
    //Это переменная для того, что бы круг проявления расходился именно от иконки меню навигации
    private const val COUNT_OF_MENU_ITEMS = 4
    private const val DURATION_OF_ANIMATION = 500L
    private const val RATIO = 2
    private const val START_RADIUS = 0f

    //В метод у нас приходит 3 параметра:
    //1 - наше rootView, которое одновременно является и контейнером и объектом анимации
    //2 - активити, для того чтобы вернуть выполнение нового треда в UI поток
    //3 - позиция в меню навигации, что бы круг проявления расходился именно от иконки меню навигации
    fun performFragmentCircularRevealAnimation(rootView: View, activity: Activity, position: Int) {
        //Создаем новый тред
        Executors.newSingleThreadExecutor().execute {
            //В бесконечном цикле проверям, когда наше анимируемое view будет "прикреплено" к экрану
            while (true) {
                //Когда оно будет прикреплено выполним код
                if (rootView.isAttachedToWindow) {
                    //Возвращаемся в главный тред, чтобы выполнить анимацию
                    activity.runOnUiThread {
                        //Cупер сложная математика вычесления старта анимации
                        val itemCenter = rootView.width / (COUNT_OF_MENU_ITEMS * RATIO)
                        val step = (itemCenter * RATIO) * (position - 1) + itemCenter

                        val x: Int = step
                        val y: Int = rootView.y.roundToInt() + rootView.height

                        val endRadius = hypot(rootView.width.toDouble(), rootView.height.toDouble())
                        //Создаем саму анимацию
                        ViewAnimationUtils.createCircularReveal(
                            rootView,
                            x,
                            y,
                            START_RADIUS,
                            endRadius.toFloat()
                        ).apply {
                            //Устанавливаем время анимации
                            duration = DURATION_OF_ANIMATION
                            //Интерполятор для более естесственной анимации
                            interpolator = AccelerateDecelerateInterpolator()
                            //Запускаем
                            start()
                        }
                        //Выставляяем видимость нашего елемента
                        rootView.visibility = View.VISIBLE
                    }
                    return@execute
                }
            }
        }
    }
}