package com.murdeshwar.myrecipe.data.source.local


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = LocalRecipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalIngredient(
    @PrimaryKey
    val ingredientId: Int = 0,
    val recipeId: Int,
    val originalName: String,
    val amount: Double,
    val unit: String
)

@Entity(
    tableName = "steps",
    foreignKeys = [
        ForeignKey(
            entity = LocalRecipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalStep(
    @PrimaryKey(autoGenerate = true) val stepId: Int = 0,
    val recipeId: Int, // Foreign key
    val number: Int,
    val step: String
)

data class RecipeWithIngredientsAndSteps(
    @Embedded val recipe: LocalRecipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<LocalIngredient>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<LocalStep>
)
