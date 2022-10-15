package com.andreikslpv.filmfinder.domain

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}