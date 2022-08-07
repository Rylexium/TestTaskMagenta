package com.example.test_task_magents.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_picture")
data class FavoritePicture(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val author: String,
    val picture: String)
