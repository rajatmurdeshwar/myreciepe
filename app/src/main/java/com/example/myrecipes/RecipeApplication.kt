package com.example.myrecipes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}