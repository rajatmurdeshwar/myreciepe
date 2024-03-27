package com.example.myrecipes.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class LocalRecipe(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val img: String,
    val tittle: String,
    val des: String,
    val ing: String,
    val category: String
)